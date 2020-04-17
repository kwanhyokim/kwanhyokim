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
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelSignAssembly;
import lombok.extern.slf4j.Slf4j;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.*;
/**
 * 설명 : 청취 단계 ( 2단계 ) 패널 생성기
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 14.
 */
@Slf4j
@Service("listenPhasePanelAssembly")
public class ListenPhasePanelAssembly extends PanelSignAssembly {

    private ListenPhasePanelAssembly(){}

    @Override
    protected List<Panel> defaultPanelSetting(PersonalPhaseMeta personalPhaseMeta) {
        final List<Panel> panelList = new ArrayList<>();

        appendPreferGenreSimilarTrackPanelList(personalPhaseMeta, panelList , PREFER_GENRE_SIMILAR_PANEL_SIZE  );

        if( isAppendSimilarTrackPanel(panelList.size()) ){
            appendSimilarTrackPanelList(personalPhaseMeta , panelList ,PREFER_GENRE_SIMILAR_PANEL_SIZE - panelList.size() );
        }
        appendListenMoodPopularChanelPanelList(personalPhaseMeta, panelList,LISTEN_MOOD_POPULAR_PANEL_SIZE);

        if( isAppendPreferGenreChannelPanel(panelList.size()) ){
            appendPreferGenreChannelPanelList(personalPhaseMeta, panelList, ( SIMILAR_TRACK_PANEL_SIZE+LISTEN_MOOD_POPULAR_PANEL_SIZE )  - panelList.size() );

            if( isAppendDefaultPanel(panelList.size()) ){
                List filterChnlIdList = panelList.stream()
                        .filter(panel -> (
                                    RecommendPanelType.LISTEN_MOOD_POPULAR_CHANNEL.equals(panel.getType())
                                            || RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL.equals(panel.getType())
                        ) && panel.getContent() != null)
                        .map(panel-> {
                            return panel.getContent().getId();
                        })
                        .collect(Collectors.toList());

                appendDefaultPopularChannelPanel(personalPhaseMeta, panelList,( SIMILAR_TRACK_PANEL_SIZE+LISTEN_MOOD_POPULAR_PANEL_SIZE ) - panelList.size() ,filterChnlIdList );
            }

        }


        return panelList;
    }

    @Override
    public List<Panel> getRecommendPanelList(Long characterNo, OsType osType) {
        return null;
    }
    @Override
    protected void appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta ,final List<Panel> panelList){
        appendPreferArtistPopularTrackPanel(personalPhaseMeta,panelList);
        appendPreferenceChartPanel(personalPhaseMeta,panelList);

        sort(personalPhaseMeta , panelList);

    }
    private boolean isAppendSimilarTrackPanel(int panelListSize){
        return PREFER_GENRE_SIMILAR_PANEL_SIZE > panelListSize;
    }

    private boolean isAppendPreferGenreChannelPanel(int panelListSize){
        return (SIMILAR_TRACK_PANEL_SIZE + LISTEN_MOOD_POPULAR_PANEL_SIZE) > panelListSize;
    }

    private boolean isAppendDefaultPanel(int panelListSize){
        return (SIMILAR_TRACK_PANEL_SIZE + LISTEN_MOOD_POPULAR_PANEL_SIZE) > panelListSize;
    }

}

