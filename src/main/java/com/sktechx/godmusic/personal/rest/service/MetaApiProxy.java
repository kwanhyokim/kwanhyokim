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

package com.sktechx.godmusic.personal.rest.service;

import java.util.List;

import com.sktechx.godmusic.personal.rest.model.dto.AlbumDto;
import com.sktechx.godmusic.personal.rest.model.dto.PlayListDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendPanelTrackDto;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 8. 1.
 */
@FeignClient("meta-api")
public interface MetaApiProxy {

    @GetMapping("/meta/v1/artist/{artistId}")
    public CommonApiResponse<ArtistDto> artists(@PathVariable("artistId") Long artistId);

    @GetMapping("/meta/v1/album/{albumId}")
    public CommonApiResponse<AlbumDto> album(@PathVariable("albumId") Long albumId);

    @GetMapping("/meta/v1/channel/{channelId}")
    public CommonApiResponse<PlayListDto> channel(@PathVariable("channelId") Long channelId);

    @GetMapping("/meta/v1/track/{trackId}")
    public CommonApiResponse<TrackDto> track(@PathVariable("trackId") Long trackId);

    @GetMapping("/meta/v1/chart/track/{chartId}")
    public CommonApiResponse<PlayListDto> chart(@PathVariable("chartId") Long chartId);


    @GetMapping("/meta/v1/track/list")
    public CommonApiResponse<ListDto<List<RecommendPanelTrackDto>>> recommendPanelTracks(@RequestParam(value="trackIdList") Long[] trackIdList);


}
