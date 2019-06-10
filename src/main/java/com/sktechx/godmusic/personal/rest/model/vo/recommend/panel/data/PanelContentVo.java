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

import java.util.Date;
import java.util.List;

import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import lombok.Data;

/**
 * 설명 : 추천 패널에서 사용할 공통 컨텐츠
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 20.
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PanelContentVo {
    private Object id;
    private CodeEnum type;
    private Integer renewTrackCount;

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

    private YnType renewYn;

    private GenreVo genre;

    @JsonIgnore
    private OsType osType;

    // WEB 에서 추천 아이디가 너무 길 경우 Number 타입은 정확하게 변환이 안되어 web 요청일 경우 String 타입으로 내려줌
    public Object getId(){
        if(ObjectUtils.isEmpty(osType) || !OsType.WEB.equals(osType)){
            return id;
        }else{
            return String.valueOf(id);
        }
    }
}
