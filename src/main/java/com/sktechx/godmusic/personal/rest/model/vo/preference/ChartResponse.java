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
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sktechx.godmusic.personal.common.domain.domain.HomeContentType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 7. 24.
 */
@Data
@JsonPropertyOrder({"type", "list"})
public class ChartResponse<T> {
    @ApiModelProperty("선호 장르/아티스트 목록")
    List<T> list;

    @JsonProperty("type")
    HomeContentType homeContentType;

    public ChartResponse(List<T> list, HomeContentType homeContentType) {
        this.list = list;
        this.homeContentType = homeContentType;
    }
}
