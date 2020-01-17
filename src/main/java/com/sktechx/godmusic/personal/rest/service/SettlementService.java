/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.service;

import com.sktechx.godmusic.personal.rest.model.dto.listen.SettlementInfoDto;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenTrackRequest;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ResourcePlayLogRequestParam;

/**
 * 정산 처리를 위한 서비스
 * 향후 별도 서버로 분리 필요
 *
 * @author 박상현/SKTECH (sanghyun.park.tx@sk.com)
 * @date 2019. 3. 11.
 */
public interface SettlementService {

    // 정산 정보 조회
    String getServiceCode(Long memberNo, String settlementTypeCode);

    String getServiceCodeByPrchsId(Long prchsId, String settlementTypeCode);

    SettlementInfoDto getSettlementInfo(Long memberNo, String settlementTypeCode);

    String evaluateServiceId(ListenTrackRequest request, SettlementInfoDto settlement);

    String evaluateServiceIdByResourcePlayLogRequest(ResourcePlayLogRequestParam param,
                                                     SettlementInfoDto settlementInfo);

}
