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
import java.util.concurrent.TimeUnit;

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
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.chart.TasteMixVo;
import lombok.Builder;
import lombok.Data;

/**
 * 설명 : 추천 패널에서 사용할 공통 컨텐츠
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 20.
 */
@Data
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PanelContentVo {
    private Long id;
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

    private GenreVo genre;

    private YnType renewYn;

    @JsonIgnore
    private OsType osType;

    @JsonIgnore
    private String chartTaste;

    public TasteMixVo getTasteMix(){
        return TasteMixVo.from(chartTaste);
    }

    // WEB 에서 추천 아이디가 javascript가 파싱 가능한 숫자 범위를 넘어가므로, web 요청일 경우 String 타입으로 내려줌
    public Object getId(){
        if(ObjectUtils.isEmpty(osType) || !OsType.WEB.equals(osType)){
            return id;
        }else{
            return String.valueOf(id);
        }
    }

    public YnType getRenewYn(){

        if(!ObjectUtils.isEmpty(this.renewYn)){
            return renewYn;
        }

        Date stdDate = new Date((System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)));
        if(stdDate.before(this.createDtime)){
            return YnType.Y;
        }else{
            return YnType.N;
        }

    }
}
