/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.vo;

import java.util.List;

import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 남재우(Peter)/njw0619@sk.com
 * @date 2018.07.03
 */

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelListResponse {
    @ApiModelProperty(required = true, value = "운영자추천 DJ채널 리스트")
    private List<ChnlDto> list;
}
