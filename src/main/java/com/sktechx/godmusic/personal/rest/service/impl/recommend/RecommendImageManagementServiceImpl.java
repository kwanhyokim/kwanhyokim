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

package com.sktechx.godmusic.personal.rest.service.impl.recommend;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sktechx.godmusic.personal.common.domain.type.ImageDisplayType;
import com.sktechx.godmusic.personal.common.domain.type.OsType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.model.dto.ImageManagementDto;
import com.sktechx.godmusic.personal.rest.repository.RecommendImageManagementMapper;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendImageManagementService;
import lombok.extern.slf4j.Slf4j;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 11. 3.
 */
@Service
@Slf4j
public class RecommendImageManagementServiceImpl implements RecommendImageManagementService {

    private final RecommendImageManagementMapper recommendImageManagementMapper;

    public RecommendImageManagementServiceImpl(
            RecommendImageManagementMapper recommendImageManagementMapper) {
        this.recommendImageManagementMapper = recommendImageManagementMapper;
    }

    @Override
    public List<ImageManagementDto> getRecommendImageList(RecommendPanelContentType recommendType, Long recommendId, ImageDisplayType imageType, OsType osType) {
        List<ImageManagementDto> imageList = Collections.emptyList();

        switch (recommendType) {
            case RC_SML_TR:
                imageList = recommendImageManagementMapper.selectRecommendImageManagementList(recommendType, recommendId, imageType, osType);
                break;

            case RC_ATST_TR:
                imageList = recommendImageManagementMapper.selectFixedRecommendImageList(recommendType, recommendId, imageType, osType);
                break;

            case RC_GR_TR:
            case RC_CF_TR:
                imageList = recommendImageManagementMapper.selectMappingImageRecommendImageList(recommendType, recommendId, imageType, osType);
                break;
        }

        return imageList;
    }
}
