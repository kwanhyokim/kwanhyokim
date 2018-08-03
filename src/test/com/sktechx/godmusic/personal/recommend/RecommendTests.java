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

import com.sktechx.godmusic.personal.CommonTest;
import com.sktechx.godmusic.personal.common.domain.type.ChartType;
import com.sktechx.godmusic.personal.common.domain.type.PersonalPhaseType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.model.dto.*;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.PreferGenrePopularChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhase;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.repository.ChannelMapper;
import com.sktechx.godmusic.personal.rest.repository.ChartMapper;
import com.sktechx.godmusic.personal.rest.repository.RecommendMapper;
import com.sktechx.godmusic.personal.rest.repository.RecommendMapper;
import com.sktechx.godmusic.personal.rest.service.ChannelService;
import com.sktechx.godmusic.personal.rest.service.impl.recommend.RecommendPanelAssemblyFactory;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendPanelService;
import com.sktechx.godmusic.personal.rest.service.recommend.phase.PersonalRecommendPhaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 설명 : 추천 테스트
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 27.
 */
public class RecommendTests extends CommonTest {
    @Autowired
    protected RecommendPanelService recommendPanelService;

    @MockBean
    protected PersonalRecommendPhaseService personalRecommendPhaseService;

    @MockBean
    protected ChannelService channelService;

    @MockBean
    protected ChannelMapper channelMapper;

    @MockBean
    protected RecommendMapper recommendMapper;

    @Autowired
    protected RecommendPanelAssemblyFactory recommendPanelAssemblyFactory;

    @MockBean
    protected ChartMapper chartMapper;

    protected CharacterPreferGenreDto makeMockPreferGenrePopular(Long id, String name , String genreType){
        CharacterPreferGenreDto preferGenreDto = new CharacterPreferGenreDto();
        preferGenreDto.setPreferGenreId(id);
        preferGenreDto.setPreferGenreNm(name);
        preferGenreDto.setPreferType(genreType);
        return preferGenreDto;
    }
    protected List<PreferGenrePopularChnlDto> makeMockPreferGenrePopularChnl(int size){
        List<PreferGenrePopularChnlDto> chnlList = new ArrayList();
        for(int i = 0 ; i < size ; i++){
            PreferGenrePopularChnlDto chnlDto=  new PreferGenrePopularChnlDto();

            ChnlDto chnl = new ChnlDto();
            chnl.setChnlId(new Long(i+1));
            chnl.setChnlNm("인기채널 "+(i+1));
            chnl.setChnlDispNm("인기\n채널 "+(i+1));

            chnlDto.setPopularChannel(chnl);
            chnlDto.setCharacterNo(new Long(i+1));
            chnlDto.setPreferGenreId(new Long(i+1));
            chnlList.add(chnlDto);

        }
        return chnlList;
    }

    protected ChartDto makeMockChart(Long chartId, ChartType chartType , String chartName){
        ChartDto chartDto = new ChartDto();
        chartDto.setChartId(chartId);
        chartDto.setChartType(chartType);
        chartDto.setChartNm(chartName);

        return chartDto;
    }
    protected List<RecommendTrackDto> makeMockRecommendTrackDto(int size){
        List<RecommendTrackDto> recommendTrackList = new ArrayList<>();

        for(int i = 0; i < size ; i++){

            RecommendTrackDto trackDto = new RecommendTrackDto();
            trackDto.setRcmmdId(new Long(i+1));
            trackDto.setTrackList(makeMockTrackList());
            trackDto.setSvcGenreDto(new ServiceGenreDto(new Long(i+1),"장르"));
            trackDto.setCreateDtime(new Date());

            recommendTrackList.add(trackDto);
        }

        return recommendTrackList;
    }
    protected RecommendArtistDto makeMockRecommendArtistDto(){
        RecommendArtistDto artistDto = new RecommendArtistDto();
        artistDto.setRcmmdArtistId(1L);

        ArtistDto artist = new ArtistDto();

        artist.setArtistId(1L);
        artist.setArtistNm("name");
        artist.setImgList(Arrays.asList(new ImageDto()));

        artistDto.setArtistList(Arrays.asList(artist,new ArtistDto(),new ArtistDto()));
        return artistDto;
    }
    protected PersonalPhaseMeta makeMockPersonalPhaseMeta(PersonalPhaseType personalPhaseType , List<PersonalPanel> personalpanelList , List<CharacterPreferGenreDto> preferGenreList){
        PersonalPhaseMeta phaseMeta = new PersonalPhaseMeta();
        PersonalPhase personalPhase = new PersonalPhase();
        personalPhase.setPhaseType(personalPhaseType);
        personalPhase.setAvaliableDateTime(new Date());

        phaseMeta.setPersonalPhaseList(Arrays.asList(personalPhase));
        phaseMeta.setRcmmdPanelList(personalpanelList);
        phaseMeta.setPreferGenreList(preferGenreList);
        return phaseMeta;
    }

    protected List<TrackDto> makeMockTrackList(){
        List<TrackDto> trackList = new ArrayList<>();

        TrackDto track =new TrackDto();
        track.setTrackId(1L);
        track.setTrackNm("인기 노래 1");

        trackList.add(track);

        track =new TrackDto();
        track.setTrackId(2L);
        track.setTrackNm("인기 노래 2");

        trackList.add(track);

        track =new TrackDto();
        track.setTrackId(3L);
        track.setTrackNm("인기 노래 3");

        trackList.add(track);

        return trackList;
    }
    protected List<ChnlDto> makeMockHotPlayChannels(int size){
        List<ChnlDto> hotplayList = new ArrayList<>();


        for(int i = 0 ;  i < size ; i++){
            ChnlDto chnl = new ChnlDto();
            chnl.setChnlId(new Long(i));
            chnl.setChnlNm("인기채널 "+i);
            chnl.setChnlDispNm("인기\n채널 "+i);

            hotplayList.add(chnl);
        }

        return hotplayList;
    }

    protected PersonalPanel makeMockPersonalPanel(RecommendPanelContentType panelContentType , Long rcmmdId){
        PersonalPanel personalPanel = new PersonalPanel();
        personalPanel.setRecommendPanelContentType(panelContentType);
        personalPanel.setRecommendId(rcmmdId);

        return personalPanel;
    }
}
