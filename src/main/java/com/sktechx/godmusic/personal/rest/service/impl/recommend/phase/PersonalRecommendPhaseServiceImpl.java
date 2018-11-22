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

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.domain.type.PersonalPhaseType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferDispDto;
import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferGenreDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.PanelContentVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhase;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.repository.CharacterPreferGenreMapper;
import com.sktechx.godmusic.personal.rest.repository.RecommendMapper;
import com.sktechx.godmusic.personal.rest.repository.RecommendReadMapper;
import com.sktechx.godmusic.personal.rest.service.impl.recommend.RecommendPanelAssemblyFactory;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelAssembly;
import com.sktechx.godmusic.personal.rest.service.recommend.phase.PersonalRecommendPhaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

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
    private RecommendMapper recommendMapper;

    @Autowired
    private RecommendReadMapper recommendReadMapper;
    @Autowired
    private RedisService redisService;

    @Override
    public PersonalPhaseMeta getPersonalRecommendPhaseMeta(Long characterNo , OsType osType){
        PersonalPhaseMeta personalPhaseMeta = null;

        long startTime = System.currentTimeMillis();
        long elapsed = 0;

        if(characterNo == null){
            return getGuestPhaseMeta(osType);
        }

        try {

            String personalRecommendPhaseKey = String.format(PERSONAL_RECOMMEND_PHASE_KEY, characterNo);

            if (redisService.exists(personalRecommendPhaseKey)) {

                PersonalPhaseMeta cachePersonalPhaseMeta = redisService.get(personalRecommendPhaseKey, PersonalPhaseMeta.class);

                cachePersonalPhaseMeta.setOsType(osType);

                elapsed = System.currentTimeMillis() - startTime;
                log.info("getPersonalRecommendPhaseMeta , getCachePersonPhaseMeta : {}",elapsed);
                return cachePersonalPhaseMeta;
            }

            startTime = System.currentTimeMillis();

            personalPhaseMeta = new PersonalPhaseMeta();

            personalPhaseMeta.setCharacterNo(characterNo);
            personalPhaseMeta.setOsType(osType);

            //선호 장르 리스트
            List<CharacterPreferGenreDto> characterPreferGenreList = characterPreferGenreMapper.selectCharacterPreferGenreList(characterNo);
            characterPreferGenreList = fillCharacterPreferGenre(characterPreferGenreList , characterNo);
            personalPhaseMeta.setPreferGenreList(characterPreferGenreList);

            //선호 노출 리스트
            List<CharacterPreferDispDto> characterPreferDispList = characterPreferGenreMapper.selectCharacterPreferDispList(characterNo);
            personalPhaseMeta.setPreferDispList(characterPreferDispList);

            //개인화 추천 패널
            List<PersonalPanel> rcmmdPanelList = recommendReadMapper.selectPersonalRecommendPanelMeta(characterNo, SIMILAR_TRACK_DISP_STANDARD_COUNT , RCMMD_CF_TRACK_DISP_STANDARD_COUNT);
            personalPhaseMeta.setRcmmdPanelList(rcmmdPanelList);

            //현재 노출 되는 패널 정보 입력
            if(isRcmmdUsageChannelIdFilter(personalPhaseMeta.getFirstPhaseType())){
                PanelAssembly panelAssembly = recommendPanelAssemblyFactory.getRecommendPanelAssembly(personalPhaseMeta.getFirstPhaseType());
                List<Panel> panelList = panelAssembly.assembleRecommendPanel(personalPhaseMeta);
                List<Long> filterChnlIdList =  getRcmmdUsageChannelIdList(panelList);
                if(!CollectionUtils.isEmpty(filterChnlIdList)){
                    personalPhaseMeta.setRcmmdPanelDispChnlIdList(filterChnlIdList);
                }
            }

            redisService.setWithPrefix(personalRecommendPhaseKey, personalPhaseMeta, "NX", "PX", hourlyRemainMillisecond());

            elapsed = System.currentTimeMillis() - startTime;
            log.info("getPersonalRecommendPhaseMeta , end : {}",elapsed);

        }catch(Exception ex){
            log.error("getPersonalRecommendPhaseMeta not catched exception : {}",ex.getMessage());
            personalPhaseMeta = getGuestPhaseMeta(osType);
        }
        return personalPhaseMeta;
    }

    private boolean isRcmmdUsageChannelIdFilter(PersonalPhaseType personalPhaseType){

        if(PersonalPhaseType.VISIT.equals(personalPhaseType)  ||
                PersonalPhaseType.LISTEN.equals(personalPhaseType) ||
                    PersonalPhaseType.RECOMMEND.equals(personalPhaseType) ){
            return true;
        }
        return false;
    }
    private List<Long> getRcmmdUsageChannelIdList(List<Panel> panelList){
        if(CollectionUtils.isEmpty(panelList)){
            return null;
        }
        return panelList.stream()
                .filter(panel->{
                    RecommendPanelType recommendPanelType = panel.getType();
            if(RecommendPanelType.POPULAR_CHANNEL.equals(recommendPanelType)
                    || RecommendPanelType.LISTEN_MOOD_POPULAR_CHANNEL.equals(recommendPanelType)
                        || RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL.equals(recommendPanelType)){
                return true;
            }
            return false;
        }).map(panel->{
            PanelContentVo content = panel.getContent();
            if(content != null && content.getId() != null){
                return content.getId();
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

        cal.set(Calendar.MINUTE , 59);
        cal.set(Calendar.SECOND,56);
        cal.set(Calendar.MILLISECOND,999);

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
