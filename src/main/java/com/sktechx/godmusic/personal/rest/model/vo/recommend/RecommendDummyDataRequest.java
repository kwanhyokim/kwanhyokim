/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.vo.recommend;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 설명 : 추천 데이터 임의 생성
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 09. 11.
 */

@Data
@ApiModel(value = "추천 데이터 생성 파라미터")
public class RecommendDummyDataRequest {
    @NotNull
    @ApiModelProperty(required = true, example = "1", value = "추천 단계 1~3")
    private Integer rcmmdPhase;
    @NotNull
    @ApiModelProperty(required = true, example = "2", value = "단계별 패널 개수")
    private Integer panelCount;
}
