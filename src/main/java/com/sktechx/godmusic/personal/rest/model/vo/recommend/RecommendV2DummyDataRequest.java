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

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 설명 : 추천 데이터 임의 생성
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 09. 11.
 */

@Data
@ApiModel(value = "추천 데이터 생성 파라미터")
public class RecommendV2DummyDataRequest {
    @NotNull
    @ApiModelProperty(required = true, example = "RC_SML_TR", value = "오늘의 플로(RC_SML_TR), 나를 위한 FLO(RC_CF_TR), 아티스트 FLO(RC_ATST_TR)")
    private String type;
    @NotNull
    @ApiModelProperty(required = true, example = "10", value = "단계별 패널 개수")
    private Integer panelCount;

}
