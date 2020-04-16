/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.model.dto;

import com.sktechx.godmusic.lib.domain.code.YnType;
import lombok.Builder;
import lombok.Data;

/**
 * 설명 : 믹스 상태 정의 DTO
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2020-04-07
 */

@Builder
@Data
public class TasteMixDto {

    private YnType mixYn;   // 믹스 여부
    private String status;  // 믹스 상태
    // mixed : 개인화 차트
    // not_mixed : 개인화 차트 아님(실시간 차트)
    // require_more_listen : 더욱 청취 필요
    // same : 개인화 차트와 실시간 차트가 동일
    private String descriptionMessage;  // 차트의 믹스 상태에 대한 설명
    private String displayMessage;      // 차트 믹스 전시 메시지 (토스트 팝업용)

}
