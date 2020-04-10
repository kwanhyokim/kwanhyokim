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
 * 설명 : tb_badge DTO
 *
 * @author Groot(조민국) / dev.mingood@sk.com
 * @since 2020. 03. 26
 */
@Getter
@ToString
public class BadgeDto {
    private int badgeId;
    private int badgeTypeId;
    private String badgeCd;
    private String badgeNm;

    private String badgeDesc;
    private String issuConditionDesc;

    private String defaultDesc;
    private String defaultImgUrl;
    private String defaultPopupImgUrl;
    private String popupImgUrl;
    private String issuAfImgUrl;

    private YnType useYn;
    private int dispSn;
}
