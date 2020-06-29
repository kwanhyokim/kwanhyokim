/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenRequest;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendUpdateRequest;
import com.sktechx.godmusic.personal.rest.repository.ListenMapper;
import com.sktechx.godmusic.personal.rest.service.ListenService;
import com.sktechx.godmusic.personal.rest.client.PersonalMongoClient;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendDummyDataService;
import lombok.extern.slf4j.Slf4j;

import static com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType.*;

/**
 * 실제론 Purchase 에서 처리해야하지만 청취로그 특성상 빈번한 호출이 예상되어 일단 필요한부분 구현
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 8.
 */
@Slf4j
@Service
public class ListenServiceImpl implements ListenService {

    @Autowired
    private ListenMapper listenMapper;

    @Autowired
    private RecommendDummyDataService recommendDummyDataService;

    @Autowired
    private PersonalMongoClient personalMongoClient;

    @Override
    public void addListenHistByChannel(ListenRequest param, Long memberNo, Long characterNo) {

        String listenType = param.getListenType();
        Long listenTypeId = param.getListenTypeId();

        listenMapper.addListenHistByChannel(listenType, listenTypeId, memberNo, characterNo);

        //추천 패널의 경우 기존 추천 데이터  삭제 방지를 위한 DB 업데이트 처리
        if (isRecommendListen(listenType)) {

            // 방금레이더 추천 정보는 Mongo에 만 존재하므로 mysql 쪽은 Update 하지 않는다.
            if (!RC_LKSM_TR.getCode().equals(listenType)) {
                recommendDummyDataService.updateRecommendDataRemovePrevent(param, characterNo);
            }

            // mongo
            personalMongoClient.updateRecommendDelTargetYn(
                    characterNo, listenType, listenTypeId,
                    RecommendUpdateRequest.builder().delTargetYn("N").build()
            );
        }
    }

    private boolean isRecommendListen(String listenType) {
        return RC_ATST_TR.getCode().equals(listenType)
                || RC_SML_TR.getCode().equals(listenType)
                || RC_GR_TR.getCode().equals(listenType)
                || RC_CF_TR.getCode().equals(listenType)
                || RC_LKSM_TR.getCode().equals(listenType);
    }

}
