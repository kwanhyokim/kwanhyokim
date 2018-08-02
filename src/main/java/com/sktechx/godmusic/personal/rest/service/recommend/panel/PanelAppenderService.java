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

import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;

import java.util.List;

/**
 * 설명 : 패널 추가
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 08. 02.
 */
public interface PanelAppenderService {
    // 3-A
    void appendRecommendCfTrackPanelList(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList , int limitSize);

    // 2-A'
    void appendPreferGenreSimilarTrackPanelList(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList , int limitSize);

    // 2-B
    void appendListenMoodPopularChanelPanelList(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList , int limitSize);

    // 2-C
    void appendPreferArtistPopularTrackPanel(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList);

    //TOP100, KIDS
    void appendPreferenceChartPanel(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList);

    // 1-A'
    void appendPreferGenreChannelPanelList(PersonalPhaseMeta personalPhaseMeta, final List<Panel> panelList , int limitSize);

    // 2-A
    void appendSimilarTrackPanelList(PersonalPhaseMeta personalPhaseMeta, final List<Panel> panelList , int limitSize);

    // 인기채널

    void appendDefaultPopularChannelPanel(PersonalPhaseMeta personalPhaseMeta ,final List<Panel> panelList , int limitSize);
}
