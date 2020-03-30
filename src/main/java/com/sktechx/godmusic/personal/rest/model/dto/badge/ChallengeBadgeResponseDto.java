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

/**
 * 설명 : 도전 중인 배지 response DTO
 *
 * @author Groot(조민국) / dev.mingood@sk.com
 * @since 2020. 03. 26
 */
@Getter
@ToString
public class ChallengeBadgeResponseDto {
    private String title;
    private String badgeImgUrl;

    // TODO 추후 삭제
    public ChallengeBadgeResponseDto(String title, String badgeImgUrl) {
        this.title = title;
        this.badgeImgUrl = badgeImgUrl;
    }

    public ChallengeBadgeResponseDto(BadgeTypeDto entity) {
        this.title = entity.getBadgeTypeNm();
        this.badgeImgUrl = entity.getIssuBfImgUrl();
    }
}
