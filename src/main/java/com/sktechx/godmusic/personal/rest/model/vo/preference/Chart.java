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
 * 설명 : 선호 장르 응답 VO
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 7. 19.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@JsonPropertyOrder({"type", "id", "name", "imgList", "list"})
public class Chart {

    @ApiModelProperty(value = "타입")
    private String type;

    @ApiModelProperty(value = "차트아이디")
    @JsonProperty("id")
    private Long chartId;

    @ApiModelProperty(value = "차트명")
    @JsonProperty("name")
    private String chartNm;

    @ApiModelProperty(value = "이미지목록(차트.트랙[0].앨범.앨범이미지목록")
    @JsonProperty("imgList")
    private List<AlbumImg> albumImgList;

    @ApiModelProperty("차트별 특랙목록")
    @JsonProperty("list")
    private List<ChartMusicContent> chartMusicContentList;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Builder
    @JsonPropertyOrder({"size", "url"})
    public static class AlbumImg {
        @ApiModelProperty(value = "이미지URL")
        private String url;
        @ApiModelProperty(value = "이미지사이즈")
        private Integer size;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Builder
    @JsonPropertyOrder({"id", "name", "priority", "beforePriority"})
    public static class ChartMusicContent {
        @ApiModelProperty(value = "트랙아이디")
        @JsonProperty("id")
        private Long trackId;

        @ApiModelProperty(value = "트랙명")
        @JsonProperty("name")
        private String trackNm;

        @ApiModelProperty(value = "순서")
        @JsonProperty("priority")
        private Integer trackSn;

        @ApiModelProperty(value = "이전순서")
        @JsonProperty("beforePriority")
        private Integer trackBfSn;
    }

}