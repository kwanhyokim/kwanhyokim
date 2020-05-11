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
import org.springframework.data.util.Optionals;
import org.springframework.stereotype.Service;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.domain.PreferPropsType;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartDto;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartTrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.chart.DispPropsImageDto;
import com.sktechx.godmusic.personal.rest.model.vo.chart.ChartDispPropsVo;
import com.sktechx.godmusic.personal.rest.model.vo.chart.ChartVo;
import com.sktechx.godmusic.personal.rest.repository.ChartMapper;
import com.sktechx.godmusic.personal.rest.repository.ImageManagementMapper;
import com.sktechx.godmusic.personal.rest.service.image.ImageReadService;
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
    private ImageManagementMapper imageManagementMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ImageReadService imageReadService;

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

    public ChartVo getChartVoForDetailWithTrackList(Long characterNo, Long chartId, OsType osType,
            int trackLimitSize) {

        ChartTrackDto chartTrackDto =
                Optional.ofNullable(
                        metaClient.getChartWithTrackList(chartId, trackLimitSize)
                                .getData()
                ).orElseThrow(() -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA));


        chartTrackDto.makeTrackDispSn();

        return ChartVo.from(
                getPreferDisp(
                        chartDispPropsDto ->
                                chartDispPropsDto.getChartId().equals(chartId)
                ),
                chartTrackDto
        );
    }
    @Override
    public ChartDto getChartDtoForHomeByDispPropsTypeWithTrackList(Long characterNo, String dispPropsType,
            OsType osType, int trackLimitSize) {

        ChartDispPropsVo chartDispPropsVo = getPreferDisp(
                currentChartDispPropsDto -> dispPropsType.equals(
                        (currentChartDispPropsDto).getDispPropsType()
                )
        );

        getChartBackgroundImage(chartDispPropsVo.getChartId(), osType)
            .ifPresent(
                dispPropsImageDto -> chartDispPropsVo.setImgList(dispPropsImageDto.getImgList())
        );

        final ChartDto[] chartDtoArray = new ChartDto[1];

        Optionals.ifPresentOrElse(
                Optional.ofNullable(
                        redisService.getWithPrefix(
                                PreferPropsType.TOP100.getCode().equals(dispPropsType) ?
                                        REALTIME_CHART_KEY : KIDS_CHART_KEY
                                ,
                                ChartDto.class
                        )
                ),

                chartDto -> chartDtoArray[0] = chartDto
                ,

                () -> {

                    ChartTrackDto chartTrackDto =
                            Optional.ofNullable(
                                    metaClient.getChartWithTrackList(chartDispPropsVo
                                            .getChartId(), trackLimitSize)
                                            .getData()
                            ).orElseThrow(() -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA));


                    chartTrackDto.makeTrackDispSn();

                    chartDtoArray[0] = ChartDto.from(chartDispPropsVo, chartTrackDto);

                }
        );

        return chartDtoArray[0];
    }

    private Optional<DispPropsImageDto> getChartBackgroundImage(Long chartId, OsType osType){
        return Optional.ofNullable(imageReadService.getChartBackgroundImage(chartId, osType));
    }
}
