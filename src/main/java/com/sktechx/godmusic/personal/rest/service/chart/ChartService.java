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

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartDispPropsDto;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartDispPropsDtoWrapper;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartDto;
import com.sktechx.godmusic.personal.rest.model.vo.chart.ChartVo;
import com.sktechx.godmusic.personal.rest.repository.ChartMapper;

import static com.sktechx.godmusic.personal.common.domain.constant.RedisKeyConstant.CHART_DISPLAY_PROPERTIES_KEY;

/**
 * 설명 : 차트 서비스
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 23.
 */
public interface ChartService {

    ChartMapper getChartMapper();

    RedisService getRedisService();

    ChartVo getChartWithTrackList(Long characterNo, Long chartId, OsType osType,
            int trackLimitSize);

    ChartDto getChartByDispPropsTypeWithTrackList(Long characterNo, String dispPropsType,
            OsType osType,
            int trackLimitSize);

    default ChartDispPropsDto getPreferDisp(Predicate<ChartDispPropsDto> predicate, OsType osType
            , Boolean useNewImgUrl){

        ChartDispPropsDtoWrapper chartDispPropsDtoWrapper =
                getRedisService().getWithPrefix(CHART_DISPLAY_PROPERTIES_KEY,
                ChartDispPropsDtoWrapper.class
                );

        List<ChartDispPropsDto> chartDispPropsDtoList;

        if(chartDispPropsDtoWrapper == null){
            chartDispPropsDtoList = getChartMapper().selectPreferDisp();
            getRedisService().setWithPrefix(CHART_DISPLAY_PROPERTIES_KEY,
                    ChartDispPropsDtoWrapper.builder()
                            .chartDispPropsDtoList(chartDispPropsDtoList)
                            .build(),
                    86400);
        }else{
            chartDispPropsDtoList = chartDispPropsDtoWrapper.getChartDispPropsDtoList();
        }

        ChartDispPropsDto chartDispPropsDto =
                chartDispPropsDtoList.stream()
                .filter( predicate )
                .findFirst()
                .orElseThrow( () -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA));

        chartDispPropsDto.getImgList().stream()
                .filter( imageInfo -> osType.equals(imageInfo.getOsType()))
                .findFirst()
                .ifPresent(
                        imageInfo -> {

                            imageInfo.replaceUrlByNew(useNewImgUrl);
                            chartDispPropsDto.setImgList(Collections.singletonList(imageInfo));
                        }

                );

        return chartDispPropsDto;
    }

}
