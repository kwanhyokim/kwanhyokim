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
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPanel;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
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



    List<RecommendTrackDto> selectRecommendCfTrackListByIdList(List<Long> recommendIdList, int limitSize);


}
