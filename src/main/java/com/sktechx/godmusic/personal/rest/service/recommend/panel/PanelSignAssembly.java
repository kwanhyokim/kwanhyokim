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
import com.sktechx.godmusic.personal.common.domain.PreferPropsType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.ChartDto;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.MoodPopularChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.PreferGenrePopularChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendTrackDto;
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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.*;
import static com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType.*;
/**
 * 설명 : 로그인 사용자 패널 생성기
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 24.
 */
@Slf4j
public abstract class PanelSignAssembly extends PanelAssembly {

    protected abstract void appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta, final List<Panel> panelList);

    public List<Panel> assembleRecommendPanel(final PersonalPhaseMeta personalPhaseMeta) throws Exception {
        final List<Panel> panelList = defaultPanelSetting(personalPhaseMeta);

        appendPreferencePanel(personalPhaseMeta, panelList);

        return panelList;
    }

    protected void appendPreferGenreChannelPanelList(final PersonalPhaseMeta personalPhaseMeta,
                                                     final List<Panel> panelList,
                                                     int panelLimitSize) {

        List<Long> preferGenreIdList = personalPhaseMeta.getPreferGenreIdList(panelLimitSize);
        List<PreferGenrePopularChnlDto> appendChannelList = channelService.getPreferGenrePopularChannelList(
                                                                    preferGenreIdList,
                                                                    POPULAR_CHNL_TRACK_LIMIT_SIZE,
                                                                    personalPhaseMeta.getOsType());
        if (!CollectionUtils.isEmpty(appendChannelList)) {
            appendChannelList
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(channel -> {
                        try{
                            panelList.add(createPreferGenrePopularChannelPanel(personalPhaseMeta,channel));
                        }catch(Exception e){
                            log.error("appendPreferGenreChannelPanelList error : {}", e.getMessage());
                        }
                    });
        }

    }

    public void appendPreferenceChartPanel(final PersonalPhaseMeta personalPhaseMeta, final List<Panel> panelList) {
        if(!CollectionUtils.isEmpty(personalPhaseMeta.getPreferDispList())) {

            personalPhaseMeta.getPreferDispList()
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(characterPreferDisp -> {

                        RecommendPanelType recommendPanelType = getPreferRecommendPanelType(characterPreferDisp.getDispPropsType());
                        if (recommendPanelType != null) {
                            Panel panel = createChartPanel(recommendPanelType, personalPhaseMeta.getOsType(), PREFER_DISP_CHART_TRACK_LIMIT_SIZE);
                            if (panel != null) {
                                panelList.add(panel);
                            }
                        }

                    });
        }
    }

    protected void appendPreferArtistPopularTrackPanel(final PersonalPhaseMeta personalPhaseMeta, final List<Panel> panelList) {
        Long rcmmdId = personalPhaseMeta.getRecommendPersonalPanelRcmmdId(RC_ATST_TR);

        if (rcmmdId != null) {
            RecommendArtistDto recommendArtistDto = recommendMapper.selectRecommendArtistById(rcmmdId);

            if (recommendArtistDto != null && !CollectionUtils.isEmpty(recommendArtistDto.getArtistList())) {

                try {

                    if(isArtistPopularTrackPanelAppend(recommendArtistDto)){
                        panelList.add(new ArtistPanel(recommendArtistDto));
                    }

                } catch (Exception e) {
                    log.error("PanelSignAssembly appendPreferArtistPanel artistPanel create error : {}", e.getMessage());
                }
            }
        }
    }

    private boolean isArtistPopularTrackPanelAppend(RecommendArtistDto recommendArtistDto){

        List<ArtistDto> artistList = recommendArtistDto.getArtistList();

        long representationArtistCount = artistList.stream().filter(artistDto -> {
            if("REPRSNT".equals(artistDto.getArtistType())){
                return true;
            }
            return false;
        }).count();

        long trackCount = recommendArtistDto.getTrackCount();

        if(representationArtistCount == 1){
            if(trackCount >= ARTIST_POPULAR_TRACK_DISP_ONE_ARTIST_COUNT){
                return true;
            }
        }else if(representationArtistCount == 2){
            if(trackCount >= ARTIST_POPULAR_TRACK_DISP_TWO_ARTIST_COUNT){
                return true;
            }
        }else if(representationArtistCount >= 3){
            if(trackCount >= ARTIST_POPULAR_TRACK_DISP_STANDARD_COUNT){
                return true;
            }
        }

        return false;
    }


    protected void appendListenMoodPopularChanelPanelList(final PersonalPhaseMeta personalPhaseMeta,
                                                          final List<Panel> panelList,
                                                          int panelLimitSize) {
        List<Long> moodIdList = personalPhaseMeta.getListenMoodIdList(RC_MD_CN, panelLimitSize);

        if (!CollectionUtils.isEmpty(moodIdList)) {
            List<MoodPopularChnlDto> appendChannelList =
                    channelService.getListenMoodPopularChannelIdList(moodIdList,POPULAR_CHNL_TRACK_LIMIT_SIZE, personalPhaseMeta.getOsType());

            if (!CollectionUtils.isEmpty(appendChannelList)) {
                appendChannelList
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(channel -> {
                        try {
                            panelList.add(createListenMoodPopularChannelPanel(personalPhaseMeta,channel));
                        } catch (Exception e) {
                            log.error("appendPreferGenreChannelPanelList error : {}", e.getMessage());
                        }
                    });
            }
        }
    }

    protected void appendSimilarTrackPanelList(final PersonalPhaseMeta personalPhaseMeta,
                                               final List<Panel> panelList,
                                               int panelLimitSize) {

        List<Long> rcmmdIdList = personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RC_SML_TR);

        if(!CollectionUtils.isEmpty(rcmmdIdList)){

            List<RecommendTrackDto> similarTrackList =
                    recommendMapper.selectRecommendSimilarTrackListByIdList(rcmmdIdList, panelLimitSize,
                                                SIMILAR_TRACK_LIMIT_SIZE, personalPhaseMeta.getOsType());

            if(!CollectionUtils.isEmpty(similarTrackList)){
                similarTrackList
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(similarTrack ->{
                        try {
                            if(similarTrack.getTrackCount() >= SIMILAR_TRACK_DISP_STANDARD_COUNT){
                                panelList.add(createSimilarTrackPanel (personalPhaseMeta, similarTrack));
                            }
                        } catch (Exception e) {
                            log.error("appendSimilarTrackPanelList error : {}", e.getMessage());
                        }
                    });
            }
        }
    }

    protected void appendPreferGenreSimilarTrackPanelList(PersonalPhaseMeta personalPhaseMeta,
                                                          final List<Panel> panelList,
                                                          int panelLimitSize) {

        List<Long> rcmmdIdList = personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RC_GR_TR);

        if (!CollectionUtils.isEmpty(rcmmdIdList)) {

            List<RecommendTrackDto> preferGenreSimilarTrackList =
                    recommendMapper.selectRecommendPreferGenreSimilarTrackListByIdList(rcmmdIdList, panelLimitSize,
                            PREFER_GENRE_SIMILAR_TRACK_LIMIT_SIZE, personalPhaseMeta.getOsType());

            if(!CollectionUtils.isEmpty(preferGenreSimilarTrackList)){
                preferGenreSimilarTrackList
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(preferGenreSimilarTrack ->{
                        try {
                            if(preferGenreSimilarTrack.getTrackCount() >= PREFER_GENRE_SIMILAR_TRACK_DISP_STANDARD_COUNT){
                                panelList.add(createPreferGenreSimilarTrackPanel(personalPhaseMeta,preferGenreSimilarTrack));
                            }
                        } catch (Exception e) {
                            log.error("appendPreferGenreSimilarTrackPanelList error : {}", e.getMessage());
                        }
                    });
            }

        }
    }


    private RecommendPanelType getPreferRecommendPanelType(String preferPropsType ){

        if(PreferPropsType.TOP100.getCode().equals(preferPropsType)){
            return RecommendPanelType.LIVE_CHART;
        }else if(PreferPropsType.KIDS100.getCode().equals(preferPropsType)){
            return RecommendPanelType.KIDS_CHART;
        }
        return null;

    }



    private Panel createPreferGenreSimilarTrackPanel(final PersonalPhaseMeta personalPhaseMeta,
                                                     final RecommendTrackDto preferGenreSimilarTrack){
        return new PreferGenreSimilarTrackPanel(preferGenreSimilarTrack,
                                                getDefaultBgImageList(preferGenreSimilarTrack.getImgList(),personalPhaseMeta.getOsType()));
    }
    private Panel createSimilarTrackPanel(final PersonalPhaseMeta personalPhaseMeta,
                                          final RecommendTrackDto similarTrack){
        return new PreferSimilarTrackPanel( similarTrack, getDefaultBgImageList(similarTrack.getImgList(),personalPhaseMeta.getOsType()));
    }
    private Panel createListenMoodPopularChannelPanel(final PersonalPhaseMeta personalPhaseMeta,
                                                      final MoodPopularChnlDto moodPopularChnlDto){
        ChnlDto channel = moodPopularChnlDto.getChannel();
        return new ListenMoodPopularChannelPanel(channel, getDefaultBgImageList(channel.getImgList(), personalPhaseMeta.getOsType()));
    }

    private Panel createPreferGenrePopularChannelPanel(final PersonalPhaseMeta personalPhaseMeta,
                                                       final PreferGenrePopularChnlDto preferGenrePopularChannel) {

        ChnlDto channel = preferGenrePopularChannel.getChannel();
        GenreVo genre = new GenreVo(preferGenrePopularChannel.getPreferGenreId());

        return new PreferGenrePopularChannelPanel(channel,genre,
                getDefaultBgImageList( channel.getImgList(), personalPhaseMeta.getOsType()));
    }
}
