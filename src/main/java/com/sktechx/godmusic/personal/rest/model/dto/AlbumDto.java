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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.domain.type.AlbumType;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 설명 : 앨범 DTO
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "title", "categoryType", "representationArtist", "artistList", "imgList", "releaseYmd", "genreStyle", "albumTypeStr", "likeYn"})
@ApiModel(value = "앨범")
@EqualsAndHashCode
public class AlbumDto implements Comparable<AlbumDto> {

    @JsonProperty("id")
    @ApiModelProperty(required = true, example = "2834", value = "아이디")
    private Long albumId;

    @JsonProperty("title")
    @ApiModelProperty(required = true, example = "김경호 6집 (The Life)", value = "제목")
    private String albumTitle;

    private String categoryType;

    @JsonProperty("representationArtist")
    @ApiModelProperty(value = "대표 아티스트")
    private ArtistDto artist;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ApiModelProperty(required = true, value = "아티스트 목록")
    private List<ArtistDto> artistList;

    @ApiModelProperty(required = false, value = "앨범 이미지")
    private List<ImageInfo> imgList;

    @ApiModelProperty(required = true, example = "200108", value = "발매년월일")
    private String releaseYmd;

    @ApiModelProperty(required = true, example = "락 발라드,락,헤비 메탈", value = "장르")
    private String genreStyle;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private AlbumType albumType;

    @ApiModelProperty(required = true, example = "정규", value = "앨범 타입")
    private String albumTypeStr;

    @ApiModelProperty(required = true, example = "Y", value = "좋아요")
    private YnType likeYn;

    public String getAlbumTypeStr(){
        if(!ObjectUtils.isEmpty(this.albumType)){
            return this.albumType.getValue();
        }
        return null;
    }

    public ArtistDto getArtist(){
        if(this.artist != null){
            return artist;
        }else{
            if(!CollectionUtils.isEmpty(artistList)){
                return artistList.get(0);
            }
        }
        return null;
    }

    public void setImgList(List<ImageInfo> imgList) {

        if (imgList != null) {
            imgList.sort(null);
        }

        this.imgList = imgList;
    }

    @Override
    public int compareTo(@Nullable AlbumDto o) {
        if (o == null) return -1;
        return Long.compare(this.albumId, o.albumId);
    }

    @JsonIgnore
    public ImageInfo getImageInfoBySize(Long size) {

        if (!CollectionUtils.isEmpty(imgList)) {
            return imgList.stream().filter(imageInfo -> imageInfo.getSize().equals(size)).findFirst().orElse(imgList.get(0));
        }

        return null;
    }
}
