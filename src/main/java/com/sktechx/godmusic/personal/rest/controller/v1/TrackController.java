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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.RequestGMContext;
import com.sktechx.godmusic.personal.common.domain.ListResponse;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.service.TrackService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequestMapping(Naming.serviceCode+"/v1/tracks")
public class TrackController {

    @Autowired
    private TrackService trackService;

    @ApiOperation(value = "많이 들은  ( 기존 /v2/my/track/most/list GET )")
    @GetMapping("/mostlistened")
    public CommonApiResponse<ListResponse> mostTrackList(@PageableDefault(size=50, page=0) Pageable pageable) {

        return new CommonApiResponse<>(new ListResponse(trackService.mostTrackList(GMContext.getContext().getCharacterNo(), pageable)));
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page.", defaultValue = "5")
    })
    @ApiOperation(value = "최근 들은 by Peter ( 기존 /v2/my/track/recent/list GET )")
    @GetMapping("/recentlistened")
    public CommonApiResponse<ListResponse> recentListenedTrackList(
            @ApiIgnore @RequestGMContext GMContext ctx,
            @ApiIgnore @PageableDefault(size=50, page=0) Pageable pageable) {
        return new CommonApiResponse<>(new ListResponse(trackService.getMyRecentTrackList(ctx.getMemberNo(), ctx.getCharacterNo(), pageable)));
    }
}
