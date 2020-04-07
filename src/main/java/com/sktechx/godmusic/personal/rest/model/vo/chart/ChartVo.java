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

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.domain.type.ChartType;
import com.sktechx.godmusic.personal.common.domain.type.PlayListType;
import com.sktechx.godmusic.personal.rest.model.dto.ChartDto;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartTrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendChartTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import lombok.Builder;

/**
 * 설명 : 차트 vo
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2020-03-31
 */

@Builder
public class ChartVo {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    private String playTime;
    private Integer trackCount;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date createDateTime;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date updateDateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date registDateTime;

    private List<ImageInfo> imgList;

    private List<RecommendChartTrackDto> trackList;

    private ChartType chartType;

    @JsonProperty("type")
    private PlayListType playListType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date renewDateTime;

    private YnType renewYn;

    private Integer renewTrackCnt;

    private String description;

    @JsonProperty("renewTrackCount")
    public Integer getRenewTrackCnt() {
        return this.renewYn == YnType.N ? 0 : renewTrackCnt;
    }

    public void setImgList(List<ImageInfo> imgList) {

        if (imgList != null) {
            imgList.sort(null);
        }

        this.imgList = imgList;
    }

    public static ChartVo from (ChartDto chartDto, ChartTrackDto chartTrackDto){
        return ChartVo.builder()
                .id(chartTrackDto.getId())
                .name(chartTrackDto.getName())
                .playListType(chartTrackDto.getType())
                .trackList(chartTrackDto.getTrackList())
                .build();
    }
}


