/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.model.vo.listen.token;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * 설명 : 정산 토큰
 *
 * @author groot
 * @since 2019. 12. 19
 */
@Getter
@ToString
@Builder
public class SettlementToken {
    private Integer version;
    private String serviceId;
    private Long purchaseId;
    private Long goodsId;
}
