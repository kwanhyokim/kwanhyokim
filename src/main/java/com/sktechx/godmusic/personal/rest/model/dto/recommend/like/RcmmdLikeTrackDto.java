/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.model.dto.recommend.like;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * 설명 : 좋아요 추천 트랙 전시용 vo
 */

@Builder
@Data
public class RcmmdLikeTrackDto {

    private Long characterNo;
    private Date createDtime;
    private Date updateDtime;

    private List<RcmmdLikeTrackDetailDto> rcmmdList;

}
