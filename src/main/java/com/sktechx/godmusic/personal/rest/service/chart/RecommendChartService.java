/*
 * Copyright (c) 2020 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.service.chart;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.rest.model.vo.chart.ChartVo;

/**
 * 설명 : 추천 차트 조회 서비스
 */

public interface RecommendChartService {
    ChartVo getRecommendChart(Long characterNo, OsType osType, Long chartId, String mixYn);
}
