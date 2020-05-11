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
import com.sktechx.godmusic.personal.rest.model.dto.ImageManagementDto;
import com.sktechx.godmusic.personal.rest.model.dto.chart.DispPropsImageDto;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 9. 14.
 */
@BaseMapper
public interface ImageManagementMapper {

    @RedisCacheable(format = RedisKeyConstant.PERSONAL_IMAGE_MANAGEMENT_KEY, params = {"#p0" , "#p1" , "#p2"}, expireSeconds = 60)
    List<ImageManagementDto> selectImageManagementList(@Param("imgContentType") String imgContentType,
                                                   @Param("imgContentId") Long imgContentId,
                                                   @Param("shortcutType") String shortcutType);

    List<DispPropsImageDto> selectChartBackgroundImageList(@Param("dispId") Long dispId,
            @Param("osType") OsType osType
    );

    List<DispPropsImageDto> selectDefaultMixChartBackgroundImageList(@Param("dispId") Long dispId);

    List<DispPropsImageDto> selectMixChartBackgroundImageList(@Param("dispId") Long svcGenreId,
            @Param("osType") OsType osType);

}


