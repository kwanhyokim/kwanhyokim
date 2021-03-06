/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.client.fallback;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.common.domain.ListResponse;
import com.sktechx.godmusic.personal.rest.client.PersonalMongoClient;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartTrackTasteMixDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.*;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.like.RcmmdLikeTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.like.*;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenDeleteTrackRequest;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendUpdateRequest;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPanel;
import com.sktechx.godmusic.personal.rest.model.vo.test.RecommendChartRequest;
import com.sktechx.godmusic.personal.rest.service.LikeService;
import com.sktechx.godmusic.personal.rest.service.TrackService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 08. 30.
 */
@SuppressWarnings("unchecked")
@Slf4j
@Service
public class PersonalMongoClientFallbackFactory implements FallbackFactory<PersonalMongoClient> {

    @Autowired
    @Qualifier("trackService")
    TrackService trackService;

    @Autowired
    @Qualifier("likeService")
    LikeService likeService;

    @Override
    public PersonalMongoClient create(Throwable throwable) {

        final CommonApiResponse emptyApiResponse = new CommonApiResponse<>(null);
        final CommonApiResponse emptyListApiResponse = new CommonApiResponse<>(new ListDto<>(null));

        return new PersonalMongoClient() {

            @Override
            public CommonApiResponse<ListResponse> getMostListenedTracks(Long characterNo, int page, int size) {
                log.warn("{}@getMostListenedTracks-fallback, message={}", characterNo, throwable.getMessage());
                Page<?> result = trackService.mostTrackList(characterNo, PageRequest.of(page, size));
                return new CommonApiResponse<>(ListResponse.of(result));
            }

            @Override
            public CommonApiResponse<ListResponse> getRecentListenedTracks(Long memberNo, Long characterNo, int page, int size) {
                log.warn("{}@getRecentListenedTracks-fallback, message={}", characterNo, throwable.getMessage());
                Page<?> result = trackService.getMyRecentTrackList(memberNo, characterNo, PageRequest.of(page, size));
                return new CommonApiResponse<>(ListResponse.of(result));
            }

            /**
             * MongoDB 와 MySQL 모두에서 삭제해야 하기 때문에 fallback 시에 MySQL에만 삭제하면 안됨
             */
            @Override
            public CommonApiResponse<Void> deleteRecentListenTrack(Long memberNo, Long characterNo, ListenDeleteTrackRequest request) {
                log.warn("{}@deleteRecentListenTrack-fallback, message={}", characterNo, throwable.getMessage());
                return emptyApiResponse;
            }

            @Override
            public CommonApiResponse<LikeAlbumListResponse> getLikeAlbums(Long characterNo, int page, int size) {
                log.warn("{}@getLikeAlbums-fallback, message={}", characterNo, throwable.getMessage());
                LikeAlbumListResponse result = likeService.getAlbumLikeListByLikeType(characterNo, PageRequest.of(page, size));
                return new CommonApiResponse<>(result);
            }

            @Override
            public CommonApiResponse<LikeArtistListResponse> getLikeArtists(Long characterNo, int page, int size) {
                log.warn("{}@getLikeArtists-fallback, message={}", characterNo, throwable.getMessage());
                LikeArtistListResponse result = likeService.getArtistLikeListByLikeType(characterNo, PageRequest.of(page, size));
                return new CommonApiResponse<>(result);
            }

            @Override
            public CommonApiResponse<LikeTrackListResponse> getLikeTracks(Long characterNo, int page, int size) {
                log.warn("{}@getLikeTracks-fallback, message={}", characterNo, throwable.getMessage());
                LikeTrackListResponse result = likeService.getTrackLikeListByLikeType(characterNo, PageRequest.of(page, size));
                return new CommonApiResponse<>(result);
            }

            @Override
            public CommonApiResponse<LikeYnResponse> existLike(Long characterNo, String likeType, Long likeTypeId) {
                log.warn("{}@existLike-fallback, message={}", characterNo, throwable.getMessage());
                LikeYnResponse result = likeService.getLikeYn(likeType, likeTypeId, characterNo);
                return new CommonApiResponse<>(result);
            }

            /**
             * MongoDB 와 MySQL 모두에서 정렬해야 하기 때문에 fallback 시에 MySQL에만 정렬하면 안됨
             */
            @Override
            public CommonApiResponse<Void> sortLikes(Long characterNo, LikeTypeIdListRequest request) {
                log.warn("{}@sortLikes-fallback, message={}", characterNo, throwable.getMessage());
                return emptyApiResponse;
            }

            /**
             * MongoDB 와 MySQL 모두에 저장해야 하기 때문에 fallback 시에 MySQL에만 저장하면 안됨
             */
            @Override
            public CommonApiResponse<Void> appendLike(Long characterNo, LikeRequest request) {
                log.warn("{}@appendLike-fallback, message={}", characterNo, throwable.getMessage());
                return emptyApiResponse;
            }

            /**
             * MongoDB 와 MySQL 모두에서 삭제해야 하기 때문에 fallback 시에 MySQL에만 삭제하면 안됨
             */
            @Override
            public CommonApiResponse<Void> deleteLikes(Long characterNo, LikeTypeIdListRequest request) {
                log.warn("{}@deleteLikes-fallback, message={}", characterNo, throwable.getMessage());
                return emptyApiResponse;
            }

            @Override
            public CommonApiResponse<ListDto<List<RecommendPanelTrackDto>>> getRecommendTrackListByRcmmdTypeAndRcmmdId(
                    String rcmmdType, Long rcmmdId, Long characterNo, int size) {
                log.warn("{}@getRecommendTrackListByRcmmdTypeAndRcmmdId-fallback, message={}",
                        characterNo, throwable.getMessage());
                return emptyListApiResponse;
            }

            @Override
            public CommonApiResponse<RecommendArtistDto> getRecommendArtistFlo(Long rcmmdId,
                    Long characterNo) {
                log.warn("{}@getRecommendArtistFlo-fallback, message={}",
                        characterNo, throwable.getMessage());
                return emptyApiResponse;
            }
            @Override
            public CommonApiResponse<ListDto<List<RecommendArtistDto>>> getRecommendArtistFloListByCharacterNo(
                    Long characterNo) {
                log.warn("{}@getRecommendArtistFloListByCharacterNo-fallback, message={}",
                        characterNo, throwable.getMessage());
                return emptyListApiResponse;
            }
            @Override
            public CommonApiResponse<ListDto<List<RecommendArtistDto>>> getRecommendArtistFloListWithTrackByCharacterNo(
                    Long characterNo) {
                log.warn("{}@getRecommendArtistFloListWithTrackByCharacterNo-fallback, message={}",
                        characterNo, throwable.getMessage());
                return emptyListApiResponse;
            }

            @Override
            public CommonApiResponse<RecommendForMeDto> getRecommendForMeFlo(Long rcmmdId,
                    Long characterNo) {
                log.warn("{}@getRecommendFormeFlo-fallback, message={}",
                        characterNo, throwable.getMessage());
                return emptyApiResponse;
            }
            @Override
            public CommonApiResponse<ListDto<List<RecommendForMeDto>>> getRecommendForMeFloListByCharacterNo(
                    Long characterNo) {
                log.warn("{}@getRecommendFormeFloListByCharacterNo-fallback, message={}",
                        characterNo, throwable.getMessage());
                return emptyListApiResponse;
            }
            @Override
            public CommonApiResponse<ListDto<List<RecommendForMeDto>>> getRecommendForMeFloListWithTrackByCharacterNo(
                    Long characterNo) {
                log.warn("{}@getRecommendFormeFloListWithTrackByCharacterNo-fallback, message={}",
                        characterNo, throwable.getMessage());
                return emptyListApiResponse;
            }

            @Override
            public CommonApiResponse<RecommendSimilarTrackDto> getRecommendTodayFlo(Long rcmmdId,
                    Long characterNo) {
                log.warn("{}@getRecommendTodayFlo-fallback, message={}",
                        characterNo, throwable.getMessage());
                return emptyApiResponse;
            }

            @Override
            public CommonApiResponse<ListDto<List<RecommendSimilarTrackDto>>> getRecommendTodayFloListByCharacterNo(
                    Long characterNo) {
                log.warn("{}@getRecommendTodayFloListByCharacterNo-fallback, message={}",
                        characterNo, throwable.getMessage());
                return emptyListApiResponse;
            }
            @Override
            public CommonApiResponse<ListDto<List<RecommendSimilarTrackDto>>> getRecommendTodayFloListWithTrackByCharacterNo(
                    Long characterNo) {
                log.warn("{}@getRecommendTodayFloListWithTrackByCharacterNo-fallback, message={}",
                        characterNo, throwable.getMessage());
                return emptyListApiResponse;
            }

            @Override
            public CommonApiResponse<Void> updateRecommendDelTargetYn(Long characterNo,
                    String rcmmdType, Long rcmmdId, RecommendUpdateRequest request) {

                log.warn("{}@updateRecommendDelTargetYn-fallback, message={}, params={},{}",
                        characterNo, throwable.getMessage(),
                        rcmmdType, rcmmdId
                );
                return emptyApiResponse;
            }
            @Override
            public CommonApiResponse<ListDto<List<PersonalPanel>>> getRcmmdPanelMetaByCharacterNo(
                    Long characterNo) {
                log.warn("{}@getRcmmdPanelMetaByCharacterNo-fallback, message={}",
                        characterNo, throwable.getMessage()
                );
                return emptyListApiResponse;
            }

            @Override
            public CommonApiResponse<Void> addRecommendChart(
                    Long characterNo,
                    RecommendChartRequest recommendChartRequest) {
                log.warn("{}@addRecommentChart-fallback, message={}",
                        characterNo, throwable.getMessage()
                );
                return emptyApiResponse;
            }
            @Override
            public CommonApiResponse<Void> deleteRecommendChart(Long characterNo,
                    @RequestBody RecommendChartRequest recommendChartRequest) {
                log.warn("{}@deleteRecommentChart-fallback, message={}",
                        characterNo, throwable.getMessage()
                );
                return emptyApiResponse;
            }
            @Override
            public CommonApiResponse<ChartTrackTasteMixDto> getRecommendChartTrackTasteMixDto(
                    Long characterNo, Long chartId) {
                log.warn("{}@getRecommendChartTrackTasteMixDto-fallback, message={}",
                        characterNo, throwable.getMessage()
                );
                return emptyApiResponse;
            }
            @Override
            public CommonApiResponse<RcmmdLikeTrackDto> getRecommendLikeTrack(Long characterNo,
                    Long rcmmdId) {

                log.warn("{}{}@getRecommendLikeTrack-fallback, message={}",
                        characterNo, rcmmdId, throwable.getMessage()
                );

                return emptyApiResponse;
            }
            @Override
            public CommonApiResponse<ListDto<List<RcmmdLikeTrackDto>>> getRecommendLikeTracksList(
                    Long characterNo) {

                log.warn("{}@getRecommendLikeTracksList-fallback, message={}",
                        characterNo, throwable.getMessage()
                );

                return emptyListApiResponse;
            }

            @Override
            public CommonApiResponse<AdaptivePanelTrackDto> getLikeRelatedRecommendTracks(Long characterNo,
                    Long rcmmdId,
                    String rcmmdType) {

                log.warn("{}@getLikeRelatedRecommendTracks-fallback, message={}",
                        characterNo, throwable.getMessage());

                return emptyApiResponse;
            }
        };
    }
}
