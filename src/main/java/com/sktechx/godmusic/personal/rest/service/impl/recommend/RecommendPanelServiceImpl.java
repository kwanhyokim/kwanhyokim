/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.impl.recommend;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.personal.common.domain.type.*;
import com.sktechx.godmusic.personal.rest.model.dto.*;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.MyMostTrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.artist.ArtistPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.ChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.ListenMoodPopularChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.PopularChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.PreferGenrePopularChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.chart.ChartPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.GenreVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.PreferGenreSimilarTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.PreferSimilarTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.RcmmdTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.TrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.repository.ArtistMapper;
import com.sktechx.godmusic.personal.rest.repository.ChannelMapper;
import com.sktechx.godmusic.personal.rest.repository.TrackMapper;
import com.sktechx.godmusic.personal.rest.service.ChartService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendPanelService;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelAssembly;
import com.sktechx.godmusic.personal.rest.service.recommend.phase.PersonalRecommendPhaseService;
import lombok.extern.slf4j.Slf4j;

import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.sql.Wrapper;
import java.util.*;

/**
 * 설명 : 추천 패널 데이터 생성
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
@Service
@Slf4j
public class RecommendPanelServiceImpl implements RecommendPanelService {

    @Autowired
    private ChartService chartService;

    @Autowired
    private ChannelMapper channelMapper;


    @Autowired
    private ArtistMapper artistMapper;

    @Autowired
    private TrackMapper trackMapper;

    @Autowired
    private PersonalRecommendPhaseService personalRecommendPhaseService;

    @Autowired
    private RecommendPanelAssemblyFactory recommendPanelAssemblyFactory;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public List<Panel> createRecommendPanelList(Long characterNo , OsType osType) {
        PersonalPhaseMeta personalPhaseMeta = personalRecommendPhaseService.getPersonalRecommendPhaseMeta(characterNo, osType);

        PanelAssembly panelAssembly = recommendPanelAssemblyFactory.getRecommendPanelAssembly(personalPhaseMeta.getFirstPhaseType());

        return panelAssembly.assembleRecommendPanel(personalPhaseMeta);
    }

    @Override
    public List<ImageDto> getPanelBackgroundImageList(RecommendPanelType recommendPanelType , OsType osType){
        //TODO : 패널 별 기본 배경이미지 GET ( 캐시 관리 필요 )
        return Arrays.asList(new ImageDto(),new ImageDto());
    }


    @Override
    public List<Panel> createMockupRecommendPanelList() {

        List<Panel> mockPanelList = new ArrayList<>();

        try{

            //TODO : mockup 데이터 생성
            ChartDto liveChart = chartService.getLiveChart();

            liveChart.setChartType(ChartType.RTIME);
            ChartPanel liveChartPanel = new ChartPanel(RecommendPanelType.LIVE_CHART, liveChart, makePanelBackGroundImageList("https://api3-dev.musicmates.co.kr/img/recommend/new_poc/image_top_100_1.png"));
            mockPanelList.add(liveChartPanel);


            ChnlDto popularChannel =channelMapper.selectChannelById(983L);
            popularChannel.setChnlNm("들으면 힘을 주는 CCM BEST ");
            popularChannel.setChnlDispNm("들으면 힘을 주는 \nCCM BEST ");
            popularChannel.setTrackCount(channelMapper.selectChannelTrackCount(983L));
            ChannelPanel popularChannelPanel = new PopularChannelPanel(RecommendPanelType.POPULAR_CHANNEL, popularChannel , makePanelBackGroundImageList("https://api3-dev.musicmates.co.kr/img/recommend/new_poc/1_a_2_b_2_line.png"));
            mockPanelList.add(popularChannelPanel);


            popularChannel =channelMapper.selectChannelById(18401L);
            popularChannel.setChnlNm("꽃보다 할배 리턴즈 BGM #4");
            popularChannel.setChnlDispNm("꽃보다 할배 리턴즈 \nBGM #4");
            popularChannel.setTrackCount(channelMapper.selectChannelTrackCount(18401L));
            popularChannelPanel = new PopularChannelPanel(RecommendPanelType.POPULAR_CHANNEL, popularChannel , makePanelBackGroundImageList("https://api3-dev.musicmates.co.kr/img/recommend/new_poc/1_a_2_b_2_line.png"));
            mockPanelList.add(popularChannelPanel);

            popularChannel =channelMapper.selectChannelById(18400L);
            popularChannel.setChnlNm("한적한 시골길 드라이브를 위한 재즈");
            popularChannel.setChnlDispNm("한적한 시골길 드라이브를\n 위한 재즈");
            popularChannel.setTrackCount(channelMapper.selectChannelTrackCount(18400L));
            popularChannelPanel = new PopularChannelPanel(RecommendPanelType.POPULAR_CHANNEL, popularChannel , makePanelBackGroundImageList("https://api3-dev.musicmates.co.kr/img/recommend/new_poc/1_a_2_b_2_line.png"));
            mockPanelList.add(popularChannelPanel);

            ChnlDto preferPopularGenreChannel = channelMapper.selectChannelById(18399L);
            preferPopularGenreChannel.setChnlNm("팝의 여제, 비욘세 필청 트랙 모음");
            preferPopularGenreChannel.setChnlDispNm("팝의 여제, 비욘세 \n필청 트랙 모음");
            preferPopularGenreChannel.setTrackCount(channelMapper.selectChannelTrackCount(18399L));
            GenreVo danceGenre = makeGenre(1L, "POP");
            ChannelPanel preferPopularGenreChannelPanel = new PreferGenrePopularChannelPanel(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL, preferPopularGenreChannel,danceGenre , makePanelBackGroundImageList("https://api3-dev.musicmates.co.kr/img/recommend/new_poc/1_a_2_b_3_line.png") );
            mockPanelList.add(preferPopularGenreChannelPanel);

            preferPopularGenreChannel = channelMapper.selectChannelById(18398L);
            preferPopularGenreChannel.setChnlNm("쿨한 느낌이 매력적인 드라이빙 트렌디");
            preferPopularGenreChannel.setChnlDispNm("쿨한 느낌이 매력적인\n드라이빙 트렌디");
            preferPopularGenreChannel.setTrackCount(channelMapper.selectChannelTrackCount(18398L));
            GenreVo traditionalGenre = makeGenre(2L, "국악");
            preferPopularGenreChannelPanel = new PreferGenrePopularChannelPanel(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL, preferPopularGenreChannel,traditionalGenre , makePanelBackGroundImageList("https://api3-dev.musicmates.co.kr/img/recommend/new_poc/1_a_2_b_3_line.png") );

            mockPanelList.add(preferPopularGenreChannelPanel);

            ChnlDto listenMoodPolularChannel = channelMapper.selectChannelById(18397L);
            listenMoodPolularChannel.setChnlNm("커피 한 잔과 함께 낮잠이 고플 때");
            listenMoodPolularChannel.setChnlDispNm("커피 한 잔과 함께\n낮잠이 고플 때");
            listenMoodPolularChannel.setTrackCount(channelMapper.selectChannelTrackCount(18397L));

            ChannelPanel listenMoodPopularChannelPanel = new ListenMoodPopularChannelPanel(RecommendPanelType.LISTEN_MOOD_POPULAR_CHANNEL, listenMoodPolularChannel, makePanelBackGroundImageList("https://api3-dev.musicmates.co.kr/img/recommend/new_poc/1_a_2_b_3_line.png") );
            mockPanelList.add(listenMoodPopularChannelPanel);


            Panel artistPanel = new ArtistPanel(RecommendPanelType.ARTIST_POPULAR_TRACK , makeMockRecommendArtistDto());
            mockPanelList.add(artistPanel);


            TrackPanel preferSimilarTrack = new PreferSimilarTrackPanel(RecommendPanelType.PREFER_SIMILAR_TRACK,makeMockRecommendTrackDto(222L,makeSvcGenre(3L,"댄스"),Arrays.asList(80419008L,80419007L,80419006L,80419005L,80419004L,80419003L,80419002L,80419001L,80419000L,2091L,2092L,2093L,2094L,2100L,2101L)),makePanelBackGroundImageList("https://api3-dev.musicmates.co.kr/img/recommend/new_poc/image_likeu_1.png"));
            mockPanelList.add(preferSimilarTrack);

            preferSimilarTrack = new PreferSimilarTrackPanel(RecommendPanelType.PREFER_SIMILAR_TRACK,makeMockRecommendTrackDto(223L,makeSvcGenre(4L,"힙합"),Arrays.asList(2091L,2092L,2093L,2094L,2100L,2101L,80419008L,80419007L,80419006L,80419005L,80419004L,80419003L,80419002L,80419001L,80419000L)),makePanelBackGroundImageList("https://api3-dev.musicmates.co.kr/img/recommend/new_poc/image_likeu_1.png"));
            mockPanelList.add(preferSimilarTrack);


            TrackPanel preferGenreSimilarTrack = new PreferGenreSimilarTrackPanel(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK,makeMockRecommendTrackDto(225L,makeSvcGenre(4L,"힙합"),Arrays.asList(641L,642L,643L,644L,645L,2101L,80419008L,697L,698L,699L,80419004L,741L,742L,743L,744L)) , makePanelBackGroundImageList("https://api3-dev.musicmates.co.kr/img/recommend/new_poc/image_likeu_1.png") );
            mockPanelList.add(preferGenreSimilarTrack);

            preferGenreSimilarTrack = new PreferGenreSimilarTrackPanel(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK,makeMockRecommendTrackDto(226L,makeSvcGenre(5L,"간지"),Arrays.asList(80419004L,741L,742L,743L,744L,641L,642L,643L,644L,645L,2101L,80419008L,697L,698L,699L)) , makePanelBackGroundImageList("https://api3-dev.musicmates.co.kr/img/recommend/new_poc/image_likeu_1.png") );
            mockPanelList.add(preferGenreSimilarTrack);

            TrackPanel rcmmdTrack = new RcmmdTrackPanel(RecommendPanelType.RCMMD_TRACK, makeMockRecommendTrackDto(300L,makeSvcGenre(10L,"군가"), Arrays.asList(1010L,1011L,1025L,1026L,1030L,1032L,1039L,643L,644L,645L,2101L,80419008L,697L,698L,699L)) , makePanelBackGroundImageList("https://api3-dev.musicmates.co.kr/img/recommend/new_poc/image_madeforu_1.png"));
            mockPanelList.add(rcmmdTrack);

            rcmmdTrack = new RcmmdTrackPanel(RecommendPanelType.RCMMD_TRACK, makeMockRecommendTrackDto(301L,makeSvcGenre(12L,"축가"), Arrays.asList(1010L,1011L,1025L,1026L,1030L,1032L,1039L,643L,644L,645L,2101L,80419008L,697L,698L,699L)) , makePanelBackGroundImageList("https://api3-dev.musicmates.co.kr/img/recommend/new_poc/image_madeforu_1.png"));
            mockPanelList.add(rcmmdTrack);


            ChartDto kidsChart = chartService.getKidsChart();
            kidsChart.setChartId(2L);
            kidsChart.setChartNm("Kids");
            liveChart.setChartType(ChartType.RTIME);
            ChartPanel kidsChartPanel = new ChartPanel(RecommendPanelType.KIDS_CHART, kidsChart, makePanelBackGroundImageList("https://api3-dev.musicmates.co.kr/img/recommend/new_poc/image_kids_1.png"));
            mockPanelList.add(kidsChartPanel);

        }catch(Exception e){
            e.printStackTrace();
        }
        return mockPanelList;
    }


    private RecommendTrackDto makeMockRecommendTrackDto(Long rcmmdId , ServiceGenreDto svcGenre , List<Long> trackIdList){
        RecommendTrackDto recommendTrackDto = new RecommendTrackDto();


        recommendTrackDto.setRcmmdId(rcmmdId);
        recommendTrackDto.setSvcGenreDto(svcGenre);
        recommendTrackDto.setCreateDtime(new Date());
        recommendTrackDto.setTrackList(trackMapper.selectTrackList(trackIdList));

        return recommendTrackDto;
    }

    private RecommendArtistDto makeMockRecommendArtistDto(){
        RecommendArtistDto recommendArtistDto = new RecommendArtistDto();

        recommendArtistDto.setRcmmdArtistId(12345L);
        recommendArtistDto.setCreateDtime(new Date());
        recommendArtistDto.setUpdateDtime(new Date());
        recommendArtistDto.setArtistList(artistMapper.getArtistList(Arrays.asList(12L,14L,15L)));
        return recommendArtistDto;
    }


    private ServiceGenreDto makeSvcGenre(Long genreId, String genreNm){
        ServiceGenreDto svcGenre = new ServiceGenreDto();
        svcGenre.setSvcGenreId(genreId);
        svcGenre.setSvcGenreNm(genreNm);
        return svcGenre;
    }
    private GenreVo makeGenre(Long genreId, String genreNm){
        GenreVo genre = new GenreVo();
        genre.setId(genreId);
        genre.setName(genreNm);
        return genre;
    }

    private List<ImageDto> makePanelBackGroundImageList(String url){
        List<ImageDto> artistImgList = new ArrayList<>();
        ImageDto image = new ImageDto();
        image.setSize(75);
        image.setUrl(url);

        artistImgList.add(image);
        image = new ImageDto();
        image.setSize(140);
        image.setUrl(url);
        artistImgList.add(image);

        image = new ImageDto();
        image.setSize(200);
        image.setUrl(url);
        artistImgList.add(image);


        image = new ImageDto();
        image.setSize(350);
        image.setUrl(url);
        artistImgList.add(image);

        image = new ImageDto();
        image.setSize(500);
        image.setUrl(url);
        artistImgList.add(image);

        image = new ImageDto();
        image.setSize(1000);
        image.setUrl(url);
        artistImgList.add(image);

        return artistImgList;
    }


    @Override
    public ListDto<List<MyMostTrackDto>> getRecommendPanelPopularTrackList(Long characterNo, Long rcmmdArtistId) {
        return getTrackList(trackMapper.selectRecommendPanelPopularTrackList(characterNo, rcmmdArtistId));
    }
    @Override
    public ListDto<List<MyMostTrackDto>> getRecommendPanelSimilarTrackList(Long characterNo, Long rcmmdTrackId) {
        return getTrackList(trackMapper.selectRecommendPanelSimilarTrackList(characterNo, rcmmdTrackId));
    }
    @Override
    public ListDto<List<MyMostTrackDto>> getRecommendPanelGenreTrackList(Long characterNo, Long rcmmdGenreId) {
        return getTrackList(trackMapper.selectRecommendPanelGenreTrackList(characterNo, rcmmdGenreId));
    }
    @Override
    public ListDto<List<MyMostTrackDto>> getRecommendPanelCfTrackList(Long characterNo, Long rcmmdMforuId) {
        return getTrackList(trackMapper.selectRecommendPanelCfTrackList(characterNo, rcmmdMforuId));
    }

	@Override
	public ListDto<List<MyMostTrackDto>> getRecommendPanelTrackList(Long characterNo, RecommendPanelContentType recommendPanelContentType, Long panelContentId) {

		ListDto<List<MyMostTrackDto>> trackList = null;

    	switch (recommendPanelContentType){
		    // 아티스트
		    case RC_ATST_TR:
			    trackList = getRecommendPanelPopularTrackList(characterNo, panelContentId);
			    break;
		    // 선호 유사
		    case RC_SML_TR:
			    trackList = getRecommendPanelSimilarTrackList(characterNo, panelContentId);
			    break;
		    // 유사 장르
		    case RC_GR_TR:
			    trackList = getRecommendPanelGenreTrackList(characterNo, panelContentId);
			    break;
		    // 추천
		    case RC_CF_TR:
			    trackList = getRecommendPanelCfTrackList(characterNo, panelContentId);
			    break;
	    }

		return trackList;
	}

    private ListDto<List<MyMostTrackDto>> getTrackList(List<Long> trackIdList){

        if(CollectionUtils.isEmpty(trackIdList)){
            return null;
        }
        URI uri = UriComponentsBuilder.newInstance().scheme("http").host("meta-api")
                .path("meta/v1/track/list")
                .queryParam("trackIdList", trackIdList.toArray(new Long[0]))
                .build().encode().toUri();


        CommonApiResponse<ListDto<List<MyMostTrackDto>>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<CommonApiResponse<ListDto<List<MyMostTrackDto>>>>() {}).getBody();


        return response.getData();

    }


}


