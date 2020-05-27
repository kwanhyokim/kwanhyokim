/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.service.mongo;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.rest.client.PersonalMongoClient;
import com.sktechx.godmusic.personal.rest.model.vo.like.*;
import com.sktechx.godmusic.personal.rest.model.vo.video.RangeResponse;
import com.sktechx.godmusic.personal.rest.model.vo.video.VideoVo;
import com.sktechx.godmusic.personal.rest.service.LikeService;
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
                        return likeService.getAlbumLikeListByLikeType(characterNo, pageable);
                        //throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);
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
                        return likeService.getArtistLikeListByLikeType(characterNo, pageable);
                        //throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);
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
                        return likeService.getTrackLikeListByLikeType(characterNo, pageable);
                        //throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);
                    };
                    return new LikeTrackListResponse(new PageImpl<>(likeTrackListResponse.getList(), pageable, likeTrackListResponse.getTotalCount()));
                },
                () -> likeService.getTrackLikeListByLikeType(characterNo, pageable)
        );
    }

    /**
     * 2020.03.19
     * Notice. 몽고 2차 도입으로 인해 좋아요 추가 API는 Personal-Mgo 로 이관 및 직접 서비스 함
     *         데이타 싱크를 위해서 Personal-Mgo -> Personal 로 Feign 통신 하도록 변경 됨
     *         따라서 해당 호출 로직 주석 처리 함. 항후 이슈 없을 경우 메소드 삭제 처리
     */
    @Override
    public void addLike(LikeRequest request, Long characterNo) {

        log.warn("[좋아요][추가] Personal -> Personal Mgo 호출 발생. characterNo = {}", characterNo);

//        mongoRedisService.executeService(
//            () -> {
//                if (request.typeIsTrackArtistAlbum()) {
//                    personalMongoClient.appendLike(characterNo, request);
//                }
//                else {
//                    log.info("This {} likeType is not supported to the personal-mgo-api", request.getLikeType());
//                }
//            }
//        );
    }

    /**
     * 2020.03.19
     * Notice. 몽고 2차 도입으로 인해 좋아요 삭제 API는 Personal-Mgo 로 이관 및 직접 서비스 함
     *         데이타 싱크를 위해서 Personal-Mgo -> Personal 로 Feign 통신 하도록 변경 됨
     *         따라서 해당 호출 로직 주석 처리 함. 항후 이슈 없을 경우 메소드 삭제 처리
     */
    @Override
    public void deleteLike(LikeTypeIdListRequest request, Long characterNo) {

        log.warn("[좋아요][삭제] Personal -> Personal Mgo 호출 발생. characterNo = {}", characterNo);

//        mongoRedisService.executeService(
//            () -> {
//                if (LikeRequest.LikeType.contains(request.getLikeType())) {
//                    personalMongoClient.deleteLikes(characterNo, request);
//                } else {
//                    log.info("This {} likeType is not supported to the personal-mgo-api", request.getLikeType());
//                }
//            }
//        );
    }

    /**
     * 2020.03.19
     * Notice. 몽고 2차 도입으로 인해 좋아요 순서 변경 API는 Personal-Mgo 로 이관 및 직접 서비스 함
     *         데이타 싱크를 위해서 Personal-Mgo -> Personal 로 Feign 통신 하도록 변경 됨
     *         따라서 해당 호출 로직 주석 처리 함. 항후 이슈 없을 경우 메소드 삭제 처리
     */
    @Override
    public void updateLike(LikeTypeIdListRequest request, Long characterNo) {

        log.warn("[좋아요][노출순서변경] Personal -> Personal Mgo 호출 발생. characterNo = {}", characterNo);

//        mongoRedisService.executeService(
//            () -> {
//                if (LikeRequest.LikeType.contains(request.getLikeType())) {
//                    personalMongoClient.sortLikes(characterNo, request);
//                } else {
//                    log.info("This {} likeType is not supported to the personal-mgo-api", request.getLikeType());
//                }
//            }
//        );
    }

    @Override
    public LikeYnResponse getLikeYn(String likeType, Long likeTypeId, Long characterNo) {
        return mongoRedisService.executeService(
                () -> {
                    if (LikeRequest.LikeType.contains(likeType)) {
                        CommonApiResponse<LikeYnResponse> result = personalMongoClient.existLike(characterNo, likeType, likeTypeId);
                        return result.getData();
                    }
                    else {
                        return likeService.getLikeYn(likeType, likeTypeId, characterNo);
                    }
                },
                () -> likeService.getLikeYn(likeType, likeTypeId, characterNo)
        );
    }

    @Override
    public RangeResponse<VideoVo> getLikeVideos(Long characterNo, Pageable pageable) {
        // TODO 몽고DB 서빙 구현
        return null;
    }
}
