/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.recommend.phase;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.domain.type.PersonalPhaseType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.common.util.DateUtil;
import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferDispDto;
import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferGenreDto;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendDuplicateCountDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.PanelContentVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhase;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.repository.AfloMapper;
import com.sktechx.godmusic.personal.rest.repository.ChannelMapper;
import com.sktechx.godmusic.personal.rest.repository.CharacterPreferGenreMapper;
import com.sktechx.godmusic.personal.rest.repository.RecommendReadMapper;
import com.sktechx.godmusic.personal.rest.service.home.HomeMetaService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendPanelAssemblyFactory;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelAssembly;
import lombok.extern.slf4j.Slf4j;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.*;
import static com.sktechx.godmusic.personal.common.domain.constant.RedisKeyConstant.PERSONAL_RECOMMEND_PHASE_KEY;
/**
 * 설명 : 사용자 청취 단계 / 패널 메타 관리
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 18.
 */
@Service
@Slf4j
public class PersonalRecommendPhaseServiceImpl  implements PersonalRecommendPhaseService {

    @Override
    public void clearPersonalRecommendPhaseMetaCache(Long characterNo) {
        redisService.delWithPrefix(String.format(PERSONAL_RECOMMEND_PHASE_KEY, characterNo));
    }
    @Override
    public PersonalPhaseMeta getPersonalRecommendPhaseMetaExcept(Long characterNo, OsType osType,
            String appVer, RecommendPanelContentType recommendPanelContentType) {

        return personalPhaseMetaSupport.filterPanelByRecommendContentType(
            getInnerPersonalRecommendPhaseMeta(characterNo, osType, appVer),
            recommendPanelContentType
        );
    }

    @Override
    public PersonalPhaseMeta getPersonalRecommendPhaseMeta(Long characterNo , OsType osType,
            String appVer) {

        return getInnerPersonalRecommendPhaseMeta(characterNo, osType, appVer);
    }

    private PersonalPhaseMeta getInnerPersonalRecommendPhaseMeta(Long characterNo , OsType osType,
            String appVer) {

        if(characterNo == null){
            return getGuestPhaseMeta(osType);
        }

        PersonalPhaseMeta personalPhaseMeta;

        try {

            // aflo 유효기간 지난 경우, 캐쉬 삭제 처리
            Date afloExpireDate = afloMapper.selectAfloCharacterNo(characterNo);

            // AFLO 사용자 위한 캐쉬 처리 (#1)
            if( !ObjectUtils.isEmpty(afloExpireDate) && afloExpireDate.before(new Date())){
                clearPersonalRecommendPhaseMetaCache(characterNo);
            }

            // AFLO 채널 최신 생성 시각
            Date afloChnlRecentCreateDtime = Optional.ofNullable(
                    channelMapper.selectAfloChannelList(characterNo)
            ).orElseGet(Collections::emptyList)
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(chnlDto -> Objects.nonNull(chnlDto.getCreateDtime()))
                    .findFirst()
                    .orElseGet(ChnlDto::new)
                    .getCreateDtime();

            String personalRecommendPhaseKey = String.format(PERSONAL_RECOMMEND_PHASE_KEY, characterNo);
            personalPhaseMeta = redisService.getWithPrefix(personalRecommendPhaseKey, PersonalPhaseMeta.class);

            // AFLO 사용자 위한 캐쉬 처리 (#2)
            boolean clearCache = isClearCache(afloExpireDate, afloChnlRecentCreateDtime,
                    personalPhaseMeta);

            if(clearCache){
                clearPersonalRecommendPhaseMetaCache(characterNo);
                personalPhaseMeta = null;
            }

            if (!ObjectUtils.isEmpty(personalPhaseMeta)) {

                personalPhaseMeta.setOsType(osType);
                personalPhaseMeta.setAppVer(appVer);
                return personalPhaseMeta;
            }

            personalPhaseMeta = new PersonalPhaseMeta();

            personalPhaseMeta.setCharacterNo(characterNo);
            personalPhaseMeta.setOsType(osType);
            personalPhaseMeta.setAppVer(appVer);
            personalPhaseMeta.setAfloCharacterExpireDtime(afloExpireDate);
            personalPhaseMeta.setAfloChnlRecentCreateDtime(afloChnlRecentCreateDtime);

            //선호 장르 리스트
            CompletableFuture<List<CharacterPreferGenreDto>> futureCharacterPreferGenreList =
                    homeMetaService.getCharacterPreferGenreList(characterNo);
            CompletableFuture<List<CharacterPreferDispDto>> futureCharacterPreferDispList =
                    homeMetaService.getCharacterPreferDispList(characterNo);
            CompletableFuture<List<PersonalPanel>> futureRcmmdPanelList =
                    homeMetaService.getPersonalRecommendPanelMeta(characterNo, false);
            CompletableFuture<List<PersonalPanel>> futureRcmmdMgoPanelList =
                    homeMetaService.getPersonalRecommendPanelMgoMeta(characterNo);
            CompletableFuture.allOf(futureCharacterPreferGenreList, futureCharacterPreferDispList
                    , futureRcmmdPanelList, futureRcmmdMgoPanelList);

            List<CharacterPreferGenreDto> characterPreferGenreList = futureCharacterPreferGenreList.get();
            List<CharacterPreferDispDto> characterPreferDispList = futureCharacterPreferDispList.get();

            Set<PersonalPanel> rcmmdPanelSet = new HashSet<>();

            Optional.ofNullable(futureRcmmdPanelList.get()).ifPresent(rcmmdPanelSet::addAll);
            Optional.ofNullable(futureRcmmdMgoPanelList.get()).ifPresent(rcmmdPanelSet::addAll);

            List<PersonalPanel> rcmmdPanelList =
                    rcmmdPanelSet.stream()
                    .sorted(Comparator.comparing(PersonalPanel::getDispStdStartDt).reversed())
                    .collect(Collectors.toList());

            characterPreferGenreList = fillCharacterPreferGenre(characterPreferGenreList , characterNo);
            personalPhaseMeta.setPreferGenreList(characterPreferGenreList);

            //선호 노출 리스트
            personalPhaseMeta.setPreferDispList(characterPreferDispList);

            //개인화 추천 패널
            personalPhaseMeta.setRcmmdPanelList(rcmmdPanelList);

            filterRecommendPanelDuplicateTracks(personalPhaseMeta);

            //현재 노출 되는 패널 정보 입력
            if(isRcmmdUsageChannelIdFilter(personalPhaseMeta.getFirstPhaseType())){
                PanelAssembly panelAssembly = recommendPanelAssemblyFactory.getRecommendPanelAssembly(personalPhaseMeta.getFirstPhaseType());
                List<Panel> panelList = panelAssembly.makeHomePanelListForMainTop(personalPhaseMeta);
                List<Long> filterChnlIdList =  getRcmmdUsageChannelIdList(panelList);
                if(!CollectionUtils.isEmpty(filterChnlIdList)){
                    personalPhaseMeta.setRcmmdPanelDispChnlIdList(filterChnlIdList);
                }
            }

            redisService.setWithPrefix(personalRecommendPhaseKey, personalPhaseMeta, "NX",
                    "PX", DateUtil.hourlyRemainMillisecond());

        }catch(Exception ex){
            ex.printStackTrace();
            log.error("getPersonalRecommendPhaseMeta not catched exception : {}",ex.getMessage());
            personalPhaseMeta = getGuestPhaseMeta(osType);
        }
        return personalPhaseMeta;
    }

    private boolean isClearCache(Date afloExpireDate, Date afloChnlRecentCreateDtime,
            PersonalPhaseMeta cachePersonalPhaseMeta) {
        boolean clearCache = false;
        if (!ObjectUtils.isEmpty(cachePersonalPhaseMeta)) {

            // 캐쉬 만기 시각이 없고, 테이블에 만 있는 경우
            if (ObjectUtils.isEmpty(cachePersonalPhaseMeta.getAfloCharacterExpireDtime())
                    && !ObjectUtils.isEmpty(afloExpireDate)) {
                clearCache = true;
            }

            // 캐쉬의 AFLO 만기시각과 DB의 만기 시각이 다른 경우
            if (!ObjectUtils.isEmpty(cachePersonalPhaseMeta.getAfloCharacterExpireDtime()) && !ObjectUtils.isEmpty(afloExpireDate) &&
                    cachePersonalPhaseMeta.getAfloCharacterExpireDtime().compareTo(afloExpireDate) != 0) {
                clearCache = true;
            }

            // 캐쉬에 AFLO 만기 시각이 있지만, 추천 패널이 비었거나, 추천 패널에 AFLO가 없는 경우
            if (!ObjectUtils.isEmpty(cachePersonalPhaseMeta.getAfloCharacterExpireDtime()) &&
                    ( CollectionUtils.isEmpty(cachePersonalPhaseMeta.getRcmmdPanelList()) ||
                      !CollectionUtils.isEmpty(cachePersonalPhaseMeta.getRcmmdPanelList()) &&
                        cachePersonalPhaseMeta.getRcmmdPanelList().stream()
                                    .filter(personalPanel1 -> RecommendPanelContentType.AFLO.equals(personalPanel1.getRecommendPanelContentType())).count() == 0
                    )
            ){
                clearCache = true;
            }

            // 채널 최신 생성일이 있고, 변경된 경우
            if(!ObjectUtils.isEmpty(cachePersonalPhaseMeta.getAfloChnlRecentCreateDtime()) &&
                    cachePersonalPhaseMeta.getAfloChnlRecentCreateDtime().compareTo(afloChnlRecentCreateDtime) != 0
            ){
                clearCache = true;
            }


            if(
                !ObjectUtils.isEmpty(cachePersonalPhaseMeta.getRecommendPersonalPanelTopItem()) &&
                !ObjectUtils.isEmpty(cachePersonalPhaseMeta.getRecommendPersonalPanelTopItem().getDispStdStartDt()) &&

                Duration.between(
                    LocalDateTime.now(),
                    new Timestamp(cachePersonalPhaseMeta.getRecommendPersonalPanelTopItem().getDispStdStartDt().getTime()).toLocalDateTime()
                ).toDays() <= -30 ){

                clearCache = true;

            }

        }
        return clearCache;
    }

    public void filterRecommendPanelDuplicateTracks(PersonalPhaseMeta personalPhaseMeta){

        //2-A , 2-A' 패널이 2개 이상일 경우 패널간 중복 곡 5개 이상 발생시 패널 제거 로직
        List<Long> similarTrackPanelList = personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RecommendPanelContentType.RC_SML_TR);
        if( !CollectionUtils.isEmpty(similarTrackPanelList) && similarTrackPanelList.size() == SIMILAR_TRACK_PANEL_SIZE){
            RecommendDuplicateCountDto duplicateCountDto = recommendReadMapper.selectSimilarTrackPanelBetweenDuplicateCount(personalPhaseMeta.getCharacterNo());
            if(duplicateCountDto != null
                    && duplicateCountDto.getDuplicateCount() >= SIMILAR_TRACK_DUPLICATE_COUNT
                        && duplicateCountDto.getRcmmdId() != null){
                personalPhaseMeta.removeRecommendPersonalPanel(RecommendPanelContentType.RC_SML_TR , duplicateCountDto.getRcmmdId());
            }
        }

        List<Long> preferGenreSimilarTrackPanelList = personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RecommendPanelContentType.RC_GR_TR);
        if( !CollectionUtils.isEmpty(preferGenreSimilarTrackPanelList) && preferGenreSimilarTrackPanelList.size() == PREFER_GENRE_SIMILAR_PANEL_SIZE ){
            RecommendDuplicateCountDto duplicateCountDto = recommendReadMapper.selectPreferGenreSimilarTrackPanelBetweenDuplicateCount(personalPhaseMeta.getCharacterNo());
            if(duplicateCountDto != null
                    && duplicateCountDto.getDuplicateCount() >= SIMILAR_TRACK_DUPLICATE_COUNT
                        && duplicateCountDto.getRcmmdId() != null){
                personalPhaseMeta.removeRecommendPersonalPanel(RecommendPanelContentType.RC_GR_TR , duplicateCountDto.getRcmmdId());
            }
        }
    }
    private boolean isRcmmdUsageChannelIdFilter(PersonalPhaseType personalPhaseType){
	    return PersonalPhaseType.VISIT.equals(personalPhaseType) || PersonalPhaseType.LISTEN
			    .equals(personalPhaseType) || PersonalPhaseType.RECOMMEND.equals(personalPhaseType);
    }
    private List<Long> getRcmmdUsageChannelIdList(List<Panel> panelList){
        if(CollectionUtils.isEmpty(panelList)){
            return null;
        }
        return panelList.stream()
                .filter(panel->{
                    RecommendPanelType recommendPanelType = panel.getType();
	                return RecommendPanelType.POPULAR_CHANNEL.equals(recommendPanelType)
			                || RecommendPanelType.LISTEN_MOOD_POPULAR_CHANNEL
			                .equals(recommendPanelType)
			                || RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL
			                .equals(recommendPanelType);
                }).map(panel->{
            PanelContentVo content = panel.getContent();
            if(content != null && content.getId() != null){
                return (Long) content.getId();
            }
            return null;
        }).collect(Collectors.toList());
    }
    private List<CharacterPreferGenreDto> fillCharacterPreferGenre(List<CharacterPreferGenreDto> characterPreferGenreList , Long characterNo){

        if(CollectionUtils.isEmpty(characterPreferGenreList)){
            characterPreferGenreList = new ArrayList<CharacterPreferGenreDto>();
            List<CharacterPreferGenreDto> fillPreferGenreList =  selectFillPreferGenreList(characterNo);
            if(!CollectionUtils.isEmpty(fillPreferGenreList)){
                characterPreferGenreList.addAll(fillPreferGenreList);
            }
        }else if( CHARACTER_PREFER_GENRE_VIEW_LIMIT_SIZE > characterPreferGenreList.size()){
            List<CharacterPreferGenreDto> fillPreferGenreList =  selectFillPreferGenreList(characterNo);
            if(!CollectionUtils.isEmpty(fillPreferGenreList)){
                characterPreferGenreList.addAll(fillPreferGenreList);
            }
        }

        return characterPreferGenreList;
    }
    private List<CharacterPreferGenreDto> selectFillPreferGenreList(Long characterNo){
        return characterPreferGenreMapper.selectCharacterPreferDispMapGenre(characterNo);
    }

    private PersonalPhaseMeta getGuestPhaseMeta(OsType osType){
        PersonalPhaseMeta guestPhaseMeta = new PersonalPhaseMeta();
        guestPhaseMeta.setPersonalPhaseList(Arrays.asList(new PersonalPhase(PersonalPhaseType.GUEST ,new Date())));
        guestPhaseMeta.setOsType(osType);
        return guestPhaseMeta;
    }

    @Autowired
    private RecommendPanelAssemblyFactory recommendPanelAssemblyFactory;

    @Autowired
    private CharacterPreferGenreMapper characterPreferGenreMapper;

    @Autowired
    private RecommendReadMapper recommendReadMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private HomeMetaService homeMetaService;

    @Autowired
    private AfloMapper afloMapper;

    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    private PersonalPhaseMetaSupport personalPhaseMetaSupport;


}
