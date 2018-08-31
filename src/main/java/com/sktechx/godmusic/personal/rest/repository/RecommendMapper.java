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

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.mybatis.annotation.BaseMapper;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.*;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPanel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 설명 :  추천 관련 Repository
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 19.
 */
@BaseMapper
public interface RecommendMapper {
    List<PersonalPanel> selectPersonalRecommendPanelMeta(@Param("characterNo") Long characterNo);

    // 2-A' 선호장르 유사곡 패널
    List<RecommendTrackDto> selectRecommendPreferGenreSimilarTrackListByIdList(@Param("recommendIdList") List<Long> recommendIdList ,
                                                                               @Param("recommendLimitSize") int recommendLimitSize,
                                                                               @Param("trackLimitSize") int trackLimitSize,
                                                                               @Param("osType") OsType osType);

    // 2-A 유사곡 패널
    List<RecommendTrackDto> selectRecommendSimilarTrackListByIdList(@Param("recommendIdList") List<Long> recommendIdList ,
                                                                    @Param("recommendLimitSize") int recommendLimitSize,
                                                                    @Param("trackLimitSize") int trackLimitSiz,
                                                                    @Param("osType") OsType osType);
    // 3-A 청취 CF 패널
    List<RecommendTrackDto> selectRecommendCfTrackListByIdList(@Param("recommendIdList") List<Long> recommendIdList ,
                                                               @Param("recommendLimitSize") int recommendLimitSize,
                                                               @Param("trackLimitSize") int trackLimitSiz,
                                                               @Param("osType") OsType osType);


    // 2-C 선호 아티스트 인기곡
    RecommendArtistDto selectRecommendArtistById(@Param("recommendArtistId") Long recommendArtistId);


    // 추천 패널 기본 이미지
    List<ImageInfo> selectRecommendPanelDefaultImageList();

	List<CharacterPreferArtistDto> selectCharacterPreferArtist(@Param("characterNo") Long characterNo);

    List<SimilarArtistDto> selectSimilarArtistByIdList(@Param("artistIdList") List<Long> artistIdList);

    List<SimilarArtistDto> selectSimilarArtistGroupByPerCount(@Param("artistIdList") List<Long> artistIdList, @Param("perCount") int perCount);

    List<RecommendArtistTrackListDto> selectSimilarArtistTrack(@Param("artistIdList") List<Long> artistIdList);

    void updateRcmmdArtistDispStdEndDt(@Param("characterNo") Long characterNo);

    void insertRcmmdArtist(@Param("recommendArtistDto") RecommendArtistDto recommendArtistDto);

    void insertRcmmdArtistList(@Param("rcmmdArtistId") Long rcmmdArtistId, @Param("artistId") Long artistId, @Param("artistType") String artistType, @Param("dispSn") int dispSn);

    void insertRcmmdArtistTrackList(@Param("rcmmdArtistId") Long rcmmdArtistId, @Param("trackId") Long trackId, @Param("dispSn") int dispSn);

	List<PreferGenreTrackDto> selectPreferGenreTrack(@Param("characterNo") Long characterNo);

    List<SimilarTrackDto> selectSimilarTrackListByIdList(@Param("trackIds") List<Long> trackIds);

    void insertRcmmdPreferGenreSimilarTrack(@Param("similarTrack") RecommendPreferGenreSimilarTrackDto recommendPreferGenreSimilarTrackDto);

    void updateRcmmdPreferGenreSimilarTrack(@Param("characterNo") Long characterNo);

    void insertRcmmdPreferGenreSimilarTrackList(@Param("rcmmdPreferGenreSimilarTrackId") Long rcmmdPreferGenreSimilarTrackId, @Param("trackId") Long trackId, @Param("dispSn") int dispSn);

    //추천 데이터 삭제 방지
    int updateRecommendDataRemovePrevent(@Param("recommendPanelContentType") RecommendPanelContentType recommendPanelContentType
            , @Param("rcmmdId") Long rcmmdId
            , @Param("characterNo") Long characterNo);

}
