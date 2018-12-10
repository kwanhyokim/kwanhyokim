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

import com.sktechx.godmusic.lib.mybatis.annotation.ReadOnlyMapper;
import com.sktechx.godmusic.lib.redis.annotation.RedisCacheable;
import com.sktechx.godmusic.personal.common.domain.constant.RedisKeyConstant;
import com.sktechx.godmusic.personal.common.domain.type.ImageDisplayType;
import com.sktechx.godmusic.personal.common.domain.type.OsType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.model.dto.ImageManagementDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 11. 3.
 */
@ReadOnlyMapper
public interface RecommendImageManagementMapper {

//    @RedisCacheable(key = RedisKeyConstant.RECOMMEND_IMAGE_MANAGEMENT_KEY, params = {"#p0", "#p1", "#p2", "#p3"})
    List<ImageManagementDto> selectRecommendImageManagementList(@Param("recommendType") RecommendPanelContentType recommendType,
                                                                @Param("recommendId") Long recommendId,
                                                                @Param("imageType") ImageDisplayType imageType,
                                                                @Param("osType") OsType osType);

//    @RedisCacheable(key = RedisKeyConstant.RECOMMEND_IMAGE_MANAGEMENT_KEY, params = {"#p0", "#p1", "#p2", "#p3"})
    List<ImageManagementDto> selectMappingImageRecommendImageList(@Param("recommendType") RecommendPanelContentType recommendType,
                                                                  @Param("recommendId") Long recommendId,
                                                                  @Param("imageType") ImageDisplayType imageType,
                                                                  @Param("osType") OsType osType);

    List<ImageManagementDto> selectFixedRecommendImageList(@Param("recommendType") RecommendPanelContentType recommendType,
                                                                  @Param("recommendId") Long recommendId,
                                                                  @Param("imageType") ImageDisplayType imageType,
                                                                  @Param("osType") OsType osType);
}
