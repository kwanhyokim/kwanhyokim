/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.rest.client.fallback.MetaMgoClientFallbackFactory;
import com.sktechx.godmusic.personal.rest.client.model.GetTrackListRequest;
import com.sktechx.godmusic.personal.rest.client.model.MetaVideoRequestVo;
import com.sktechx.godmusic.personal.rest.model.dto.AlbumDto;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.vo.video.VideoVo;

/**
 * 설명 : meta-mgo 연동
 *
 */

@FeignClient(value = "meta-mgo-api", fallbackFactory = MetaMgoClientFallbackFactory.class)
public interface MetaMgoClient {

    @GetMapping("/meta/v1/artist/{artistId}")
    CommonApiResponse<ArtistDto> artists(@PathVariable("artistId") Long artistId);

    @GetMapping("/meta/v1/album/{albumId}")
    CommonApiResponse<AlbumDto> album(@PathVariable("albumId") Long albumId);

    @GetMapping("/meta/v1/track/{trackId}")
    CommonApiResponse<TrackDto> track(@PathVariable("trackId") Long trackId);

    @PostMapping("/meta/internal/videos")
    CommonApiResponse<ListDto<List<VideoVo>>> getVideos(@RequestBody MetaVideoRequestVo metaVideoRequestVo);

    @GetMapping("/meta/internal/videos/{videoId}")
    CommonApiResponse<VideoVo> getVideo(@PathVariable("videoId") Long videoId);

    @GetMapping("/meta/v1/ping")
    CommonApiResponse<Void> ping();

    @PostMapping("/meta/v2/track/list")
    CommonApiResponse<ListDto<List<TrackDto>>> getTrackList(@RequestBody GetTrackListRequest request);

}
