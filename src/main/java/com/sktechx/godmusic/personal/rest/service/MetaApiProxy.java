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

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.rest.model.dto.*;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendPanelTrackDto;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 8. 1.
 */
public interface MetaApiProxy {

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

    default CommonApiResponse<?> validChannelOrEmpty(Long channelId, String channelType) {
        CommonApiResponse<ChannelValidityDto> response = this.validChannel(channelId, channelType);
        return response.getData().getValid() ? response : new CommonApiResponse<>(null);
    }

}
