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
 * 설명 : 캐시드 스트리밍 토큰
 *
 * @author groot
 * @since 2020. 01. 06
 */
@Getter
@ToString
@Builder
public class CachedStreamingToken {
    private String svdId;
    private Long prchsId;
    private Long goodsId;
}
