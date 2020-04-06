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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sktechx.godmusic.personal.rest.domain.badge.BadgeIssueDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 설명 : 배지 상세 조회 response DTO
 *
 * @author Groot(조민국) / dev.mingood@sk.com
 * @since 2020. 03. 24
 */
@Getter
@ToString
public class BadgeDetailResponseDto {
    private int badgeIssueId;

    // App UI Type
    private String uiType;

    // 배지 이름
    private String title;
    // 배지 내용
    private String description;
    // 배지 이미지
    private String badgeImgUrl;
    // 배지 상세 팝업 이미지
    private String popupImgUrl;
    // 배지 배경 컬러 코드
    private String bgColorCode;

    // 부가 노출 텍스트 ex) 곡명, 아티스트명
    private String subTitle1;
    private String subTitle2;

    // 서브이미지 ex) 앨범 이미지
    private List<ImageInfo> subImgList;

    // 배지 획득 날짜
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private Date issueDtime;

    @JsonIgnore
    private String badgeType;

    public BadgeDetailResponseDto(BadgeIssueDto badgeIssue) {
        this.badgeIssueId = badgeIssue.getBadgeIssuId();
        this.uiType = badgeIssue.getBadgeTypeDto().getBadgeUiType();
        this.title = badgeIssue.getBadgeDto().getBadgeNm();
        this.description = badgeIssue.getBadgeDto().getBadgeDesc();
        this.badgeImgUrl = badgeIssue.getBadgeDto().getIssuAfImgUrl();
        this.popupImgUrl = badgeIssue.getBadgeDto().getPopupImgUrl();
        this.bgColorCode = badgeIssue.getBadgeTypeDto().getBackgroundRgbValue();
        this.badgeType = badgeIssue.getBadgeTypeDto().getBadgeType();
        this.issueDtime = badgeIssue.getIssuDtime();
    }

    public void setUiType(String uiType) {
        this.uiType = uiType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSubTitle1(String subTitle1) {
        this.subTitle1 = subTitle1;
    }

    public void setSubTitle2(String subTitle2) {
        this.subTitle2 = subTitle2;
    }

    public void setSubImgList(List<ImageInfo> subImgList) {
        this.subImgList = subImgList;
    }
}
