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

package com.sktechx.godmusic.personal.rest.model.vo.myplaylist;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 8. 3.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "My Playlist 수정")
public class MyPlaylistUpdateRequest {

    @NotNull
    @Length(max = 400, message = "리스트명은 최대 400자까지 입력하실 수 있습니다")
    private String memberChannelName;
}
