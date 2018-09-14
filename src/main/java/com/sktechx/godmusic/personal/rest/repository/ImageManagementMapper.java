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

import com.sktechx.godmusic.lib.mybatis.annotation.BaseMapper;
import com.sktechx.godmusic.lib.redis.annotation.RedisCacheable;
import com.sktechx.godmusic.personal.common.domain.constant.RedisKeyConstant;
import com.sktechx.godmusic.personal.rest.model.dto.ImageManagementDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
//                                             @Param("osType") String osType,
//                                             @Param("dfkType") String dfkType,
//                                             @Param("imgDispType") String imgDispType,
                                                   @Param("shortcutType") String shortcutType);
}
