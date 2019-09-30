/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.vo.recommend;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import lombok.Builder;
import lombok.Data;

/**
 * 설명 : 추천 패널 API 응답
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecommendPanelListResponse {

    List<Panel> forMePanelList;
    List<Panel> todayFloPanelList;
    List<Panel> artistFloPanelList;
}
