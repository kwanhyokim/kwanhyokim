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

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.mybatis.annotation.BaseMapper;
import com.sktechx.godmusic.lib.redis.annotation.RedisCacheable;
import com.sktechx.godmusic.personal.common.domain.constant.RedisKeyConstant;
import com.sktechx.godmusic.personal.common.domain.type.RecommendChartPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartDispPropsDto;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartDto;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 7. 19.
 */
@BaseMapper
public interface ChartMapper {

    ChartDto selectPreferDispChart(@Param("chartPanelType") RecommendChartPanelType recommendChartPanelType,
                                   @Param("osType") OsType osType ,
                                   @Param("trackLimitSize") int trackLimitSize);

    @RedisCacheable(key = RedisKeyConstant.PERSONAL_PREFERENCE_GENRE_DEFAULT_KEY, expireSeconds = 60)
    List<ChartDto> selectChartListByDefaultGenre();

    List<ChartDto> selectChartListByPreferGenre(@Param("characterNo") Long characterNo);

    List<ChartDispPropsDto> selectPreferDisp();




}
