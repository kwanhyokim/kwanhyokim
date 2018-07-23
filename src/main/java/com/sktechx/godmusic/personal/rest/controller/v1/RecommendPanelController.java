/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.controller.v1;

import com.netflix.discovery.converters.Auto;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendPanelResponse;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendPanelService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 설명 : 추천 컨트롤러
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
@RestController
@RequestMapping(Naming.serviceCode+"/v1/recommends")
public class RecommendPanelController {
    @Autowired
    private RecommendPanelService recommendPanelService;

    @ApiOperation(value = "추천 홈 패널 조회", httpMethod = "GET", notes = "추천 패널 조회 MockUp API" , response = RecommendPanelResponse.class)
    @GetMapping(value = "/home/panels")
    public RecommendPanelResponse recommendHomePanels(){
        RecommendPanelResponse mockResponse = new RecommendPanelResponse();
        mockResponse.setPanelList(recommendPanelService.createRecommendPanelList());
        return mockResponse;
    }
}
