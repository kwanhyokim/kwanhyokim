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

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendChartTrackDto;
import lombok.Builder;
import lombok.Data;

/**
 * 설명 :
 *
 * @author N/A
 * @date 2020. 02. 17.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class ChartMetaDto {

    private Long chartId;
    private LocalDateTime chartStdTime;
    private String chartTaste;

    private List<RecommendChartTrackDto> trackList;

}
