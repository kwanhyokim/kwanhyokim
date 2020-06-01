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

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

/**
 * 설명 : tb_badge_issue DTO
 *
 * @author Groot(조민국) / dev.mingood@sk.com
 * @since 2020. 03. 26
 */
@Getter
@ToString
public class BadgeIssueDto {
    private int badgeIssuId;
    private Long characterNo;
    private int badgeId;

    private String issuType;    // 발행타입
    private String issuTypeId;
    private int listenCnt;
    private String confrmDvcOsType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date issuDtime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date confrmDtime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date createDtime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updateDtime;

    private BadgeDto badgeDto;
    private BadgeTypeDto badgeTypeDto;

    public Long getIssuTypeIdToLong() {
        return Long.valueOf(issuTypeId);
    }
}
