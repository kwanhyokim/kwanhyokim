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
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

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
     * 멤버 No
     */
    @ApiModelProperty(value = "멤버 번호")
    private Long memberNo;

    /**
     * 캐릭터 No
     */
    @ApiModelProperty(value = "캐릭터 번호")
    private Long characterNo;

    /**
     * 시청 횟수
     */
    @ApiModelProperty(value = "시청 횟수")
    private Long watchCount;

    /**
     * 마지막 시청 일시
     */
    @ApiModelProperty(value = "마지막 시청 일시")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date lastWatchDateTime;

    /**
     * 영상 ID
     */
    @ApiModelProperty(value = "VIDEO ID")
    @JsonProperty("id")
    protected Long videoId;

    /**
     * 영상 타이틀
     */
    @ApiModelProperty(value = "VIDEO 제목")
    @JsonProperty(value = "videoNm")
    String title;

    /**
     * 영상 서브 타이틀
     */
    @ApiModelProperty(value = "VIDEO 부제목")
    @JsonProperty(value = "videoSubtitle")
    String subtitle;

    /**
     * 영상 타입
     * 영상 유형 - TEASER(티저), MV(뮤직비디오), LIVE(라이브), INTERVIEW(인터뷰), ETC(혼합)
     */
    @ApiModelProperty(value = "영상 유형 - TEASER(티저), MV(뮤직비디오), LIVE(라이브), INTERVIEW(인터뷰), ETC(기타)")
    String videoType;

    /**
     * 영상 등급
     * ALL(전체), 12_OVER(12세이상), 15_OVER(15세이상), 19_OVER(19세이상), ADULT(청소년관람불가), NOT_RATING(등급없음), RESTRICT(영상노출제한)
     */
    @ApiModelProperty(value = "영상 등급 - ALL(전체), 12_OVER(12세이상), 15_OVER(15세이상), 19_OVER(19세이상), ADULT(청소년관람불가), NOT_RATING(등급없음), RESTRICT(영상노출제한)")
    @JsonProperty(value = "mediaRatingType")
    String rating;

    /**
     * 인기도
     */
    @JsonProperty(value = "videoPopularity")
    Long popularity;

    /**
     * 영상 런닝 타임
     */
    @ApiModelProperty(value = "영상 런닝 타임")
    String playTm;

    /**
     * 권리사 ID
     */
    @ApiModelProperty(value = "권리사 ID")
    Long agencyId;

    /**
     * 전시 여부
     */
    @ApiModelProperty(value = "전시 여부")
    YnType displayYn;

    /**
     * 발매 일자
     */
    @ApiModelProperty(value = "발매 일자")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    @JsonProperty(value = "videoReleaseDt")
    Date releaseDateTime;

    /**
     * 서비스 무료 여부
     */
    @ApiModelProperty(value = "서비스 무료 여부")
    @JsonProperty("svcFreeYn")
    YnType freeYn;

    /**
     * 스트리밍 서비스 가능 여부
     */
    @ApiModelProperty(value = "스트리밍 서비스 가능 여부")
    @JsonProperty("svcStreamingYn")
    YnType streamingYn;

    /**
     * 동영상 대표 아티스트 목록
     */
    @ApiModelProperty(value = "동영상 대표 아티스트 목록")
    @JsonProperty("artistList")
    List<VideoVo.VideoArtistVo> representativeArtists;

    /**
     * 동영상 썸네일 이미지 목록
     */
    @ApiModelProperty(value = "동영상 썸네일 이미지 목록")
    @JsonProperty("thumbnailImageList")
    List<ThumbnailImageVo> thumbnails;

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

    public static MostWatchedVideoVo mock() {
        return MostWatchedVideoVo.builder()
                .memberNo(1000000L)
                .characterNo(2000000L)
                .videoId(1000L)
                .title("다니엘 뮤직 비디오")
                .subtitle("다니엘 뮤직 비디오(서브타이틀)")
                .videoType("MUSIC_VIDEO")
                .rating("15_OVER")
                .playTm("03:40")
                .agencyId(1234L)
                .displayYn(YnType.Y)
                .releaseDateTime(new Date())
                .freeYn(YnType.Y)
                .streamingYn(YnType.Y)
                .representativeArtists(Lists.newArrayList(VideoVo.VideoArtistVo.builder().id(100L).name("Daniel").build()))
                .thumbnails(Lists.newArrayList(ThumbnailImageVo.builder().height(100).width(100).url("https://i.ytimg.com/vi/m8MfJg68oCs/hqdefault.jpg").build()))
                .createDtime(new Date())
                .updateDtime(new Date())
                .build();
    }
}
