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
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.chart.ChartPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.PreferGenreSimilarTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.PreferSimilarTrackPanel;
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
 * 설명 : 방문 단계 테스트
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 10.
 */
@Slf4j
public class ListenPanelTests extends RecommendTests{


    @Captor
    ArgumentCaptor<Integer> captor;

    @BeforeEach
    public void init(){
        given(channelService.getHotplayChannelList(anyInt())).willReturn(makeMockHotPlayChannels(3));
    }

    @Test
    public void 전체_패널_대체_테스트(){
        //2-A , 2-B 패널이 아무것도 없을 경우 1-A패널로 대체 되는지 테스트
        given(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(anyLong() , anyObject())).willReturn(makeMockPersonalPhaseMeta(PersonalPhaseType.LISTEN , null , null));
        List<Panel> panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);

        assertNotNull(panelList);
        assertEquals(3, panelList.size());
        panelList.stream().forEach(panel -> {
            assertThat(panel, instanceOf(PopularChannelPanel.class));
        });
    }


    @Test
    public void 유사곡_패널_대체_테스트_1(){
        PersonalPanel personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_SML_TR, Arrays.asList(1L));
        given(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(anyLong() , anyObject())).willReturn(makeMockPersonalPhaseMeta(PersonalPhaseType.LISTEN , Arrays.asList(personalPanel) , null));

        //유사곡 2-A 패널이 하나 있을 경우 , 2-A , 1-A , 1-A
        int recommendTrackSize = 1;
        int preferGenreSimilarTrackPanelSize = 2;
        int listenMoodPopularChannelPanelSize = 1;

        given(channelService.getHotplayChannelList(anyInt())).willReturn(makeMockHotPlayChannels(( preferGenreSimilarTrackPanelSize+listenMoodPopularChannelPanelSize ) - recommendTrackSize));
        given(recommendMapper.selectRecommendSimilarTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(recommendTrackSize));

        //유사곡 패널 1개 대체
        List<Panel> panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);
        verify(channelService).getHotplayChannelList(captor.capture());
        assertEquals(new Integer(2),captor.getValue());

        assertNotNull(panelList);
        assertEquals(3, panelList.size());
        assertThat(panelList.get(0), instanceOf(PreferSimilarTrackPanel.class));
        assertThat(panelList.get(1), instanceOf(PopularChannelPanel.class));
        assertThat(panelList.get(2), instanceOf(PopularChannelPanel.class));

    }
    @Test
    public void 유사곡_패널_대체_테스트_2(){
        PersonalPanel personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_SML_TR, Arrays.asList(1L));

        given(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(anyLong() , anyObject())).willReturn(makeMockPersonalPhaseMeta(PersonalPhaseType.LISTEN , Arrays.asList(personalPanel) , null));

        //유사곡 2-A 패널이 두개 있을 경우 , 2-A , 2-A , 1-A
        int recommendTrackSize = 2;
        int preferGenreSimilarTrackPanelSize = 2;
        int listenMoodPopularChannelPanelSize = 1;

        given(recommendMapper.selectRecommendSimilarTrackListByIdList(anyList(),anyInt())).willReturn(makeMockRecommendTrackDto(recommendTrackSize));
        given(channelService.getHotplayChannelList(anyInt())).willReturn(makeMockHotPlayChannels(( preferGenreSimilarTrackPanelSize+listenMoodPopularChannelPanelSize ) - recommendTrackSize));
        List<Panel> panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);
        verify(channelService).getHotplayChannelList(captor.capture());
        assertEquals(new Integer(1),captor.getValue());


        assertNotNull(panelList);
        assertEquals(3, panelList.size());

        assertThat(panelList.get(0), instanceOf(PreferSimilarTrackPanel.class));
        assertThat(panelList.get(1), instanceOf(PreferSimilarTrackPanel.class));
        assertThat(panelList.get(2), instanceOf(PopularChannelPanel.class));

    }

    @Test
    public void 청취무드_인기채널_패널_대체_테스트(){

        PersonalPanel personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_SML_TR, Arrays.asList(1L));

        List<PersonalPanel> rcmmdPanelList = new ArrayList();
        rcmmdPanelList.add(personalPanel);

        personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_MD_CN, Arrays.asList(1L));
        rcmmdPanelList.add(personalPanel);

        //유사곡 2-A 패널이 한개 있을 경우, 청취무드 인기채널 2-B 하나 있을 경우  , 2-A , 2-B , 1-A
        int recommendTrackSize = 1;
        int listenMoodChnlSize = 1;
        int preferGenreSimilarTrackPanelSize = 2;
        int listenMoodPopularChannelPanelSize = 1;

        given(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(anyLong() , anyObject())).willReturn(makeMockPersonalPhaseMeta(PersonalPhaseType.LISTEN , rcmmdPanelList , null));
        given(recommendMapper.selectRecommendSimilarTrackListByIdList(anyList(),anyInt())).willReturn(makeMockRecommendTrackDto(recommendTrackSize));
        given(recommendMapper.selectRecommendListenMoodChannelListByIdList(anyList(),anyInt())).willReturn(makeMockHotPlayChannels(1));

        given(channelService.getHotplayChannelList(anyInt())).willReturn(makeMockHotPlayChannels(( listenMoodPopularChannelPanelSize+preferGenreSimilarTrackPanelSize ) - ( recommendTrackSize + listenMoodChnlSize )));

        List<Panel> panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);
        assertNotNull(panelList);
        assertEquals(3, panelList.size());

        assertThat(panelList.get(0), instanceOf(PreferSimilarTrackPanel.class));
        assertThat(panelList.get(0).getType(), is(RecommendPanelType.PREFER_SIMILAR_TRACK) );
        assertThat(panelList.get(1), instanceOf(ListenMoodPopularChannelPanel.class));
        assertThat(panelList.get(1).getType(), is(RecommendPanelType.LISTEN_MOOD_POPULAR_CHANNEL) );
        assertThat(panelList.get(2), instanceOf(PopularChannelPanel.class));
        assertThat(panelList.get(2).getType(), is(RecommendPanelType.POPULAR_CHANNEL) );
    }

    @Test
    public void 선호장르_유사곡_패널_테스트(){

        // 2-A' , 2-A , 2-B
        PersonalPanel personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_SML_TR, Arrays.asList(1L));

        List<PersonalPanel> rcmmdPanelList = new ArrayList();
        rcmmdPanelList.add(personalPanel);

        personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_MD_CN, Arrays.asList(1L));
        rcmmdPanelList.add(personalPanel);


        personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_GR_TR, Arrays.asList(1L));
        rcmmdPanelList.add(personalPanel);


        int recommendTrackSize = 1;
        int listenMoodChnlSize = 1;
        int preferGenreTrackSize = 1;

        given(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(anyLong() , anyObject())).willReturn(makeMockPersonalPhaseMeta(PersonalPhaseType.LISTEN , rcmmdPanelList , null));
        given(recommendMapper.selectRecommendSimilarTrackListByIdList(anyList(),anyInt())).willReturn(makeMockRecommendTrackDto(recommendTrackSize));
        given(recommendMapper.selectRecommendListenMoodChannelListByIdList(anyList(), anyInt())).willReturn(makeMockHotPlayChannels(listenMoodChnlSize));
        given(recommendMapper.selectRecommendPreferGenreSimilarTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(preferGenreTrackSize));


        List<Panel> panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);

        assertNotNull(panelList);
        assertEquals(3, panelList.size());

        assertThat(panelList.get(0), instanceOf(PreferGenreSimilarTrackPanel.class));
        assertThat(panelList.get(0).getType(), is(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK) );
        assertThat(panelList.get(1), instanceOf(PreferSimilarTrackPanel.class));
        assertThat(panelList.get(1).getType(), is(RecommendPanelType.PREFER_SIMILAR_TRACK) );
        assertThat(panelList.get(2), instanceOf(ListenMoodPopularChannelPanel.class));
        assertThat(panelList.get(2).getType(), is(RecommendPanelType.LISTEN_MOOD_POPULAR_CHANNEL) );

    }

    @Test
    public void 선호장르_유사곡_패널_테스트2(){
        // 2-A' , 2-A' , 2-B
        PersonalPanel personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_MD_CN, Arrays.asList(1L));

        List<PersonalPanel> rcmmdPanelList = new ArrayList();
        rcmmdPanelList.add(personalPanel);

        personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_GR_TR, Arrays.asList(1L));
        rcmmdPanelList.add(personalPanel);

        int listenMoodChnlSize = 1;
        int preferGenreTrackSize = 2;

        given(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(anyLong() , anyObject())).willReturn(makeMockPersonalPhaseMeta(PersonalPhaseType.LISTEN , rcmmdPanelList , null));
        given(recommendMapper.selectRecommendListenMoodChannelListByIdList(anyList(), anyInt())).willReturn(makeMockHotPlayChannels(listenMoodChnlSize));
        given(recommendMapper.selectRecommendPreferGenreSimilarTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(preferGenreTrackSize));

        List<Panel> panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);

        assertNotNull(panelList);
        assertEquals(3, panelList.size());

        assertThat(panelList.get(0), instanceOf(PreferGenreSimilarTrackPanel.class));
        assertThat(panelList.get(0).getType(), is(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK) );
        assertThat(panelList.get(1), instanceOf(PreferGenreSimilarTrackPanel.class));
        assertThat(panelList.get(1).getType(), is(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK) );
        assertThat(panelList.get(2), instanceOf(ListenMoodPopularChannelPanel.class));
        assertThat(panelList.get(2).getType(), is(RecommendPanelType.LISTEN_MOOD_POPULAR_CHANNEL) );
    }



    @Test
    public void 선호장르_유사곡_패널_테스트3(){
        // 2-A' , 2-A' , 1-A
        PersonalPanel personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_GR_TR, Arrays.asList(1L));

        List<PersonalPanel> rcmmdPanelList = new ArrayList();
        rcmmdPanelList.add(personalPanel);

        int preferGenreTrackSize = 2;
        int preferGenreSimilarTrackPanelSize = 2;
        int listenMoodPopularChannelPanelSize = 1;

        given(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(anyLong() , anyObject())).willReturn(makeMockPersonalPhaseMeta(PersonalPhaseType.LISTEN , rcmmdPanelList , null));
        given(recommendMapper.selectRecommendPreferGenreSimilarTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(preferGenreTrackSize));
        given(channelService.getHotplayChannelList(anyInt())).willReturn(makeMockHotPlayChannels(( preferGenreSimilarTrackPanelSize+listenMoodPopularChannelPanelSize ) - preferGenreTrackSize));

        List<Panel> panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);

        assertNotNull(panelList);
        assertEquals(3, panelList.size());

        assertThat(panelList.get(0), instanceOf(PreferGenreSimilarTrackPanel.class));
        assertThat(panelList.get(0).getType(), is(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK) );
        assertThat(panelList.get(1), instanceOf(PreferGenreSimilarTrackPanel.class));
        assertThat(panelList.get(1).getType(), is(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK) );
        assertThat(panelList.get(2), instanceOf(PopularChannelPanel.class));
        assertThat(panelList.get(2).getType(), is(RecommendPanelType.POPULAR_CHANNEL) );
    }

    @Test
    public void 아티스트_인기곡_추가_테스트(){

        // 2-A' , 2-A', 2-C , 1-A
        PersonalPanel personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_GR_TR, Arrays.asList(1L));
        List<PersonalPanel> rcmmdPanelList = new ArrayList();
        rcmmdPanelList.add(personalPanel);
        personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_ATST_TR, Arrays.asList(1L));
        rcmmdPanelList.add(personalPanel);
        given(recommendMapper.selectRecommendArtistById(anyLong())).willReturn(makeMockRecommendArtistDto());

        int preferGenreTrackSize = 2;
        int preferGenreSimilarTrackPanelSize = 2;
        int listenMoodPopularChannelPanelSize = 1;

        given(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(anyLong() , anyObject())).willReturn(makeMockPersonalPhaseMeta(PersonalPhaseType.LISTEN , rcmmdPanelList , null));
        given(recommendMapper.selectRecommendPreferGenreSimilarTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(preferGenreTrackSize));
        given(channelService.getHotplayChannelList(anyInt())).willReturn(makeMockHotPlayChannels(( preferGenreSimilarTrackPanelSize+listenMoodPopularChannelPanelSize ) - preferGenreTrackSize));

        List<Panel> panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);

        assertNotNull(panelList);
        assertEquals(4, panelList.size());

        assertThat(panelList.get(0), instanceOf(PreferGenreSimilarTrackPanel.class));
        assertThat(panelList.get(0).getType(), is(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK) );
        assertThat(panelList.get(1), instanceOf(PreferGenreSimilarTrackPanel.class));
        assertThat(panelList.get(1).getType(), is(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK) );

        assertThat(panelList.get(2), instanceOf(ArtistPanel.class));
        assertThat(panelList.get(2).getType(), is(RecommendPanelType.ARTIST_POPULAR_TRACK) );

        assertThat(panelList.get(3), instanceOf(PopularChannelPanel.class));
        assertThat(panelList.get(3).getType(), is(RecommendPanelType.POPULAR_CHANNEL) );


    }

    @Test
    public void 차트_추가_테스트(){

        //TOP100, 2-A' , 2-A', 2-C , 2-B , KIDS
        PersonalPanel personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_GR_TR, Arrays.asList(1L));
        List<PersonalPanel> rcmmdPanelList = new ArrayList();
        rcmmdPanelList.add(personalPanel);
        personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_ATST_TR, Arrays.asList(1L));
        rcmmdPanelList.add(personalPanel);

        personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_MD_CN, Arrays.asList(1L));
        rcmmdPanelList.add(personalPanel);
        given(recommendMapper.selectRecommendListenMoodChannelListByIdList(anyList(), anyInt())).willReturn(makeMockHotPlayChannels(1));
        given(recommendMapper.selectRecommendArtistById(anyLong())).willReturn(makeMockRecommendArtistDto());
        List<PreferGenreDto> preferGenreList  = Arrays.asList(makeMockPreferGenrePopular(1L,"TOP100","TOP100"),makeMockPreferGenrePopular(2L,"KIDS","KIDS"));

        given(chartMapper.selectMainPanelChart("LIVE_CHART")).willReturn(makeMockChart(1L,ChartType.DAILY,"실시간 차트"));
        given(chartMapper.selectMainPanelChart("KIDS_CHART")).willReturn(makeMockChart(2L,ChartType.DAILY,"KIDS 차트"));

        int preferGenreTrackSize = 2;
        int preferGenreSimilarTrackPanelSize = 2;
        int listenMoodPopularChannelPanelSize = 1;

        given(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(anyLong() , anyObject())).willReturn(makeMockPersonalPhaseMeta(PersonalPhaseType.LISTEN , rcmmdPanelList , preferGenreList));
        given(recommendMapper.selectRecommendPreferGenreSimilarTrackListByIdList(anyList(), anyInt())).willReturn(makeMockRecommendTrackDto(preferGenreTrackSize));
        given(channelService.getHotplayChannelList(anyInt())).willReturn(makeMockHotPlayChannels(( preferGenreSimilarTrackPanelSize+listenMoodPopularChannelPanelSize ) - preferGenreTrackSize));

        List<Panel> panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);

        assertNotNull(panelList);
        assertEquals(6, panelList.size());

        assertThat(panelList.get(0), instanceOf(ChartPanel.class));
        assertThat(panelList.get(0).getType(), is(RecommendPanelType.LIVE_CHART) );

        assertThat(panelList.get(1), instanceOf(PreferGenreSimilarTrackPanel.class));
        assertThat(panelList.get(1).getType(), is(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK) );
        assertThat(panelList.get(2), instanceOf(PreferGenreSimilarTrackPanel.class));
        assertThat(panelList.get(2).getType(), is(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK) );

        assertThat(panelList.get(3), instanceOf(ArtistPanel.class));
        assertThat(panelList.get(3).getType(), is(RecommendPanelType.ARTIST_POPULAR_TRACK) );

        assertThat(panelList.get(4), instanceOf(ListenMoodPopularChannelPanel.class));
        assertThat(panelList.get(4).getType(), is(RecommendPanelType.LISTEN_MOOD_POPULAR_CHANNEL) );

        assertThat(panelList.get(5), instanceOf(ChartPanel.class));
        assertThat(panelList.get(5).getType(), is(RecommendPanelType.KIDS_CHART) );

    }


}
