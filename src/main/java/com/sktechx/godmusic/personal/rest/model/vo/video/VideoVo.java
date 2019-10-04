/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.model.vo.video;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.sktechx.godmusic.lib.domain.code.YnType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

@SuppressWarnings("WeakerAccess")
@Getter
@Builder
@ToString(callSuper = true)
public class VideoVo {

    @ApiModelProperty(value = "동영상 아이디")
    @JsonProperty("id")
    private Long videoId;

    @ApiModelProperty(value = "권리사 아이디")
    private Long agencyId;

    @ApiModelProperty(value = "동영상 인기도(조회수)")
    private Long videoPopularity;

    @ApiModelProperty(value = "동영상 파일 수정 일시")
    private Long videoFileUpdateDtime;

    @ApiModelProperty(value = "동영상 명")
    private String videoNm;

    @ApiModelProperty(value = "동영상 부제목")
    private String videoSubtitle;

    @ApiModelProperty(value = "재생 시간")
    private String playTm;

    @ApiModelProperty(value = "동영상 발매일(yyyy-mm-dd hh:mm:ss)")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime videoReleaseDt;

    @ApiModelProperty(value = "영상 시청 연령 타입")
    private String mediaRatingType;

    @ApiModelProperty(value = "영상 시청 연령 타입 Description")
    private String mediaRatingTypeStr;

    @ApiModelProperty(value = "동영상 타입")
    private String videoType;

    @ApiModelProperty(value = "동영상 타입 Description")
    private String videoTypeStr;

    @ApiModelProperty(value = "동영상 대표 아티스트 목록")
    private List<VideoArtistVo> artistList;

    @ApiModelProperty(value = "동영상 썸네일 목록")
    private List<VideoThumbnailImageVo> thumbnailImageList;

    @ApiModelProperty(value = "동영상 구간 스냅샷 목록")
    private List<VideoGridThumbnailImageVo> gridThumbnailImageList;

    @ApiModelProperty(value = "서비스 스트리밍 여부")
    private YnType svcStreamingYn;

    @ApiModelProperty(value = "서비스 무료 여부")
    private YnType svcFreeYn;

    @ApiModelProperty(value = "동영상 대표 아티스트")
    VideoArtistVo representationArtist;

    public static VideoVo mock() {

        VideoVo mock = VideoVo.builder()
                .videoId(1000L)
                .videoNm("다니엘 뮤직 비디오")
                .videoSubtitle("다니엘 뮤직 비디오(서브타이틀)")
                .videoType("MV")
                .mediaRatingType("15_OVER")
                .playTm("03:40")
                .agencyId(1234L)
                .videoReleaseDt(LocalDateTime.now())
                .svcFreeYn(YnType.Y)
                .svcStreamingYn(YnType.Y)
                .representationArtist(VideoArtistVo.builder().artistId(100L).artistNm("Daniel").build())
                .artistList(Lists.newArrayList(VideoArtistVo.builder().artistId(100L).artistNm("Daniel").build()))
                .thumbnailImageList(Lists.newArrayList(VideoThumbnailImageVo.builder().width(100).height(100).url("https://i.ytimg.com/vi/m8MfJg68oCs/hqdefault.jpg").build()))
                .videoFileUpdateDtime(System.currentTimeMillis())
                .build();

        return mock;
    }
}

