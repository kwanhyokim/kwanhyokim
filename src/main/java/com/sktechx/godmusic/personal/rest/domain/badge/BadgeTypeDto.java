/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.domain.badge;

import com.sktechx.godmusic.lib.domain.code.YnType;
import lombok.Getter;
import lombok.ToString;

/**
 * 설명 : tb_badge_type DTO
 *
 * @author Groot(조민국) / dev.mingood@sk.com
 * @since 2020. 03. 26
 */
@Getter
@ToString
public class BadgeTypeDto {
    private int badgeTypeId;

    private String badgeType;
    private String badgeTypeNm;
    private String badgeUiType;
    private String issuType;            // 발행타입
    private String aggregatePeriodType; // 집계기간
    private String backgroundRgbValue;
    private YnType useYn;
    private int dispSn;
    private String issuBfImgUrl;
}
