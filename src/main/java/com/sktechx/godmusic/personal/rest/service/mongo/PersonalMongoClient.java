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
import com.sktechx.godmusic.personal.common.domain.ListResponse;
import com.sktechx.godmusic.personal.rest.model.vo.like.*;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenDeleteTrackRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 08. 30.
 */
@FeignClient(value = "personal-mgo-api", fallback = PersonalMongoClientFallback.class)
public interface PersonalMongoClient {

    /**
     * 많이들은 트랙 목록
     */
    @GetMapping("/personal-mgo/v1/tracks/mostlistened")
    CommonApiResponse<ListResponse> getMostListenedTracks(
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo,
            @RequestParam(name = "page") int page, @RequestParam(name = "size") int size);

    /**
     * 최근들은 트랙 목록
     */
    @GetMapping("/personal-mgo/v1/tracks/recentlistened")
    CommonApiResponse<ListResponse> getRecentListenedTracks(
            @RequestHeader(name = "x-gm-fallback-mno") Long memberNo,
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo,
            @RequestParam(name = "page") int page, @RequestParam(name = "size") int size);

    /**
     * 최근들은 트랙 삭제
     */
    @DeleteMapping("/personal-mgo/v1/tracks/recentlistened")
    CommonApiResponse<Void> deleteRecentListenTrack(
            @RequestHeader(name = "x-gm-fallback-mno") Long memberNo,
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo,
            ListenDeleteTrackRequest request);

    /**
     * 앨범 좋아요 목록
     */
    @GetMapping("/personal-mgo/v1/like/type/album/list")
    CommonApiResponse<LikeAlbumListResponse> getLikeAlbums(
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo,
            @RequestParam(name = "page") int page, @RequestParam(name = "size") int size);

    /**
     * 아티스트 좋아요 목록
     */
    @GetMapping("/personal-mgo/v1/like/type/artist/list")
    CommonApiResponse<LikeArtistListResponse> getLikeArtists(
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo,
            @RequestParam(name = "page") int page, @RequestParam(name = "size") int size);

    /**
     * 트랙 좋아요 목록
     */
    @GetMapping("/personal-mgo/v1/like/type/track/list")
    CommonApiResponse<LikeTrackListResponse> getLikeTracks(
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo,
            @RequestParam(name = "page") int page, @RequestParam(name = "size") int size);

    /**
     * 트랙/앨범/아티스트 좋아요 여부 확인
     */
    @GetMapping("/personal-mgo/v1/like/type/{likeType}/ids/{likeTypeId}")
    CommonApiResponse<LikeYnResponse> existLike(
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo,
            @PathVariable("likeType") String likeType, @PathVariable("likeTypeId") Long likeTypeId);

    /**
     * 트랙/앨범/아티스트 좋아요 순서 변경
     */
    @PutMapping("personal-mgo/v1/like")
    CommonApiResponse<Void> sortLikes(
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo,
            LikeTypeIdListRequest request);

    /**
     * 트랙/앨범/아티스트 좋아요 추가
     */
    @PostMapping("/personal-mgo/v1/like")
    CommonApiResponse<Void> appendLike(
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo,
            LikeRequest request);

    /**
     * 트랙/앨범/아티스트 좋아요 삭제
     */
    @DeleteMapping("/personal-mgo/v1/like")
    CommonApiResponse<Void> deleteLikes(
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo,
            LikeTypeIdListRequest request);
}
