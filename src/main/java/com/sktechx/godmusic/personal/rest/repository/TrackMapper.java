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

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import com.sktechx.godmusic.lib.mybatis.annotation.BaseMapper;
import com.sktechx.godmusic.personal.rest.model.dto.MostListenedTrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;

/**
 * 설명 : 트랙
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 23.
 */
@BaseMapper
public interface TrackMapper {
    //TODO : 추후 meta api 호출
    List<TrackDto> selectTrackList(@Param("trackIdList") List<Long> trackIdList);

    // added by bob 2018.08.01
    List<Long> selectRecommendPanelPopularTrackList(@Param("characterNo") Long characterNo, @Param("rcmmdArtistId")Long rcmmdArtistId);
    List<Long> selectRecommendPanelSimilarTrackList(@Param("characterNo") Long characterNo, @Param("rcmmdTrackId")Long rcmmdTrackId);
    List<Long> selectRecommendPanelGenreTrackList(@Param("characterNo") Long characterNo, @Param("rcmmdGenreId")Long rcmmdGenreId);
    List<Long> selectRecommendPanelCfTrackList(@Param("characterNo") Long characterNo, @Param("rcmmdMforuId")Long rcmmdMforuId);

    List<MostListenedTrackDto> selectMostListenedTrackList(@Param("characterNo") Long characterNo, @Param("pageable") Pageable pageable);
    long selectMostListenedTrackTotalCount(@Param("characterNo") Long characterNo);

    List<MostListenedTrackDto> selectMyRecentTrackList(@Param("memberNo") Long memberNo, @Param("characterNo") Long characterNo, @Param("pageable") Pageable pageable);
    long selectMyRecentTrackTotalCount(@Param("memberNo") Long memberNo, @Param("characterNo") Long characterNo);

    void deleteMyRecentTrackList(@Param("memberNo") Long memberNo, @Param("characterNo") Long characterNo, @Param("trackId") Long trackId);
}
