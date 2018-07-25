/*
 *
 *  * Copyright (c) 2018 SK TECHX.
 *  * All right reserved.
 *  *
 *  * This software is the confidential and proprietary information of SK TECHX.
 *  * You shall not disclose such Confidential Information and
 *  * shall use it only in accordance with the terms of the license agreement
 *  * you entered into with SK TECHX.
 *
 */

package com.sktechx.godmusic.personal.rest.controller.v1;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.RequestGMContext;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.model.vo.preference.Chart;
import com.sktechx.godmusic.personal.rest.model.vo.preference.PreferGenreResponse;
import com.sktechx.godmusic.personal.rest.service.PreferenceService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 설명 : 선호 장르 컨트롤러
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 7. 19.
 */
@Slf4j
@RestController
@RequestMapping(Naming.serviceCode+"/v1/preferences")
public class PreferenceController {

    @Autowired
    private PreferenceService preferenceService;

    @ApiOperation(value = "장르 추천", httpMethod = "GET", notes = "선호 장르 추천 API", response = PreferGenreResponse.class)
    @GetMapping(value = "/geners")
    public CommonApiResponse<PreferGenreResponse> preferencesGeners(@ApiIgnore @RequestGMContext GMContext ctx) {
        log.info("GMContext : {}", GMContext.getContext());
        return new CommonApiResponse<>(preferenceService.getPreferenceGenreList(ctx.getCharacterNo()));
    }

    @ApiOperation(value = "장르 추천 상세", httpMethod = "GET", notes = "선호 장르 추천별 플레이 리스트 API", response = Chart.class)
    @GetMapping(value = "/geners/{chartId}")
    public CommonApiResponse<Chart> preferencesGeners(@ApiParam(name = "chartId", required = true, value = "차트아이디", defaultValue = "3325") @PathVariable("chartId") Long chartId) {
        log.info("GMContext : {}", GMContext.getContext());
        return new CommonApiResponse<>(preferenceService.getPreferenceGenre(chartId));
    }


//    @ApiOperation(value = "아티스트 추천", httpMethod = "GET", notes = "선호 아티스트 추천 API", response null)
//    @GetMapping(value = "/artits")
//    public void preferencesArtists() {
//
//    }
}
