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
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartDispPropsDto;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.chart.ChartVo;
import com.sktechx.godmusic.personal.rest.repository.ChartMapper;
import com.sktechx.godmusic.personal.rest.service.mongo.PersonalMongoClient;
import lombok.extern.slf4j.Slf4j;

/**
 * 설명 : 각종 차트 서비스
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 23.
 */
@Service("mongoChartService")
@Slf4j
public class MongoChartServiceImpl implements ChartService {


    @Autowired
    private ChartMapper chartMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private PersonalMongoClient personalMongoClient;

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

        ChartDispPropsDto chartDispPropsDto = Optional.ofNullable(
                getPreferDisp(
                        cuurentChartDispPropsDto ->
                                cuurentChartDispPropsDto.getChartId().equals(chartId)
                , osType
                ,   true)
        ).orElseThrow(() -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA));


        return ChartVo.from(
                    chartDispPropsDto
                ,
                Optional.ofNullable(
                        personalMongoClient.getRecommendChart(
                                characterNo,
                                chartId,
                                trackLimitSize).getData()
                )
                .orElseGet(
                        () -> metaClient.getChartWithTrackList(chartDispPropsDto.getChartId(), trackLimitSize).getData()
                )
        );
    }

    @Override
    public ChartDto getChartByDispPropsTypeWithTrackList(Long characterNo, String dispPropsType,
            OsType osType,
            int trackLimitSize) {

        ChartDispPropsDto chartDispPropsDto = Optional.ofNullable(
                getPreferDisp(
                    currentChartDispPropsDto -> dispPropsType.equals(
                            (currentChartDispPropsDto).getDispPropsType()
                    )
                , osType
                , true)
        ).orElseThrow(() -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA));

        ChartDto chartDto =
                ChartDto.from(
                        Optional.ofNullable(
                personalMongoClient.getRecommendChart(
                        characterNo,
                        chartDispPropsDto.getChartId(),
                        trackLimitSize).getData()
        ).orElseGet(
                () -> metaClient.getChartWithTrackList(chartDispPropsDto.getChartId(), trackLimitSize).getData()
        ));

        chartDto.setImgList(
                chartDispPropsDto.getImgList().stream().map(
                chartImageInfo -> (ImageInfo)chartImageInfo).collect(Collectors.toList())
        );
        chartDto.setChartNm(chartDispPropsDto.getChartNm());

        return chartDto;

    }
}
