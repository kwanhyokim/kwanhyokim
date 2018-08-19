/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.chnl;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.redis.manager.RedisConnManager;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.CommonTest;
import com.sktechx.godmusic.personal.common.domain.type.ChartType;
import com.sktechx.godmusic.personal.common.domain.type.PersonalPhaseType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.recommend.RecommendMockData;
import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferGenreDto;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.MoodPopularChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.PreferGenrePopularChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.artist.ArtistPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.ListenMoodPopularChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.PopularChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.chart.ChartPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.PreferGenreSimilarTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.PreferSimilarTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPanel;
import com.sktechx.godmusic.personal.rest.repository.RecommendMapper;
import com.sktechx.godmusic.personal.rest.service.ChannelService;
import com.sktechx.godmusic.personal.rest.service.recommend.phase.PersonalRecommendPhaseService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * 설명 : 채널 테스트
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 10.
 */
@Slf4j
@Tag("dev")
public class ChnlTests extends CommonTest {

    @Autowired
    private ChannelService channelService;


    @Autowired
    private RecommendMapper recommendMapper;
    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisConnManager redisConnManager;
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
    public void 인기채널_GET_테스트(){
        int limitSize = 3;
        int trackLimitSize = 15;
        List<ChnlDto> chnlList = channelService.getPopularChannelList(limitSize,trackLimitSize,OsType.AOS);

        assertNotNull(chnlList);
        assertEquals(chnlList.size(), limitSize);
    }

    @Test
    public void 청취무드_인기채널_GET_테스트(){
        List<RecommendTrackDto> recommendTrackDtoList =
                recommendMapper.selectRecommendCfTrackListByIdList(Arrays.asList(1L,2L) , 2 , 15,OsType.AOS);

        log.info("recommendTrackDtoList : {}",recommendTrackDtoList);
    }

}
