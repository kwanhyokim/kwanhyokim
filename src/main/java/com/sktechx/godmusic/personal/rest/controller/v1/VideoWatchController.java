/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.controller.v1;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.RequestGMContext;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.model.vo.video.MostWatchedVideoVo;
import com.sktechx.godmusic.personal.rest.model.vo.video.RangeResponse;
import com.sktechx.godmusic.personal.rest.model.vo.video.WatchedVideoDeleteRequest;
import com.sktechx.godmusic.personal.rest.service.video.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 10. 01.
 */
@Slf4j
@Api(value = "보관함의 최근 본 영상 기능 제공 API", description = "보관함의 최근 본 영상 기능 제공 API")
@RestController
@RequestMapping(Naming.serviceCode+"/v1/videos")
public class VideoWatchController {

    @Autowired
    VideoService videoService;

    /**
     * 최근 본 영상 목록 조회
     */
    @ApiOperation(value = "최근 본 영상 목록 조회", httpMethod = "GET", notes = "보관함 > 최근 본 영상")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", required = true, dataType = "int", paramType = "query", value = "페이지", defaultValue = "1"),
            @ApiImplicitParam(name = "size", required = true, dataType = "int", paramType = "query", value = "사이즈", defaultValue = "50")
    })
    @GetMapping(value = "/recentwatched")
    public CommonApiResponse<RangeResponse<MostWatchedVideoVo>> getRecentWatchedVideos(
            @PageableDefault(size = 50) Pageable pageable,
            @ApiIgnore @RequestGMContext GMContext context) {

        Long characterNo = context.getCharacterNo();
        RangeResponse<MostWatchedVideoVo> response = videoService.getRecentWatchedVideos(characterNo, pageable);
        return new CommonApiResponse<>(response);
    }

    /**
     * 최근 본 영상 목록 삭제
     */
    @ApiOperation(value = "최근 본 영상 목록 삭제", httpMethod = "DELETE", notes = "보관함 > 최근 본 영상")
    @DeleteMapping(value = "/recentwatched")
    public CommonApiResponse<Void> removeRecentWatchedVideos(
            @ApiIgnore @RequestGMContext GMContext context,
            @RequestBody @Valid WatchedVideoDeleteRequest request) {

        Long characterNo = context.getCharacterNo();
        videoService.deleteRecentWatchedVideos(characterNo, request.getVideoIds());
        return CommonApiResponse.emptySuccess();
    }
}
