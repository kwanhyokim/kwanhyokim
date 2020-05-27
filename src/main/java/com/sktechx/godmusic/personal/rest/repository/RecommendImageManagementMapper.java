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
import com.sktechx.godmusic.lib.mybatis.annotation.ReadOnlyMapper;
import com.sktechx.godmusic.personal.common.domain.type.ImageDisplayType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.model.dto.ImageManagementDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;

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

    List<ImageInfo> selectRecommendPanelDefaultImageList();

    /**
     * 반응형 패널(방금레이더) 추천 이미지 조회
     */
    List<ImageManagementDto> selectAdaptivePanelImageList(@Param("recommendType") RecommendPanelContentType recommendType,
                                                          @Param("imageType") ImageDisplayType imageType,
                                                          @Param("osType") OsType osType);


    // 추천 패널 상세 헤더 배경 이미지
    String selectRecommendPanelInfoBgImageUrl(@Param("recommendPanelContentType") String recommendPanelContentType
            , @Param("rcmmdId") Long rcmmdId
            , @Param("osType") OsType osType
            , @Param("dispSn") int dispSn);
}
