/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.model.dto.chart;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * 설명 : 차트 전시 정보 dto
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2020-04-17
 */

@Builder
@Data
public class ChartDispPropsDto {
    private Long chartId;
    private String chartNm;
    private String chartdispNm;
    private String dispPropsType;

    public static List<ChartDispPropsDto> defaultChartDispPropsList(){

        List<ChartDispPropsDto> list = new ArrayList<>();

        list.add(ChartDispPropsDto.builder()
                .chartId(1L)
                .chartNm("FLO 차트")
                .chartdispNm("FLO 차트")
                .dispPropsType("TOP100")
                .build());

        list.add(ChartDispPropsDto.builder()
                .chartId(3569L)
                .chartNm("키즈 차트")
                .chartdispNm("키즈 차트")
                .dispPropsType("KIDS100")
                .build());

        return list;
    }


}
