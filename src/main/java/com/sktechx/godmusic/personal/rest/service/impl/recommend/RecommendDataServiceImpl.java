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
                    int updateCount = recommendMapper.updateRecommendDataRemovePrevent(recommendPanelContentType , request.getListenTypeId() , characterNo);

                    if(updateCount > 0){
                        updateForeignRecommendDataRemovePrevent(recommendPanelContentType , request.getListenTypeId());
                    }
                }
            }
        }catch(Exception e){
            log.error("Recommend :: updateRecommendDataPrevent :: Error Message",e.getMessage());
        }
    }

    private void updateForeignRecommendDataRemovePrevent(RecommendPanelContentType recommendPanelContentType , Long rcmmdId){
        switch(recommendPanelContentType){
            case RC_ATST_TR :
                recommendMapper.updateArtistListRemovePrevent(rcmmdId);
                recommendMapper.updateArtistTrackListRemovePrevent(rcmmdId);
                break;
            case RC_SML_TR :
                recommendMapper.updateSimilarTrackListRemovePrevent(rcmmdId);
                break;
            case RC_GR_TR :
                recommendMapper.updatePreferGenreSimilarTrackListRemovePrevent(rcmmdId);
                break;
            case RC_CF_TR :
                recommendMapper.updateMforuListRemoveRemovePrevent(rcmmdId);
                 break;
            default :
                return;
        }
    }
}