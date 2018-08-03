/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.impl.recommend.panel;

import com.netflix.discovery.converters.Auto;
import com.sktechx.godmusic.personal.common.domain.type.OsType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.ChartDto;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.ImageDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.PreferGenrePopularChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.artist.ArtistPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.ListenMoodPopularChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.PopularChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.PreferGenrePopularChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.chart.ChartPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.GenreVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.PreferGenreSimilarTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.PreferSimilarTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.RcmmdTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.repository.ChannelMapper;
import com.sktechx.godmusic.personal.rest.repository.ChartMapper;
import com.sktechx.godmusic.personal.rest.repository.RecommendMapper;
import com.sktechx.godmusic.personal.rest.service.ChannelService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendPanelService;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelAppenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 설명 : 추천 패널 추가 서비스
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 08. 02.
 */
@Slf4j
@Service
public class PanelAppenderServiceImpl implements PanelAppenderService {

    @Autowired
    private RecommendMapper recommendMapper;

    @Autowired
    private RecommendPanelService recommendPanelService;

    @Autowired
    private ChartMapper chartMapper;

    @Autowired
    private ChannelService channelService;


    @Override
    public void appendRecommendCfTrackPanelList(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList, int limitSize) {
        List<Long> rcmmdIdList = personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RecommendPanelContentType.RC_CF_TR);
        if(!CollectionUtils.isEmpty(rcmmdIdList)){
            List<RecommendTrackDto> recommendCfTrackList =  recommendMapper.selectRecommendCfTrackListByIdList(rcmmdIdList , limitSize);
            if(!CollectionUtils.isEmpty(recommendCfTrackList)){
                List<ImageDto> bgImgList = recommendPanelService.getPanelBackgroundImageList(RecommendPanelType.RCMMD_TRACK,personalPhaseMeta.getOsType());
                recommendCfTrackList.stream().forEach(recommendCfTrack->{
                    try{
                        panelList.add(new RcmmdTrackPanel(RecommendPanelType.RCMMD_TRACK , recommendCfTrack,bgImgList));
                    }catch(Exception e){
                        log.error("appendRecommendCfTrackPanelList error : {}",e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
        }
    }
    @Override
    public void appendPreferGenreSimilarTrackPanelList(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList, int limitSize) {
        List<Long> rcmmdIdList = personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RecommendPanelContentType.RC_GR_TR);
        if(!CollectionUtils.isEmpty(rcmmdIdList)){

            List<RecommendTrackDto> recommendPreferGenreSimilarTrackList =  recommendMapper.selectRecommendPreferGenreSimilarTrackListByIdList(rcmmdIdList , limitSize);
            if(!CollectionUtils.isEmpty(recommendPreferGenreSimilarTrackList)){
                List<ImageDto> bgImgList = recommendPanelService.getPanelBackgroundImageList(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK,personalPhaseMeta.getOsType());
                recommendPreferGenreSimilarTrackList.stream().forEach(preferGenreSimilarTrack->{
                    try{
                        panelList.add(new PreferGenreSimilarTrackPanel(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK,preferGenreSimilarTrack,bgImgList));
                    }catch(Exception e){
                        log.error("addPreferGenreSimilarTrackPanelList error : {}",e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    @Override
    public void appendListenMoodPopularChanelPanelList(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList, int limitSize) {
        List<Long> rcmmdIdList = personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RecommendPanelContentType.RC_MD_CN);
        if(!CollectionUtils.isEmpty(rcmmdIdList)){
            List<ChnlDto> popularChannelList = recommendMapper.selectRecommendListenMoodChannelListByIdList(rcmmdIdList , limitSize);
            if(!CollectionUtils.isEmpty(popularChannelList)){
                List<ImageDto> bgImgList = recommendPanelService.getPanelBackgroundImageList(RecommendPanelType.POPULAR_CHANNEL , personalPhaseMeta.getOsType());
                popularChannelList.stream().forEach(popularChannel ->{
                    try{
                        panelList.add(new ListenMoodPopularChannelPanel(RecommendPanelType.LISTEN_MOOD_POPULAR_CHANNEL,popularChannel,bgImgList));
                    }catch(Exception e){
                        log.error("VisitPhasePanel appendListenMoodPopularChanelPanelList error : {}",e.getMessage());
                        e.printStackTrace();
                    }

                });
            }
        }
    }

    @Override
    public void appendPreferArtistPopularTrackPanel(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList) {
        Long rcmmdId = personalPhaseMeta.getRecommendPersonalPanelRcmmdId(RecommendPanelContentType.RC_ATST_TR);
        if(rcmmdId != null){
            RecommendArtistDto recommendArtistDto = recommendMapper.selectRecommendArtistById(rcmmdId);
            if(recommendArtistDto != null){
                try{
                    panelList.add(new ArtistPanel(RecommendPanelType.ARTIST_POPULAR_TRACK, recommendArtistDto));
                }catch(Exception e){
                    log.error("PanelSignAssembly appendPreferArtistPanel artistPanel create error : {}",e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }



    @Override
    public void appendPreferenceChartPanel(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList) {
        Optional.ofNullable(personalPhaseMeta.getPreferGenreList()).orElse(Collections.emptyList()).stream()
            .forEach(preferGenreDto -> {
                if("TOP100".equals(preferGenreDto.getPreferType())){
                    Panel chartPanel = createChartPanel("LIVE_CHART" , personalPhaseMeta.getOsType()) ;
                    if(chartPanel != null){
                        panelList.add(0,chartPanel);
                    }
                }else if("KIDS".equals(preferGenreDto.getPreferType())){
                    Panel chartPanel = createChartPanel("KIDS_CHART" , personalPhaseMeta.getOsType()) ;
                    panelList.add ( chartPanel );
                }
            });
    }

    @Override
    public void appendPreferGenreChannelPanelList(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList, int limitSize) {
        List<Long> preferGenreIdList = personalPhaseMeta.getPreferGenreIdList(limitSize);
        if(!CollectionUtils.isEmpty(preferGenreIdList)){
            List<PreferGenrePopularChnlDto> preferGenrePopularChnlList = chartMapper.selectPreferGenrePopularChannel(preferGenreIdList);
            if(!CollectionUtils.isEmpty(preferGenrePopularChnlList)) {
                List<ImageDto> bgImgList = recommendPanelService.getPanelBackgroundImageList(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL , personalPhaseMeta.getOsType());
                preferGenrePopularChnlList.stream().forEach(channel -> {
                    try {
                        panelList.add(new PreferGenrePopularChannelPanel(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL, channel.getPopularChannel(), new GenreVo(channel), bgImgList));
                    } catch (Exception e) {
                        log.error("VisitPhasePanel appendPreferencePanel error : {}", e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    @Override
    public void appendSimilarTrackPanelList(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList, int limitSize) {
        List<Long> rcmmdIdList = personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RecommendPanelContentType.RC_SML_TR);
        if(!CollectionUtils.isEmpty(rcmmdIdList)){
            List<RecommendTrackDto> recommendSimilarTrackList =  recommendMapper.selectRecommendSimilarTrackListByIdList(rcmmdIdList,limitSize);
            if(!CollectionUtils.isEmpty(recommendSimilarTrackList)){
                List<ImageDto> bgImgList = recommendPanelService.getPanelBackgroundImageList(RecommendPanelType.PREFER_SIMILAR_TRACK,personalPhaseMeta.getOsType());
                recommendSimilarTrackList.stream().forEach(recommendTrack->{
                    try{
                        panelList.add(new PreferSimilarTrackPanel(RecommendPanelType.PREFER_SIMILAR_TRACK,recommendTrack,bgImgList));
                    }catch(Exception e){
                        log.error("ListenPhasePanelAssembly appendSimilarTrackPanelList error : {}",e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    @Override
    public void appendDefaultPopularChannelPanel(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList, int limitSize) {
        List<ImageDto> bgImgList = recommendPanelService.getPanelBackgroundImageList(RecommendPanelType.POPULAR_CHANNEL , personalPhaseMeta.getOsType());
        List<ChnlDto> hotplayChannelList = channelService.getHotplayChannelList(limitSize);

        hotplayChannelList.stream().forEach(channel -> {
            try{
                panelList.add(new PopularChannelPanel(RecommendPanelType.POPULAR_CHANNEL,channel,bgImgList));
            }catch(Exception e){
                log.error("GuestPhasePanel defaultPanelSetting IllegalAccessException : {}",e.getMessage());
            }
        });

    }

    private Panel createChartPanel(String chartType, OsType osType){
        ChartDto chartDto = chartMapper.selectMainPanelChart(chartType);
        RecommendPanelType chartPanelType = RecommendPanelType.fromCode(chartType);

        List<ImageDto> bgImgList = recommendPanelService.getPanelBackgroundImageList(chartPanelType , osType);
        if(chartDto != null){
            try{
                return new ChartPanel(chartPanelType,chartDto,bgImgList);
            }catch(Exception e){
                log.error("PanelSignAssembly createChartPanel create error : {}",e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }
}
