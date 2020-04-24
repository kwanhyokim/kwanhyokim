/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.chart;

import com.sktechx.godmusic.personal.rest.model.vo.chart.ChartVo;
import lombok.Builder;
import lombok.Data;

/**
 * 설명 : 차트 취향 표시용 VO
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2020-04-24
 */

@Builder
@Data
public class TasteMixVo {
    private String status;  // 믹스 상태

    public static TasteMixVo from (String chartTaste){

        return TasteMixVo.builder()
                .status(
                        ChartVo.RCMMD_TASTE_MIX_VO_MAP.get(
                        chartTaste == null ? "NOT_MIXED" : chartTaste)
                                .getStatus()
                )
                .build();
    }
}
