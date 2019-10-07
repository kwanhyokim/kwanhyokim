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
import com.sktechx.godmusic.personal.rest.model.vo.like.*;
import com.sktechx.godmusic.personal.rest.service.LikeService;
import com.sktechx.godmusic.personal.rest.service.TrackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 08. 30.
 */
@Slf4j
@Service("likeMongoService")
public class LikeMongoServiceImpl implements LikeService {

    @Autowired
    PersonalMongoClient personalMongoClient;

    @Autowired
    @Qualifier("likeService")
    LikeService likeService;

    @Autowired
    MongoRedisService mongoRedisService;

    @Override
    public LikePlaylistListResponse getPlayListLikeListByLikeType(Long characterNo, String appVersion, Pageable pageable) {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public LikeAlbumListResponse getAlbumLikeListByLikeType(Long characterNo, Pageable pageable) {
        return mongoRedisService.executeService(
                () -> {
                    CommonApiResponse<LikeAlbumListResponse> result = personalMongoClient.getLikeAlbums(characterNo, pageable.getPageNumber(), pageable.getPageSize());
                    LikeAlbumListResponse likeAlbumListResponse = Optional.ofNullable(result.getData()).orElse(null);
                    if (likeAlbumListResponse == null) {
                        throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);
                    };
                    return new LikeAlbumListResponse(new PageImpl<>(likeAlbumListResponse.getList(), pageable, likeAlbumListResponse.getTotalCount()));
                },
                () -> likeService.getAlbumLikeListByLikeType(characterNo, pageable)
        );
    }

    @Override
    public LikeArtistListResponse getArtistLikeListByLikeType(Long characterNo, Pageable pageable) {
        return mongoRedisService.executeService(
                () -> {
                    CommonApiResponse<LikeArtistListResponse> result = personalMongoClient.getLikeArtists(characterNo, pageable.getPageNumber(), pageable.getPageSize());
                    LikeArtistListResponse likeArtistListResponse = Optional.ofNullable(result.getData()).orElse(null);
                    if (likeArtistListResponse == null) {
                        throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);
                    };
                    return new LikeArtistListResponse(new PageImpl<>(likeArtistListResponse.getList(), pageable, likeArtistListResponse.getTotalCount()));
                },
                () -> likeService.getArtistLikeListByLikeType(characterNo, pageable)
        );
    }

    @Override
    public LikeTrackListResponse getTrackLikeListByLikeType(Long characterNo, Pageable pageable) {
        return mongoRedisService.executeService(
                () -> {
                    CommonApiResponse<LikeTrackListResponse> result = personalMongoClient.getLikeTracks(characterNo, pageable.getPageNumber(), pageable.getPageSize());
                    LikeTrackListResponse likeTrackListResponse = Optional.ofNullable(result.getData()).orElse(null);
                    if (likeTrackListResponse == null) {
                        throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);
                    };
                    return new LikeTrackListResponse(new PageImpl<>(likeTrackListResponse.getList(), pageable, likeTrackListResponse.getTotalCount()));
                },
                () -> likeService.getTrackLikeListByLikeType(characterNo, pageable)
        );
    }

    @Override
    public void addLike(LikeRequest request, Long characterNo) {
        mongoRedisService.executeService(
            () -> {
                if (request.typeIsTrackArtistAlbum()) {
                    personalMongoClient.appendLike(characterNo, request);
                }
                else {
                    log.info("This {} likeType is not supported to the personal-mgo-api", request.getLikeType());
                }
            }
        );
    }

    @Override
    public void deleteLike(LikeTypeIdListRequest request, Long characterNo) {
        mongoRedisService.executeService(
            () -> {
                if (LikeRequest.LikeType.contains(request.getLikeType())) {
                    personalMongoClient.deleteLikes(characterNo, request);
                } else {
                    log.info("This {} likeType is not supported to the personal-mgo-api", request.getLikeType());
                }
            }
        );
    }

    @Override
    public void updateLike(LikeTypeIdListRequest request, Long characterNo) {
        mongoRedisService.executeService(
            () -> {
                if (LikeRequest.LikeType.contains(request.getLikeType())) {
                    personalMongoClient.sortLikes(characterNo, request);
                } else {
                    log.info("This {} likeType is not supported to the personal-mgo-api", request.getLikeType());
                }
            }
        );
    }

    @Override
    public LikeYnResponse getLikeYn(String likeType, Long likeTypeId, Long characterNo) {
        return mongoRedisService.executeService(
                () -> {
                    CommonApiResponse<LikeYnResponse> result = personalMongoClient.existLike(characterNo, likeType, likeTypeId);
                    return result.getData();
                },
                () -> likeService.getLikeYn(likeType, likeTypeId, characterNo)
        );
    }
}
