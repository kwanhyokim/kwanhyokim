/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.impl.recommend;

import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenRequest;
import com.sktechx.godmusic.personal.rest.repository.RecommendMapper;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 설명 : XXXXXXXX
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 08. 25.
 */
@Slf4j
@Service
public class RecommendDataServiceImpl implements RecommendDataService {

    @Autowired
    private RecommendMapper recommendMapper;


    @Override
    public void updateRecommendDataRemovePrevent(ListenRequest request, Long characterNo) {

        try{
            if(request != null){
                RecommendPanelContentType recommendPanelContentType = RecommendPanelContentType.fromCode(request.getListenType());
                if(recommendPanelContentType != null && request.getListenTypeId() != null && characterNo != null){
                    recommendMapper.updateRecommendDataRemovePrevent(recommendPanelContentType , request.getListenTypeId() , characterNo);
                }
            }
        }catch(Exception e){
            log.error("Recommend :: updateRecommendDataPrevent :: Error Message",e.getMessage());
        }
    }
}