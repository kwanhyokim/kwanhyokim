/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.recommend.phase;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.CommonTest;
import com.sktechx.godmusic.personal.rest.service.recommend.phase.PersonalRecommendPhaseService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 설명 : XXXXXXXX
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 08. 02.
 */
@Slf4j
@Tag("dev")
@Tag("recommendPhase")
public class PersonalPhaseRedisTests extends CommonTest {

    @Autowired
    private RedisService redisService;

    @Autowired
    private PersonalRecommendPhaseService personalRecommendPhaseService;
    @BeforeEach
    public void waitRedisConneting()throws Exception{
//        while(true){
//            if(redisConnManager != null && redisConnManager.getJedis() != null){
//                if(redisConnManager.getJedis().isConnected()){
//                    break;
//                }
//            }
//            Thread.sleep(500);
//            log.info("redis disconnected.");
//        }
//        log.info("redis connected.");
    }

    @Test
    public void REDIS_SET_테스트(){
        personalRecommendPhaseService.getPersonalRecommendPhaseMeta(52L, OsType.AOS);
    }

}
