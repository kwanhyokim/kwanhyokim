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

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartDto;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartTrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartTrackTasteMixTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.chart.ChartDispPropsVo;
import com.sktechx.godmusic.personal.rest.model.vo.chart.ChartVo;
import com.sktechx.godmusic.personal.rest.repository.ChartMapper;
import com.sktechx.godmusic.personal.rest.service.mongo.PersonalMongoClient;
import lombok.extern.slf4j.Slf4j;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toMap;

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

        ChartDispPropsVo chartDispPropsVo = Optional.ofNullable(
                getPreferDisp(
                        currentChartDispPropsDto ->
                                currentChartDispPropsDto.getChartId().equals(chartId)
                , osType
                ,   true)
        ).orElseThrow(() -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA));

        ChartTrackDto chartTrackDto = getChartTrackDto(characterNo, trackLimitSize,
                chartDispPropsVo);

        chartTrackDto.disableRank();
        chartTrackDto.makeTrackDispSn();

        return ChartVo.from( chartDispPropsVo, chartTrackDto );
    }

    // 개인화 차트 홈
    @Override
    public ChartDto getChartByDispPropsTypeWithTrackList(Long characterNo, String dispPropsType,
            OsType osType,
            int trackLimitSize) {

        ChartDispPropsVo chartDispPropsVo = Optional.ofNullable(
                getPreferDisp(
                    currentChartDispPropsDto -> dispPropsType.equals(
                            (currentChartDispPropsDto).getDispPropsType()
                    )
                , osType
                , true)
        ).orElseThrow(() -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA));

        ChartTrackDto chartTrackDto = getChartTrackDto(characterNo, trackLimitSize,
                chartDispPropsVo);

        chartTrackDto.disableRank();
        chartTrackDto.makeTrackDispSn();

        return ChartDto.from( chartDispPropsVo, chartTrackDto );
    }

    private ChartTrackDto getChartTrackDto(Long characterNo, int trackLimitSize,
            ChartDispPropsVo chartDispPropsVo) {

        ChartTrackDto chartTrackDto = Optional.ofNullable(
                metaClient
                        .getChartWithTrackList(chartDispPropsVo.getChartId(), 100)
                        .getData()
        ).orElseThrow(() -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA));


        if(characterNo == null) {
            chartTrackDto.setChartTaste("REQUIRE_LOGIN");
            return chartTrackDto;
        }

        chartTrackDto.setChartTaste("NOT_MIXED");

        Optional.ofNullable(
                personalMongoClient.getRecommendChartTrackTasteMixDto(
                        characterNo,
                        chartDispPropsVo.getChartId()
                ).getData())
        .ifPresent(
                trackTasteMixDto -> {
                    if (trackTasteMixDto.isRequireReordering()) {
                        Map<Long, Integer> chartTrackDisplayOrderMap =
                                trackTasteMixDto.getTrackList().stream().collect(
                                toMap(
                                        ChartTrackTasteMixTrackDto::getTrackId,
                                        ChartTrackTasteMixTrackDto::getDisplayOrder, (dupA, dupB) -> dupA)
                                );

                        chartTrackDto.getTrackList()
                                .sort(comparingInt(value ->
                                        chartTrackDisplayOrderMap.getOrDefault(
                                                value.getTrackId(),
                                                Integer.MAX_VALUE))
                                );
                    }

                    chartTrackDto.setChartTaste(trackTasteMixDto.getStatus());
                }
        );

        chartTrackDto.decreaseTrackListSizeTo(trackLimitSize);

        return chartTrackDto;
    }
}
