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

package com.sktechx.godmusic.personal.rest.model.vo.preference;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sktechx.godmusic.personal.rest.model.dto.AlbumDto;
import com.sktechx.godmusic.personal.rest.model.dto.ChartDto;
import com.sktechx.godmusic.personal.rest.model.dto.GenreDto;
import com.sktechx.godmusic.personal.rest.model.dto.ImageDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 설명 : 선호 장르 응답 VO
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 7. 19.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Chart {
    @ApiModelProperty(value = "차트아이디")
    @JsonProperty("id")
    private Long chartId;

    @ApiModelProperty(value = "차트명")
    @JsonProperty("name")
    private String chartNm;

    @ApiModelProperty(value = "앨범")
    private Album album;

    @ApiModelProperty(value = "차트별 특랙목록")
    @JsonProperty("list")
    private List<ChartMusicContent> chartMusicCongtentList;

    public Chart() {}

    public Chart(ChartDto chartDto) {
        this.chartId = chartDto.getChartId();
        this.chartNm = chartDto.getChartNm();

        album = new Album(chartDto.getTrackList().get(0).getAlbum()); // FIXME:
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class Album {
        private Long albumId;

        @JsonProperty("imageList")
        private List<AlbumImg> albumImgList;

        public Album(AlbumDto albumDto) {
            this.albumId = albumDto.getAlbumId();
            albumImgList = albumDto.getImgList().stream().map(AlbumImg::new).collect(Collectors.toList());
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class AlbumImg {
        private String url;
        private int size;

        public AlbumImg(ImageDto image) {
            this.url = image.getUrl();
            this.size = image.getSize();
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class ChartMusicContent {
        @JsonProperty("id")
        private Long trackId;
        @JsonProperty("name")
        private String trackNm;
        @JsonProperty("priority")
        private Integer trackSn;
        @JsonProperty("beforePriority")
        private Integer trackBfSn;

        public ChartMusicContent(TrackDto trackDto) {
            this.trackId = trackDto.getTrackId();
            this.trackNm = trackDto.getTrackNm();
            this.trackSn = trackDto.getTrackSn();
            this.trackBfSn = trackDto.getTrackBfSn();
        }
    }

}


