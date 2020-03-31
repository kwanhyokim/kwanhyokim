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

import com.sktechx.godmusic.personal.rest.domain.badge.BadgeIssueDto;
import lombok.Getter;
import lombok.ToString;

/**
 * 설명 : 내가 모은 배지 response DTO
 *
 * @author Groot(조민국) / dev.mingood@sk.com
 * @since 2020. 03. 26
 */
@Getter
@ToString
public class ReceivedBadgeResponseDto {
    private int badgeIssueId;
    private String title;
    private String badgeImgUrl;

    private String badgeType;

    // TODO 추후 삭제
    public ReceivedBadgeResponseDto(int badgeIssueId, String title, String badgeImgUrl) {
        this.badgeIssueId = badgeIssueId;
        this.title = title;
        this.badgeImgUrl = badgeImgUrl;
    }

    public ReceivedBadgeResponseDto(BadgeIssueDto badgeIssue) {
        this.badgeIssueId = badgeIssue.getBadgeIssuId();
        this.title = badgeIssue.getBadgeDto().getBadgeNm();
        this.badgeImgUrl = badgeIssue.getBadgeDto().getIssuAfImgUrl();
        this.badgeType = badgeIssue.getBadgeTypeDto().getBadgeType();
    }
}
