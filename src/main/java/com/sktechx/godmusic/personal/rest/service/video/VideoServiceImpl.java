/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.service.video;

import com.google.common.collect.Lists;
import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.client.model.MetaVideoRequestVo;
import com.sktechx.godmusic.personal.rest.model.dto.MostListenedTrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.MostWatchedVideoDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.vo.video.MostWatchedVideoVo;
import com.sktechx.godmusic.personal.rest.model.vo.video.RangeResponse;
import com.sktechx.godmusic.personal.rest.model.vo.video.VideoVo;
import com.sktechx.godmusic.personal.rest.repository.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 10. 13.
 */
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    VideoMapper videoMapper;

    @Autowired
    MetaClient metaClient;

    /**
     * 캐릭터의 최근 본 영상 목록 조회
     */
    @Override
    public RangeResponse<MostWatchedVideoVo> getRecentWatchedVideos(Long characterNo, Pageable pageable) {
        requireNonNull(characterNo);
        requireNonNull(pageable);

        List<MostWatchedVideoDto> watchedVideos = videoMapper.selectRecentVideoList(characterNo, pageable);
        if (CollectionUtils.isEmpty(watchedVideos)) {
            throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);
        }

        Map<Long, MostWatchedVideoDto> watchedVideoIndex = watchedVideos.stream()
                .collect(toMap(MostWatchedVideoDto::getVideoId, Function.identity()));

        Map<Long, VideoVo> videoIndex = findVideos(watchedVideoIndex.keySet().stream().collect(toList()))
                .stream()
                .collect(toMap(VideoVo::getVideoId, Function.identity()));

        long totalCount = videoMapper.selectRecentVideoTotalCount(characterNo);

        List<MostWatchedVideoVo> result = Lists.newArrayList();
        for (Long videoId : watchedVideoIndex.keySet()) {
            if (videoIndex.containsKey(videoId)) {
                result.add(MostWatchedVideoVo.with(watchedVideoIndex.get(videoId), videoIndex.get(videoId)));
            }
        }

        return RangeResponse.of(new PageImpl<>(result, pageable, totalCount));
    }

    private List<VideoVo> findVideos(List<Long> videoIds) {
        return Optional.ofNullable(metaClient.getVideos(MetaVideoRequestVo.of(videoIds))
                .getData().getList())
                .orElse(Collections.emptyList());
    }

    /**
     * 캐릭터의 최근 본 영상 목록 삭제
     */
    @Override
    @Transactional
    public void deleteRecentWatchedVideos(Long characterNo, List<Long> videoIds) {
        videoMapper.deleteRecentVideoList(characterNo, videoIds);
    }
}
