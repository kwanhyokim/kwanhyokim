/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.model.vo.video;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.domain.type.MediaRatingType;
import com.sktechx.godmusic.personal.common.domain.type.VideoType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString(callSuper = true)
public class VideoVo {

    @ApiModelProperty(value = "동영상 아이디")
    @JsonProperty("id")
    private Long videoId;
    private Long agencyId;

    @ApiModelProperty(value = "동영상 인기도(조회수)")
    private Long videoPopularity;

    @ApiModelProperty(value = "동영상 명")
    private String videoNm;
    @ApiModelProperty(value = "동영상 부제목")
    private String videoSubtitle;

    @ApiModelProperty(value = "재생 시간")
    private String playTm;

    @ApiModelProperty(value = "동영상 발매일")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate videoReleaseDt;

    @ApiModelProperty(value = "영상 시청 연령 타입")
    private MediaRatingType mediaRatingType;

    @ApiModelProperty(value = "동영상 타입 코드")
    private VideoType videoType;
    @ApiModelProperty(value = "동영상 타입 Description")
    private String videoTypeValue;

    @ApiModelProperty(value = "동영상 대표 아티스트")
    private VideoArtistVo representationArtist;
    @ApiModelProperty(value = "동영상 대표 아티스트 목록")
    private List<VideoArtistVo> artistList;

    @ApiModelProperty(value = "동영상 썸네일 목록")
    private List<VideoThumbnailImageVo> thumbnailImageList;

    @ApiModelProperty(value = "서비스 스트리밍 여부")
    private YnType svcStreamingYn;
    @ApiModelProperty(value = "서비스 무료 여부")
    private YnType svcFreeYn;


}

