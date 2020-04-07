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
import com.sktechx.godmusic.personal.common.domain.type.RecommendChartPanelType;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.model.dto.ChartDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.chart.ChartVo;
import com.sktechx.godmusic.personal.rest.repository.ChartMapper;
import lombok.extern.slf4j.Slf4j;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.KIDS_CHART_EXPIRED_SECONDS;
import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.REALTIME_CHART_EXPIRED_SECONDS;
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
    public ChartDto getRealTimeTrackChart(OsType osType, int trackLimitSize) {

        ChartDto realTimeTrackChart = null;
        try{
            realTimeTrackChart =  redisService.getWithPrefix(REALTIME_CHART_KEY , ChartDto.class);
        }catch(Exception e){
            log.error("getRealTimeTrackChart error : {}",e.getMessage());
        }finally{
            if(realTimeTrackChart == null){
                realTimeTrackChart = chartMapper.selectPreferDispChart(RecommendChartPanelType.TOP100 , osType, trackLimitSize);
                if(realTimeTrackChart != null){

                    int priority = 1;
                    for(TrackDto trackDto : realTimeTrackChart.getTrackList()){
                        trackDto.setTrackSn(priority++);
                    }

                    redisService.setWithPrefix(REALTIME_CHART_KEY, realTimeTrackChart, REALTIME_CHART_EXPIRED_SECONDS);
                }
            }
        }
        return realTimeTrackChart;
    }

    @Override
    public ChartDto getKidsChart(OsType osType, int trackLimitSize) {
        ChartDto kidsTrackChart = null;
        try{
            kidsTrackChart =  redisService.getWithPrefix(KIDS_CHART_KEY , ChartDto.class);
        }catch(Exception e){
            log.error("getKidsChart error : {}",e.getMessage());
        }finally{
            if(kidsTrackChart == null){
                kidsTrackChart = chartMapper.selectPreferDispChart(RecommendChartPanelType.KIDS, osType, trackLimitSize);
                if(kidsTrackChart != null){
                    redisService.setWithPrefix(KIDS_CHART_KEY, kidsTrackChart , KIDS_CHART_EXPIRED_SECONDS);
                }
            }
        }
        return kidsTrackChart;
    }
    @Override
    public ChartTrackDto getChartWithTrackList(Long chartId, OsType osType, int trackLimitSize) {

        ChartTrackDto chartTrackDto =
            Optional.ofNullable(
                metaClient.getChartWithTrackList(chartId).getData()
            ).orElseThrow(
                () -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA)
            );

        ChartDto chartDto = chartMapper.selectPreferDisp(chartId, osType);

        ChartVo chartVo = ChartVo.from(chartDto, chartTrackDto);

        return null;
    }
}
