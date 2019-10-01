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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * 설명 :
 *
 * @author N/A
 * @date 2019. 09. 30.
 */
@Getter
@Builder
@ToString
public class VideoThumbnailImageVo {

    @JsonCreator
    public VideoThumbnailImageVo(@JsonProperty("width") int width, @JsonProperty("height") int height, @JsonProperty("url") String url){
        this.width = width;
        this.height = height;
        this.url = url;
    }

    @ApiModelProperty(value = "동영상 썸네일 폭")
    private int width;
    @ApiModelProperty(value = "동영상 썸네일 높이")
    private int height;

    @ApiModelProperty(value = "동영상 썸네일 URL")
    private String url;

}
