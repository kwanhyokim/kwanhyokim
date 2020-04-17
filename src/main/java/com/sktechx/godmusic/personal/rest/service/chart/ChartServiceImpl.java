/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.chart;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartDispPropsDto;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartDto;
import com.sktechx.godmusic.personal.rest.model.vo.chart.ChartVo;
import com.sktechx.godmusic.personal.rest.repository.ChartMapper;
import lombok.extern.slf4j.Slf4j;

import static com.sktechx.godmusic.personal.common.domain.constant.RedisKeyConstant.KIDS_CHART_KEY;
import static com.sktechx.godmusic.personal.common.domain.constant.RedisKeyConstant.REALTIME_CHART_KEY;

/**
 * 설명 : 각종 차트 서비스
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 23.
 */
@Service("chartService")
@Slf4j
public class ChartServiceImpl implements ChartService {


    @Autowired
    private ChartMapper chartMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MetaClient metaClient;

    @Override
    public ChartMapper getChartMapper() {
        return chartMapper;
    }
    @Override
    public RedisService getRedisService() {
        return redisService;
    }
    @Override
    public ChartVo getChartWithTrackList(Long characterNo, Long chartId, OsType osType,
            int trackLimitSize) {

        return ChartVo.from(
                getPreferDisp(
                        chartDispPropsDto ->
                                chartDispPropsDto.getChartId().equals(chartId)
                        , osType
                        , false
                ),
                Optional.ofNullable(
                        metaClient.getChartWithTrackList(chartId, trackLimitSize).getData()
                ).orElseThrow(
                        () -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA)
                )
        );
    }
    @Override
    public ChartDto getChartByDispPropsTypeWithTrackList(Long characterNo, String dispPropsType,
            OsType osType, int trackLimitSize) {

        ChartDispPropsDto chartDispPropsDto = getPreferDisp(
                currentChartDispPropsDto -> dispPropsType.equals(
                        (currentChartDispPropsDto).getDispPropsType()
                )
                , osType
                , false
        );

        ChartDto chartDto;

        String cacheKey = "TOP100".equals(dispPropsType) ? REALTIME_CHART_KEY :KIDS_CHART_KEY ;

        chartDto = redisService.getWithPrefix(cacheKey , ChartDto.class);

        if(chartDto == null) {
            chartDto = ChartDto.from(Optional.ofNullable(
                    metaClient.getChartWithTrackList(chartDispPropsDto
                    .getChartId(), trackLimitSize)
                            .getData()
            ).orElseThrow(() -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA)));
        }

        return chartDto;
    }
}
