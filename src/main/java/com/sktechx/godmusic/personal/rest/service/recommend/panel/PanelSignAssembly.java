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
import com.sktechx.godmusic.personal.common.domain.type.ChartType;
import com.sktechx.godmusic.personal.common.domain.type.PreferGenreType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.ChartDto;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.MoodPopularChnlDto;
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
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 설명 : 로그인 사용자 패널 생성기
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 24.
 */
@Slf4j
public abstract class PanelSignAssembly extends PanelAssembly {


    private final int popularChnlTrackLimitSize = 10;
    private final int preferGenreSimilarTrackLimitSize = 10;
    private final int similarTrackLimitSize = 10;

    private final int preferGenreChartTrackLimitSize = 12;

    protected abstract void appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta, final List<Panel> panelList);

    public List<Panel> assembleRecommendPanel(final PersonalPhaseMeta personalPhaseMeta) {
        final List<Panel> panelList = defaultPanelSetting(personalPhaseMeta);

        appendPreferencePanel(personalPhaseMeta, panelList);

        return panelList;
    }

    protected void appendPreferGenreChannelPanelList(final PersonalPhaseMeta personalPhaseMeta, final List<Panel> panelList, int limitSize) {

        List<Long> preferGenreIdList = personalPhaseMeta.getPreferGenreIdList(limitSize, Arrays.asList(PreferGenreType.PREFER));
        if (!CollectionUtils.isEmpty(preferGenreIdList)) {

            List<PreferGenrePopularChnlDto> preferGenrePopularChnlList = channelService.getPreferGenrePopularChannelIdList(preferGenreIdList);
            if (!CollectionUtils.isEmpty(preferGenrePopularChnlList)) {

                List<Long> chnlIdList = preferGenrePopularChnlList
                        .stream()
                        .filter(Objects::nonNull)
                        .map(preferGenrePopularChnl -> {
                            return preferGenrePopularChnl.getChnlId();
                        }).collect(Collectors.toList());

                Optional.ofNullable(channelMapper.selectChannelListByIdList(chnlIdList, popularChnlTrackLimitSize, personalPhaseMeta.getOsType())).ifPresent(popularChannelList->{
                    popularChannelList
                            .stream()
                            .filter(Objects::nonNull)
                            .forEach(channel -> {
                                try {
                                    GenreVo genre = new GenreVo();
                                    Optional<PreferGenrePopularChnlDto> findPreferGenrePopularChnlDto = preferGenrePopularChnlList.stream()
                                            .filter(dto -> dto.getChnlId().equals(channel.getChnlId()))
                                            .findFirst();
                                    if (findPreferGenrePopularChnlDto.isPresent()) {
                                        genre.setId(findPreferGenrePopularChnlDto.get().getPreferGenreId());
                                    } else {
                                        genre.setId(0L);
                                    }
                                    panelList.add(new PreferGenrePopularChannelPanel(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL, channel, genre , getDefaultBgImageList(  channel.getImgList(),personalPhaseMeta.getOsType() )           ));
                                } catch (Exception e) {
                                    log.error("appendPreferGenreChannelPanelList error : {}", e.getMessage());
                                    e.printStackTrace();
                                }
                            });
                });
            }
        }

    }

    public void appendPreferenceChartPanel(PersonalPhaseMeta personalPhaseMeta, final List<Panel> panelList) {
        Optional.ofNullable(personalPhaseMeta.getPreferGenreList()).ifPresent(preferGenreList -> {
            preferGenreList
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(characterPreferGenre -> {
                        if (PreferGenreType.TOP100.equals(characterPreferGenre.getPreferType())) {
                            Panel chartPanel = createChartPanel(RecommendPanelType.LIVE_CHART,"ALL" , ChartType.HOURLY, personalPhaseMeta.getOsType() , preferGenreChartTrackLimitSize);
                            panelList.add(chartPanel);
                        } else if (PreferGenreType.KIDS.equals(characterPreferGenre.getPreferType())) {
                            Panel chartPanel = createChartPanel(RecommendPanelType.KIDS_CHART,"KIDS",ChartType.HOURLY, personalPhaseMeta.getOsType(),preferGenreChartTrackLimitSize);
                            panelList.add(chartPanel);
                        }
                    });
        });
    }

    protected void appendPreferArtistPopularTrackPanel(PersonalPhaseMeta personalPhaseMeta, final List<Panel> panelList) {
        Long rcmmdId = personalPhaseMeta.getRecommendPersonalPanelRcmmdId(RecommendPanelContentType.RC_ATST_TR);

        if (rcmmdId != null) {
            RecommendArtistDto recommendArtistDto = recommendMapper.selectRecommendArtistById(rcmmdId);
            if (recommendArtistDto != null) {
                try {
                    panelList.add(new ArtistPanel(RecommendPanelType.ARTIST_POPULAR_TRACK, recommendArtistDto));
                } catch (Exception e) {
                    log.error("PanelSignAssembly appendPreferArtistPanel artistPanel create error : {}", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    protected void appendListenMoodPopularChanelPanelList(final PersonalPhaseMeta personalPhaseMeta, final List<Panel> panelList, int limitSize) {

        List<Long> moodIdList = personalPhaseMeta.getListenMoodIdList(RecommendPanelContentType.RC_MD_CN);
        if (!CollectionUtils.isEmpty(moodIdList)) {
            List<MoodPopularChnlDto> moodPopularChnlList = channelService.getListenMoodPopularChannelIdList(moodIdList);

            if (!CollectionUtils.isEmpty(moodPopularChnlList)) {
                List<Long> chnlIdList = moodPopularChnlList.stream().map(moodPopularChnlDto -> moodPopularChnlDto.getChnlId()).collect(Collectors.toList());
                    Optional.ofNullable(channelMapper.selectChannelListByIdList(chnlIdList, popularChnlTrackLimitSize, personalPhaseMeta.getOsType())).ifPresent(popularChannelList->{
                        popularChannelList
                                .stream()
                                .filter(Objects::nonNull)
                                .forEach(channel -> {
                                    try {
                                        panelList.add(new ListenMoodPopularChannelPanel(RecommendPanelType.LISTEN_MOOD_POPULAR_CHANNEL, channel ,getDefaultBgImageList(  channel.getImgList() , personalPhaseMeta.getOsType() )));
                                    } catch (Exception e) {
                                        log.error("appendPreferGenreChannelPanelList error : {}", e.getMessage());
                                        e.printStackTrace();
                                    }
                                });
                    });

            }
        }
    }

    protected void appendSimilarTrackPanelList(final PersonalPhaseMeta personalPhaseMeta, final List<Panel> panelList, int rcmmdLimitSize) {
        List<PersonalPanel> rcmmdPanelList = personalPhaseMeta.getRecommendPersonalPanelList(RecommendPanelContentType.RC_SML_TR);
        if (!CollectionUtils.isEmpty(rcmmdPanelList)) {
            List<Long> rcmmdIdList = rcmmdPanelList.stream().map(personalPanel -> personalPanel.getRecommendId()).collect(Collectors.toList());
               Optional.ofNullable(recommendMapper.selectRecommendSimilarTrackListByIdList(rcmmdIdList, rcmmdLimitSize, similarTrackLimitSize, personalPhaseMeta.getOsType())).ifPresent(recommendSimilarTrackList->{
                   recommendSimilarTrackList
                           .stream()
                           .filter(Objects::nonNull)
                           .forEach(similarTrack -> {

                               Optional<PersonalPanel> personalPanel = rcmmdPanelList.stream()
                                       .filter(panel -> panel.getRecommendId().equals(similarTrack.getRcmmdId()))
                                       .findFirst();
                               if (personalPanel.isPresent()) {
                                   similarTrack.setTrackCount(personalPanel.get().getTrackCount());
                               }
                               try {
                                   panelList.add(new PreferSimilarTrackPanel(RecommendPanelType.PREFER_SIMILAR_TRACK, similarTrack, getDefaultBgImageList(  similarTrack.getImgList() , personalPhaseMeta.getOsType() )));
                               } catch (Exception e) {
                                   log.error("ListenPhasePanelAssembly appendSimilarTrackPanelList error : {}", e.getMessage());
                                   e.printStackTrace();
                               }
                           });
               });
        }
    }

    protected void appendPreferGenreSimilarTrackPanelList(PersonalPhaseMeta personalPhaseMeta, final List<Panel> panelList, int rcmmdLimitSize) {
        List<PersonalPanel> rcmmdPanelList = personalPhaseMeta.getRecommendPersonalPanelList(RecommendPanelContentType.RC_GR_TR);

        if (!CollectionUtils.isEmpty(rcmmdPanelList)) {
            List<Long> rcmmdIdList = rcmmdPanelList.stream().map(personalPanel -> personalPanel.getRecommendId()).collect(Collectors.toList());

            if (!CollectionUtils.isEmpty(rcmmdIdList)) {
                Optional.ofNullable(recommendMapper.selectRecommendPreferGenreSimilarTrackListByIdList(rcmmdIdList, rcmmdLimitSize, preferGenreSimilarTrackLimitSize, personalPhaseMeta.getOsType()))
                        .ifPresent(preferGenreSimilarTrackList ->{
                            preferGenreSimilarTrackList
                                    .stream()
                                    .filter(Objects::nonNull)
                                    .forEach(preferGenreSimilarTrack -> {
                                        try {
                                            Optional<PersonalPanel> personalPanel = rcmmdPanelList.stream()
                                                    .filter(panel -> panel.getRecommendId().equals(preferGenreSimilarTrack.getRcmmdId()))
                                                    .findFirst();
                                            if (personalPanel.isPresent()) {
                                                preferGenreSimilarTrack.setTrackCount(personalPanel.get().getTrackCount());
                                            }
                                            panelList.add(new PreferGenreSimilarTrackPanel(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK
                                                             , preferGenreSimilarTrack
                                                             , getDefaultBgImageList( preferGenreSimilarTrack.getImgList() , personalPhaseMeta.getOsType())
                                            ));
                                        } catch (Exception e) {
                                            log.error("addPreferGenreSimilarTrackPanelList error : {}", e.getMessage());
                                            e.printStackTrace();
                                        }
                                    });
                        });

            }
        }
    }

    private Panel createChartPanel(RecommendPanelType recommendPanelType,String svcContentType ,ChartType chartType ,  OsType osType, int trackLimitSize) {
        ChartDto chartDto = chartMapper.selectPreferGenreChart(svcContentType , chartType, osType, trackLimitSize);
        if (chartDto != null) {
            try {
                return new ChartPanel(recommendPanelType, chartDto, getDefaultBgImageList( chartDto.getImgList() , osType) );
            } catch (Exception e) {
                log.error("PanelSignAssembly createChartPanel create error : {}", e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }


}
