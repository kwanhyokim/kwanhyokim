/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.service.image;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.domain.constant.RedisKeyConstant;
import com.sktechx.godmusic.personal.rest.model.dto.ImageManagementDto;
import com.sktechx.godmusic.personal.rest.model.dto.chart.DispPropsImageDto;
import com.sktechx.godmusic.personal.rest.repository.ImageManagementMapper;

/**
 * 설명 : 어드민에서 관리되는 이미지를 조회하는 서비스
 *
 */

@Service
public class ImageReadServiceImpl implements ImageReadService {

    @Autowired
    ImageManagementMapper imageManagementMapper;

    @Autowired
    RedisService redisService;

    // 일반 차트 배경 이미지 조회
    @Override
    public DispPropsImageDto getChartBackgroundImage(Long chartId, OsType osType) {
        DispPropsImageDto dispPropsImageDto = redisService.getWithPrefix(
                String.format(RedisKeyConstant.PERSONAL_CHART_BACKGROUND_IMAGE_KEY, chartId,
                        osType)
                , DispPropsImageDto.class
        );

        if( dispPropsImageDto != null){
            return dispPropsImageDto;
        }

        dispPropsImageDto = Optional.ofNullable(
                imageManagementMapper.selectChartBackgroundImageList(chartId, osType)
            )
                .orElseGet(Collections::emptyList)
                .stream()
                .findFirst()
            .orElse(null);

        redisService.setWithPrefix(
                String.format(RedisKeyConstant.PERSONAL_CHART_BACKGROUND_IMAGE_KEY, chartId,
                        osType)
                , dispPropsImageDto
        );

        return dispPropsImageDto;

    }

    // 추천 차트 배경 이미지 조회
    @Override
    public DispPropsImageDto getMixChartBackgroundImage(Long svcGenreId,
            Long chartId, OsType osType) {

        // 추천 차트 장르별 캐쉬
        DispPropsImageDto dispPropsImageDto = redisService.getWithPrefix(
                String.format(RedisKeyConstant.PERSONAL_PRICHART_BACKGROUND_IMAGE_KEY, svcGenreId,
                        osType)
                ,
                DispPropsImageDto.class
        );

        if(dispPropsImageDto == null){
            dispPropsImageDto =
                    imageManagementMapper.selectMixChartBackgroundImageList(svcGenreId, osType);


            if(dispPropsImageDto != null) {
                redisService.setWithPrefix(
                        String.format(RedisKeyConstant.PERSONAL_PRICHART_BACKGROUND_IMAGE_KEY,
                            svcGenreId, osType)
                        , dispPropsImageDto
                        , 3600);

                if(dispPropsImageDto.getImgList() != null) {
                    Collections.shuffle(dispPropsImageDto.getImgList());

                    dispPropsImageDto.setImgList(
                            dispPropsImageDto.getImgList().stream().limit(1).collect(
                                    Collectors.toList()
                            )
                    );
                }
            }
        }

        // 장르별 이미지 없을 경우, 디폴트 DB 조회 처리
        if(dispPropsImageDto == null){
            dispPropsImageDto = redisService.getWithPrefix(
                    String.format(RedisKeyConstant.PERSONAL_PRICHART_DEFAULT_BACKGROUND_IMAGE_KEY
                            , chartId)
                    ,
                    DispPropsImageDto.class
            );
        }

        if(dispPropsImageDto == null){
            dispPropsImageDto = imageManagementMapper.selectDefaultMixChartBackgroundImageList(chartId);

            if(dispPropsImageDto != null) {
                redisService.setWithPrefix(
                        String.format(RedisKeyConstant.PERSONAL_PRICHART_DEFAULT_BACKGROUND_IMAGE_KEY,
                        chartId)
                        , dispPropsImageDto
                        , 3600);
            }
        }

        // DB에도 디폴트 이미지 없을 경우
        if(dispPropsImageDto == null) {
            dispPropsImageDto = DispPropsImageDto.getDefault(chartId);
        }

        return dispPropsImageDto;

    }

    // 선호 장르 이미지 조회
    @Override
    public List<ImageManagementDto> getImageManagementList(String imgContentType,
            Long imgContentId, String shortcutType) {

        return imageManagementMapper.selectImageManagementList(imgContentType, imgContentId, shortcutType);
    }
}
