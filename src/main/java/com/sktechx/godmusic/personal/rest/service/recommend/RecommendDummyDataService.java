/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.recommend;

import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenRequest;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendV2DummyDataRequest;

/**
 * 설명 : 추천 데이터
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 08. 25.
 */
public interface RecommendDummyDataService {
    void updateRecommendDataRemovePrevent(ListenRequest request, Long characterNo);

    int addTpoRecommendDummyData(Long characterNo);
    int deleteTpoRecommendDummyData(Long characterNo);

    void createRecommendV2DummyData(Long characterNo , RecommendV2DummyDataRequest recommendDummyDataRequest);
    void updateRecommendV2DummyData(Long characterNo , RecommendV2DummyDataRequest recommendDummyDataRequest);
    void deleteRecommendV2DummyData(Long characterNo , RecommendV2DummyDataRequest recommendDummyDataRequest);

    void updateAfloChnl();

    void addChart(Long characterNo);

    void deleteChart(Long characterNo);

    String clearCacheHome(Long characterNo);

    void addPrivateChart(Long characterNo, String mix);

    void deletePrivateChart(Long characterNo);

}
