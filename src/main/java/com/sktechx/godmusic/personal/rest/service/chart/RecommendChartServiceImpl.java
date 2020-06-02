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

import org.springframework.stereotype.Service;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.rest.model.vo.chart.ChartVo;

/**
 * 설명 : 차트 조회 서비스
 *
 */

@Service
public class RecommendChartServiceImpl implements RecommendChartService{

    private final ChartServiceFactory chartServiceFactory;
    private final int CHART_TRACK_MAX_SIZE = 100;

    public RecommendChartServiceImpl(ChartServiceFactory chartServiceFactory){
        this.chartServiceFactory = chartServiceFactory;
    }

    @Override
    public ChartVo getRecommendChart(Long characterNo, OsType osType, Long chartId, String mixYn) {
        ChartVo chartVo = null;

        if("Y".equals(mixYn) && characterNo != null){
            chartVo =
                    chartServiceFactory.getChartServiceBy(ChartType.MONGO)
                            .getChartVoForDetailWithTrackList(
                                characterNo, chartId, osType, CHART_TRACK_MAX_SIZE
                    );
        }

        if(chartVo == null) {
            chartVo =
                    chartServiceFactory.getChartServiceBy(ChartType.CHART)
                            .getChartVoForDetailWithTrackList(
                                characterNo, chartId, osType, CHART_TRACK_MAX_SIZE
                    );
        }

        if(chartVo != null){
            chartVo.setRequestedMixYn(mixYn);
        }

        return chartVo;
    }
}
