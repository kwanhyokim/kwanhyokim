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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.rest.model.dto.ImageManagementDto;
import com.sktechx.godmusic.personal.rest.model.dto.chart.DispPropsImageDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.repository.ImageManagementMapper;

/**
 * 설명 : 어드민에서 관리되는 이미지를 조회하는 서비스
 *
 */

@Service
public class ImageReadServiceImpl implements ImageReadService {

    @Autowired
    ImageManagementMapper imageManagementMapper;

    // 일반 차트 배경 이미지 조회
    @Override
    public DispPropsImageDto getChartBackgroundImage(Long chartId, OsType osType) {
        return
            Optional.ofNullable(
                imageManagementMapper.selectChartBackgroundImageList(chartId, osType)
            )
                .orElseGet(Collections::emptyList)
                .stream()
                .findFirst()
            .orElse(null);

    }

    // 추천 차트 배경 이미지 조회
    @Override
    public DispPropsImageDto getMixChartBackgroundImage(Long svcGenreId,
            Long chartId, OsType osType) {

        DispPropsImageDto[] resultDispPropsImageDto = new DispPropsImageDto[1];

        Optional.ofNullable(
                imageManagementMapper.selectMixChartBackgroundImageList(svcGenreId, osType)
        )
        .ifPresent(
                dispPropsImageDtos -> {
                    if(!CollectionUtils.isEmpty(dispPropsImageDtos)) {

                        // 이미지 셔플
                        resultDispPropsImageDto[0] = dispPropsImageDtos.get(0);

                        if(resultDispPropsImageDto[0].getImgList() != null) {
                            Collections.shuffle(resultDispPropsImageDto[0].getImgList());

                            resultDispPropsImageDto[0].setImgList(
                                    resultDispPropsImageDto[0].getImgList().stream().limit(1).collect(
                                            Collectors.toList())
                            );
                        }

                    }
                }
        );

        if(resultDispPropsImageDto[0] == null) {
            Optional.ofNullable(
                    imageManagementMapper.selectDefaultMixChartBackgroundImageList(chartId)
            )
            .ifPresent(
                dispPropsImageDtos -> {
                    if (!CollectionUtils.isEmpty(dispPropsImageDtos)) {
                        resultDispPropsImageDto[0] = dispPropsImageDtos.get(0);
                    }
                }
            );
        }

        if(resultDispPropsImageDto[0] == null) {
            List<ImageInfo> imageInfoList = new ArrayList<>(1);

            if(chartId == 1L) {
                imageInfoList.add(new ImageInfo(750L,
                        "https://www3.music-flo.com/mmate/feapi/img/display/genre_rc/temp/main_panel.jpg"));
                resultDispPropsImageDto[0] = DispPropsImageDto.builder().id(1L).imgList(imageInfoList).build();
            }else {
                imageInfoList.add(new ImageInfo(750L,
                        "https://www3.music-flo.com/mmate/feapi/img/display/genre_rc/temp/kids_panel.jpg"));
                resultDispPropsImageDto[0] = DispPropsImageDto.builder().id(1L).imgList(imageInfoList).build();
            }
        }

        return resultDispPropsImageDto[0];

    }

    // 선호 장르 이미지 조회
    @Override
    public List<ImageManagementDto> getImageManagementList(String imgContentType,
            Long imgContentId, String shortcutType) {

        return imageManagementMapper.selectImageManagementList(imgContentType, imgContentId, shortcutType);
    }
}
