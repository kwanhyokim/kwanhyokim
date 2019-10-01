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

import java.util.List;

import com.sktechx.godmusic.personal.common.domain.type.MediaRatingType;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.video.VideoPanel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 설명 : 추천 트랙 DTO
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 19.
 */
@Data
public class VideoDto {

    @ApiModelProperty(value = "비디오 아이디")
    private Long videoId;

    @ApiModelProperty(value = "비디오 제목")
    private String videoNm;

    @ApiModelProperty(value = "영상등급")
    private MediaRatingType mediaRatingType;

    @ApiModelProperty(value = "재생시간")
    private String playTm;

    @ApiModelProperty(value = "썸네일 이미지 url")
    private String videoImgUrl;

    @ApiModelProperty(value = "동영상 연관 아티스트 정보")
    private List<ArtistDto> artistList;


    public VideoPanel convertToVideoPanel(){
        return VideoPanel.builder()
                .videoId(this.videoId)
                .videoNm(this.videoNm)
                .mediaRatingType(this.mediaRatingType)
                .playTm(this.playTm)
                .build();
    }
}
