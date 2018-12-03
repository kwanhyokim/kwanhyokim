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

package com.sktechx.godmusic.personal.rest.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 9. 14.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "멤버채널 이미지 관리")
public class MemberChannelImageDto {

    private Long memberChnlImgId;
    private Long memberChnlId;
    private String imgDispType;
    private String osType;
    private Long imgSize;
    private String imgUrl;

}
