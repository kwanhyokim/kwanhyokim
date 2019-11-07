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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.domain.type.MediaRatingType;
import com.sktechx.godmusic.personal.common.util.DateUtil;
import com.sktechx.godmusic.personal.rest.model.dto.MostWatchedVideoDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 10. 01.
 */
@Data
@Builder
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class MostWatchedVideoVo {

    /**
     * 캐릭터 No
     */
    @ApiModelProperty(value = "캐릭터 번호")
    private Long characterNo;

    /**
     * 시청 횟수
     */
    @ApiModelProperty(value = "시청 횟수")
    private Integer watchCount;

    /**
     * 마지막 시청 일시
     */
    @ApiModelProperty(value = "마지막 시청 일시")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date lastWatchDateTime;

    /**
     * 생성일
     */
    @ApiModelProperty(value = "생성일")
    @JsonProperty("createDateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    Date createDtime;

    /**
     * 수정일
     */
    @ApiModelProperty(value = "수정일")
    @JsonProperty("updateDateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    Date updateDtime;

    /**
     * 영상 메타 정보
     */

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
    private Date videoReleaseDt;

    @ApiModelProperty(value = "영상 시청 연령 타입")
    private MediaRatingType mediaRatingType;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date dispStartDtime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date dispEndDtime;

    @ApiModelProperty(value = "전시 여부")
    private YnType displayYn;

    public boolean exhibitable() {
        if (this.displayYn == YnType.N || this.dispStartDtime == null || this.dispEndDtime == null) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = this.dispStartDtime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endTime = this.dispEndDtime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return this.displayYn == YnType.Y
                && now.isAfter(startTime) && now.isBefore(endTime);
    }

    public static MostWatchedVideoVo with(MostWatchedVideoDto watch, VideoVo video) {
        return MostWatchedVideoVo.builder()
                .characterNo(watch.getCharacterNo())
                .watchCount(watch.getWatchCount())
                .lastWatchDateTime(watch.getLastWatchDateTime())
                .videoId(video.getVideoId())
                .agencyId(video.getAgencyId())
                .videoPopularity(video.getVideoPopularity())
                .videoFileUpdateDtime(video.getVideoFileUpdateDtime())
                .videoNm((video.getVideoNm()))
                .videoSubtitle(video.getVideoSubtitle())
                .playTm(video.getPlayTm())
                .videoReleaseDt(video.getVideoReleaseDt())
                .mediaRatingType(video.getMediaRatingType())
                .mediaRatingTypeStr(video.getMediaRatingTypeStr())
                .videoType(video.getVideoType())
                .videoTypeStr(video.getVideoTypeStr())
                .artistList(video.getArtistList())
                .thumbnailImageList(video.getThumbnailImageList())
                .gridThumbnailImageList(video.getGridThumbnailImageList())
                .svcStreamingYn(video.getSvcStreamingYn())
                .svcFreeYn(video.getSvcFreeYn())
                .representationArtist(video.getRepresentationArtist())
                .displayYn(video.getDisplayYn())
                .build();
    }
}
