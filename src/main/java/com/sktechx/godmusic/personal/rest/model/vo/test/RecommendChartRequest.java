/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.model.vo.test;

import java.util.List;
import javax.validation.constraints.NotEmpty;

import lombok.Builder;
import lombok.Data;

/**
 * 설명 : 추천 차트 몽고 생성용 request vo
 *
 */

@Builder
@Data
public class RecommendChartRequest {

    @NotEmpty
    private Long chartId;

    @NotEmpty
    private Long characterNo;

    @NotEmpty
    private String chartTaste;

    @NotEmpty
    private List<Long> trackIdList;
}
