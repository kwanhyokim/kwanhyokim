/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.service.recommend;

import java.util.List;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.header.RecommendPanelHeaderVo;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2020-03-19
 */

public interface RecommendPanelHeaderService {

    RecommendImageManagementService getRecommendImageManagementService();

    RecommendPanelHeaderVo getRecommendPanelInfo(Long characterNo,
            String recommendPanelContentType,
            Long panelContentId,
            OsType osType);


    default List<ImageInfo> getRecommendPanelInfoBgImage(String recommendPanelContentType,
            Long panelContentId, OsType osType , int dispSn){

        return getRecommendImageManagementService().selectRecommendPanelInfoBgImageUrl(
                recommendPanelContentType, panelContentId, osType , (dispSn == 0 ? 1 : dispSn)
        );
    }
}
