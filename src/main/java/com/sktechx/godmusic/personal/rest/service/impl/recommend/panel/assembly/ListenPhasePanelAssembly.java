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

import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelSignAssembly;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 설명 : 청취 단계 ( 2단계 ) 패널 생성기
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 14.
 */
@Slf4j
@Service("listenPhasePanelAssembly")
public class ListenPhasePanelAssembly extends PanelSignAssembly {
    final int similarTrackPanelSize = 2;
    final int preferGenreSimilarTrackPanelSize = 2;

    final int listenMoodPopularChannelPanelSize = 1;

    @Override
    protected List<Panel> defaultPanelSetting(PersonalPhaseMeta personalPhaseMeta) {
        final List<Panel> panelList = new ArrayList<>();

        panelAppender.appendPreferGenreSimilarTrackPanelList(personalPhaseMeta, panelList , preferGenreSimilarTrackPanelSize);
        if(preferGenreSimilarTrackPanelSize > panelList.size()){
            panelAppender.appendSimilarTrackPanelList(personalPhaseMeta , panelList ,preferGenreSimilarTrackPanelSize - panelList.size() );
            if(similarTrackPanelSize > panelList.size()){
                panelAppender.appendPreferGenreChannelPanelList(personalPhaseMeta, panelList, similarTrackPanelSize - panelList.size() );
            }
        }
        panelAppender.appendListenMoodPopularChanelPanelList(personalPhaseMeta, panelList,listenMoodPopularChannelPanelSize);

        if(( similarTrackPanelSize+listenMoodPopularChannelPanelSize )  > panelList.size()){
            panelAppender.appendDefaultPopularChannelPanel(personalPhaseMeta, panelList,( similarTrackPanelSize+listenMoodPopularChannelPanelSize ) - panelList.size() );
        }
        return panelList;
    }
    @Override
    protected void appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta ,final List<Panel> panelList){
        panelAppender.appendPreferArtistPopularTrackPanel(personalPhaseMeta,panelList);
        panelAppender.appendPreferenceChartPanel(personalPhaseMeta,panelList);

        sort(personalPhaseMeta.getFirstPhaseType() , panelList);
    }

}

