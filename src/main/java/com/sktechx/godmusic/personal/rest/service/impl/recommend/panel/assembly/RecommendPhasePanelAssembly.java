/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.impl.recommend.panel.assembly;

import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.RcmmdTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelSignAssembly;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.*;
import static com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType.RC_CF_TR;

/**
 * 설명 : 추천 단계 ( 3단계 ) 패널
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 14.
 */

@Slf4j
@Service("recommendPhasePanelAssembly")
public class RecommendPhasePanelAssembly extends PanelSignAssembly {

    private RecommendPhasePanelAssembly(){}
    @Override
    protected List<Panel> defaultPanelSetting(PersonalPhaseMeta personalPhaseMeta) {

        long startTime = System.currentTimeMillis();
        long elapsed = 0;

        final List<Panel> panelList = new ArrayList();

        appendRecommendCfTrackPanelList(personalPhaseMeta, panelList, RCMMD_CF_PANEL_DEFAULT_SIZE);
        elapsed = System.currentTimeMillis() - startTime;
        log.info("defaultPanelSetting appendRecommendCfTrackPanelList  : {}",elapsed);

        boolean isFillRecommendPanel = false;
        if(RCMMD_CF_PANEL_DEFAULT_SIZE > panelList.size()){
            isFillRecommendPanel = true;
        }

        startTime = System.currentTimeMillis();
        appendPreferGenreSimilarTrackPanelList(personalPhaseMeta, panelList, PREFER_GENRE_SIMILAR_TRACK_PANEL_APPEND_SIZE);
        elapsed = System.currentTimeMillis() - startTime;
        log.info("defaultPanelSetting appendPreferGenreSimilarTrackPanelList  : {}",elapsed);

        int panelDefaultSize = RCMMD_CF_PANEL_DEFAULT_SIZE+SIMILAR_TRACK_PANEL_APPEND_SIZE;

        if( panelDefaultSize > panelList.size()){

            startTime = System.currentTimeMillis();

            appendSimilarTrackPanelList(personalPhaseMeta , panelList ,panelDefaultSize  - panelList.size() );
            elapsed = System.currentTimeMillis() - startTime;
            log.info("panelDefaultSize appendSimilarTrackPanelList  : {}",elapsed);

            if(panelDefaultSize > panelList.size()){

                startTime = System.currentTimeMillis();

                appendPreferGenreChannelPanelList(personalPhaseMeta, panelList, panelDefaultSize - panelList.size() );
                elapsed = System.currentTimeMillis() - startTime;
                log.info("panelDefaultSize panelList.size appendSimilarTrackPanelList  : {}",elapsed);


                startTime = System.currentTimeMillis();
                List filterChnlIdList = panelList.stream()
                        .filter(panel -> (
                                RecommendPanelType.LISTEN_MOOD_POPULAR_CHANNEL.equals(panel.getType())
                                        || RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL.equals(panel.getType())
                        ) && panel.getContent() != null)
                        .map(panel-> {
                            return panel.getContent().getId();
                        })
                        .collect(Collectors.toList());

                if(panelDefaultSize > panelList.size()){
                    appendDefaultPopularChannelPanel(personalPhaseMeta , panelList ,panelDefaultSize - panelList.size() , filterChnlIdList);
                }
                elapsed = System.currentTimeMillis() - startTime;
                log.info("panelDefaultSize end  : {}",elapsed);
            }
        }else{
            if(isFillRecommendPanel){

                startTime = System.currentTimeMillis();

                int mforuPanelCount = panelCount(RecommendPanelType.RCMMD_TRACK,panelList);
                int panelAppendCount = RCMMD_CF_PANEL_DEFAULT_SIZE - mforuPanelCount;
                if(panelAppendCount > 0){
                    appendSimilarTrackPanelList(personalPhaseMeta,panelList,panelAppendCount);

                    if(panelDefaultSize >= panelList.size()){
                        appendPreferGenreChannelPanelList(personalPhaseMeta, panelList, panelAppendCount );

                        List filterChnlIdList = panelList.stream()
                                .filter(panel -> (
                                        RecommendPanelType.LISTEN_MOOD_POPULAR_CHANNEL.equals(panel.getType())
                                                || RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL.equals(panel.getType())
                                ) && panel.getContent() != null)
                                .map(panel-> {
                                    return panel.getContent().getId();
                                })
                                .collect(Collectors.toList());

                        if(panelDefaultSize >= panelList.size()){
                            appendDefaultPopularChannelPanel(personalPhaseMeta , panelList ,panelAppendCount , filterChnlIdList);
                        }
                    }
                }

                elapsed = System.currentTimeMillis() - startTime;
                log.info("isFillRecommendPanel end  : {}",elapsed);
            }
        }

        return panelList;
    }

    @Override
    protected void appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta ,final List<Panel> panelList) {

        long startTime = System.currentTimeMillis();
        long elapsed = 0;

        appendPreferArtistPopularTrackPanel(personalPhaseMeta,panelList);
        elapsed = System.currentTimeMillis() - startTime;
        log.info("appendPreferencePanel  appendPreferencePanel : {}",elapsed);

        startTime = System.currentTimeMillis();
        appendPreferenceChartPanel(personalPhaseMeta,panelList);
        elapsed = System.currentTimeMillis() - startTime;
        log.info("appendPreferencePanel  appendPreferenceChartPanel : {}",elapsed);


        startTime = System.currentTimeMillis();
        sort(personalPhaseMeta, panelList);
        elapsed = System.currentTimeMillis() - startTime;
        log.info("appendPreferencePanel  sort : {}",elapsed);
    }

    private void appendRecommendCfTrackPanelList(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList, int panelLimitSize) {
        List<Long> rcmmdIdList = personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RC_CF_TR);

        if(!CollectionUtils.isEmpty(rcmmdIdList)){
            List<RecommendTrackDto> recommendCfTrackList =
                    recommendMapper.selectRecommendCfTrackListByIdList(rcmmdIdList, panelLimitSize, RCMMD_CF_TRACK_LIMIT_SIZE, personalPhaseMeta.getOsType());

            if (!CollectionUtils.isEmpty(recommendCfTrackList)) {
                recommendCfTrackList
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(cfTrack -> {
                        try {

                            if(cfTrack.getTrackCount() >= RCMMD_CF_TRACK_DISP_STANDARD_COUNT){

                                panelList.add(createRecommendCfTrackPanel(personalPhaseMeta,cfTrack));
                            }

                        } catch (Exception e) {
                            log.error("RecommendPhasePanelAssembly appendRecommendCfTrackPanelList error : {}", e.getMessage());
                        }
                    });
            }
        }

    }

    private Panel createRecommendCfTrackPanel(final PersonalPhaseMeta personalPhaseMeta,
                                              final RecommendTrackDto cfTrack){

        return new RcmmdTrackPanel(cfTrack, getDefaultBgImageList( cfTrack.getImgList() , personalPhaseMeta.getOsType()));
    }

}
