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

import com.sktechx.godmusic.lib.mybatis.annotation.BaseMapper;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendTrackDto;

/**
 * 설명 :  추천 더미 데이터 관련 Repository
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 19.
 */
@BaseMapper
public interface RecommendDummyDataMapper {

    Long selectRcmmdMforuRcmmdId(@Param("characterNo") Long characterNo);
    Long selectRcmmdSimilarTrackRcmmdId(@Param("characterNo") Long characterNo);
    Long selectRcmmdListenMoodChnlRcmmdId(@Param("characterNo") Long characterNo);

    void deleteRcmmdMforuData(@Param("characterNo") Long characterNo);
    void deleteRcmmdMforuSubData(@Param("characterNo") Long characterNo);

    void deleteRcmmdSimilarTrackData(@Param("characterNo") Long characterNo);
    void deleteRcmmdSimilarTrackSubData(@Param("characterNo") Long characterNo);

    void deleteRcmmdListenMoodChnlTrackData(@Param("characterNo") Long characterNo, @Param("rcmmdId") Long rcmmdId);

    void insertRcmmdMforuData(@Param("recommendTrackDto") RecommendTrackDto recommendTrackDto);
    void insertRcmmdMforuSubData(@Param("rcmmdId") Long rcmmdId);


    void insertRcmmdSimilarTrackData(@Param("recommendTrackDto") RecommendTrackDto recommendTrackDto);
    void insertRcmmdSimilarTrackSubData(@Param("rcmmdId") Long rcmmdId);

    List<Long> selectRandomSvcGenreId(@Param("size") int size);

    int insertTpoRecommendData(@Param("characterNo") Long characterNo);

    int deleteTpoRecommendData(@Param("characterNo") Long characterNo);

    int selectTpoRecommendDataCount(@Param("characterNo") Long characterNo);

    void deleteArtistFlo(@Param("characterNo") Long characterNo);
}
