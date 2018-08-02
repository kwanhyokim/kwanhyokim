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

import com.sktechx.godmusic.personal.common.domain.type.*;
import com.sktechx.godmusic.personal.rest.model.dto.PreferGenreDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.artist.ArtistPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.ListenMoodPopularChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.PopularChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.PreferGenrePopularChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.chart.ChartPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.PreferGenreSimilarTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.PreferSimilarTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.RcmmdTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPanel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

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
 * 설명 : 추천 단계 테스트
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 10.
 */
@Slf4j
public class RecommendPanelPreferGenreTests extends RecommendTests{
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
        personalPanelList.add(makeMockPersonalPanel(RecommendPanelContentType.RC_CF_TR, Arrays.asList(1L)));
        personalPanelList.add(makeMockPersonalPanel(RecommendPanelContentType.RC_GR_TR, Arrays.asList(1L)));
        personalPanelList.add(makeMockPersonalPanel(RecommendPanelContentType.RC_SML_TR, Arrays.asList(1L)));
        personalPanelList.add(makeMockPersonalPanel(RecommendPanelContentType.RC_ATST_TR, Arrays.asList(1L)));

        given(channelService.getHotplayChannelList(anyInt())).willReturn(makeMockHotPlayChannels(3));

    }

    @Test
    public void 전체_패널_대체_테스트(){
        // CASE : 추천 데이터가 없을 경우
        // expected : 1-A, 1-A, 1-A
        given(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(anyLong() , anyObject())).willReturn(makeMockPersonalPhaseMeta(PersonalPhaseType.RECOMMEND , null , null));
        List<Panel> panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);

        assertNotNull(panelList);
        assertEquals(3, panelList.size());
        panelList.stream().forEach(panel -> {
            assertThat(panel, instanceOf(PopularChannelPanel.class));
            assertThat(panel.getType(), is(RecommendPanelType.POPULAR_CHANNEL));
        });
    }


    @Test
    public void 선호장르_유사곡_존재_테스트_1(){
        // CASE 1 : 3-A , 3-A, 2-A' 패널 데이터 존재
        // expected : 3-A, 3-A , 2-A'

        cfTrackDataSize = 2;
        similarTrackDataSize = 0;
        preferGenreSimilarTrackDataSize = 1;
        preferGenrePolularChnlDataSize = 0;
        given(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(anyLong() , anyObject())).willReturn(makeMockPersonalPhaseMeta(PersonalPhaseType.RECOMMEND , personalPanelList , null));
        given(recommendMapper.selectRecommendCfTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(cfTrackDataSize));
        given(recommendMapper.selectRecommendPreferGenreSimilarTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(preferGenreSimilarTrackDataSize));

        List<Panel>  panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);
        assertNotNull(panelList);
        assertEquals(3, panelList.size());
        assertThat(panelList.get(0), instanceOf(RcmmdTrackPanel.class));
        assertThat(panelList.get(0).getType(), is(RecommendPanelType.RCMMD_TRACK) );
        assertThat(panelList.get(1), instanceOf(RcmmdTrackPanel.class));
        assertThat(panelList.get(1).getType(), is(RecommendPanelType.RCMMD_TRACK) );
        assertThat(panelList.get(2), instanceOf(PreferGenreSimilarTrackPanel.class));
        assertThat(panelList.get(2).getType(), is(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK) );
    }

    @Test
    public void 선호장르_유사곡_존재_테스트_2(){
        // CASE 2 : 3-A , 2-A', 2-A' 패널 데이터 존재
        // expected : 3-A, 2-A' , 2-A' , 1-A
        cfTrackDataSize = 1;
        preferGenreSimilarTrackDataSize = 2;
        similarTrackDataSize = 0;
        preferGenrePolularChnlDataSize = 0;

        given(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(anyLong() , anyObject())).willReturn(makeMockPersonalPhaseMeta(PersonalPhaseType.RECOMMEND , personalPanelList , null));
        given(recommendMapper.selectRecommendCfTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(cfTrackDataSize));
        given(recommendMapper.selectRecommendPreferGenreSimilarTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(preferGenreSimilarTrackDataSize));
        given(channelService.getHotplayChannelList(anyInt())).willReturn(makeMockHotPlayChannels(1));

        List<Panel> panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);
        assertNotNull(panelList);
        assertEquals(4, panelList.size());
        assertThat(panelList.get(0), instanceOf(RcmmdTrackPanel.class));
        assertThat(panelList.get(0).getType(), is(RecommendPanelType.RCMMD_TRACK) );
        assertThat(panelList.get(1), instanceOf(PreferGenreSimilarTrackPanel.class));
        assertThat(panelList.get(1).getType(), is(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK) );
        assertThat(panelList.get(2), instanceOf(PreferGenreSimilarTrackPanel.class));
        assertThat(panelList.get(2).getType(), is(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK) );
        assertThat(panelList.get(3), instanceOf(PopularChannelPanel.class));
        assertThat(panelList.get(3).getType(), is(RecommendPanelType.POPULAR_CHANNEL) );
    }

    @Test
    public void 선호장르_유사곡_존재_테스트_3(){
        // CASE 3 : 3-A , 2-A, 2-A' 패널 데이터 존재
        // expected : 3-A, 2-A',2-A
        cfTrackDataSize = 1;
        preferGenreSimilarTrackDataSize = 1;
        similarTrackDataSize = 1;
        preferGenrePolularChnlDataSize = 0;

        given(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(anyLong() , anyObject())).willReturn(makeMockPersonalPhaseMeta(PersonalPhaseType.RECOMMEND , personalPanelList , null));
        given(recommendMapper.selectRecommendCfTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(cfTrackDataSize));
        given(recommendMapper.selectRecommendPreferGenreSimilarTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(preferGenreSimilarTrackDataSize));
        given(recommendMapper.selectRecommendSimilarTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(similarTrackDataSize));

        given(channelService.getHotplayChannelList(anyInt())).willReturn(makeMockHotPlayChannels(1));

        List<Panel> panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);
        assertNotNull(panelList);
        assertEquals(3, panelList.size());
        assertThat(panelList.get(0), instanceOf(RcmmdTrackPanel.class));
        assertThat(panelList.get(0).getType(), is(RecommendPanelType.RCMMD_TRACK) );
        assertThat(panelList.get(1), instanceOf(PreferGenreSimilarTrackPanel.class));
        assertThat(panelList.get(1).getType(), is(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK) );
        assertThat(panelList.get(2), instanceOf(PreferSimilarTrackPanel.class));
        assertThat(panelList.get(2).getType(), is(RecommendPanelType.PREFER_SIMILAR_TRACK) );
    }
    @Test
    public void 선호장르_유사곡_존재_테스트_4(){
        // CASE 4 : 3-A , 2-A, 2-A', 2-A' 패널 데이터 존재
        // expected : 3-A, 2-A',2-A' ,2-A
        cfTrackDataSize = 1;
        preferGenreSimilarTrackDataSize = 2;
        similarTrackDataSize = 1;

        given(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(anyLong() , anyObject())).willReturn(makeMockPersonalPhaseMeta(PersonalPhaseType.RECOMMEND , personalPanelList , null));
        given(recommendMapper.selectRecommendCfTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(cfTrackDataSize));
        given(recommendMapper.selectRecommendPreferGenreSimilarTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(preferGenreSimilarTrackDataSize));
        given(recommendMapper.selectRecommendSimilarTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(similarTrackDataSize));

        given(channelService.getHotplayChannelList(anyInt())).willReturn(makeMockHotPlayChannels(1));

        List<Panel> panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);
        assertNotNull(panelList);
        assertEquals(4, panelList.size());
        assertThat(panelList.get(0), instanceOf(RcmmdTrackPanel.class));
        assertThat(panelList.get(0).getType(), is(RecommendPanelType.RCMMD_TRACK) );
        assertThat(panelList.get(1), instanceOf(PreferGenreSimilarTrackPanel.class));
        assertThat(panelList.get(1).getType(), is(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK) );
        assertThat(panelList.get(2), instanceOf(PreferGenreSimilarTrackPanel.class));
        assertThat(panelList.get(2).getType(), is(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK) );
        assertThat(panelList.get(3), instanceOf(PreferSimilarTrackPanel.class));
        assertThat(panelList.get(3).getType(), is(RecommendPanelType.PREFER_SIMILAR_TRACK) );
    }

    @Test
    public void 선호장르_인기채널_대체_테스트(){

        // CASE 1 : 3-A 패널데이터 및  1-A' 선호장르 인기채널 1개 존재
        // expected : 3-A, 1-A' , 1-A
        List<PreferGenreDto> preferGenreList  = Arrays.asList(makeMockPreferGenrePopular(1L,"댄스","댄스"));

        preferGenrePolularChnlDataSize = 1;
        cfTrackDataSize = 1;
        defaultPanelTotalSize =  3;
        similarTrackDataSize = 1;
        preferGenreSimilarTrackDataSize =0;
        given(chartMapper.selectPreferGenrePopularChannel(anyObject())).willReturn(makeMockPreferGenrePopularChnl(preferGenrePolularChnlDataSize));
        given(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(anyLong() , anyObject())).willReturn(makeMockPersonalPhaseMeta(PersonalPhaseType.RECOMMEND , personalPanelList , preferGenreList));
        given(recommendMapper.selectRecommendCfTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(cfTrackDataSize));

        given(channelService.getHotplayChannelList(anyInt())).willReturn(makeMockHotPlayChannels(defaultPanelTotalSize - (preferGenrePolularChnlDataSize+cfTrackDataSize)));

        List<Panel>  panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);
        assertNotNull(panelList);
        assertEquals(3, panelList.size());
        assertThat(panelList.get(0), instanceOf(RcmmdTrackPanel.class));
        assertThat(panelList.get(0).getType(), is(RecommendPanelType.RCMMD_TRACK) );
        assertThat(panelList.get(1), instanceOf(PreferGenrePopularChannelPanel.class));
        assertThat(panelList.get(1).getType(), is(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL) );
        assertThat(panelList.get(2), instanceOf(PopularChannelPanel.class));
        assertThat(panelList.get(2).getType(), is(RecommendPanelType.POPULAR_CHANNEL) );


        // CASE 2 : 3-A 패널데이터 및  1-A' 선호장르 인기채널 2개 존재
        // expected : 3-A, 1-A' , 1-A'

        preferGenrePolularChnlDataSize = 2;
        cfTrackDataSize = 1;
        given(chartMapper.selectPreferGenrePopularChannel(anyObject())).willReturn(makeMockPreferGenrePopularChnl(preferGenrePolularChnlDataSize));
        given(recommendMapper.selectRecommendCfTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(cfTrackDataSize));

        given(channelService.getHotplayChannelList(anyInt())).willReturn(makeMockHotPlayChannels(defaultPanelTotalSize - (preferGenrePolularChnlDataSize+cfTrackDataSize)));

        panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);
        assertNotNull(panelList);
        assertEquals(3, panelList.size());
        assertThat(panelList.get(0), instanceOf(RcmmdTrackPanel.class));
        assertThat(panelList.get(0).getType(), is(RecommendPanelType.RCMMD_TRACK) );
        assertThat(panelList.get(1), instanceOf(PreferGenrePopularChannelPanel.class));
        assertThat(panelList.get(1).getType(), is(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL) );
        assertThat(panelList.get(2), instanceOf(PreferGenrePopularChannelPanel.class));
        assertThat(panelList.get(2).getType(), is(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL) );



        // CASE 3 : 3-A , 2-A 패널데이터 및  1-A' 선호장르 인기채널 1개 존재
        // expected : 3-A, 2-A , 1-A'
        preferGenrePolularChnlDataSize = 1;
        cfTrackDataSize = 1;
        similarTrackDataSize = 1;
        given(recommendMapper.selectRecommendSimilarTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(similarTrackDataSize));
        given(chartMapper.selectPreferGenrePopularChannel(anyObject())).willReturn(makeMockPreferGenrePopularChnl(preferGenrePolularChnlDataSize));
        given(recommendMapper.selectRecommendCfTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(cfTrackDataSize));

        panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);
        assertNotNull(panelList);
        assertEquals(3, panelList.size());
        assertThat(panelList.get(0), instanceOf(RcmmdTrackPanel.class));
        assertThat(panelList.get(0).getType(), is(RecommendPanelType.RCMMD_TRACK) );
        assertThat(panelList.get(1), instanceOf(PreferSimilarTrackPanel.class));
        assertThat(panelList.get(1).getType(), is(RecommendPanelType.PREFER_SIMILAR_TRACK) );
        assertThat(panelList.get(2), instanceOf(PreferGenrePopularChannelPanel.class));
        assertThat(panelList.get(2).getType(), is(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL) );


        // CASE 4 : 3-A , 2-A' 패널데이터 및  1-A' 선호장르 인기채널 2개 존재
        // expected : 3-A, 2-A' , 1-A', 1-A'
        preferGenrePolularChnlDataSize = 2;
        cfTrackDataSize = 1;
        preferGenreSimilarTrackDataSize =1;
        similarTrackDataSize = 0;
        given(recommendMapper.selectRecommendSimilarTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(similarTrackDataSize));
        given(chartMapper.selectPreferGenrePopularChannel(anyObject())).willReturn(makeMockPreferGenrePopularChnl(preferGenrePolularChnlDataSize));
        given(recommendMapper.selectRecommendCfTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(cfTrackDataSize));
        given(recommendMapper.selectRecommendPreferGenreSimilarTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(preferGenreSimilarTrackDataSize));

        panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);
        assertNotNull(panelList);
        assertEquals(4, panelList.size());
        assertThat(panelList.get(0), instanceOf(RcmmdTrackPanel.class));
        assertThat(panelList.get(0).getType(), is(RecommendPanelType.RCMMD_TRACK) );
        assertThat(panelList.get(1), instanceOf(PreferGenreSimilarTrackPanel.class));
        assertThat(panelList.get(1).getType(), is(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK) );
        assertThat(panelList.get(2), instanceOf(PreferGenrePopularChannelPanel.class));
        assertThat(panelList.get(2).getType(), is(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL) );
        assertThat(panelList.get(2), instanceOf(PreferGenrePopularChannelPanel.class));
        assertThat(panelList.get(2).getType(), is(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL) );



    }


}
