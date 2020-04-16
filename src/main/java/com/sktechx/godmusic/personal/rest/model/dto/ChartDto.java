/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sktechx.godmusic.personal.common.domain.type.ChartType;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import lombok.Builder;
import lombok.Data;

/**
 * 설명 : 차트 DTO
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 22.
 */
@Builder
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ChartDto {
    @JsonProperty("id")
    private Long chartId;

    @JsonProperty("name")
    private String chartNm;

    private Long svcContentId;

    private ChartType chartType;

    private List<TrackDto> trackList;
    @JsonProperty("createDateTime")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date createDtime;

    @JsonProperty("updateDateTime")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date updateDtime;

    private Date dispStartDtime;

    private Integer trackCount;


    private List<ImageInfo> imgList;

    public void setImgList(List<ImageInfo> imgList) {

        if (imgList != null) {
            imgList.sort(null);
        }

        this.imgList = imgList;
    }

    public static ChartDto from (ChartTrackDto chartTrackDto){
        return ChartDto.builder()
                .chartId(chartTrackDto.getId())
                .chartNm(chartTrackDto.getName())
                .chartType(chartTrackDto.getChartType())
                .trackList(chartTrackDto.getTrackList())
                .createDtime(chartTrackDto.getCreateDateTime())
                .updateDtime(chartTrackDto.getUpdateDateTime())
                .dispStartDtime(chartTrackDto.getDispStartDtime())
                .trackCount(chartTrackDto.getTotalCount())
                .imgList(chartTrackDto.getImgList())
                .build();
    }
}
