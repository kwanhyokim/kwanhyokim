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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.util.DateUtil;
import lombok.Data;

/**
 * 설명 : 트랙 DTO
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class TrackDto {
    @JsonProperty("id")
    private Long trackId;
    @JsonProperty("name")
    private String trackNm;

    @JsonProperty("priority")
    private Integer trackSn;

    @JsonProperty("beforePriority")
    private Integer trackBfSn;

    private String holdbackYn;

    private YnType adultAuthYn;

    private String playTime;

    private String titleYn;

    private Long agencyId;

    @JsonProperty("representationArtist")
    private ArtistDto artist;
    private List<ArtistDto> artistList;

    private AlbumDto album;

    @JsonProperty("createDateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date createDtime;

    @JsonProperty("updateDateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date updateDtime;

    @JsonProperty("fileUpdateDateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date fileUpdateDateTime;

    @JsonProperty("renewDateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date renewDtime;

    private RankDto rank;

    private YnType displayYn;

    public YnType getRenewYn(){
        if(renewDtime!= null &&
                chnlRenewDtime!=null &&
                    renewDtime.compareTo(chnlRenewDtime) == 0 ){
            Calendar cal = Calendar.getInstance();
            cal.setTime(renewDtime);
            cal.add(Calendar.DATE, 1);
            if( new Date().getTime() < cal.getTime().getTime() ) {
                return YnType.Y;
            }
        }
        return YnType.N;
    }


    @JsonIgnore
    private Date chnlRenewDtime;

    private YnType svcFlacYn;

    private YnType svcStreamingYn;

    private YnType svcDrmYn;

    private YnType freeYn;

    @JsonIgnore
    public boolean isNotStreamable() {
        return YnType.N == this.svcStreamingYn;
    }

    @JsonIgnore
    public boolean isNotDownloadable() {
        return YnType.N == this.svcDrmYn;
    }

    /**
     * Note. fileUpdateDateTime field 는 personal-mgo 와 Feign 통신시, MyBatis 로부터 DB fetch 시에 모두 이용되는 필드이기 때문에
     *       해당 method 를 선언하여 MyBatis 에서 데이타 맵핑시 본 setter method 를 이용하도록 선언함.
     */
    public void setModFileUpdateDtime(long epochSecond) {
        if(epochSecond > 0) {
            this.fileUpdateDateTime = DateUtil.asDate(epochSecond);
        }
    }
}

