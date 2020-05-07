/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.model.vo.chart;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartDispPropsDto;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartDispPropsImageDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import lombok.Builder;
import lombok.Data;

/**
 * 설명 : 차트 전시 정보 dto
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2020-04-17
 */

@Builder
@Data
public class ChartDispPropsVo {
    private Long chartId;
    private String chartNm;
    private String chartdispNm;
    private String dispPropsType;

    private List<ImageInfo> imgList;


    public static ChartDispPropsVo from (ChartDispPropsDto chartDispPropsDto,
            ChartDispPropsImageDto chartDispPropsImageDto){


        if(chartDispPropsDto == null || chartDispPropsImageDto == null){
            return ChartDispPropsVo.builder().build();
        }

        List<ImageInfo> imageInfoList = chartDispPropsImageDto.getImgList();

        if(CollectionUtils.isEmpty(imageInfoList)) {
            imageInfoList = new ArrayList<>(1);

            if (chartDispPropsDto.getChartId() == 1L) {
                imageInfoList.add(new ImageInfo(750L, "https://www3.music-flo.com/mmate/feapi/img/display/genre_rc/temp/main_panel.jpg"));
            } else {
                imageInfoList.add(new ImageInfo(750L, "https://www3.music-flo.com/mmate/feapi/img/display/genre_rc/temp/kids_panel.jpg"));
            }
        }

        return ChartDispPropsVo.builder()
                .chartId(chartDispPropsDto.getChartId())
                .chartNm(chartDispPropsDto.getChartNm())
                .chartdispNm(chartDispPropsDto.getChartdispNm())
                .dispPropsType(chartDispPropsDto.getDispPropsType())
                .imgList(imageInfoList)
                .build();
    }

}
