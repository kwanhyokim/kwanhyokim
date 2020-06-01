/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.model.vo.badge;

import com.sktechx.godmusic.personal.rest.domain.badge.BadgeTypeDto;
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
public class ChallengeBadgeResponseVo {
    private String title;
    private String badgeImgUrl;
    private String bgColorCode;

    public ChallengeBadgeResponseVo(BadgeTypeDto badgeTypeDto) {
        this.title = badgeTypeDto.getBadgeTypeNm();
        this.badgeImgUrl = badgeTypeDto.getIssuBfImgUrl();
        this.bgColorCode = badgeTypeDto.getBackgroundRgbValue();
    }
}
