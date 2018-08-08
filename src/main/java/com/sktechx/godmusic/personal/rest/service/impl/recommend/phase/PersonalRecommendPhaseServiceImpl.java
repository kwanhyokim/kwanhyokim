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

import com.netflix.discovery.converters.Auto;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.domain.type.PersonalPhaseType;
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
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 설명 : 사용자 청취 단계 / 패널 메타 관리
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 18.
 */
@Service
@Slf4j
public class PersonalRecommendPhaseServiceImpl  implements PersonalRecommendPhaseService {


    public static final String PERSONAL_RECOMMEND_PHASE_KEY ="godmusic.personalapi.recommend.phase:%s";

    @Autowired
    private CharacterPreferGenreMapper characterPreferGenreMapper;

    @Autowired
    private RecommendMapper recommendMapper;

    @Autowired
    private RedisService redisService;


    public static void main(String[] args){
        String personalRecommendPhaseKey = String.format(PERSONAL_RECOMMEND_PHASE_KEY, 170L);

        log.info(personalRecommendPhaseKey);
    }
    @Override
    public PersonalPhaseMeta getPersonalRecommendPhaseMeta(Long characterNo , OsType osType){

        if(characterNo == null){
            return getGuestPhaseMeta(osType);
        }

        String personalRecommendPhaseKey = String.format(PERSONAL_RECOMMEND_PHASE_KEY, characterNo);

        if (redisService.exists(personalRecommendPhaseKey) ){
            PersonalPhaseMeta cachePersonalPhaseMeta = redisService.get(personalRecommendPhaseKey, PersonalPhaseMeta.class);
            cachePersonalPhaseMeta.setOsType(osType);
            return cachePersonalPhaseMeta;
        }
        PersonalPhaseMeta personalPhaseMeta = new PersonalPhaseMeta();

        personalPhaseMeta.setCharacterNo(characterNo);
        personalPhaseMeta.setOsType(osType);
        //선호 장르 리스트
        List<CharacterPreferGenreDto> characterPreferGenreList = characterPreferGenreMapper.selectCharacterPreferGenreList(characterNo);
        personalPhaseMeta.setPreferGenreList(characterPreferGenreList);

        //개인화 추천 패널
        List<PersonalPanel> rcmmdPanelList = recommendMapper.selectPersonalRecommendPanelMeta(characterNo);
        personalPhaseMeta.setRcmmdPanelList(rcmmdPanelList);

        redisService.setWithPrefix(personalRecommendPhaseKey,personalPhaseMeta,"NX" ,"PX" ,todayRemainMillisecond());

        return personalPhaseMeta;
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
