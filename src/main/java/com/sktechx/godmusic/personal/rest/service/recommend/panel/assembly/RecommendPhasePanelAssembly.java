/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.recommend.panel.assembly;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.RcmmdTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelSignAssembly;
import lombok.extern.slf4j.Slf4j;

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
    public List<Panel> makeHomePanelListForMainTop(final PersonalPhaseMeta personalPhaseMeta){


        final List<Panel> panelList = new ArrayList();

        appendRecommendCfTrackPanelList(personalPhaseMeta, panelList, RCMMD_CF_PANEL_DEFAULT_SIZE);

        boolean isFillRecommendPanel = false;
        if(RCMMD_CF_PANEL_DEFAULT_SIZE > panelList.size()){
            isFillRecommendPanel = true;
        }

        appendPreferGenreSimilarTrackPanelList(personalPhaseMeta, panelList, PREFER_GENRE_SIMILAR_TRACK_PANEL_APPEND_SIZE);

        int panelDefaultSize = RCMMD_CF_PANEL_DEFAULT_SIZE+SIMILAR_TRACK_PANEL_APPEND_SIZE;

        if( panelDefaultSize > panelList.size()){


            appendSimilarTrackPanelList(personalPhaseMeta , panelList ,panelDefaultSize  - panelList.size() );

            if(panelDefaultSize > panelList.size()){


                appendPreferGenreChannelPanelList(personalPhaseMeta, panelList, panelDefaultSize - panelList.size() );
                List filterChnlIdList = panelList.stream()
                        .filter(panel -> (
                                RecommendPanelType.LISTEN_MOOD_POPULAR_CHANNEL.equals(panel.getType())
                                        || RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL.equals(panel.getType())
                        ) && panel.getContent() != null)
                        .map(panel-> panel.getContent().getId())
                        .collect(Collectors.toList());

                if(panelDefaultSize > panelList.size()){
                    appendDefaultPopularChannelPanel(personalPhaseMeta , panelList ,panelDefaultSize - panelList.size() , filterChnlIdList);
                }
            }
        }else{
            if(isFillRecommendPanel){

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
                                .map(panel-> panel.getContent().getId())
                                .collect(Collectors.toList());

                        if(panelDefaultSize >= panelList.size()){
                            appendDefaultPopularChannelPanel(personalPhaseMeta , panelList ,panelAppendCount , filterChnlIdList);
                        }
                    }
                }

            }
        }

        return panelList;
    }
    @Override
    public List<Panel> makeHomePanelListForMainMiddle(Long characterNo, OsType osType) {
        return null;
    }

    private void appendRecommendCfTrackPanelList(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList, int panelLimitSize) {
        List<Long> rcmmdIdList = personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RC_CF_TR);

        if(!CollectionUtils.isEmpty(rcmmdIdList)){
            List<RecommendTrackDto> recommendCfTrackList =
                    recommendReadMapper.selectRecommendCfTrackListByIdList(rcmmdIdList, panelLimitSize, RCMMD_CF_TRACK_LIMIT_SIZE, personalPhaseMeta.getOsType());

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
