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

    // 배지 이름 (장르주의자의 경우 장르명까지 배지 이름에 포함됨)
    private String title;
    // 배지 내용
    private String description;
    // 배지 이미지
    private String badgeImgUrl;

    // 부가 노출 텍스트 ex) 곡명, 아티스트명
    private String subTitle1;
    private String subTitle2;

    // 서브이미지 ex) 앨범 이미지
    private List<ImageInfo> subImgList;

    // 배지 획득 날짜
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private Date issueDtime;

    public BadgeDetailResponseDto(BadgeIssueDto entity) {
        this.badgeIssueId = entity.getBadgeIssuId();
        this.uiType = entity.getBadgeTypeDto().getBadgeUiType();
        this.title = entity.getBadgeDto().getBadgeNm();
        this.description = entity.getBadgeDto().getBadgeDesc();
        this.badgeImgUrl = entity.getBadgeDto().getIssuAfImgUrl();
        this.issueDtime = entity.getIssuDtime();
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
