/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.recommend;

import com.sktechx.godmusic.personal.common.domain.type.OsType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.ImageDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.MyMostTrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendArtistDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;

import java.util.List;

/**
 * 설명 : XXXXXXXX
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
public interface RecommendPanelService {
    List<Panel> createMockupRecommendPanelList();
    List<Panel> createRecommendPanelList(Long characterNo, OsType osType);

    List<ImageDto> getPanelBackgroundImageList(RecommendPanelType recommendPanelType,OsType osType);

    // added by bob 2018.08.01
    // edited by bob 2018.08.02
    ListDto<List<MyMostTrackDto>>getRecommendPanelPopularTrackList(Long characterNo, Long rcmmdArtistId);
    ListDto<List<MyMostTrackDto>>getRecommendPanelSimilarTrackList(Long characterNo, Long rcmmdTrackId);
    ListDto<List<MyMostTrackDto>> getRecommendPanelGenreTrackList(Long characterNo, Long rcmmdGenreId);
    ListDto<List<MyMostTrackDto>>getRecommendPanelCfTrackList(Long characterNo, Long rcmmdMforuId);

	ListDto<List<MyMostTrackDto>> getRecommendPanelTrackList(Long characterNo, RecommendPanelContentType recommendPanelType, Long panelContentId);

}
