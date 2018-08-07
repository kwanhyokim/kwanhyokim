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

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.rest.model.dto.MemberChannelDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 8. 6.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder({"memberChannel", "duplicatedYn", "agencyCancelYn", "successfulCnt"})
@Builder
public class MyPlaylistTrackCreateResponse {
    private MemberChannelDto memberChannel;
    private YnType duplicatedYn;
    private YnType agencyCancelYn;
    private Integer successfulCnt;
}