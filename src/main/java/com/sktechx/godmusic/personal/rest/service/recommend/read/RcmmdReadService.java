/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.service.recommend.read;

import java.util.List;
import java.util.Optional;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.client.PersonalMongoClient;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendPanelTrackDto;

import static com.sktechx.godmusic.personal.common.domain.constant.RedisKeyConstant.PERSONAL_USEMGO_KEY;

/**
 * 설명 : 추천 컨텐츠 조회 서비스
 *
 */

public interface RcmmdReadService {

    //  추천 패널 타입 지정 ( 몽고 사용 여부 체크에도 같은 코드로 이용)
    RecommendPanelContentType getRecommendPanelContentType();

    // 상세 헤더 조회용
    Optional<? extends RecommendDto> getRecommend(Long characterNo, Long rcmmdId);

    // 홈 중단 조회용 ( 캐쉬 사용?)
    List<? extends RecommendDto> getRecommendListByCharacterNo(Long characterNo);

    // 홈 패널 조회용
    List<? extends RecommendDto> getRecommendListWithTrackByCharacterNoOrderByDispStartDtime(
            Long characterNo,
            int panelMaxSize,
            int trackMaxSize,
            OsType osType
    );

    // 상세 트랙 조회용
    List<RecommendPanelTrackDto> getRecommendTrackListByCharacterNoAndRcmmdId(
            Long characterNo, Long rcmmdId);

    PersonalMongoClient getPersonalMongoClient();
    MetaClient getMetaClient();

    RedisService getRedisService();

    default boolean checkUseMongo(){
        return getRedisService().existsWithPrefix(String.format(PERSONAL_USEMGO_KEY,
                getRecommendPanelContentType().getCode()));
    }
}
