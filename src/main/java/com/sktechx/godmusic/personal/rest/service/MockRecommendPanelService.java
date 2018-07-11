/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service;

import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;

import java.util.List;

/**
 * 설명 : XXXXXXXX
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
public interface MockRecommendPanelService {
    List<Panel> createMockUpRecommendPanelList();
}
