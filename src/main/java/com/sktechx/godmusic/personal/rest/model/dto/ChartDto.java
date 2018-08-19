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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sktechx.godmusic.personal.common.domain.type.ChartType;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 설명 : 차트 DTO
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 22.
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ChartDto {
    @JsonProperty("id")
    private Long chartId;

    @JsonProperty("name")
    private String chartNm;

    private String chartDispNm;

    private ChartType chartType;

    private List<TrackDto> trackList;
    @JsonProperty("createDateTime")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date createDtime;

    @JsonProperty("updateDateTime")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date updateDtime;

    private Integer trackCount;


    private List<ImageInfo> imgList;

}
