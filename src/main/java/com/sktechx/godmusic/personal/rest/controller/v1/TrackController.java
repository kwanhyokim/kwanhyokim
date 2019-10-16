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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.RequestGMContext;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.personal.common.domain.ListResponse;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenDeleteTrackRequest;
import com.sktechx.godmusic.personal.rest.service.TrackService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(Naming.serviceCode+"/v1/tracks")
public class TrackController {

    @Autowired
    @Qualifier("trackService")
    private TrackService trackService;

    @Autowired
    @Qualifier("trackMongoService")
    private TrackService trackMongoService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page.", defaultValue = "5")
    })
    @ApiOperation(value = "많이 들은  ( 기존 /v2/my/track/most/list GET )")
    @GetMapping("/mostlistened")
    public CommonApiResponse<ListResponse> mostTrackList(
            @ApiIgnore @PageableDefault(size=50, page=0) Pageable pageable) {

        Page<?> result = trackMongoService.mostTrackList(GMContext.getContext().getCharacterNo(), pageable);
        return new CommonApiResponse<>(new ListResponse(result));
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

        Page<?> result = trackMongoService.getMyRecentTrackList(ctx.getMemberNo(), ctx.getCharacterNo(), pageable);
        return new CommonApiResponse<>(new ListResponse(result));
    }

    @ApiOperation(value = "최근 들은 삭제 ")
    @DeleteMapping("/recentlistened")
    public CommonApiResponse<ListResponse> deleteMyrecentListenedTrackList(
            @ApiIgnore @RequestGMContext GMContext ctx,
            @Valid @RequestBody ListenDeleteTrackRequest listenDeleteTrackRequest) {

        if(listenDeleteTrackRequest == null){
            throw new CommonBusinessException(CommonErrorDomain.BAD_REQUEST);
        }

        // MongoDB 에서 삭제
        trackMongoService.deleteMyRecentTrackList(ctx.getMemberNo(), ctx.getCharacterNo(), listenDeleteTrackRequest.getTrackIds());

        // MySQLDB 에서 삭제
        trackService.deleteMyRecentTrackList(ctx.getMemberNo(), ctx.getCharacterNo(), listenDeleteTrackRequest.getTrackIds());

        return CommonApiResponse.emptySuccess();
    }

}
