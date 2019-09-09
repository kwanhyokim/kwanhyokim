/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.service.mongo;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.personal.common.domain.ListResponse;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenDeleteTrackRequest;
import com.sktechx.godmusic.personal.rest.service.TrackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 08. 30.
 */
@Slf4j
@Service("trackMongoService")
public class TrackMongoServiceImpl implements TrackService {

    @Autowired
    PersonalMongoClient personalMongoClient;

    @Autowired
    @Qualifier("trackService")
    TrackService trackService;

    @Autowired
    MongoRedisService mongoRedisService;

    @Override
    public PageImpl<?> mostTrackList(Long characterNo, Pageable pageable) {
        return mongoRedisService.executeService(
                () -> {
                    CommonApiResponse<ListResponse> result = personalMongoClient.getMostListenedTracks(characterNo, pageable.getPageNumber(), pageable.getPageSize());
                    ListResponse data = Optional.ofNullable(result.getData()).orElse(null);
                    if (data == null) {
                        return trackService.mostTrackList(characterNo, pageable);
                    }
                    return new PageImpl<>(result.getData().getList(), pageable, result.getData().getTotalCount());
                },
                () -> trackService.mostTrackList(characterNo, pageable)
        );
    }

    @Override
    public PageImpl<?> getMyRecentTrackList(Long memberNo, Long characterNo, Pageable pageable) {
        return mongoRedisService.executeService(
                () -> {
                    CommonApiResponse<ListResponse> result = personalMongoClient.getRecentListenedTracks(memberNo, characterNo, pageable.getPageNumber(), pageable.getPageSize());
                    ListResponse data = Optional.ofNullable(result.getData()).orElse(null);
                    if (data == null) {
                        return trackService.getMyRecentTrackList(memberNo, characterNo, pageable);
                    }
                    return new PageImpl<>(result.getData().getList(), pageable, result.getData().getTotalCount());
                },
                () -> trackService.getMyRecentTrackList(memberNo, characterNo, pageable)
        );

    }

    @Override
    public void deleteMyRecentTrackList(Long memberNo, Long characterNo, List<Long> trackIds) {
        mongoRedisService.executeService(
                () -> personalMongoClient.deleteRecentListenTrack(memberNo, characterNo, ListenDeleteTrackRequest.builder().trackIds(trackIds).build())
        );
    }
}
