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

import java.net.URI;
import java.util.*;

import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.rest.repository.*;
import com.sktechx.godmusic.personal.rest.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.domain.type.ChartType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.ChartDto;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.ServiceGenreDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.*;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
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
import com.sktechx.godmusic.personal.rest.service.ChartService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendPanelService;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelAssembly;
import com.sktechx.godmusic.personal.rest.service.recommend.phase.PersonalRecommendPhaseService;
import lombok.extern.slf4j.Slf4j;

/**
 * 설명 : 추천 패널 데이터 생성
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
@Service
@Slf4j
public class RecommendPanelServiceImpl implements RecommendPanelService {

    public static final String RECOMMEND_PANEL_DEFAULT_IMG_KEY ="godmusic.personalapi.recommend.home.panel.default.imglist";

    @Autowired
    private ChartService chartService;

    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    private ChannelService channelService;
    @Autowired
    private ChartMapper chartMapper;

    @Autowired
    private ArtistMapper artistMapper;

    @Autowired
    private TrackMapper trackMapper;

    @Autowired
    private PersonalRecommendPhaseService personalRecommendPhaseService;

    @Autowired
    private RecommendPanelAssemblyFactory recommendPanelAssemblyFactory;

    @Autowired
    private RecommendMapper recommendMapper;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private RedisService redisService;

    @Override
    public List<Panel> createRecommendPanelList(Long characterNo , OsType osType) {
        PersonalPhaseMeta personalPhaseMeta = personalRecommendPhaseService.getPersonalRecommendPhaseMeta(characterNo, osType);
        PanelAssembly panelAssembly = recommendPanelAssemblyFactory.getRecommendPanelAssembly(personalPhaseMeta.getFirstPhaseType());

        return panelAssembly.assembleRecommendPanel(personalPhaseMeta);
    }

    @Override
    public List<Panel> createMockupRecommendPanelList() {

        List<Panel> mockPanelList = new ArrayList<>();

        try{

            //TODO : mockup 데이터 생성
            ChartDto liveChart = chartMapper.selectPreferGenreChart("ALL", ChartType.HOURLY, OsType.AOS , 15);

            ChartPanel liveChartPanel = new ChartPanel(RecommendPanelType.LIVE_CHART, liveChart, makePanelBackGroundImageList("https://api3-dev.musicmates.co.kr/img/recommend/new_poc/image_top_100_1.png"));
            mockPanelList.add(liveChartPanel);

            List<ChnlDto> popularChannelList = channelService.getPopularChannelList(3, OsType.AOS);

            if(!CollectionUtils.isEmpty(popularChannelList)){
                popularChannelList.stream().forEach(chnlDto -> {
                    ChannelPanel popularChannelPanel = new PopularChannelPanel(RecommendPanelType.POPULAR_CHANNEL, chnlDto , chnlDto.getImgList());
                    mockPanelList.add(popularChannelPanel);
                });
            }

            List<ChnlDto> preferPopularGenreChannelList = channelMapper.selectChannelListByIdList(Arrays.asList(18399L),12, OsType.AOS);
            GenreVo danceGenre = makeGenre(1L, "POP");
            ChannelPanel preferPopularGenreChannelPanel = new PreferGenrePopularChannelPanel(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL, preferPopularGenreChannelList.get(0),danceGenre , preferPopularGenreChannelList.get(0).getImgList() );
            mockPanelList.add(preferPopularGenreChannelPanel);


            preferPopularGenreChannelList = channelMapper.selectChannelListByIdList(Arrays.asList(18398L),12, OsType.AOS);
            GenreVo traditionalGenre = makeGenre(2L, "국악");
            preferPopularGenreChannelPanel = new PreferGenrePopularChannelPanel(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL, preferPopularGenreChannelList.get(0),traditionalGenre ,preferPopularGenreChannelList.get(0).getImgList());
            mockPanelList.add(preferPopularGenreChannelPanel);

            List<ChnlDto> listenMoodPolularChannelList = channelMapper.selectChannelListByIdList(Arrays.asList(18397L),12, OsType.AOS);
            ChannelPanel listenMoodPopularChannelPanel = new ListenMoodPopularChannelPanel(RecommendPanelType.LISTEN_MOOD_POPULAR_CHANNEL, listenMoodPolularChannelList.get(0) ,listenMoodPolularChannelList.get(0).getImgList());
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


            ChartDto kidsChart = chartMapper.selectPreferGenreChart("KIDS", ChartType.HOURLY, OsType.AOS , 15);
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
        recommendTrackDto.setRcmmdCreateDtime(new Date());
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

    private List<ImageInfo> makePanelBackGroundImageList(String url){
        List<ImageInfo> artistImgList = new ArrayList<>();
        ImageInfo image = new ImageInfo();
        image.setSize(new Long(75));
        image.setUrl(url);

        artistImgList.add(image);
        image = new ImageInfo();
        image.setSize(new Long(140));
        image.setUrl(url);
        artistImgList.add(image);

        image = new ImageInfo();
        image.setSize(new Long(200));
        image.setUrl(url);
        artistImgList.add(image);


        image = new ImageInfo();
        image.setSize(new Long(350));
        image.setUrl(url);
        artistImgList.add(image);

        image = new ImageInfo();
        image.setSize(new Long(500));
        image.setUrl(url);
        artistImgList.add(image);

        image = new ImageInfo();
        image.setSize(new Long(1000));
        image.setUrl(url);
        artistImgList.add(image);

        return artistImgList;
    }


    @Override
    public ListDto<List<RecommendPanelTrackDto>> getRecommendPanelPopularTrackList(Long characterNo, Long rcmmdArtistId) {
        return getTrackList(trackMapper.selectRecommendPanelPopularTrackList(characterNo, rcmmdArtistId));
    }
    @Override
    public ListDto<List<RecommendPanelTrackDto>> getRecommendPanelSimilarTrackList(Long characterNo, Long rcmmdTrackId) {
        return getTrackList(trackMapper.selectRecommendPanelSimilarTrackList(characterNo, rcmmdTrackId));
    }
    @Override
    public ListDto<List<RecommendPanelTrackDto>> getRecommendPanelGenreTrackList(Long characterNo, Long rcmmdGenreId) {
        return getTrackList(trackMapper.selectRecommendPanelGenreTrackList(characterNo, rcmmdGenreId));
    }
    @Override
    public ListDto<List<RecommendPanelTrackDto>> getRecommendPanelCfTrackList(Long characterNo, Long rcmmdMforuId) {
        return getTrackList(trackMapper.selectRecommendPanelCfTrackList(characterNo, rcmmdMforuId));
    }

	@Override
	public ListDto<List<RecommendPanelTrackDto>> getRecommendPanelTrackList(Long characterNo, RecommendPanelContentType recommendPanelContentType, Long panelContentId) {

		ListDto<List<RecommendPanelTrackDto>> trackList = null;

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

    @Override
    public RecommendPanelInfoDto getRecommendPanelInfo(RecommendPanelContentType recommendPanelContentType,
            Long panelContentId) {

        RecommendPanelInfoDto panel = null;


        switch (recommendPanelContentType){
            // 아티스트
            case RC_ATST_TR:

                // 아티스트의 첫 이미지를 배경 이미지로 사용
                List<ArtistDto> artistDtoList = artistMapper.getArtistList(Arrays.asList(12L,14L,15L));
                panel =  new RecommendPanelInfoDto.Builder()
                        .title("Musician focus")
                        .subTitle("마이큐 인기곡")
                        .imgList((artistDtoList == null || artistDtoList.get(0) == null? null : artistDtoList.get(0).getImgList()))
                        .artistList(artistDtoList)
                        .artistCount(3)
                        .newYn(YnType.Y)
                        .build();
                break;
            // 선호 유사
            case RC_SML_TR:
                panel = new RecommendPanelInfoDto.Builder()
                        .title("Like U")
                        .subTitle("많이 들었던 노래와\n 유사한 선곡")
                        .imgList(makePanelBackGroundImageList("https://api3-dev.musicmates.co.kr/img/recommend/new_poc/image_likeu_1.png"))
                        .trackCount(60)
                        .newYn(YnType.Y)
                        .renewDtime(new Date())
                        .build();
                break;
            // 유사 장르
            case RC_GR_TR:
                panel = new RecommendPanelInfoDto.Builder()
                        .title("Like U")
                        .subTitle("유사 장르")
                        .imgList(makePanelBackGroundImageList("https://api3-dev.musicmates.co.kr/img/recommend/new_poc/image_likeu_1.png") )
                        .trackCount(60)
                        .newYn(YnType.Y)
                        .renewDtime(new Date())
                        .build();

                break;
            // 추천
            case RC_CF_TR:
                panel = new RecommendPanelInfoDto.Builder()
                        .title("Made For U")
                        .subTitle("추천")
                        .imgList(makePanelBackGroundImageList("https://api3-dev.musicmates.co.kr/img/recommend/new_poc/image_madeforu_1.png"))
                        .trackCount(60)
                        .newYn(YnType.Y)
                        .renewDtime(new Date())
                        .build();
                break;
        }

        return panel;
    }

    public List<ImageInfo> getRecommendPanelDefaultImageList(OsType osType){

        List<ImageInfo> imgList = null;

        try{
            imgList = redisService.getListWithPrefix(RECOMMEND_PANEL_DEFAULT_IMG_KEY,ImageInfo.class);
        }catch(Exception e){
            log.error("getRecommendPanelDefaultImageList error : {}",e.getMessage());
        }finally {
            if(CollectionUtils.isEmpty(imgList)){
                imgList = recommendMapper.selectRecommendPanelDefaultImageList();
                if(!CollectionUtils.isEmpty(imgList)){
                    redisService.setWithPrefix(RECOMMEND_PANEL_DEFAULT_IMG_KEY, imgList);
                }
            }
        }

        if(!CollectionUtils.isEmpty(imgList)){
            Collections.shuffle(imgList);

            ImageInfo info = imgList.stream().filter(imageInfo -> osType.equals(imageInfo.getOsType())).findFirst().orElse(null);
            return Arrays.asList(info);
        }
        return null;
    }
    private ListDto<List<RecommendPanelTrackDto>> getTrackList(List<Long> trackIdList){

        if(CollectionUtils.isEmpty(trackIdList)){
            return null;
        }
        URI uri = UriComponentsBuilder.newInstance().scheme("http").host("meta-api")
                .path("meta/v1/track/list")
                .queryParam("trackIdList", trackIdList.toArray(new Long[0]))
                .build().encode().toUri();


        CommonApiResponse<ListDto<List<RecommendPanelTrackDto>>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<CommonApiResponse<ListDto<List<RecommendPanelTrackDto>>>>() {}).getBody();


        return response.getData();

    }




}


