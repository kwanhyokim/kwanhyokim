/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.model.vo;

import com.fasterxml.jackson.annotation.*;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.rest.model.dto.AlbumDto;
import com.sktechx.godmusic.personal.rest.model.dto.LastListenHistoryDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 남재우(Peter)/njw0619@sk.com
 * @date 2018.08.16
 */

@Data
@Builder
@EqualsAndHashCode(of = "listenId")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "id", "name", "trackCount", "updatedTrackCount", "lastListenDateTime", "imgList", "artistName"})
@ApiModel(value = "최근들은 컨텐츠")
public class LastListenHistoryVo {

    @JsonIgnore
    private String listenId;

    @ApiModelProperty(required = true, example = "MY", value = "컨텐츠 타입", notes = "MY:MY플레이리스트, DJ:DJ채널, ALBUM:앨범")
    @JsonProperty("type")
    private String listenType;

    @ApiModelProperty(required = true, example = "1", value = "아이디")
    @JsonProperty("id")
    private String listenTypeId;

    @ApiModelProperty(required = true, example = "월간 인기 차트", value = "제목")
    @JsonProperty("name")
    private String contentTitle;

    private Long trackCount;

    @ApiModelProperty(required = true, example = "2018-07-16 13:52:23", value = "최근 들은 시간")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    @JsonProperty("lastListenDateTime")
    private Date lastListenDtime;

    @ApiModelProperty(value = "이미지 리스트")
    private List<ImageInfo> imgList;

    @ApiModelProperty(example = "윤종신", value = "아티스트 명(앨범일 경우만 데이터 존재)")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AlbumDto album;

    private Integer renewTrackCount;

    private YnType renewYn;

    public static LastListenHistoryVo from(LastListenHistoryDto other) {
        return LastListenHistoryVo.builder()
                .listenId(other.getListenId())
                .listenType(other.getListenType())
                .listenTypeId(String.valueOf(other.getListenTypeId()))
                .contentTitle(other.getContentTitle())
                .trackCount(other.getTrackCount())
                .lastListenDtime(other.getLastListenDtime())
                .imgList(other.getImgList())
                .album(other.getAlbum())
                .renewTrackCount(other.getRenewTrackCnt())
                .renewYn(other.getRenewYn())
                .build();
    }
}
