/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.client.fallback;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.common.domain.ListResponse;
import com.sktechx.godmusic.personal.rest.client.PersonalMongoClient;
import com.sktechx.godmusic.personal.rest.model.dto.MostListenedTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.like.*;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenDeleteTrackRequest;
import com.sktechx.godmusic.personal.rest.service.LikeService;
import com.sktechx.godmusic.personal.rest.service.TrackService;
import feign.hystrix.FallbackFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 08. 30.
 */
@Component
public class PersonalMongoClientFallbackFactory implements FallbackFactory<PersonalMongoClient> {

    @Autowired
    @Qualifier("trackService")
    TrackService trackService;

    @Autowired
    @Qualifier("likeService")
    LikeService likeService;

    @Override
    public PersonalMongoClient create(Throwable throwable) {
        return new PersonalMongoClient() {

            @Override
            public CommonApiResponse<ListResponse> getMostListenedTracks(Long characterNo, int page, int size) {
                Page<?> result = trackService.mostTrackList(characterNo, PageRequest.of(page, size));
                return CommonApiResponse.of(ListResponse.of(result));
            }

            @Override
            public CommonApiResponse<ListResponse> getRecentListenedTracks(Long memberNo, Long characterNo, int page, int size) {
                Page<?> result = trackService.getMyRecentTrackList(memberNo, characterNo, PageRequest.of(page, size));
                return CommonApiResponse.of(ListResponse.of(result));
            }

            /**
             * MongoDB 와 MySQL 모두에서 삭제해야 하기 때문에 fallback 시에 MySQL에만 삭제하면 안됨
             */
            @Override
            public CommonApiResponse<Void> deleteRecentListenTrack(Long memberNo, Long characterNo, ListenDeleteTrackRequest request) {
                return CommonApiResponse.of(null);
            }

            @Override
            public CommonApiResponse<LikeAlbumListResponse> getLikeAlbums(Long characterNo, int page, int size) {
                LikeAlbumListResponse result = likeService.getAlbumLikeListByLikeType(characterNo, PageRequest.of(page, size));
                return CommonApiResponse.of(result);
            }

            @Override
            public CommonApiResponse<LikeArtistListResponse> getLikeArtists(Long characterNo, int page, int size) {
                LikeArtistListResponse result = likeService.getArtistLikeListByLikeType(characterNo, PageRequest.of(page, size));
                return CommonApiResponse.of(result);
            }

            @Override
            public CommonApiResponse<LikeTrackListResponse> getLikeTracks(Long characterNo, int page, int size) {
                LikeTrackListResponse result = likeService.getTrackLikeListByLikeType(characterNo, PageRequest.of(page, size));
                return CommonApiResponse.of(result);
            }

            @Override
            public CommonApiResponse<LikeYnResponse> existLike(Long characterNo, String likeType, Long likeTypeId) {
                LikeYnResponse result = likeService.getLikeYn(likeType, likeTypeId, characterNo);
                return CommonApiResponse.of(result);
            }

            /**
             * MongoDB 와 MySQL 모두에서 정렬해야 하기 때문에 fallback 시에 MySQL에만 정렬하면 안됨
             */
            @Override
            public CommonApiResponse<Void> sortLikes(Long characterNo, LikeTypeIdListRequest request) {
                return CommonApiResponse.of(null);
            }

            /**
             * MongoDB 와 MySQL 모두에 저장해야 하기 때문에 fallback 시에 MySQL에만 저장하면 안됨
             */
            @Override
            public CommonApiResponse<Void> appendLike(Long characterNo, LikeRequest request) {
                return CommonApiResponse.of(null);
            }

            /**
             * MongoDB 와 MySQL 모두에서 삭제해야 하기 때문에 fallback 시에 MySQL에만 삭제하면 안됨
             */
            @Override
            public CommonApiResponse<Void> deleteLikes(Long characterNo, LikeTypeIdListRequest request) {
                return CommonApiResponse.of(null);
            }
        };
    }

}
