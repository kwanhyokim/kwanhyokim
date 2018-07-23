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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sktechx.godmusic.personal.rest.model.dto.GenreDto;
import com.sktechx.godmusic.personal.rest.model.dto.ImageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
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
public class Genre {
    @JsonProperty("id")
    private Long chartId;
    @JsonProperty("name")
    private String chartNm;
    @JsonProperty("imageList")
    private List<AlbumImg> albumImgList;

    public Genre() {
    }

    public Genre(GenreDto genreDto) {
        this.chartId = genreDto.getChnl().getChnlId();
        this.chartNm = genreDto.getChnl().getChnlNm();
        this.albumImgList = genreDto.getChnl().getTrackList().get(0).getAlbum().getImgList().stream()
                .map(AlbumImg::new)
                .collect(Collectors.toList()); // FIXME: stream
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class AlbumImg {
        private String url;
        private int size;

        public AlbumImg() {
        }

        public AlbumImg(ImageDto image) {
            this.url = image.getUrl();
            this.size = image.getSize();
        }
    }

}


