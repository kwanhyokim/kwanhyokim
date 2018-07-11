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

            channelPanel = new ChannelPanel(panelType,makePanelBackGroundImageList(),makeDriveChannel());
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
                chartPanel = new ChartPanel(panelType ,makePanelBackGroundImageList(),makeLiveChartChannel());
            }else{
                chartPanel = new ChartPanel(panelType ,makePanelBackGroundImageList(),makeKidsChartChannel());
            }

        }catch(Exception e){
        }

        return chartPanel;
    }

    private Panel createMockTrackPanel(RecommendPanelType recommendPanelType){
        Panel trackPanel = null;
        try{

            if(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK.equals(recommendPanelType)){
                    trackPanel = new PreferGenreSimilarTrackPanel(recommendPanelType,makePanelBackGroundImageList(),makeTrackList());
            }else if(RecommendPanelType.PREFER_SIMILAR_TRACK.equals(recommendPanelType)){
                trackPanel = new PreferSimilarTrackPanel(recommendPanelType,makePanelBackGroundImageList(),"댄스",makeTrackList());
            }else if(RecommendPanelType.RCMMD_TRACK.equals(recommendPanelType)){
                trackPanel = new RcmmdTrackPanel(recommendPanelType,makePanelBackGroundImageList(),"댄스",makeTrackList());
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

        TrackDto track = new TrackDto();
        track.setTrackId(30695454L);
        track.setTrackNm("마지막처럼");
        track.setCreateDtime(new Date());
        track.setTrackBfSn(6);
        track.setUpdateDtime(new Date());
        track.setArtist(makeArtist());
        track.setAlbum(makeAlbum());
        liveChartTrackList.add(track);


        track = new TrackDto();
        track.setTrackId(30695455L);
        track.setTrackNm("마지막처럼2");
        track.setCreateDtime(new Date());
        track.setTrackBfSn(0);
        track.setUpdateDtime(new Date());
        track.setArtist(makeArtist());
        track.setAlbum(makeAlbum());
        liveChartTrackList.add(track);

        track = new TrackDto();
        track.setTrackId(30695456L);
        track.setTrackNm("마지막처럼3");
        track.setCreateDtime(new Date());
        track.setTrackBfSn(1);
        track.setUpdateDtime(new Date());
        track.setArtist(makeArtist());
        track.setAlbum(makeAlbum());
        liveChartTrackList.add(track);

        track = new TrackDto();
        track.setTrackId(30695457L);
        track.setTrackNm("마지막처럼4");
        track.setCreateDtime(new Date());
        track.setTrackBfSn(0);
        track.setUpdateDtime(new Date());
        track.setArtist(makeArtist());
        track.setAlbum(makeAlbum());
        liveChartTrackList.add(track);

        return liveChartTrackList;
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

    private List<ImageDto> makePanelBackGroundImageList(){
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
