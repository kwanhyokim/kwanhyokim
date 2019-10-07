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
import lombok.*;

/**
 * 설명 :
 *
 * @author N/A
 * @date 2019. 09. 30.
 */
@Getter
@Builder
public class VideoGridThumbnailImageVo {

    @ApiModelProperty(value = "동영상 구간 스냅샷 폭")
    private Integer width;

    @ApiModelProperty(value = "동영상 구간 스냅샷 높이")
    private Integer height;

    @ApiModelProperty(value = "동영상 구간 스냅샷 URL")
    private String url;

}
