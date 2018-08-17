/*
 *
 *  * Copyright (c) 2018 SK TECHX.
 *  * All right reserved.
 *  *
 *  * This software is the confidential and proprietary information of SK TECHX.
 *  * You shall not disclose such Confidential Information and
 *  * shall use it only in accordance with the terms of the license agreement
 *  * you entered into with SK TECHX.
 *
 */

package com.sktechx.godmusic.personal.rest.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.domain.type.ChannelType;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 7. 22.
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ChnlDto {
    @JsonProperty("id")
    private Long chnlId;
    @JsonProperty("name")
    private String chnlNm;

    private String chnlDispNm;

    @JsonProperty("type")
    private ChannelType chnlType;

    @JsonIgnore
    private AlbumDto album;

    @JsonProperty("createDateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date createDtime;

    @JsonProperty("updateDateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date updateDtime;

    private List<TrackDto> trackList;
    private Integer trackCount;

    @JsonProperty("renewDateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date renewDtime;

    @JsonProperty("renewTrackCount")
    private Integer renewTrackCnt;
    //채널 용 별도 이미지
    private List<ImageInfo> imgList;

    public YnType getRenewYn(){
        if(renewDtime!= null){
            Calendar cal = Calendar.getInstance();
            cal.setTime(renewDtime);
            cal.add(Calendar.DATE, 1);
            if( new Date().getTime() < cal.getTime().getTime() ) {
                return YnType.Y;
            }
        }
        return YnType.N;
    }

}
