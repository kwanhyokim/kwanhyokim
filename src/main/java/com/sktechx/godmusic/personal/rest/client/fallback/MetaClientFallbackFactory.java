/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.client.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.client.model.GetTrackListRequest;
import com.sktechx.godmusic.personal.rest.client.model.MetaVideoRequestVo;
import com.sktechx.godmusic.personal.rest.model.dto.*;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartTrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendPanelTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.video.VideoVo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * 설명 : 메타 클라이언트 폴백
 *
 * @author 김관효(Bob) / kwanhyo.kim@sk.com
 * @date 2019.10.07
 */

@Component
@Slf4j
public class MetaClientFallbackFactory implements FallbackFactory<MetaClient>{

    @Override
    public MetaClient create(Throwable throwable) {
        return new MetaClient() {
            @Override
            public CommonApiResponse<ArtistDto> artists(Long artistId) {
                return null;
            }
            @Override
            public CommonApiResponse<AlbumDto> album(Long albumId) {
                return null;
            }
            @Override
            public CommonApiResponse<PlayListDto> channel(Long channelId) {
                return null;
            }
            @Override
            public CommonApiResponse<ChannelValidityDto> validChannel(Long channelId,
                    String channelType) {
                return null;
            }
            @Override
            public CommonApiResponse<TrackDto> track(Long trackId) {
                return null;
            }
            @Override
            public CommonApiResponse<PlayListDto> chart(Long chartId) {
                log.warn("[차트조회] 조회실패 - {}", throwable.getMessage());
                return null;
            }
            @Override
            public CommonApiResponse<ListDto<List<RecommendPanelTrackDto>>> recommendPanelTracks(
                    Long[] trackIdList) {
                return null;
            }
            @Override
            public CommonApiResponse<ListDto<List<VideoVo>>> getVideos(
                    MetaVideoRequestVo metaVideoRequestVo) {

                return CommonApiResponse.<ListDto<List<VideoVo>>>builder()
                        .data(new ListDto<>(null)).build();
            }

            @Override
            public CommonApiResponse<VideoVo> getVideo(Long videoId) {
                log.warn("Failed to retrieve videoInfo from metaClient@MetaClientFallback - {}", throwable.getMessage());
                return null;
            }

            @Override
            public CommonApiResponse<Void> ping() {
                log.warn("[WARM-UP] ... 메타 Ping 호출 실패. message={}", throwable.getMessage());
                return null;
            }
            @Override
            public CommonApiResponse<ChartTrackDto> getChartWithTrackList(Long chartId,
                    Integer trackSize) {
                log.warn("{}@getRecommendChart-fallback, message={}",
                        chartId, throwable.getMessage()
                );
                return new CommonApiResponse<>(null);
            }
            @Override
            public CommonApiResponse<ListDto<List<TrackDto>>> getTrackList(
                    GetTrackListRequest request) {
                return CommonApiResponse.<ListDto<List<TrackDto>>>builder()
                        .data(new ListDto<>(null)).build();
            }
        };
    }
}

