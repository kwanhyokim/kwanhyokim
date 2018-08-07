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
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
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
@JsonPropertyOrder({"totalCount", "currentPage", "lastPageYn", "list"})
public class MyPlaylistTrackRetrieveAllResponse {
    private long totalCount;
    private int currentPage;
    private YnType lastPageYn;
    private List<TrackDto> list;

    public MyPlaylistTrackRetrieveAllResponse(Page<TrackDto> page){
        this.totalCount = page.getTotalElements();
        this.currentPage = page.getNumber()+1;
        this.lastPageYn = page.isLast() ? YnType.Y : YnType.N;
        this.list = page.getContent();
    }
}