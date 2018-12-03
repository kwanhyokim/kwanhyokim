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
import com.sktechx.godmusic.personal.rest.model.dto.MemberChannelImageDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 9. 14.
 */
@BaseMapper
public interface MemberChannelImageMapper {

    @RedisCacheable(format = RedisKeyConstant.PERSONAL_MEMBERCHANNEL_IMAGE_MANAGEMENT_KEY, params = {"#p0" , "#p1" , "#p2"}, expireSeconds = 60)
    List<MemberChannelImageDto> selectMemberChannelImageList(@Param("memberChnlId") Long memberChnlId,
                                                          @Param("imgDispType") String imgDispType,
                                                          @Param("osType") String osType);

    void insertMemberChannelImage(@Param("memberChnlId")Long memberChnlId,
                                  @Param("imgDispType")String imgDispType,
                                  @Param("osType")String osType,
                                  @Param("imgSize")Long imgSize,
                                  @Param("imgUrl")String imgUrl);
}
