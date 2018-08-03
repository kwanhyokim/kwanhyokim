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

import com.sktechx.godmusic.personal.common.domain.type.OsType;
import com.sktechx.godmusic.personal.common.domain.type.PersonalPhaseType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.PopularChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.PreferSimilarTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.RcmmdTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

/**
 * 설명 : 3단계 선호장르 유사곡 미존재 테스트
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 08. 02.
 */
public class RecommendPanelPreferGenreEmptyTests extends RecommendTests{
    private List<PersonalPanel> personalPanelList = new ArrayList<>();

    //추천 단계 기본 패널 개수
    private int defaultPanelTotalSize = 3;

    //3-A 데이터 수
    private int cfTrackDataSize = 2;
    //2-A 데이터 수
    private int similarTrackDataSize = 1;
    //2-A' 데이터 수
    private int preferGenreSimilarTrackDataSize = 1;
    //1-A' 인기 채널 개수
    private int preferGenrePolularChnlDataSize = 1;
    @BeforeEach
    public void init(){
        personalPanelList.add(makeMockPersonalPanel(RecommendPanelContentType.RC_CF_TR, 1L));
        personalPanelList.add(makeMockPersonalPanel(RecommendPanelContentType.RC_GR_TR, 1L));
        personalPanelList.add(makeMockPersonalPanel(RecommendPanelContentType.RC_SML_TR, 1L));
        personalPanelList.add(makeMockPersonalPanel(RecommendPanelContentType.RC_ATST_TR, 1L));

        given(channelService.getHotplayChannelList(anyInt())).willReturn(makeMockHotPlayChannels(3));
        given(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(anyLong() , anyObject())).willReturn(makeMockPersonalPhaseMeta(PersonalPhaseType.RECOMMEND , personalPanelList , null));
    }

    @Test
    public void 선호장르_유사곡_미존재_테스트_1(){
        // CASE 1: 3-A 패널 데이터 존재
        // expected : 3-A, 1-A , 1-A
        cfTrackDataSize = 1;
        similarTrackDataSize = 0;
        preferGenreSimilarTrackDataSize = 0;
        preferGenrePolularChnlDataSize = 0;
        given(recommendMapper.selectRecommendCfTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(cfTrackDataSize));
        given(recommendMapper.selectRecommendSimilarTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(similarTrackDataSize));
        given(channelService.getHotplayChannelList(anyInt())).willReturn(makeMockHotPlayChannels(defaultPanelTotalSize - (cfTrackDataSize +similarTrackDataSize)));

        List<Panel> panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);
        assertNotNull(panelList);
        assertEquals(3, panelList.size());
        assertThat(panelList.get(0), instanceOf(RcmmdTrackPanel.class));
        assertThat(panelList.get(0).getType(), is(RecommendPanelType.RCMMD_TRACK) );
        assertThat(panelList.get(1), instanceOf(PopularChannelPanel.class));
        assertThat(panelList.get(1).getType(), is(RecommendPanelType.POPULAR_CHANNEL) );
        assertThat(panelList.get(2), instanceOf(PopularChannelPanel.class));
        assertThat(panelList.get(2).getType(), is(RecommendPanelType.POPULAR_CHANNEL) );
    }

    @Test
    public void 선호장르_유사곡_미존재_테스트_2(){
        // CASE 2: 3-A , 2-A 패널 데이터 존재
        // expected : 3-A, 2-A , 1-A
        cfTrackDataSize = 1;
        similarTrackDataSize = 1;
        preferGenreSimilarTrackDataSize = 0;
        preferGenrePolularChnlDataSize = 0;
        given(recommendMapper.selectRecommendCfTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(cfTrackDataSize));
        given(recommendMapper.selectRecommendSimilarTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(similarTrackDataSize));
        given(channelService.getHotplayChannelList(anyInt())).willReturn(makeMockHotPlayChannels(defaultPanelTotalSize - (cfTrackDataSize +similarTrackDataSize)));

        List<Panel> panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);
        assertNotNull(panelList);
        assertEquals(3, panelList.size());
        assertThat(panelList.get(0), instanceOf(RcmmdTrackPanel.class));
        assertThat(panelList.get(0).getType(), is(RecommendPanelType.RCMMD_TRACK) );
        assertThat(panelList.get(1), instanceOf(PreferSimilarTrackPanel.class));
        assertThat(panelList.get(1).getType(), is(RecommendPanelType.PREFER_SIMILAR_TRACK) );
        assertThat(panelList.get(2), instanceOf(PopularChannelPanel.class));
        assertThat(panelList.get(2).getType(), is(RecommendPanelType.POPULAR_CHANNEL) );
    }



    @Test
    public void 선호장르_유사곡_미존재_테스트_3(){

        // CASE 3: 3-A, 3-A , 2-A 패널 데이터 존재
        // expected : 3-A, 3-A , 2-A
        cfTrackDataSize = 2;
        similarTrackDataSize = 1;
        preferGenreSimilarTrackDataSize = 0;
        preferGenrePolularChnlDataSize = 0;

        given(recommendMapper.selectRecommendCfTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(cfTrackDataSize));
        given(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(anyLong() , anyObject())).willReturn(makeMockPersonalPhaseMeta(PersonalPhaseType.RECOMMEND , personalPanelList , null));
        given(recommendMapper.selectRecommendSimilarTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(similarTrackDataSize));

        List<Panel> panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);
        assertNotNull(panelList);
        assertEquals(3, panelList.size());
        assertThat(panelList.get(0), instanceOf(RcmmdTrackPanel.class));
        assertThat(panelList.get(0).getType(), is(RecommendPanelType.RCMMD_TRACK) );
        assertThat(panelList.get(1), instanceOf(RcmmdTrackPanel.class));
        assertThat(panelList.get(1).getType(), is(RecommendPanelType.RCMMD_TRACK) );
        assertThat(panelList.get(2), instanceOf(PreferSimilarTrackPanel.class));
        assertThat(panelList.get(2).getType(), is(RecommendPanelType.PREFER_SIMILAR_TRACK) );
    }
    @Test
    public void 선호장르_유사곡_미존재_테스트_4(){
        // CASE 4 : 3-A , 3-A 패널 데이터만 존재
        // expected : 3-A, 3-A , 1-A
        cfTrackDataSize = 2;
        similarTrackDataSize = 0;
        preferGenreSimilarTrackDataSize = 0;
        preferGenrePolularChnlDataSize = 0;
        given(channelService.getHotplayChannelList(anyInt())).willReturn(makeMockHotPlayChannels(defaultPanelTotalSize - cfTrackDataSize));
        given(recommendMapper.selectRecommendCfTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(cfTrackDataSize));

        List<Panel> panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);
        assertNotNull(panelList);
        assertEquals(3, panelList.size());
        assertThat(panelList.get(0), instanceOf(RcmmdTrackPanel.class));
        assertThat(panelList.get(0).getType(), is(RecommendPanelType.RCMMD_TRACK) );
        assertThat(panelList.get(1), instanceOf(RcmmdTrackPanel.class));
        assertThat(panelList.get(1).getType(), is(RecommendPanelType.RCMMD_TRACK) );
        assertThat(panelList.get(2), instanceOf(PopularChannelPanel.class));
        assertThat(panelList.get(2).getType(), is(RecommendPanelType.POPULAR_CHANNEL) );
    }
}
