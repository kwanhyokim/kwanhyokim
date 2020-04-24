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

import org.springframework.stereotype.Service;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartDispPropsDto;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartDto;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartTrackDto;
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


    private final ChartMapper chartMapper;

    private final RedisService redisService;

    private final PersonalMongoClient personalMongoClient;

    private final MetaClient metaClient;
    public MongoChartServiceImpl(ChartMapper chartMapper, RedisService redisService,
            PersonalMongoClient personalMongoClient, MetaClient metaClient) {
        this.chartMapper = chartMapper;
        this.redisService = redisService;
        this.personalMongoClient = personalMongoClient;
        this.metaClient = metaClient;
    }

    // default 용
    @Override
    public ChartMapper getChartMapper() {
        return chartMapper;
    }
    @Override
    public RedisService getRedisService() {
        return redisService;
    }

    // 개인화 차트 상세
    @Override
    public ChartVo getChartWithTrackList(Long characterNo, Long chartId, OsType osType,
            int trackLimitSize) {

        ChartDispPropsDto chartDispPropsDto = Optional.ofNullable(
                getPreferDisp(
                        currentChartDispPropsDto ->
                                currentChartDispPropsDto.getChartId().equals(chartId)
                , osType
                ,   true)
        ).orElseThrow(() -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA));

        ChartTrackDto chartTrackDto = getChartTrackDto(characterNo, trackLimitSize,
                chartDispPropsDto);

        Optional.ofNullable(
                chartTrackDto.getTrackList()
        ).ifPresent(
                trackDtos -> trackDtos.forEach( TrackDto::disableRankIndicator)
        );

        return ChartVo.from( chartDispPropsDto, chartTrackDto );
    }

    // 개인화 차트 홈
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

        ChartTrackDto chartTrackDto = getChartTrackDto(characterNo, trackLimitSize,
                chartDispPropsDto);

        return ChartDto.from( chartDispPropsDto, chartTrackDto );
    }

    private ChartTrackDto getChartTrackDto(Long characterNo, int trackLimitSize,
            ChartDispPropsDto chartDispPropsDto) {

        ChartTrackDto chartTrackDto =
                personalMongoClient.getRecommendChart(characterNo,
                                        chartDispPropsDto.getChartId(),
                                        100
                                ).getData()
                ;

        if(chartTrackDto != null) {
            chartTrackDto.decreaseTrackListSizeTo(trackLimitSize);

        }else{
            chartTrackDto =
                    Optional.ofNullable(
                            metaClient
                                    .getChartWithTrackList(chartDispPropsDto.getChartId(), trackLimitSize)
                                    .getData()
                    ).orElseThrow(() -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA)
            );
        }

        return chartTrackDto;
    }
}
