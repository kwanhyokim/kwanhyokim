/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.model.vo.video;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 09. 27.
 */
@Getter
@Builder
public class ThumbnailImageVo {
    /**
     * 이미지 Height
     */
    @ApiModelProperty(value = "이미지 Height")
    private Integer height;

    /**
     * 이미지 Width
     */
    @ApiModelProperty(value = "이미지 Width")
    private Integer width;

    /**
     * 이미지 URL
     */
    @ApiModelProperty(value = "이미지 URL")
    private String url;
}
