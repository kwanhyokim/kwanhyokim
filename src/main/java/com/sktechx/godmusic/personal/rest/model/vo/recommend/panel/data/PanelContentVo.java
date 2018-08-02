/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 설명 : 추천 패널에서 사용할 공통 컨텐츠
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 20.
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PanelContentVo {
    private Long id;
    private CodeEnum type;
    private Integer updateCount;

    private List<TrackDto> trackList;
    private Integer trackCount;
    private List<ArtistDto> artistList;
    private Integer artistCount;

    @JsonProperty("createDateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date createDtime;

    @JsonProperty("updateDateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date updateDtime;

    private GenreVo genre;
}
