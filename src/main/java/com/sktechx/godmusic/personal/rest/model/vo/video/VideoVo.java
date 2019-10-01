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
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.domain.type.MediaRatingType;
import com.sktechx.godmusic.personal.common.domain.type.VideoType;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.video.VideoPanel;
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

    public VideoPanel convertToVideoPanel(){
        return VideoPanel.builder()
                .videoId(this.videoId)
                .videoNm(this.videoNm)
                .thumbnailImageList(this.thumbnailImageList)
                .artistList(this.artistList)
                .mediaRatingType(this.mediaRatingType)
                .playTm(this.playTm)
                .build();
    }

    private EnumMap<MappableMetaType, MappableMeta> mappedMeta;

    public enum MappableMetaType {

        album,
        track

    }


    public interface MappableMeta {
    }

    @Getter
    @Builder
    @ToString
    public static class TrackMappableMeta implements MappableMeta {

        private Long id;
        private String lyrics;

        public YnType hasLyricsYn() {
            return lyrics == null ? YnType.N : YnType.Y;
        }

    }

    @Getter
    @Builder
    @ToString
    public static class AlbumMappableMeta implements MappableMeta {

        private Long id;

    }

    public static class VideoVoBuilder {

        public VideoVoBuilder videoType(VideoType videoType) {

            if (videoType != null) {

                this.videoType = videoType;
                this.videoTypeValue = videoType.getValue();

            }

            return this;

        }

        public VideoVoBuilder representationArtist() {

            this.representationArtist = new VideoArtistVo.VideoArtistVoBuilder().artistId(123L).artistName("아무개").build();
            return this;

        }

        public VideoVoBuilder artistList() {

            this.artistList = Arrays.asList(
                    new VideoArtistVo.VideoArtistVoBuilder().artistId(456L).artistName("홍길동").build(),
                    new VideoArtistVo.VideoArtistVoBuilder().artistId(789L).artistName("홍길순").build()
            );

            return this;

        }

        public VideoVoBuilder thumbnailImageList() {

            this.thumbnailImageList = Arrays.asList(
                    new VideoThumbnailImageVo.VideoThumbnailImageVoBuilder()
                            .url("https://cdn.music-flo.com/image/thumbnail/LIVE/70/00/00/029/20190808165311/029000070_1.jpg")
                            .width(854)
                            .height(480)
                            .build(),
                    new VideoThumbnailImageVo.VideoThumbnailImageVoBuilder()
                            .url("https://cdn.music-flo.com/image/thumbnail/LIVE/70/00/00/029/20190808165311/029000070_1.jpg")
                            .width(854)
                            .height(480)
                            .build()
            );

            return this;

        }

        public VideoVoBuilder mappedMeta() {

            this.mappedMeta = new EnumMap<>(MappableMetaType.class);
            this.mappedMeta.put(MappableMetaType.album, new AlbumMappableMeta.AlbumMappableMetaBuilder().id(1L).build());
            this.mappedMeta.put(MappableMetaType.track, new TrackMappableMeta.TrackMappableMetaBuilder().id(1L).lyrics("남들과는 다르게 누구보다 빠르게").build());

            return this;

        }

        public static VideoVoBuilder newBuilderFromDto() {

            return new VideoVoBuilder()
                    .videoId(12345678L)
                    .videoPopularity(777L)
                    .videoNm("비디오 명")
                    .videoSubtitle("비디오 부제목")
                    .playTm("03:59")
                    .videoReleaseDt(LocalDate.now())
                    .mediaRatingType(MediaRatingType.AGE_19_OVER)
                    .videoType(VideoType.MV)
                    .representationArtist()
                    .artistList()
                    .svcFreeYn(YnType.Y)
                    .svcStreamingYn(YnType.Y)
                    .thumbnailImageList();

        }

    }


}

