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
 * 설명 : 모든 배지 리스트 (내가 모은 + 도전 중인) response DTO
 *
 * @author Groot(조민국) / dev.mingood@sk.com
 * @since 2020. 03. 26
 */
@Getter
@ToString
public class AllBadgeListResponseDto {
    private List<MyBadgeResponseDto> myBadgeList;
    private List<ChallengeBadgeResponseDto> challengeBadgeList;

    public AllBadgeListResponseDto(List<MyBadgeResponseDto> myBadgeList,
                                   List<ChallengeBadgeResponseDto> challengeBadgeList) {
        this.myBadgeList = myBadgeList;
        this.challengeBadgeList = challengeBadgeList;
    }
}
