/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.controller;

import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendPanelResponse;
import com.sktechx.godmusic.personal.rest.service.MockRecommendPanelService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 설명 : 추천 패널 컨트롤러
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
@RestController
@RequestMapping("/v1/recommend")
public class RecommendPanelController {


    @Autowired
    private MockRecommendPanelService mockRecommendPanelService;

    @ApiOperation(value = "추천 패널 조회", httpMethod = "GET", notes = "추천 패널 조회 MockUp API")
    @RequestMapping(value = "/panelList", method = RequestMethod.GET)
    public RecommendPanelResponse recommendPanels(){
        RecommendPanelResponse mockResponse = new RecommendPanelResponse();
        mockResponse.setPanelList(mockRecommendPanelService.createMockUpRecommendPanelList());
        return mockResponse;
    }
}
