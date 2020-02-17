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

import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenRequest;
import com.sktechx.godmusic.personal.rest.repository.ListenMapper;
import com.sktechx.godmusic.personal.rest.service.ListenService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendDummyDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public void addListenHistByChannel(ListenRequest param, Long memberNo, Long characterNo) {
        listenMapper.addListenHistByChannel(param.getListenType(), param.getListenTypeId(),
                memberNo, characterNo);

        //추천 패널의 경우 기존 추천 데이터  삭제 방지를 위한 DB 업데이트 처리
        if (isRecommendListen(param.getListenType())) {
            recommendDummyDataService.updateRecommendDataRemovePrevent(param, characterNo);
        }
    }

    private boolean isRecommendListen(String listenType) {
        return RC_ATST_TR.getCode().equals(listenType)
                || RC_SML_TR.getCode().equals(listenType)
                || RC_GR_TR.getCode().equals(listenType)
                || RC_CF_TR.getCode().equals(listenType);
    }

}
