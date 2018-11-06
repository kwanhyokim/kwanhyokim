/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.domain.type.RecommendChartPanelType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.ChartDto;
import com.sktechx.godmusic.personal.rest.repository.ChartMapper;
import com.sktechx.godmusic.personal.rest.service.ChartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.sktechx.godmusic.personal.common.domain.constant.RedisKeyConstant.*;
import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.*;
/**
 * 설명 : 각종 차트 서비스
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 23.
 */
@Service
@Slf4j
public class ChartServiceImpl implements ChartService {


    @Autowired
    private ChartMapper chartMapper;

    @Autowired
    private RedisService redisService;

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
}
