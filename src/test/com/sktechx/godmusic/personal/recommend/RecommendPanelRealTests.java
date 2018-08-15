/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.recommend;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.redis.manager.RedisConnManager;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.CommonTest;
import com.sktechx.godmusic.personal.common.domain.type.ChartType;
import com.sktechx.godmusic.personal.rest.model.dto.ChartDto;
import com.sktechx.godmusic.personal.rest.repository.ChannelMapper;
import com.sktechx.godmusic.personal.rest.repository.ChartMapper;
import com.sktechx.godmusic.personal.rest.repository.RecommendMapper;
import com.sktechx.godmusic.personal.rest.service.ChannelService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendPanelService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 설명 :  테스트
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 10.
 */
@Slf4j
@Tag("dev")
@Tag("recommendPanel")
public class RecommendPanelRealTests extends CommonTest {


    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private RecommendPanelService recommendPanelService;

    @Autowired
    private RecommendMapper recommendMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisConnManager redisConnManager;

    @Autowired
    private ChartMapper chartMapper;

    @BeforeEach
    public void waitRedisConneting()throws Exception{
        while(true){
            if(redisConnManager != null && redisConnManager.getJedis() != null){
                if(redisConnManager.getJedis().isConnected()){
                    break;
                }
            }
            Thread.sleep(500);
            log.info("redis disconnected.");
        }
        log.info("redis connected.");
    }

    @Test
    public void 전체_패널_대체_테스트() {
        ChartDto chartDto = chartMapper.selectPreferGenreChart("KIDS", ChartType.HOURLY, OsType.AOS , 15);
        log.info("chartDto : {}",chartDto);
    }


}
