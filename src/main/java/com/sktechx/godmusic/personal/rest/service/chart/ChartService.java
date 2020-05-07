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
import java.util.function.Predicate;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartDispPropsDto;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartDispPropsImageDto;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartDto;
import com.sktechx.godmusic.personal.rest.model.vo.chart.ChartDispPropsVo;
import com.sktechx.godmusic.personal.rest.model.vo.chart.ChartVo;
import com.sktechx.godmusic.personal.rest.repository.ChartMapper;

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

    default ChartDispPropsVo getPreferDisp(Predicate<ChartDispPropsDto> predicate, OsType osType
            , Boolean useNewImgUrl){

        ChartDispPropsDto chartDispPropsDto =
                Optional.ofNullable(
                        getChartMapper().selectPreferDisp()
                ).orElseGet( ChartDispPropsDto::defaultChartDispPropsList )
                        .stream()
                        .filter( predicate )
                        .findFirst()
                        .orElseThrow( () -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA))

                ;

        return ChartDispPropsVo.from(

                chartDispPropsDto
                ,

                (useNewImgUrl ?
                        getChartMapper().selectPreferDispNameAndMixChartBgImage()
                        :
                        getChartMapper().selectPreferDispNameAndChartBgImage(osType)
                )

                .stream()
                .filter( chartDispPropsImageDto -> chartDispPropsImageDto.getChartId().equals(chartDispPropsDto.getChartId()))
                .findFirst()
                .orElseGet( () -> ChartDispPropsImageDto.builder().chartId(1L).build())


        );

    }

}
