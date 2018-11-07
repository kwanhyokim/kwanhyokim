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
import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferDispDto;
import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferGenreDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhase;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.repository.CharacterPreferGenreMapper;
import com.sktechx.godmusic.personal.rest.repository.RecommendMapper;
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
    private CharacterPreferGenreMapper characterPreferGenreMapper;

    @Autowired
    private RecommendMapper recommendMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public PersonalPhaseMeta getPersonalRecommendPhaseMeta(Long characterNo , OsType osType){
        PersonalPhaseMeta personalPhaseMeta = null;

        if(characterNo == null){
            return getGuestPhaseMeta(osType);
        }

        try {

            String personalRecommendPhaseKey = String.format(PERSONAL_RECOMMEND_PHASE_KEY, characterNo);

            if (redisService.exists(personalRecommendPhaseKey)) {
                PersonalPhaseMeta cachePersonalPhaseMeta = redisService.get(personalRecommendPhaseKey, PersonalPhaseMeta.class);

                cachePersonalPhaseMeta.setOsType(osType);
                return cachePersonalPhaseMeta;
            }
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
            List<PersonalPanel> rcmmdPanelList = recommendMapper.selectPersonalRecommendPanelMeta(characterNo, SIMILAR_TRACK_DISP_STANDARD_COUNT , RCMMD_CF_TRACK_DISP_STANDARD_COUNT);
            personalPhaseMeta.setRcmmdPanelList(rcmmdPanelList);

            redisService.setWithPrefix(personalRecommendPhaseKey, personalPhaseMeta, "NX", "PX", hourlyRemainMillisecond());

        }catch(Exception ex){
            log.error("getPersonalRecommendPhaseMeta not catched exception : {}",ex.getMessage());
            personalPhaseMeta = getGuestPhaseMeta(osType);
        }
        return personalPhaseMeta;
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
