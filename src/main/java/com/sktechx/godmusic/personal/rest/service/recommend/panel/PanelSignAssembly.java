/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.recommend.panel;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.type.PreferGenreType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.ChartDto;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.PreferGenrePopularChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.artist.ArtistPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.ListenMoodPopularChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.PreferGenrePopularChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.chart.ChartPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.GenreVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.PreferGenreSimilarTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.PreferSimilarTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 설명 : 로그인 사용자 패널 생성기
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 24.
 */
@Slf4j
public abstract class PanelSignAssembly extends PanelAssembly{
    protected abstract void appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList);

    private final int popularChnlTrackLimitSize = 10;

    public List<Panel> assembleRecommendPanel(final PersonalPhaseMeta personalPhaseMeta){
        final List<Panel> panelList = defaultPanelSetting(personalPhaseMeta);

        appendPreferencePanel(personalPhaseMeta,panelList);

        return panelList;
    }


    protected void appendSimilarTrackPanelList(final PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList, int limitSize) {
        List<Long> rcmmdIdList = personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RecommendPanelContentType.RC_SML_TR);

        if(!CollectionUtils.isEmpty(rcmmdIdList)){
            List<RecommendTrackDto> recommendSimilarTrackList =  recommendMapper.selectRecommendSimilarTrackListByIdList(rcmmdIdList,limitSize);
            if(!CollectionUtils.isEmpty(recommendSimilarTrackList)){
                List<ImageInfo> bgImgList = recommendPanelService.getPanelBackgroundImageList(RecommendPanelType.PREFER_SIMILAR_TRACK,personalPhaseMeta.getOsType());
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



    protected void appendPreferGenreChannelPanelList(final PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList, int limitSize) {

        List<Long> preferGenreIdList = personalPhaseMeta.getPreferGenreIdList(limitSize, Arrays.asList(PreferGenreType.PREFER));

        if(!CollectionUtils.isEmpty(preferGenreIdList)){

            log.info("preferGenreIdList : {}",preferGenreIdList);
            List<PreferGenrePopularChnlDto> preferGenrePopularChnlList = channelMapper.selectPreferGenrePopularChannelIdList(preferGenreIdList,personalPhaseMeta.getOsType());

            log.info("preferGenrePopularChnlList : {}",preferGenrePopularChnlList);
            if( !CollectionUtils.isEmpty( preferGenrePopularChnlList)){

                List<Long> chnlIdList = preferGenrePopularChnlList.stream().map(preferGenrePopularChnl -> {
                    return preferGenrePopularChnl.getChnlId();
                }).collect(Collectors.toList());
                log.info("chnlIdList : {}",chnlIdList);
                List<ChnlDto> popularChannelList = channelMapper.selectPopularChannelList(chnlIdList,popularChnlTrackLimitSize,personalPhaseMeta.getOsType());

                log.info("popularChannelList : {}",popularChannelList);
                if(!CollectionUtils.isEmpty(popularChannelList)){
                    popularChannelList
                            .stream()
                            .filter(Objects::nonNull)
                            .forEach(channel->{
                                try {
                                    PreferGenrePopularChnlDto findPreferGenrePopularChnlDto = preferGenrePopularChnlList.stream()
                                                .filter(dto -> dto.getChnlId().equals(channel.getChnlId()))
                                                .findFirst().orElse(new PreferGenrePopularChnlDto());

                                    panelList.add(new PreferGenrePopularChannelPanel(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL, channel, new GenreVo(findPreferGenrePopularChnlDto)));
                                } catch (Exception e) {
                                    log.error("VisitPhasePanel appendPreferencePanel error : {}", e.getMessage());
                                    e.printStackTrace();
                                }
                            });
                }

            }

        }

    }

    public void appendPreferenceChartPanel(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList) {
        Optional.ofNullable(personalPhaseMeta.getPreferGenreList()).ifPresent(preferGenreList -> {
            preferGenreList
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(characterPreferGenre ->{
                        if(PreferGenreType.TOP100.equals(characterPreferGenre.getPreferType())){
                            Panel chartPanel = createChartPanel("LIVE_CHART" , personalPhaseMeta.getOsType()) ;
                            if(chartPanel != null){
                                panelList.add(0,chartPanel);
                            }
                        }else if(PreferGenreType.KIDS.equals(characterPreferGenre.getPreferType())){
                            Panel chartPanel = createChartPanel("KIDS_CHART" , personalPhaseMeta.getOsType()) ;
                            panelList.add ( chartPanel );
                        }
                    });
        });
    }

    protected void appendPreferArtistPopularTrackPanel(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList) {
        Long rcmmdId =personalPhaseMeta.getRecommendPersonalPanelRcmmdId(RecommendPanelContentType.RC_ATST_TR);

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

    protected void appendListenMoodPopularChanelPanelList(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList, int limitSize) {
        List<Long> rcmmdIdList = personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RecommendPanelContentType.RC_MD_CN);

        if(!CollectionUtils.isEmpty(rcmmdIdList)){
            List<ChnlDto> popularChannelList = recommendMapper.selectRecommendListenMoodChannelListByIdList(rcmmdIdList , limitSize);
            if(!CollectionUtils.isEmpty(popularChannelList)){
                List<ImageInfo> bgImgList = recommendPanelService.getPanelBackgroundImageList(RecommendPanelType.POPULAR_CHANNEL , personalPhaseMeta.getOsType());
                popularChannelList
                        .stream()
                        .filter(Objects::nonNull)
                        .forEach(popularChannel ->{
                            try{
                                panelList.add(new ListenMoodPopularChannelPanel(RecommendPanelType.LISTEN_MOOD_POPULAR_CHANNEL,popularChannel));
                            }catch(Exception e){
                                log.error("VisitPhasePanel appendListenMoodPopularChanelPanelList error : {}",e.getMessage());
                                e.printStackTrace();
                            }

                        });
            }
        }
    }

    protected void appendPreferGenreSimilarTrackPanelList(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList, int limitSize) {
        List<Long> rcmmdIdList = personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RecommendPanelContentType.RC_GR_TR);

        if(!CollectionUtils.isEmpty(rcmmdIdList)){
            List<RecommendTrackDto> recommendPreferGenreSimilarTrackList =  recommendMapper.selectRecommendPreferGenreSimilarTrackListByIdList(rcmmdIdList , limitSize);
            if(!CollectionUtils.isEmpty(recommendPreferGenreSimilarTrackList)){
                List<ImageInfo> bgImgList = recommendPanelService.getPanelBackgroundImageList(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK,personalPhaseMeta.getOsType());
                recommendPreferGenreSimilarTrackList
                        .stream()
                        .filter(Objects::nonNull)
                        .forEach(preferGenreSimilarTrack->{
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

    private Panel createChartPanel(String chartType, OsType osType){
        ChartDto chartDto = chartMapper.selectMainPanelChart(chartType);
        RecommendPanelType chartPanelType = RecommendPanelType.fromCode(chartType);

        List<ImageInfo> bgImgList = recommendPanelService.getPanelBackgroundImageList(chartPanelType , osType);
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
