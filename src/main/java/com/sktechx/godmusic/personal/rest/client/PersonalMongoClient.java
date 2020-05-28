/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.client;

import java.util.Collections;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.common.domain.ListResponse;
import com.sktechx.godmusic.personal.rest.client.fallback.PersonalMongoClientFallbackFactory;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartTrackTasteMixDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.*;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.like.RcmmdLikeTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.like.*;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenDeleteTrackRequest;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendUpdateRequest;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPanel;
import com.sktechx.godmusic.personal.rest.model.vo.test.RecommendChartRequest;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 08. 30.
 */
@FeignClient(value = "personal-mgo-api", fallbackFactory = PersonalMongoClientFallbackFactory.class)
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
    @PutMapping("/personal-mgo/v1/like")
    CommonApiResponse<Void> sortLikes(@RequestHeader(name = "x-gm-fallback-cno") Long characterNo,
            LikeTypeIdListRequest request);

    /**
     * 트랙/앨범/아티스트 좋아요 추가
     */
    @PostMapping("/personal-mgo/v1/like")
    CommonApiResponse<Void> appendLike(@RequestHeader(name = "x-gm-fallback-cno") Long characterNo,
            @RequestBody LikeRequest request);

    /**
     * 트랙/앨범/아티스트 좋아요 삭제
     */
    @DeleteMapping("/personal-mgo/v1/like")
    CommonApiResponse<Void> deleteLikes(@RequestHeader(name = "x-gm-fallback-cno") Long characterNo,
            @RequestBody LikeTypeIdListRequest request);
    // 추천 관련 API

    /**
     * 추천 패널의 추천 트랙 조회
     */
    @GetMapping("/personal-mgo/v1/recommends/{rcmmdType}/{rcmmdId}/tracks")
    CommonApiResponse<ListDto<List<RecommendPanelTrackDto>>> getRecommendTrackListByRcmmdTypeAndRcmmdId(
            @PathVariable(name = "rcmmdType") String rcmmdType,
            @PathVariable(name = "rcmmdId") Long rcmmdId,
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo,
            @RequestParam(name = "size") int size);

    /**
     * 나를 위한 FLO 추천 상세 정보
     */
    @GetMapping("/personal-mgo/v1/recommends/RC_CF_TR/{rcmmdId}")
    CommonApiResponse<RecommendForMeDto> getRecommendForMeFlo(
            @PathVariable(name = "rcmmdId") Long rcmmdId,
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo);

    /**
     * 나를 위한 FLO 추천 목록 (트랙 정보 없음)
     */
    @GetMapping("/personal-mgo/v1/recommends/RC_CF_TR/?includeTrackYn=N")
    CommonApiResponse<ListDto<List<RecommendForMeDto>>> getRecommendForMeFloListByCharacterNo(
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo);

    /**
     * 나를 위한 FLO 추천 목록 (트랙 정보 있음)
     */
    @GetMapping("/personal-mgo/v1/recommends/RC_CF_TR/?includeTrackYn=Y")
    CommonApiResponse<ListDto<List<RecommendForMeDto>>> getRecommendForMeFloListWithTrackByCharacterNo(
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo);

    /**
     * 오늘의 FLO 추천 상세 정보
     */
    @GetMapping("/personal-mgo/v1/recommends/RC_SML_TR/{rcmmdId}")
    CommonApiResponse<RecommendSimilarTrackDto> getRecommendTodayFlo(
            @PathVariable(name = "/rcmmdId") Long rcmmdId,
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo);

    /**
     * 오늘의 FLO 추천 목록 조회
     */
    @GetMapping("/personal-mgo/v1/recommends/RC_SML_TR?includeTrackYn=N")
    CommonApiResponse<ListDto<List<RecommendSimilarTrackDto>>> getRecommendTodayFloListByCharacterNo(
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo);

    /**
     * 오늘의 FLO 추천 목록 트랙 정보 포함 조회
     */
    @GetMapping("/personal-mgo/v1/recommends/RC_SML_TR?includeTrackYn=Y")
    CommonApiResponse<ListDto<List<RecommendSimilarTrackDto>>> getRecommendTodayFloListWithTrackByCharacterNo(
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo);

    /**
     * 아티스트 FLO 추천 상세 정보
     */
    @GetMapping("/personal-mgo/v1/recommends/RC_ATST_TR/{rcmmdId}")
    CommonApiResponse<RecommendArtistDto> getRecommendArtistFlo(
            @PathVariable(name = "rcmmdId") Long rcmmdId,
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo);

    /**
     * 아티스트 FLO 추천 목록 (트랙 정보 없음)
     */
    @GetMapping("/personal-mgo/v1/recommends/RC_CF_TR/?includeTrackYn=N")
    CommonApiResponse<ListDto<List<RecommendArtistDto>>> getRecommendArtistFloListByCharacterNo(
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo);

    /**
     * 아티스트 FLO 추천 목록 (트랙 정보 있음)
     */
    @GetMapping("/personal-mgo/v1/recommends/RC_CF_TR/?includeTrackYn=Y")
    CommonApiResponse<ListDto<List<RecommendArtistDto>>> getRecommendArtistFloListWithTrackByCharacterNo(
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo);

    /**
     * 추천 데이터의 삭제 여부 플래그 변경
     */
    @PutMapping("/personal-mgo/internal/recommends/{rcmmdType}/{rcmmdId}")
    CommonApiResponse<Long> updateRecommendDelTargetYn(
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo,
            @PathVariable(name = "rcmmdType") String rcmmdType,
            @PathVariable(name = "rcmmdId") Long rcmmdId,
            @RequestBody RecommendUpdateRequest request);

    /**
     * 캐릭터 번호로 추천 데이터 전부 가져오기
     */
    @GetMapping("/personal-mgo/v1/recommends")
    CommonApiResponse<ListDto<List<PersonalPanel>>> getRcmmdPanelMetaByCharacterNo(
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo
    );

    /**
     * 개인화 차트 정렬 순서 조회
     */
    @GetMapping("/personal-mgo/v1/recommends/chart/{chartId}/tracks")
    CommonApiResponse<ChartTrackTasteMixDto> getRecommendChartTrackTasteMixDto(
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo,
            @PathVariable(name = "chartId") Long chartId);

    /**
     * 좋아요 트랙 (반응형 추천) 단 건 조회
     */
    @GetMapping("/personal-mgo/v1/recommends/RC_LKSM_TR/{rcmmdId}")
    CommonApiResponse<RcmmdLikeTrackDto> getRecommendLikeTrack(
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo,
            @PathVariable(name = "rcmmdId") Long rcmmdId);

    /**
     * 좋아요 트랙(반응형 추천) 목록 조회
     */
    @GetMapping("/personal-mgo/v1/recommends/RC_LKSM_TR?includeTrackYn=N")
    CommonApiResponse<ListDto<List<RcmmdLikeTrackDto>>> getRecommendLikeTracksList(
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo);

    // for Test dummy data
    @PutMapping("/personal-mgo/test/recommends/chart")
    CommonApiResponse<Void> addRecommendChart(
            @RequestHeader(name = "x-gm-fallback-cno") Long fallBackCharacterNo,
            @RequestBody RecommendChartRequest recommendChartRequest);

    @DeleteMapping("/personal-mgo/test/recommends/chart")
    CommonApiResponse<Void> deleteRecommendChart(
            @RequestHeader(name = "x-gm-fallback-cno") Long fallBackCharacterNo,
            @RequestBody RecommendChartRequest recommendChartRequest);

    /**
     * 좋아요 연계 반응형 패널 곡 목록 조회
     */
    @GetMapping("/personal-mgo/internal/recommends/{rcmmdId}")
    CommonApiResponse<AdaptivePanelTrackDto> getLikeRelatedRecommendTracks(
            @RequestHeader(name = "x-gm-fallback-cno") Long characterNo,
            @PathVariable(name = "rcmmdId") Long rcmmdId,
            @RequestParam(name = "rcmmdType") String rcmmdType);


    default List<Long> getLikeRelatedRecommendTrackIds(Long characterNo,
                                                       Long rcmmdId,
                                                       String rcmmdType) {

        CommonApiResponse<AdaptivePanelTrackDto> response = this.getLikeRelatedRecommendTracks(characterNo, rcmmdId, rcmmdType);
        if (response != null && response.getData() != null) {
            return response.getData().getTrackIds();
        }

        return Collections.emptyList();
    }

}
