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
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhase;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.repository.ChannelMapper;
import com.sktechx.godmusic.personal.rest.repository.ChartMapper;
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

    protected PreferGenreDto makeMockPreferGenrePopular(Long id, String name , String genreType){
        PreferGenreDto preferGenreDto = new PreferGenreDto();
        preferGenreDto.setPreferGenreId(id);
        preferGenreDto.setPreferGenreNm(name);
        preferGenreDto.setPreferGenreType(genreType);
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
    protected PersonalPhaseMeta makeMockPersonalPhaseMeta(PersonalPhaseType personalPhaseType , List<PersonalPanel> personalpanelList , List<PreferGenreDto> preferGenreList){
        PersonalPhaseMeta phaseMeta = new PersonalPhaseMeta();
        phaseMeta.setPhaseList(Arrays.asList( new PersonalPhase(personalPhaseType,new Date())));
        phaseMeta.setRcmmdPanelList(personalpanelList);
        phaseMeta.setPreferGenreList(preferGenreList);
        return phaseMeta;
    }
    protected PersonalPanel makeMockPersonalPanel(RecommendPanelContentType recommendPanelContentType , int dispSn, List<Long> recommendIdList){
        PersonalPanel personalPanel = new PersonalPanel();
        personalPanel.setRecommendIdList(recommendIdList);
        personalPanel.setDispSn(dispSn);
        personalPanel.setRecommendPanelContentType(recommendPanelContentType);
        return personalPanel;
    }
    protected List<ChnlDto> makeMockHotPlayChannels(){
        List<ChnlDto> hotplayList = new ArrayList<>();

        ChnlDto chnl = new ChnlDto();
        chnl.setChnlId(1L);
        chnl.setChnlNm("인기채널 1");
        chnl.setChnlDispNm("인기\n채널 1");

        hotplayList.add(chnl);
        chnl = new ChnlDto();
        chnl.setChnlId(2L);
        chnl.setChnlNm("인기채널 2");
        chnl.setChnlDispNm("인기\n채널 2");

        hotplayList.add(chnl);
        chnl = new ChnlDto();
        chnl.setChnlId(3L);
        chnl.setChnlNm("인기채널 3");
        chnl.setChnlDispNm("인기\n채널 3");
        hotplayList.add(chnl);

        return hotplayList;
    }
}
