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

import com.netflix.discovery.converters.Auto;
import com.sktechx.godmusic.personal.common.domain.type.ChartType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.ChannelDto;
import com.sktechx.godmusic.personal.rest.model.dto.ChartDto;
import com.sktechx.godmusic.personal.rest.model.dto.ImageDto;
import com.sktechx.godmusic.personal.rest.model.dto.ServiceGenreDto;
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
import com.sktechx.godmusic.personal.rest.repository.artist.ArtistMapper;
import com.sktechx.godmusic.personal.rest.repository.channel.ChannelMapper;
import com.sktechx.godmusic.personal.rest.repository.track.TrackMapper;
import com.sktechx.godmusic.personal.rest.service.ChartService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendPanelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

    @Override
    public List<Panel> createRecommendPanelList() {

        List<Panel> mockPanelList = new ArrayList<>();

        try{

            //TODO : mockup 데이터 생성
            ChartDto liveChart = chartService.getLiveChart();

            liveChart.setChartType(ChartType.LIVE);
            ChartPanel liveChartPanel = new ChartPanel(RecommendPanelType.LIVE_CHART, liveChart, makePanelBackGroundImageList(),1);
            mockPanelList.add(liveChartPanel);


            ChannelDto popularChannel =channelMapper.selectChannelById(983L);
            popularChannel.setChnlNm("들으면 힘을 주는 \nCCM BEST ");
            popularChannel.setTrackCount(channelMapper.selectChannelTrackCount(983L));
            ChannelPanel popularChannelPanel = new PopularChannelPanel(RecommendPanelType.POPULAR_CHANNEL, popularChannel , makePanelBackGroundImageList(),1);
            mockPanelList.add(popularChannelPanel);


            popularChannel =channelMapper.selectChannelById(18401L);
            popularChannel.setChnlNm("꽃보다 할배 리턴즈 \nBGM #4");
            popularChannel.setTrackCount(channelMapper.selectChannelTrackCount(18401L));
            popularChannelPanel = new PopularChannelPanel(RecommendPanelType.POPULAR_CHANNEL, popularChannel , makePanelBackGroundImageList(),2);
            mockPanelList.add(popularChannelPanel);

            popularChannel =channelMapper.selectChannelById(18400L);
            popularChannel.setChnlNm("한적한 시골길 드라이브를\n 위한 재즈");
            popularChannel.setTrackCount(channelMapper.selectChannelTrackCount(18400L));
            popularChannelPanel = new PopularChannelPanel(RecommendPanelType.POPULAR_CHANNEL, popularChannel , makePanelBackGroundImageList(),3);
            mockPanelList.add(popularChannelPanel);

            ChannelDto preferPopularGenreChannel = channelMapper.selectChannelById(18399L);
            preferPopularGenreChannel.setChnlNm("팝의 여제, 비욘세 \n필청 트랙 모음");
            preferPopularGenreChannel.setTrackCount(channelMapper.selectChannelTrackCount(18399L));
            GenreVo danceGenre = makeGenre(1L, "POP");
            ChannelPanel preferPopularGenreChannelPanel = new PreferGenrePopularChannelPanel(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL, preferPopularGenreChannel,danceGenre , makePanelBackGroundImageList() , 1);
            mockPanelList.add(preferPopularGenreChannelPanel);

            preferPopularGenreChannel = channelMapper.selectChannelById(18398L);
            preferPopularGenreChannel.setChnlNm("쿨한 느낌이 매력적인\n드라이빙 트렌디");
            preferPopularGenreChannel.setTrackCount(channelMapper.selectChannelTrackCount(18398L));
            GenreVo traditionalGenre = makeGenre(2L, "국악");
            preferPopularGenreChannelPanel = new PreferGenrePopularChannelPanel(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL, preferPopularGenreChannel,traditionalGenre , makePanelBackGroundImageList() , 2);

            mockPanelList.add(preferPopularGenreChannelPanel);

            ChannelDto listenMoodPolularChannel = channelMapper.selectChannelById(18397L);
            listenMoodPolularChannel.setChnlNm("커피 한 잔과 함께\n낮잠이 고플 때");
            listenMoodPolularChannel.setTrackCount(channelMapper.selectChannelTrackCount(18397L));

            ChannelPanel listenMoodPopularChannelPanel = new ListenMoodPopularChannelPanel(RecommendPanelType.LISTEN_MOOD_POPULAR_CHANNEL, listenMoodPolularChannel, makePanelBackGroundImageList() , 1);
            mockPanelList.add(listenMoodPopularChannelPanel);


            Panel artistPanel = new ArtistPanel(RecommendPanelType.ARRIST_POPULAR_TRACK , makeMockRecommendArtistDto() , 1);
            mockPanelList.add(artistPanel);


            TrackPanel preferSimilarTrack = new PreferSimilarTrackPanel(RecommendPanelType.PREFER_SIMILAR_TRACK,makeMockRecommendTrackDto(222L,makeSvcGenre(3L,"댄스"),Arrays.asList(80419008L,80419007L,80419006L,80419005L,80419004L,80419003L,80419002L,80419001L,80419000L,2091L,2092L,2093L,2094L,2100L,2101L)),makePanelBackGroundImageList(),1);
            mockPanelList.add(preferSimilarTrack);

            preferSimilarTrack = new PreferSimilarTrackPanel(RecommendPanelType.PREFER_SIMILAR_TRACK,makeMockRecommendTrackDto(223L,makeSvcGenre(4L,"힙합"),Arrays.asList(2091L,2092L,2093L,2094L,2100L,2101L,80419008L,80419007L,80419006L,80419005L,80419004L,80419003L,80419002L,80419001L,80419000L)),makePanelBackGroundImageList(),2);
            mockPanelList.add(preferSimilarTrack);


            TrackPanel preferGenreSimilarTrack = new PreferGenreSimilarTrackPanel(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK,makeMockRecommendTrackDto(225L,makeSvcGenre(4L,"힙합"),Arrays.asList(641L,642L,643L,644L,645L,2101L,80419008L,697L,698L,699L,80419004L,741L,742L,743L,744L)) , makePanelBackGroundImageList(), 1 );
            mockPanelList.add(preferGenreSimilarTrack);

            preferGenreSimilarTrack = new PreferGenreSimilarTrackPanel(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK,makeMockRecommendTrackDto(226L,makeSvcGenre(5L,"간지"),Arrays.asList(80419004L,741L,742L,743L,744L,641L,642L,643L,644L,645L,2101L,80419008L,697L,698L,699L)) , makePanelBackGroundImageList(), 2 );
            mockPanelList.add(preferGenreSimilarTrack);

            TrackPanel rcmmdTrack = new RcmmdTrackPanel(RecommendPanelType.RCMMD_TRACK, makeMockRecommendTrackDto(300L,makeSvcGenre(10L,"군가"), Arrays.asList(1010L,1011L,1025L,1026L,1030L,1032L,1039L,643L,644L,645L,2101L,80419008L,697L,698L,699L)) , makePanelBackGroundImageList() , 1);
            mockPanelList.add(rcmmdTrack);

            rcmmdTrack = new RcmmdTrackPanel(RecommendPanelType.RCMMD_TRACK, makeMockRecommendTrackDto(301L,makeSvcGenre(12L,"축가"), Arrays.asList(1010L,1011L,1025L,1026L,1030L,1032L,1039L,643L,644L,645L,2101L,80419008L,697L,698L,699L)) , makePanelBackGroundImageList() , 2);
            mockPanelList.add(rcmmdTrack);


            ChartDto kidsChart = chartService.getLiveChart();
            kidsChart.setChartId(2L);
            kidsChart.setChartNm("Kids");
            liveChart.setChartType(ChartType.KIDS);
            ChartPanel kidsChartPanel = new ChartPanel(RecommendPanelType.KIDS_CHART, kidsChart, makePanelBackGroundImageList(),1);
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

//        content.setId(recommendTrackDto.getRcmmdId());
//        content.setContentType(panelType);
//        content.setTrackList(recommendTrackDto.getTrackList());
//        content.setTrackCount(recommendTrackDto.getTrackList().size());
//        content.setGenre(new GenreVo(recommendTrackDto.getSvcGenreDto()));
//        content.setCreateDtime(recommendTrackDto.getCreateDtime());
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

    //    @Override
//    public List<Panel> createMockUpRecommendPanelList() {
//
//        List<Panel> mockPanelList = new ArrayList<>();
//
//        mockPanelList.add(createMockChartPanel(RecommendPanelType.LIVE_CHART));
//
//        mockPanelList.add(createMockChannelPanel(RecommendPanelType.POPULAR_CHANNEL,null,1));
//        mockPanelList.add(createMockChannelPanel(RecommendPanelType.POPULAR_CHANNEL,null,2));
//        mockPanelList.add(createMockChannelPanel(RecommendPanelType.POPULAR_CHANNEL,null,3));
//
//
//        mockPanelList.add(createMockChannelPanel(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL,makeGenre(11L,"댄스"),1));
//        mockPanelList.add(createMockChannelPanel(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL,makeGenre(12L,"발라드"),2));
//
//
//        mockPanelList.add(createMockTrackPanel(RecommendPanelType.PREFER_SIMILAR_TRACK ,null, 1));
//        mockPanelList.add(createMockTrackPanel(RecommendPanelType.PREFER_SIMILAR_TRACK , null , 2));
//        mockPanelList.add(createMockTrackPanel(RecommendPanelType.PREFER_SIMILAR_TRACK , null ,3));
//
//        mockPanelList.add(createMockTrackPanel(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK , makeGenre(13L,"POP") , 1));
//        mockPanelList.add(createMockTrackPanel(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK , makeGenre(14L,"KPOP") , 2));
//
//        mockPanelList.add(createMockTrackPanel(RecommendPanelType.RCMMD_TRACK , makeGenre(14L, "KPOP"),1));
//        mockPanelList.add(createMockTrackPanel(RecommendPanelType.RCMMD_TRACK , makeGenre(13L, "POP"),2));
//
//        mockPanelList.add(createMockArtistPanel());
//
//        mockPanelList.add(createMockChartPanel(RecommendPanelType.KIDS_CHART));
//        return mockPanelList;
//    }
//

//
//
//    private Panel createMockChannelPanel(RecommendPanelType panelType , GenreVo genre, Integer dispSn){
//
//        Panel channelPanel = null;
//        try{
//
//            if(RecommendPanelType.POPULAR_CHANNEL.equals(panelType)){
//            channelPanel = new PopularChannelPanel(panelType,makePanelBackGroundImageList(""),makeDriveChannel() , dispSn);
//
//            }else if(RecommendPanelType.LISTEN_MOOD_POPULAR_CHANNEL.equals(panelType)){
//                channelPanel = new ListenMoodPopularChannelPanel(panelType,makePanelBackGroundImageList(""),makeDriveChannel() , dispSn);
//            }else if(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL.equals(panelType)){
//                channelPanel = new PreferGenrePopularChannelPanel(panelType,makePanelBackGroundImageList(""),makeDriveChannel() , genre,dispSn);
//            }
//        }catch(Exception e){
//
//        }
//        return channelPanel;
//    }
//
//    private Panel createMockArtistPanel(){
//        Panel artistPanel = null;
//        try{
//            artistPanel = new ArtistPanel(RecommendPanelType.ARRIST_POPULAR_TRACK,makeArtistList(),1);
//
//        }catch(Exception e){
//        }
//
//
//        return artistPanel;
//    }
//
//    private Panel createMockChartPanel(RecommendPanelType panelType){
//
//        Panel chartPanel = null;
//        try{
//            if(RecommendPanelType.LIVE_CHART.equals(panelType)){
//                chartPanel = new ChartPanel(panelType ,makePanelBackGroundImageList("TRACK1"),makeLiveChartChannel() , 1);
//            }else{
//                chartPanel = new ChartPanel(panelType ,makePanelBackGroundImageList("TRACK1"),makeKidsChartChannel() , 1);
//            }
//
//        }catch(Exception e){
//        }
//
//        return chartPanel;
//    }
//
//    private Panel createMockTrackPanel(RecommendPanelType recommendPanelType, GenreVo genre, Integer dispSn){
//        Panel trackPanel = null;
//        try{
//
//            if(RecommendPanelType.PREFER_SIMILAR_TRACK.equals(recommendPanelType)){
//                trackPanel = new PreferSimilarTrackPanel(recommendPanelType,makePanelBackGroundImageList("TRACK1"),makeTrackList(),dispSn);
//            }else if(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK.equals(recommendPanelType)){
//                trackPanel = new PreferGenreSimilarTrackPanel(recommendPanelType,makePanelBackGroundImageList("RGENRE3"),genre,makeTrackList(),dispSn);
//            }else if(RecommendPanelType.RCMMD_TRACK.equals(recommendPanelType)){
//                trackPanel = new RcmmdTrackPanel(recommendPanelType,makePanelBackGroundImageList("RGENRE3"),genre,makeTrackList(),dispSn);
//            }
//        }catch(Exception e){
//
//        }
//        return trackPanel;
//    }
//
//
//
//
//    private ChannelDto makeDriveChannel(){
//        ChannelDto liveChartChannel = new ChannelDto();
//        liveChartChannel.setChnlId(3L);
//        liveChartChannel.setChnlNm("드라이브할 때\n 듣는 신나는\n 인디음악");
//        liveChartChannel.setCreateDtime(new Date());
//        liveChartChannel.setTrackCount(60);
//        liveChartChannel.setTrackList(makeTrackList());
//
//        return liveChartChannel;
//    }
//    private ChannelDto makeKidsChartChannel(){
//        ChannelDto kidsChartChannel = new ChannelDto();
//        kidsChartChannel.setChnlId(2L);
//        kidsChartChannel.setChnlNm("KIDS");
//        kidsChartChannel.setCreateDtime(new Date());
//        kidsChartChannel.setTrackCount(10);
//        kidsChartChannel.setTrackList(makeTrackList());
//        kidsChartChannel.setUpdateDtime(new Date());
//        kidsChartChannel.setAlbum(makeAlbum());
//        return kidsChartChannel;
//    }
//    private ChannelDto makeLiveChartChannel(){
//        ChannelDto liveChartChannel = new ChannelDto();
//        liveChartChannel.setChnlId(1L);
//        liveChartChannel.setChnlNm("실시간 차트");
//        liveChartChannel.setCreateDtime(new Date());
//        liveChartChannel.setTrackCount(10);
//        liveChartChannel.setTrackList(makeTrackList());
//        liveChartChannel.setUpdateDtime(new Date());
//
//        liveChartChannel.setAlbum(makeAlbum());
//        return liveChartChannel;
//    }
//
//    private List<TrackDto> makeTrackList(){
//        List<TrackDto> liveChartTrackList = new ArrayList<>();
//
//        liveChartTrackList.add(makeTrack(30695454L, "마지막 처럼" , 1 , 6, makeArtist(), makeAlbum()));
//        liveChartTrackList.add(makeTrack(30695455L, "마지막처럼2" , 2 , 0, makeArtist(), makeAlbum()));
//        liveChartTrackList.add(makeTrack(30695456L, "마지막처럼3" , 3 , 1, makeArtist(), makeAlbum()));
//        liveChartTrackList.add(makeTrack(30695457L, "마지막처럼4" , 4 , 0, makeArtist(), makeAlbum()));
//        liveChartTrackList.add(makeTrack(30695458L, "처음처럼" , 5, 0, makeArtist(), makeAlbum()));
//        liveChartTrackList.add(makeTrack(30695459L, "처음처럼1" , 6, 0, makeArtist(), makeAlbum()));
//        liveChartTrackList.add(makeTrack(30695460L, "처음처럼2" , 7, 0, makeArtist(), makeAlbum()));
//        liveChartTrackList.add(makeTrack(30695461L, "처음처럼3" , 8, 0, makeArtist(), makeAlbum()));
//        liveChartTrackList.add(makeTrack(30695462L, "처음처럼4" , 9, 0, makeArtist(), makeAlbum()));
//        liveChartTrackList.add(makeTrack(30695463L, "처음처럼5" , 10, 0, makeArtist(), makeAlbum()));
//        liveChartTrackList.add(makeTrack(30695464L, "처음처럼6" , 11, 0, makeArtist(), makeAlbum()));
//        liveChartTrackList.add(makeTrack(30695465L, "처음처럼7" , 12, 0, makeArtist(), makeAlbum()));
//
//
//        return liveChartTrackList;
//    }
//
//    private TrackDto makeTrack(Long trackId ,String trackNm , int trackSn, int trackBfSn, ArtistDto artist, AlbumDto album){
//
//        TrackDto track = new TrackDto();
//        track.setTrackId(trackId);
//        track.setTrackNm(trackNm);
//        track.setCreateDtime(new Date());
//        track.setTrackSn(trackSn);
//        track.setTrackBfSn(trackBfSn);
//        track.setUpdateDtime(new Date());
//        track.setArtist(artist);
//        track.setAlbum(album);
//
//        return track;
//    }
//
//
//    private ArtistDto makeArtist(){
//        ArtistDto artist = new ArtistDto();
//        artist.setArtistId(20022492L);
//        artist.setArtistNm("BLACKPINK");
//        artist.setImgList(makeArtistImageList());
//
//        return artist;
//    }
//
//    private List<ArtistDto> makeArtistList(){
//        List<ArtistDto> artistList = new ArrayList();
//
//        ArtistDto artist = new ArtistDto();
//        artist.setArtistId(20022492L);
//        artist.setArtistNm("BLACKPINK");
//        artist.setImgList(makeArtistImageList());
//        artistList.add(artist);
//
//        artist = new ArtistDto();
//        artist.setArtistId(20022493L);
//        artist.setArtistNm("BLACKPINK1");
//        artist.setImgList(makeArtistImageList());
//        artistList.add(artist);
//
//        return artistList;
//    }
//
//    private AlbumDto makeAlbum(){
//        AlbumDto album = new AlbumDto();
//        album.setAlbumId(20104358L);
//        album.setTitle("마지막처럼");
//        album.setImgList(makeAlbumImageList());
//        return album;
//    }
//
//    private List<ImageDto> makeAlbumImageList(){
//        List<ImageDto> albumImgList = new ArrayList<>();
//        ImageDto image = new ImageDto();
//        image.setSize(70);
//        image.setUrl("http://asp-image.bugsm.co.kr/album/images/70/201043/20104358.jpg");
//
//        albumImgList.add(image);
//        image = new ImageDto();
//        image.setSize(75);
//        image.setUrl("http://asp-image.bugsm.co.kr/album/images/75/201043/20104358.jpg");
//        albumImgList.add(image);
//
//        image = new ImageDto();
//        image.setSize(140);
//        image.setUrl("http://asp-image.bugsm.co.kr/album/images/140/201043/20104358.jpg");
//        albumImgList.add(image);
//
//
//        image = new ImageDto();
//        image.setSize(200);
//        image.setUrl("http://asp-image.bugsm.co.kr/album/images/200/201043/20104358.jpg");
//        albumImgList.add(image);
//
//        image = new ImageDto();
//        image.setSize(350);
//        image.setUrl("http://asp-image.bugsm.co.kr/album/images/350/201043/20104358.jpg");
//        albumImgList.add(image);
//
//        image = new ImageDto();
//        image.setSize(500);
//        image.setUrl("http://asp-image.bugsm.co.kr/album/images/500/201043/20104358.jpg");
//        albumImgList.add(image);
//
//        return albumImgList;
//    }
//
//
//    private String getImageUrl(String pannelType, int size){
//        if("ARTIST3".equals(pannelType)){
//            return "https://api3-dev.musicmates.co.kr/img/recommend/typeB_"+size+".png";
//        }else if("KNAME".equals(pannelType)){
//            return "https://api3-dev.musicmates.co.kr/img/recommend/typeA_"+size+".png";
//        }else if("RGENRE3".equals(pannelType)){
//            return "https://api3-dev.musicmates.co.kr/img/recommend/typeB_"+size+".png";
//
//        }else if("TRACK1".equals(pannelType)){
//            return "https://api3-dev.musicmates.co.kr/img/recommend/typeC_"+size+".png";
//        }else{
//            return "https://api3-dev.musicmates.co.kr/img/recommend/typeB_"+size+".png";
//
//        }
//    }

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

    private List<ImageDto> makePanelBackGroundImageList(){
        List<ImageDto> artistImgList = new ArrayList<>();
        ImageDto image = new ImageDto();
        image.setSize(75);
        image.setUrl("https://api3-dev.musicmates.co.kr/img/recommend/typeC_75.png");

        artistImgList.add(image);
        image = new ImageDto();
        image.setSize(140);
        image.setUrl("https://api3-dev.musicmates.co.kr/img/recommend/typeC_140.png");
        artistImgList.add(image);

        image = new ImageDto();
        image.setSize(200);
        image.setUrl("https://api3-dev.musicmates.co.kr/img/recommend/typeC_200.png");
        artistImgList.add(image);


        image = new ImageDto();
        image.setSize(350);
        image.setUrl("https://api3-dev.musicmates.co.kr/img/recommend/typeC_350.png");
        artistImgList.add(image);

        image = new ImageDto();
        image.setSize(500);
        image.setUrl("https://api3-dev.musicmates.co.kr/img/recommend/typeC_500.png");
        artistImgList.add(image);

        image = new ImageDto();
        image.setSize(1000);
        image.setUrl("https://api3-dev.musicmates.co.kr/img/recommend/typeC_1000.png");
        artistImgList.add(image);

        return artistImgList;
    }
//    private List<ImageDto> makeArtistImageList(){
//        List<ImageDto> artistImgList = new ArrayList<>();
//        ImageDto image = new ImageDto();
//        image.setSize(70);
//        image.setUrl("http://asp-image.bugsm.co.kr/artist/images/70/200224/20022492.jpg");
//
//        artistImgList.add(image);
//        image = new ImageDto();
//        image.setSize(75);
//        image.setUrl("http://asp-image.bugsm.co.kr/artist/images/75/200224/20022492.jpg");
//        artistImgList.add(image);
//
//        image = new ImageDto();
//        image.setSize(140);
//        image.setUrl("http://asp-image.bugsm.co.kr/artist/images/140/200224/20022492.jpg");
//        artistImgList.add(image);
//
//
//        image = new ImageDto();
//        image.setSize(200);
//        image.setUrl("http://asp-image.bugsm.co.kr/artist/images/200/200224/20022492.jpg");
//        artistImgList.add(image);
//
//        image = new ImageDto();
//        image.setSize(350);
//        image.setUrl("http://asp-image.bugsm.co.kr/artist/images/350/200224/20022492.jpg");
//        artistImgList.add(image);
//
//        image = new ImageDto();https://api3-dev.musicmates.co.kr/img/recommend/typeC_1000.png
//        image.setSize(500);
//        image.setUrl("http://asp-image.bugsm.co.kr/artist/images/500/200224/20022492.jpg");
//        artistImgList.add(image);
//
//        return artistImgList;
//
//    }

}
