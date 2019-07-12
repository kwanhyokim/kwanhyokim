/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.impl.recommend.phase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.lib.utils.ComparableVersion;
import com.sktechx.godmusic.personal.common.domain.type.PersonalPhaseType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferDispDto;
import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferGenreDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendDuplicateCountDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.PanelContentVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhase;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.repository.AfloMapper;
import com.sktechx.godmusic.personal.rest.repository.CharacterPreferGenreMapper;
import com.sktechx.godmusic.personal.rest.repository.RecommendReadMapper;
import com.sktechx.godmusic.personal.rest.service.impl.recommend.RecommendPanelAssemblyFactory;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelAssembly;
import com.sktechx.godmusic.personal.rest.service.recommend.phase.PersonalRecommendPhaseService;
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
    @Autowired
    private RecommendPanelAssemblyFactory recommendPanelAssemblyFactory;

    @Autowired
    private CharacterPreferGenreMapper characterPreferGenreMapper;

    @Autowired
    private RecommendReadMapper recommendReadMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private AfloMapper afloMapper;

    @Override
    public PersonalPhaseMeta getPersonalRecommendPhaseMeta(Long characterNo , OsType osType, String appVer) {

        if( ObjectUtils.isEmpty(appVer) || new ComparableVersion(appVer).compareTo(new ComparableVersion("4.6.0")) < 0 ) {
            return getPersonalRecommendPhaseMetaWithOption(characterNo, osType, true);
        }else{
            return getPersonalRecommendPhaseMetaWithOption(characterNo, osType, false);
        }
    }
    @Override
    public void clearPersonalRecommendPhaseMetaCache(Long characterNo) {
        String personalRecommendPhaseKey = String.format(PERSONAL_RECOMMEND_PHASE_KEY, characterNo);
        redisService.delWithPrefix(personalRecommendPhaseKey);
    }

    private PersonalPhaseMeta getPersonalRecommendPhaseMetaWithOption(Long characterNo , OsType osType, Boolean checkDispEndDate){
        PersonalPhaseMeta personalPhaseMeta = null;

        if(characterNo == null){
            return getGuestPhaseMeta(osType);
        }

        try {

            // aflo 유효기간 지난 경우, 캐쉬 삭제 처리
            Date afloExpireDate = afloMapper.selectAfloCharacterNo(characterNo);

            if( !ObjectUtils.isEmpty(afloExpireDate) && afloExpireDate.before(new Date())){
                clearPersonalRecommendPhaseMetaCache(characterNo);
            }

            String personalRecommendPhaseKey = String.format(PERSONAL_RECOMMEND_PHASE_KEY, characterNo);
            PersonalPhaseMeta cachePersonalPhaseMeta = redisService.getWithPrefix(personalRecommendPhaseKey, PersonalPhaseMeta.class);

            // 캐쉬의 AFLO 만기시간과 DB의 만기 시간 비교하여 DB의 만기시간이 연장된 경우 캐쉬 클리어..
            if( !ObjectUtils.isEmpty(cachePersonalPhaseMeta.getAfloCharacterExpireDtime()) &&
                !ObjectUtils.isEmpty(afloExpireDate) &&

                cachePersonalPhaseMeta.getAfloCharacterExpireDtime().compareTo(afloExpireDate) != 0

            ){
                clearPersonalRecommendPhaseMetaCache(characterNo);
                cachePersonalPhaseMeta = null;
            }

            if (!ObjectUtils.isEmpty(cachePersonalPhaseMeta)) {

                cachePersonalPhaseMeta.setOsType(osType);
                return cachePersonalPhaseMeta;
            }

            personalPhaseMeta = new PersonalPhaseMeta();

            personalPhaseMeta.setCharacterNo(characterNo);
            personalPhaseMeta.setOsType(osType);
            personalPhaseMeta.setAfloCharacterExpireDtime(afloExpireDate);

            //선호 장르 리스트
            List<CharacterPreferGenreDto> characterPreferGenreList = characterPreferGenreMapper.selectCharacterPreferGenreList(characterNo);
            characterPreferGenreList = fillCharacterPreferGenre(characterPreferGenreList , characterNo);
            personalPhaseMeta.setPreferGenreList(characterPreferGenreList);

            //선호 노출 리스트
            List<CharacterPreferDispDto> characterPreferDispList = characterPreferGenreMapper.selectCharacterPreferDispList(characterNo);
            personalPhaseMeta.setPreferDispList(characterPreferDispList);

            //개인화 추천 패널
            List<PersonalPanel> rcmmdPanelList = recommendReadMapper.selectPersonalRecommendPanelMeta(characterNo, SIMILAR_TRACK_DISP_STANDARD_COUNT , RCMMD_CF_TRACK_DISP_STANDARD_COUNT, checkDispEndDate);

            // 개인 추천 패널이 AFLO만 있고, 다른 디스커버리 플로우 통해 선택된 항목이 있는 경우, 홈 패널에서 AFLO 제거
            if(
                !CollectionUtils.isEmpty(rcmmdPanelList) &&
                rcmmdPanelList.stream()
                        .filter(personalPanel1 -> !RecommendPanelContentType.AFLO.equals(personalPanel1.getRecommendPanelContentType())).count() == 0  &&

                ( !CollectionUtils.isEmpty(characterPreferDispList) || !CollectionUtils.isEmpty(characterPreferGenreList))


            ){
                rcmmdPanelList = null;
            }

            personalPhaseMeta.setRcmmdPanelList(rcmmdPanelList);

            filterRecommendPanelDuplicateTracks(personalPhaseMeta);

            //현재 노출 되는 패널 정보 입력
            if(isRcmmdUsageChannelIdFilter(personalPhaseMeta.getFirstPhaseType())){
                PanelAssembly panelAssembly = recommendPanelAssemblyFactory.getRecommendPanelAssembly(personalPhaseMeta.getFirstPhaseType());
                List<Panel> panelList = panelAssembly.assembleRecommendPanel(personalPhaseMeta);
                List<Long> filterChnlIdList =  getRcmmdUsageChannelIdList(panelList);
                if(!CollectionUtils.isEmpty(filterChnlIdList)){
                    personalPhaseMeta.setRcmmdPanelDispChnlIdList(filterChnlIdList);
                }
            }

            boolean result = redisService.setWithPrefix(personalRecommendPhaseKey, personalPhaseMeta, "NX", "PX", hourlyRemainMillisecond());

            log.info("{}", result);

        }catch(Exception ex){
            log.error("getPersonalRecommendPhaseMeta not catched exception : {}",ex.getMessage());
            personalPhaseMeta = getGuestPhaseMeta(osType);
        }
        return personalPhaseMeta;
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

    private long hourlyRemainMillisecond(){
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, 5);
        cal.set(Calendar.MINUTE , 59);
        cal.set(Calendar.SECOND,59);
        cal.set(Calendar.MILLISECOND,999);
        cal.add(Calendar.DAY_OF_YEAR, 1);

        long remainMillisecond = cal.getTimeInMillis() - System.currentTimeMillis();
        if(remainMillisecond > 0)
            return remainMillisecond;
        return 0;

    }
    private long todayRemainMillisecond(){
        LocalDateTime todayMaxLocalTime = LocalDate.now().atTime(LocalTime.MAX);
        long epochMilli = todayMaxLocalTime.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();

        LocalDateTime nowLocalTime = LocalDate.now().atTime(LocalTime.now());
        long localEpochMilli = nowLocalTime.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();

        if(epochMilli > localEpochMilli)
            return epochMilli - localEpochMilli;
        return 0;
    }

    private PersonalPhaseMeta getGuestPhaseMeta(OsType osType){
        PersonalPhaseMeta guestPhaseMeta = new PersonalPhaseMeta();
        guestPhaseMeta.setPersonalPhaseList(Arrays.asList(new PersonalPhase(PersonalPhaseType.GUEST ,new Date())));
        guestPhaseMeta.setOsType(osType);
        return guestPhaseMeta;
    }

}
