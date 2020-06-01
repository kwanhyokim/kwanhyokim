/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.model.dto.badge;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * 설명 : XXXXXXXXXXX
 *
 * @author Groot(조민국) / dev.mingood@sk.com
 * @since 2020. 03. 24
 */
@Getter
@ToString
public class BadgeDetailListResponseVo {
    private List<BadgeDetailResponseDto> newBadgeList;

    public BadgeDetailListResponseVo(List<BadgeDetailResponseDto> newBadgeList) {
        this.newBadgeList = newBadgeList;
    }
}
