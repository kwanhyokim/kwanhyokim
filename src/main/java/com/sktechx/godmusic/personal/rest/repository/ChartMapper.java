/*
 *
 *  * Copyright (c) 2018 SK TECHX.
 *  * All right reserved.
 *  *
 *  * This software is the confidential and proprietary information of SK TECHX.
 *  * You shall not disclose such Confidential Information and
 *  * shall use it only in accordance with the terms of the license agreement
 *  * you entered into with SK TECHX.
 *
 */

package com.sktechx.godmusic.personal.rest.repository;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.mybatis.annotation.BaseMapper;
import com.sktechx.godmusic.personal.common.domain.type.ChartType;
import com.sktechx.godmusic.personal.common.domain.type.SvcContentType;
import com.sktechx.godmusic.personal.rest.model.dto.ChartDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 7. 19.
 */
@BaseMapper
public interface ChartMapper {
    //TODO : 추후 meta api 호출
    ChartDto selectLiveChart();
    ChartDto selectKidsChart();
    //TODO : 서비스 장르 인기채널
//    List<PreferGenrePopularChnlDto> selectPreferGenrePopularChannel(List<Long> svcGenreIdList);

    ChartDto selectPreferDispChart(@Param("svcContentType") SvcContentType svcContentType,
                                   @Param("chartType") ChartType chartType ,
                                   @Param("osType") OsType osType ,
                                   @Param("trackLimitSize") int trackLimitSize);

    List<ChartDto> selectChartListByDefaultGenre();
    List<ChartDto> selectChartListByPreferGenre(@Param("characterNo") Long characterNo);
    ChartDto selectChartMusicContentList(@Param("chartId") Long chartId);
}
