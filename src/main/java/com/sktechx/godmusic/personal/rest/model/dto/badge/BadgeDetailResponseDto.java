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

import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
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
    private Long badgeIssueId;

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
    private String issueDtime;

//    private Integer listenCount;


    public BadgeDetailResponseDto(Long badgeIssueId, String uiType, String title, String description, String badgeImgUrl, String subTitle1, String subTitle2, List<ImageInfo> subImgList, String issueDtime) {
        this.badgeIssueId = badgeIssueId;
        this.uiType = uiType;
        this.title = title;
        this.description = description;
        this.badgeImgUrl = badgeImgUrl;
        this.subTitle1 = subTitle1;
        this.subTitle2 = subTitle2;
        this.subImgList = subImgList;
        this.issueDtime = issueDtime;
    }
}
