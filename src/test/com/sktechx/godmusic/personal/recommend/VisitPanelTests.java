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
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.PopularChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.PreferGenrePopularChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.chart.ChartPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPanel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

/**
 * 설명 : 방문 단계 테스트
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 10.
 */
@Slf4j
public class VisitPanelTests extends RecommendTests{


    @BeforeEach
    public void init(){
        given(channelService.getHotplayChannelList(anyInt())).willReturn(makeMockHotPlayChannels(3));
    }

    @Test
    public void GUEST_패널_생성_테스트(){
        given(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(anyLong() , anyObject())).willReturn(makeMockPersonalPhaseMeta(PersonalPhaseType.GUEST , null , null));
        List<Panel> panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);

        assertNotNull(panelList);
        assertEquals(3, panelList.size());
        panelList.stream().forEach(panel -> {
            assertThat(panel, instanceOf(PopularChannelPanel.class));
            assertThat(panel.getType(), is(RecommendPanelType.POPULAR_CHANNEL) );
        });
    }

    @Test
    public void VISIT_패널_기본_테스트(){
        given(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(anyLong() , anyObject())).willReturn(makeMockPersonalPhaseMeta(PersonalPhaseType.VISIT , null , null));

        int preferGenrePopularChnlTotalSize = 3;
        int preferGenrePolularChnlSize = 0;

        given(channelService.getHotplayChannelList(anyInt())).willReturn(makeMockHotPlayChannels(preferGenrePopularChnlTotalSize - preferGenrePolularChnlSize));

        // CASE :  2-C ,1-A' 패널이 없다면 1-A,1-A,1-A
        given(channelService.selectPreferGenrePopularChannel(anyLong())).willReturn(makeMockPreferGenrePopularChnl(0));
        List<Panel> panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);
        assertNotNull(panelList);
        assertEquals(3, panelList.size());
        panelList.stream().forEach(panel -> {
            assertThat(panel, instanceOf(PopularChannelPanel.class));
            assertThat(panel.getType(), is(RecommendPanelType.POPULAR_CHANNEL) );
        });

    }
    @Test
    public void VISIT_패널_선호장르_테스트(){

        int preferGenrePopularChnlTotalSize = 3;
        int preferGenrePolularChnlSize = 1;
        List<PreferGenreDto> preferGenreList  = Arrays.asList(makeMockPreferGenrePopular(3L,"댄스","댄스"));
        given(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(anyLong() , anyObject())).willReturn(makeMockPersonalPhaseMeta(PersonalPhaseType.VISIT, null, preferGenreList));
        given(chartMapper.selectPreferGenrePopularChannel(anyObject())).willReturn(makeMockPreferGenrePopularChnl(preferGenrePolularChnlSize));

        given(channelService.getHotplayChannelList(anyInt())).willReturn(makeMockHotPlayChannels(preferGenrePopularChnlTotalSize - preferGenrePolularChnlSize));

        List<Panel> panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);
        assertNotNull(panelList);
        assertEquals(3, panelList.size());
        assertThat(panelList.get(0), instanceOf(PreferGenrePopularChannelPanel.class));
        assertThat(panelList.get(0).getType(), is(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL) );
        assertThat(panelList.get(1), instanceOf(PopularChannelPanel.class));
        assertThat(panelList.get(1).getType(), is(RecommendPanelType.POPULAR_CHANNEL) );
        assertThat(panelList.get(2), instanceOf(PopularChannelPanel.class));
        assertThat(panelList.get(2).getType(), is(RecommendPanelType.POPULAR_CHANNEL) );

        preferGenrePolularChnlSize = 2 ;
        given(chartMapper.selectPreferGenrePopularChannel(anyObject())).willReturn(makeMockPreferGenrePopularChnl(preferGenrePolularChnlSize));
        given(channelService.getHotplayChannelList(anyInt())).willReturn(makeMockHotPlayChannels(preferGenrePopularChnlTotalSize - preferGenrePolularChnlSize));

        panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);
        assertNotNull(panelList);
        assertEquals(3, panelList.size());
        assertThat(panelList.get(0), instanceOf(PreferGenrePopularChannelPanel.class));
        assertThat(panelList.get(0).getType(), is(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL) );
        assertThat(panelList.get(1), instanceOf(PreferGenrePopularChannelPanel.class));
        assertThat(panelList.get(1).getType(), is(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL) );
        assertThat(panelList.get(2), instanceOf(PopularChannelPanel.class));
        assertThat(panelList.get(2).getType(), is(RecommendPanelType.POPULAR_CHANNEL) );

        preferGenrePolularChnlSize = 3 ;
        given(chartMapper.selectPreferGenrePopularChannel(anyObject())).willReturn(makeMockPreferGenrePopularChnl(preferGenrePolularChnlSize));
        given(channelService.getHotplayChannelList(anyInt())).willReturn(makeMockHotPlayChannels(preferGenrePopularChnlTotalSize - preferGenrePolularChnlSize));

        panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);
        assertNotNull(panelList);
        assertEquals(3, panelList.size());
        assertThat(panelList.get(0), instanceOf(PreferGenrePopularChannelPanel.class));
        assertThat(panelList.get(0).getType(), is(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL) );
        assertThat(panelList.get(1), instanceOf(PreferGenrePopularChannelPanel.class));
        assertThat(panelList.get(1).getType(), is(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL) );
        assertThat(panelList.get(2), instanceOf(PreferGenrePopularChannelPanel.class));
        assertThat(panelList.get(2).getType(), is(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL) );

    }

    @Test
    public void VISIT_패널_아티스트_인기곡_추가_테스트(){
        List<PreferGenreDto> preferGenreList  = Arrays.asList(makeMockPreferGenrePopular(3L,"댄스","댄스"));
        PersonalPanel artistPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_ATST_TR, Arrays.asList(1L));
        given(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(anyLong() , anyObject())).willReturn(makeMockPersonalPhaseMeta(PersonalPhaseType.VISIT, Arrays.asList(artistPanel), preferGenreList));
        given(chartMapper.selectPreferGenrePopularChannel(anyObject())).willReturn(makeMockPreferGenrePopularChnl(3));
        given(recommendMapper.selectRecommendArtistById(anyLong())).willReturn(makeMockRecommendArtistDto());

        List<Panel> panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);
        assertNotNull(panelList);
        assertEquals(4, panelList.size());
        assertThat(panelList.get(0), instanceOf(ArtistPanel.class));
        assertThat(panelList.get(0).getType(), is(RecommendPanelType.ARTIST_POPULAR_TRACK) );
        assertThat(panelList.get(1), instanceOf(PreferGenrePopularChannelPanel.class));
        assertThat(panelList.get(1).getType(), is(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL) );
        assertThat(panelList.get(2), instanceOf(PreferGenrePopularChannelPanel.class));
        assertThat(panelList.get(2).getType(), is(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL) );
        assertThat(panelList.get(3), instanceOf(PreferGenrePopularChannelPanel.class));
        assertThat(panelList.get(3).getType(), is(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL) );
    }


    @Test
    public void VISIT_패널_차트_테스트(){
        PersonalPanel artistPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_ATST_TR, Arrays.asList(1L));
        List<PreferGenreDto> preferGenreList  = Arrays.asList(makeMockPreferGenrePopular(1L,"TOP100","TOP100"));
        given(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(anyLong() , anyObject())).willReturn(makeMockPersonalPhaseMeta(PersonalPhaseType.VISIT, Arrays.asList(artistPanel),preferGenreList));
        given(chartMapper.selectPreferGenrePopularChannel(anyObject())).willReturn(makeMockPreferGenrePopularChnl(3));
        given(recommendMapper.selectRecommendArtistById(anyLong())).willReturn(makeMockRecommendArtistDto());

        given(chartMapper.selectMainPanelChart("LIVE_CHART")).willReturn(makeMockChart(1L,ChartType.DAILY,"실시간 차트"));
        given(chartMapper.selectMainPanelChart("KIDS_CHART")).willReturn(makeMockChart(2L,ChartType.DAILY,"KIDS 차트"));

        List<Panel> panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);
        assertNotNull(panelList);
        assertEquals(5, panelList.size());
        assertThat(panelList.get(0), instanceOf(ChartPanel.class));
        assertThat(panelList.get(0).getType(), is(RecommendPanelType.LIVE_CHART) );
        assertThat(panelList.get(1), instanceOf(ArtistPanel.class));
        assertThat(panelList.get(1).getType(), is(RecommendPanelType.ARTIST_POPULAR_TRACK) );
        assertThat(panelList.get(2), instanceOf(PreferGenrePopularChannelPanel.class));
        assertThat(panelList.get(2).getType(), is(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL) );
        assertThat(panelList.get(3), instanceOf(PreferGenrePopularChannelPanel.class));
        assertThat(panelList.get(3).getType(), is(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL) );
        assertThat(panelList.get(4), instanceOf(PreferGenrePopularChannelPanel.class));
        assertThat(panelList.get(4).getType(), is(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL) );


        preferGenreList  = Arrays.asList(makeMockPreferGenrePopular(1L,"TOP100","TOP100") ,makeMockPreferGenrePopular(2L,"KIDS","KIDS"));
        given(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(anyLong() , anyObject())).willReturn(makeMockPersonalPhaseMeta(PersonalPhaseType.VISIT, Arrays.asList(artistPanel),preferGenreList));

        panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);
        assertNotNull(panelList);
        assertEquals(6, panelList.size());
        assertThat(panelList.get(0), instanceOf(ChartPanel.class));
        assertThat(panelList.get(0).getType(), is(RecommendPanelType.LIVE_CHART) );
        assertThat(panelList.get(1), instanceOf(ArtistPanel.class));
        assertThat(panelList.get(2), instanceOf(PreferGenrePopularChannelPanel.class));
        assertThat(panelList.get(2).getType(), is(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL) );
        assertThat(panelList.get(3), instanceOf(PreferGenrePopularChannelPanel.class));
        assertThat(panelList.get(3).getType(), is(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL) );
        assertThat(panelList.get(4), instanceOf(PreferGenrePopularChannelPanel.class));
        assertThat(panelList.get(4).getType(), is(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL) );
        assertThat(panelList.get(5), instanceOf(ChartPanel.class));
        assertThat(panelList.get(5).getType(), is(RecommendPanelType.KIDS_CHART) );
    }

}
