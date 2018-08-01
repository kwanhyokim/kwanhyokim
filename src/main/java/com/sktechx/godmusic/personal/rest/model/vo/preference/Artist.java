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
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 7. 30.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
@JsonPropertyOrder({"id", "name", "list"})
public class Artist {

    @ApiModelProperty(value = "아티스트아이디")
    @JsonProperty("id")
    private Long artistId;

    @ApiModelProperty(value = "아티스트명")
    @JsonProperty("name")
    private String artistNm;

    @ApiModelProperty(value = "이미지목록(아티스트)")
    @JsonPropertyOrder("list")
    private List<ArtistImg> albumImgList;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Builder
    @JsonPropertyOrder({"size", "url"})
    public static class ArtistImg {
        @ApiModelProperty(value = "이미지URL")
        private String url;
        @ApiModelProperty(value = "이미지사이즈")
        private Integer size;
    }
}
