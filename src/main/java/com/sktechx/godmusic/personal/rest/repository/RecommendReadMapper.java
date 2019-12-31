/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.repository;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.mybatis.annotation.ReadOnlyMapper;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.*;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPanel;

/**
 * 설명 :  추천 관련 Read only Repository
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 19.
 */
@ReadOnlyMapper
public interface RecommendReadMapper {
    List<PersonalPanel> selectPersonalRecommendPanelMeta(@Param("characterNo") Long characterNo,
                                                         @Param("similarTrackDispStandardCount") int
                                                                 similarTrackDispStandardCount,
                                                         @Param("rcmmdCfTrackDispStandardCount") int
                                                                 rcmmdCfTrackDispStandardCount,
                                                         @Param("checkDispEndDt") Boolean checkDispEndDt

    );

    // 2-A' 선호장르 유사곡 패널
    List<RecommendTrackDto> selectRecommendPreferGenreSimilarTrackListByIdList(@Param("recommendIdList") List<Long>
                                                                                       recommendIdList,
                                                                               @Param("recommendLimitSize") int
                                                                                       recommendLimitSize,
                                                                               @Param("trackLimitSize") int
                                                                                       trackLimitSize,
                                                                               @Param("osType") OsType osType);

    // 2-A 유사곡 패널
    List<RecommendTrackDto> selectRecommendSimilarTrackListByIdList(@Param("recommendIdList") List<Long>
                                                                            recommendIdList,
                                                                    @Param("recommendLimitSize") int recommendLimitSize,
                                                                    @Param("trackLimitSize") int trackLimitSiz,
                                                                    @Param("osType") OsType osType);
    // 3-A 청취 CF 패널
    List<RecommendTrackDto> selectRecommendCfTrackListByIdList(@Param("recommendIdList") List<Long> recommendIdList,
                                                               @Param("recommendLimitSize") int recommendLimitSize,
                                                               @Param("trackLimitSize") int trackLimitSiz,
                                                               @Param("osType") OsType osType);


    // 2-C 선호 아티스트 인기곡
    RecommendArtistDto selectRecommendArtistById(@Param("recommendArtistId") Long recommendArtistId);


    // 추천 패널 기본 이미지
    List<ImageInfo> selectRecommendPanelDefaultImageList();

	List<CharacterPreferArtistDto> selectCharacterPreferArtist(@Param("characterNo") Long characterNo, @Param("genreId") Long genreId);

	List<CharacterPreferArtistGenreDto> selectCharacterPreferArtistGenre(@Param("characterNo") Long characterNo);

    List<SimilarArtistDto> selectSimilarArtistByIdList(@Param("artistIdList") List<Long> artistIdList);

    List<RecommendArtistTrackListDto> selectSimilarArtistTrack(@Param("artistIdList") List<Long> artistIdList);

	List<PreferGenreTrackDto> selectPreferGenreTrack(@Param("characterNo") Long characterNo);

    List<SimilarTrackDto> selectSimilarTrackListByIdList(@Param("trackIds") List<Long> trackIds);


    String selectRecommendPanelInfoBgImageUrl(@Param("recommendPanelContentType") RecommendPanelContentType recommendPanelContentType
            , @Param("rcmmdId") Long rcmmdId
            , @Param("osType") OsType osType
            , @Param("dispSn") int dispSn);

    RecommendGenreVo selectRecommendGenreByRcmmdId(@Param("rcmmdId") Long rcmmdId);

    RecommendDuplicateCountDto selectSimilarTrackPanelBetweenDuplicateCount(@Param("characterNo") Long characterNo);
    RecommendDuplicateCountDto selectPreferGenreSimilarTrackPanelBetweenDuplicateCount(@Param("characterNo") Long characterNo);


    // 좋아할만한 아티스트 MIX에서 최신 전시일 가져오기
    Date selectRecommendArtistMostRecentDispDateByCharacterNo(@Param("characterNo") Long characterNo);

    // 2-C 선호 아티스트 인기곡 리스트 by characterNo
    List<RecommendArtistDto> selectRecommendArtistByCharacterNo(
            @Param("characterNo") Long characterNo,
            @Param("recentDispStartDt") String recentDispStartDt);

    // 3-A 청취 CF 패널 조회 by characterNo
    List<RecommendTrackDto> selectRecommendCfTrackListByCharacterNo(
            @Param("characterNo") Long characterNo,
            @Param("recommendLimitSize") int recommendLimitSize,
            @Param("trackLimitSize") int trackLimitSize,
            @Param("osType") OsType osType
    );

    // 2-A 유사곡 패널 조회 by characterNo
    List<RecommendTrackDto> selectRecommendSimilarTrackListByCharacterNo(
            @Param("characterNo") Long characterNo,
            @Param("recommendLimitSize") int recommendLimitSize,
            @Param("trackLimitSize") int trackLimitSize,
            @Param("osType") OsType osType);


    RecommendSimilarTrackDto selectRecommendSimilarTrack(@Param("rcmmdId") Long rcmmdId);

    List<ImageInfo> selectTpoAndThemeImageList(@Param("osType") OsType osType);

    List<RecommendTrackDto> selectRecommendCfTrackIdListByCharacterNo(
            @Param("characterNo") Long characterNo,
            @Param("recommendLimitSize") int recommendLimitSize,
            @Param("trackLimitSize") int trackLimitSize,
            @Param("osType") OsType osType
    );

}
