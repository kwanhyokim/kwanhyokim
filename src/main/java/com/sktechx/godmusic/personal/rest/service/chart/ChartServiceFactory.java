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

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 설명 : 차트 서비스 factory
 */

@Component
public class ChartServiceFactory {
    private final ChartService chartService;
    private final ChartService mongoChartService;

    private ChartServiceFactory(
            @Qualifier("chartService")
            ChartService chartService,
            @Qualifier("mongoChartService")
            ChartService mongoChartService){

        this.chartService = chartService;
        this.mongoChartService = mongoChartService;
    }

    public ChartService getChartServiceBy(@NotNull ChartType chartType){
        if(ChartType.MONGO == chartType) {
            return mongoChartService;
        }else{
            return chartService;
        }
    }
}
