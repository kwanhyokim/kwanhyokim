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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sktechx.godmusic.personal.common.domain.type.PlayListType;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendChartTrackDto;
import lombok.Builder;
import lombok.Getter;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2020-04-01
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
public class ChartTrackDto {

    private Integer totalCount;

    private Long id;
    private PlayListType type;
    private String name;
    private String basedOnUpdate;
    private List<RecommendChartTrackDto> trackList;
}
