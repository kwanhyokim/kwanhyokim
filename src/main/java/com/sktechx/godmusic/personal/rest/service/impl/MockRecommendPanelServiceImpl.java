/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.personal.rest.model.dto.*;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.artist.ArtistPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.ChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.chart.ChartPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.PreferGenreSimilarTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.PreferSimilarTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.RcmmdTrackPanel;
import com.sktechx.godmusic.personal.rest.service.MockRecommendPanelService;
import org.springframework.stereotype.Service;
import sun.net.www.content.image.png;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 설명 : Mockup 추천 패널 데이터 생성
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
@Service
public class MockRecommendPanelServiceImpl implements MockRecommendPanelService {

    @Override
    public List<Panel> createMockUpRecommendPanelList() {

        List<Panel> mockPanelList = new ArrayList<>();

        mockPanelList.add(createMockChartPanel(RecommendPanelType.LIVE_CHART));

        mockPanelList.add(createMockChannelPanel(RecommendPanelType.POPULAR_CHANNEL));

        mockPanelList.add(createMockTrackPanel(RecommendPanelType.PREFER_SIMILAR_TRACK));
        mockPanelList.add(createMockTrackPanel(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK));
        mockPanelList.add(createMockTrackPanel(RecommendPanelType.RCMMD_TRACK));

        mockPanelList.add(createMockArtistPanel());

        mockPanelList.add(createMockChartPanel(RecommendPanelType.KIDS_CHART));
        return mockPanelList;
    }


    private Panel createMockChannelPanel(RecommendPanelType panelType){

        Panel channelPanel = null;
        try{

            channelPanel = new ChannelPanel(panelType,makePanelBackGroundImageList(""),makeDriveChannel());
        }catch(Exception e){

        }
        return channelPanel;
    }

    private Panel createMockArtistPanel(){
        Panel artistPanel = null;
        try{
            artistPanel = new ArtistPanel(RecommendPanelType.ARRIST_POPULAR_TRACK,makeArtistList());

        }catch(Exception e){
        }


        return artistPanel;
    }

    private Panel createMockChartPanel(RecommendPanelType panelType){

        Panel chartPanel = null;
        try{
            if(RecommendPanelType.LIVE_CHART.equals(panelType)){
                chartPanel = new ChartPanel(panelType ,makePanelBackGroundImageList("TRACK1"),makeLiveChartChannel());
            }else{
                chartPanel = new ChartPanel(panelType ,makePanelBackGroundImageList("TRACK1"),makeKidsChartChannel());
            }

        }catch(Exception e){
        }

        return chartPanel;
    }

    private Panel createMockTrackPanel(RecommendPanelType recommendPanelType){
        Panel trackPanel = null;
        try{

            if(RecommendPanelType.PREFER_SIMILAR_TRACK.equals(recommendPanelType)){
                    trackPanel = new PreferGenreSimilarTrackPanel(recommendPanelType,makePanelBackGroundImageList("RGENRE3"),makeTrackList());
            }else if(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK.equals(recommendPanelType)){
                trackPanel = new PreferSimilarTrackPanel(recommendPanelType,makePanelBackGroundImageList("TRACK1"),"댄스",makeTrackList());
            }else if(RecommendPanelType.RCMMD_TRACK.equals(recommendPanelType)){
                trackPanel = new RcmmdTrackPanel(recommendPanelType,makePanelBackGroundImageList("RGENRE3"),"댄스",makeTrackList());
            }
        }catch(Exception e){

        }
        return trackPanel;
    }




    private ChannelDto makeDriveChannel(){
        ChannelDto liveChartChannel = new ChannelDto();
        liveChartChannel.setChnlId(3L);
        liveChartChannel.setChnlNm("드라이브할 때\n 듣는 신나는\n 인디음악");
        liveChartChannel.setCreateDtime(new Date());
        liveChartChannel.setTrackCount(60);
        liveChartChannel.setTrackList(makeTrackList());

        return liveChartChannel;
    }
    private ChannelDto makeKidsChartChannel(){
        ChannelDto kidsChartChannel = new ChannelDto();
        kidsChartChannel.setChnlId(2L);
        kidsChartChannel.setChnlNm("KIDS");
        kidsChartChannel.setCreateDtime(new Date());
        kidsChartChannel.setTrackCount(10);
        kidsChartChannel.setTrackList(makeTrackList());
        kidsChartChannel.setUpdateDtime(new Date());
        kidsChartChannel.setAlbum(makeAlbum());
        return kidsChartChannel;
    }
    private ChannelDto makeLiveChartChannel(){
        ChannelDto liveChartChannel = new ChannelDto();
        liveChartChannel.setChnlId(1L);
        liveChartChannel.setChnlNm("실시간 차트");
        liveChartChannel.setCreateDtime(new Date());
        liveChartChannel.setTrackCount(10);
        liveChartChannel.setTrackList(makeTrackList());
        liveChartChannel.setUpdateDtime(new Date());

        liveChartChannel.setAlbum(makeAlbum());
        return liveChartChannel;
    }

    private List<TrackDto> makeTrackList(){
        List<TrackDto> liveChartTrackList = new ArrayList<>();

        liveChartTrackList.add(makeTrack(30695454L, "마지막 처럼" , 1 , 6, makeArtist(), makeAlbum()));
        liveChartTrackList.add(makeTrack(30695455L, "마지막처럼2" , 2 , 0, makeArtist(), makeAlbum()));
        liveChartTrackList.add(makeTrack(30695456L, "마지막처럼3" , 3 , 1, makeArtist(), makeAlbum()));
        liveChartTrackList.add(makeTrack(30695457L, "마지막처럼4" , 4 , 0, makeArtist(), makeAlbum()));
        liveChartTrackList.add(makeTrack(30695458L, "처음처럼" , 5, 0, makeArtist(), makeAlbum()));
        liveChartTrackList.add(makeTrack(30695459L, "처음처럼1" , 6, 0, makeArtist(), makeAlbum()));
        liveChartTrackList.add(makeTrack(30695460L, "처음처럼2" , 7, 0, makeArtist(), makeAlbum()));
        liveChartTrackList.add(makeTrack(30695461L, "처음처럼3" , 8, 0, makeArtist(), makeAlbum()));
        liveChartTrackList.add(makeTrack(30695462L, "처음처럼4" , 9, 0, makeArtist(), makeAlbum()));
        liveChartTrackList.add(makeTrack(30695463L, "처음처럼5" , 10, 0, makeArtist(), makeAlbum()));
        liveChartTrackList.add(makeTrack(30695464L, "처음처럼6" , 11, 0, makeArtist(), makeAlbum()));
        liveChartTrackList.add(makeTrack(30695465L, "처음처럼7" , 12, 0, makeArtist(), makeAlbum()));


        return liveChartTrackList;
    }

    private TrackDto makeTrack(Long trackId ,String trackNm , int trackSn, int trackBfSn, ArtistDto artist, AlbumDto album){

        TrackDto track = new TrackDto();
        track.setTrackId(trackId);
        track.setTrackNm(trackNm);
        track.setCreateDtime(new Date());
        track.setTrackSn(trackSn);
        track.setTrackBfSn(trackBfSn);
        track.setUpdateDtime(new Date());
        track.setArtist(artist);
        track.setAlbum(album);

        return track;
    }


    private ArtistDto makeArtist(){
        ArtistDto artist = new ArtistDto();
        artist.setArtistId(20022492L);
        artist.setArtistNm("BLACKPINK");
        artist.setImgList(makeArtistImageList());

        return artist;
    }

    private List<ArtistDto> makeArtistList(){
        List<ArtistDto> artistList = new ArrayList();

        ArtistDto artist = new ArtistDto();
        artist.setArtistId(20022492L);
        artist.setArtistNm("BLACKPINK");
        artist.setImgList(makeArtistImageList());
        artistList.add(artist);

        artist = new ArtistDto();
        artist.setArtistId(20022493L);
        artist.setArtistNm("BLACKPINK1");
        artist.setImgList(makeArtistImageList());
        artistList.add(artist);

        return artistList;
    }

    private AlbumDto makeAlbum(){
        AlbumDto album = new AlbumDto();
        album.setAlbumId(20104358L);
        album.setTitle("마지막처럼");
        album.setImgList(makeAlbumImageList());
        return album;
    }

    private List<ImageDto> makeAlbumImageList(){
        List<ImageDto> albumImgList = new ArrayList<>();
        ImageDto image = new ImageDto();
        image.setSize(70);
        image.setUrl("http://asp-image.bugsm.co.kr/album/images/70/201043/20104358.jpg");

        albumImgList.add(image);
        image = new ImageDto();
        image.setSize(75);
        image.setUrl("http://asp-image.bugsm.co.kr/album/images/75/201043/20104358.jpg");
        albumImgList.add(image);

        image = new ImageDto();
        image.setSize(140);
        image.setUrl("http://asp-image.bugsm.co.kr/album/images/140/201043/20104358.jpg");
        albumImgList.add(image);


        image = new ImageDto();
        image.setSize(200);
        image.setUrl("http://asp-image.bugsm.co.kr/album/images/200/201043/20104358.jpg");
        albumImgList.add(image);

        image = new ImageDto();
        image.setSize(350);
        image.setUrl("http://asp-image.bugsm.co.kr/album/images/350/201043/20104358.jpg");
        albumImgList.add(image);

        image = new ImageDto();
        image.setSize(500);
        image.setUrl("http://asp-image.bugsm.co.kr/album/images/500/201043/20104358.jpg");
        albumImgList.add(image);

        return albumImgList;
    }


    private String getImageUrl(String pannelType, int size){
        if("ARTIST3".equals(pannelType)){
            return "https://api3-dev.musicmates.co.kr/img/recommend/typeB_"+size+".png";
        }else if("KNAME".equals(pannelType)){
            return "https://api3-dev.musicmates.co.kr/img/recommend/typeA_"+size+".png";
        }else if("RGENRE3".equals(pannelType)){
            return "https://api3-dev.musicmates.co.kr/img/recommend/typeB_"+size+".png";

        }else if("TRACK1".equals(pannelType)){
            return "https://api3-dev.musicmates.co.kr/img/recommend/typeC_"+size+".png";
        }else{
            return "https://api3-dev.musicmates.co.kr/img/recommend/typeB_"+size+".png";

        }
    }
    private List<ImageDto> makePanelBackGroundImageList(String pannelType){
        List<ImageDto> artistImgList = new ArrayList<>();
        ImageDto image = new ImageDto();
        image.setSize(75);
        image.setUrl(getImageUrl(pannelType,75));

        artistImgList.add(image);
        image = new ImageDto();
        image.setSize(140);
        image.setUrl(getImageUrl(pannelType,140));
        artistImgList.add(image);

        image = new ImageDto();
        image.setSize(200);
        image.setUrl(getImageUrl(pannelType,200));
        artistImgList.add(image);


        image = new ImageDto();
        image.setSize(350);
        image.setUrl(getImageUrl(pannelType,350));
        artistImgList.add(image);

        image = new ImageDto();
        image.setSize(500);
        image.setUrl(getImageUrl(pannelType,500));
        artistImgList.add(image);

        image = new ImageDto();
        image.setSize(1000);
        image.setUrl(getImageUrl(pannelType,1000));
        artistImgList.add(image);

        return artistImgList;
    }
    private List<ImageDto> makeArtistImageList(){
        List<ImageDto> artistImgList = new ArrayList<>();
        ImageDto image = new ImageDto();
        image.setSize(70);
        image.setUrl("http://asp-image.bugsm.co.kr/artist/images/70/200224/20022492.jpg");

        artistImgList.add(image);
        image = new ImageDto();
        image.setSize(75);
        image.setUrl("http://asp-image.bugsm.co.kr/artist/images/75/200224/20022492.jpg");
        artistImgList.add(image);

        image = new ImageDto();
        image.setSize(140);
        image.setUrl("http://asp-image.bugsm.co.kr/artist/images/140/200224/20022492.jpg");
        artistImgList.add(image);


        image = new ImageDto();
        image.setSize(200);
        image.setUrl("http://asp-image.bugsm.co.kr/artist/images/200/200224/20022492.jpg");
        artistImgList.add(image);

        image = new ImageDto();
        image.setSize(350);
        image.setUrl("http://asp-image.bugsm.co.kr/artist/images/350/200224/20022492.jpg");
        artistImgList.add(image);

        image = new ImageDto();
        image.setSize(500);
        image.setUrl("http://asp-image.bugsm.co.kr/artist/images/500/200224/20022492.jpg");
        artistImgList.add(image);

        return artistImgList;

    }
}
