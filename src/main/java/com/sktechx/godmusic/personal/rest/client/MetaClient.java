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
import org.springframework.web.bind.annotation.*;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.rest.client.fallback.MetaClientFallbackFactory;
import com.sktechx.godmusic.personal.rest.client.model.MetaVideoRequestVo;
import com.sktechx.godmusic.personal.rest.model.dto.*;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendPanelTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.video.VideoVo;

/**
 * 설명 : 메타 연동
 *
 * @author 김관효(Bob) kwanhyo.kim@sk.com
 * @date 2019.10.07
 */

@FeignClient(value = "meta-api", fallbackFactory = MetaClientFallbackFactory.class)
public interface MetaClient {

    @GetMapping("/meta/v1/artist/{artistId}")
    CommonApiResponse<ArtistDto> artists(@PathVariable("artistId") Long artistId);

    @GetMapping("/meta/v1/album/{albumId}")
    CommonApiResponse<AlbumDto> album(@PathVariable("albumId") Long albumId);

    @GetMapping("/meta/v1/channel/{channelId}")
    CommonApiResponse<PlayListDto> channel(@PathVariable("channelId") Long channelId);

    @GetMapping("/meta/v1/channel/{channelId}/valid")
    CommonApiResponse<ChannelValidityDto> validChannel(@PathVariable("channelId") Long channelId,
            @RequestParam(value = "type") String channelType);

    @GetMapping("/meta/v1/track/{trackId}")
    CommonApiResponse<TrackDto> track(@PathVariable("trackId") Long trackId);

    @GetMapping("/meta/v1/chart/track/{chartId}")
    CommonApiResponse<PlayListDto> chart(@PathVariable("chartId") Long chartId);

    @GetMapping("/meta/v1/track/list")
    CommonApiResponse<ListDto<List<RecommendPanelTrackDto>>> recommendPanelTracks(
            @RequestParam(value = "trackIdList") Long[] trackIdList);

    @PostMapping("/meta/internal/videos")
    CommonApiResponse<ListDto<List<VideoVo>>> getVideos(@RequestBody MetaVideoRequestVo metaVideoRequestVo);

    default CommonApiResponse<?> validChannelOrEmpty(Long channelId, String channelType) {
        CommonApiResponse<ChannelValidityDto> response = this.validChannel(channelId, channelType);
        return response.getData().getValid() ? response : new CommonApiResponse<>(null);
    }

    @GetMapping("/meta/internal/videos/{videoId}")
    CommonApiResponse<VideoVo> getVideo(@PathVariable("videoId") Long videoId);


}
