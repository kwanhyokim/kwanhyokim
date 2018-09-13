/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.recommend;

import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.CommonTest;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendDummyDataRequest;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendDataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 설명 : 추천 데이터 생성 테스트
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 09. 11.
 */
@Slf4j
public class RecommendDummyDataTests extends CommonTest {
    @Autowired
    private RecommendDataService recommendDataService;
    private RecommendDummyDataRequest badRequest = new RecommendDummyDataRequest();
    private Long characterNo = 52L;

    @Test
    public void 요청값_유효성_예외_1() {
        Assertions.assertThrows(CommonBusinessException.class, () -> {
            badRequest.setRcmmdPhase(0);
            badRequest.setPanelCount(2);
            recommendDataService.createRecommendDummyData(characterNo, badRequest);
        });
    }
    @Test
    public void 요청값_유효성_예외_2() {
        Assertions.assertThrows(CommonBusinessException.class, () -> {
            badRequest.setRcmmdPhase(4);
            badRequest.setPanelCount(2);
            recommendDataService.createRecommendDummyData(characterNo, badRequest);

        });
    }
    @Test
    public void 요청값_유효성_예외_3() {
        Assertions.assertThrows(CommonBusinessException.class , ()->{
            badRequest.setRcmmdPhase(1);
            badRequest.setPanelCount(3);
            recommendDataService.createRecommendDummyData(characterNo,badRequest);
        });

    }
    @Test
    public void 요청값_유효성_예외_4() {
        Assertions.assertThrows(CommonBusinessException.class , ()->{
            badRequest.setRcmmdPhase(1);
            badRequest.setPanelCount(0);
            recommendDataService.createRecommendDummyData(characterNo,badRequest);
        });

    }
    @Test
    public void 요청값_유효성_예외_5() {
        Assertions.assertThrows(CommonBusinessException.class , ()->{
            badRequest.setRcmmdPhase(2);
            badRequest.setPanelCount(0);
            recommendDataService.createRecommendDummyData(characterNo,badRequest);
        });

    }

    @Test
    public void mforu() {
        RecommendDummyDataRequest request = new RecommendDummyDataRequest();
        request.setPanelCount(2);
        request.setRcmmdPhase(1);
        recommendDataService.createRecommendDummyData(characterNo ,request);
    }

}
