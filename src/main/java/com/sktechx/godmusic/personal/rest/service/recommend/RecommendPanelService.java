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

import java.util.List;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendPanelTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendPanelResponse;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;

/**
 * 설명 : XXXXXXXX
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
public interface RecommendPanelService {

	List<Panel> createRecommendPanelList(Long characterNo, OsType osType, String appVer);

	RecommendPanelResponse createRecommendV2PanelList(Long characterNo, OsType osType, String appVer);

	ListDto<List<RecommendPanelTrackDto>> getRecommendPanelTrackList(
			Long characterNo, String recommendPanelType, Long panelContentId
	);

	void addPreferArtistPanel(Long characterNo);

	void addPreferGenrePanel(Long characterNo);

	List<Panel> getRecommendPanelList(Long characterNo, String recommendPanelType, OsType osType);

}
