/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.dto.recommend;

import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.ChannelDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 설명 : 추천청취 분위기 인기 채널
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 21.
 */
@Data
public class RecommendListenMoodDto {
    private Long characterNo;
    private Date dispStdStartDt;
    private Date dispStdEndDt;
    private Long moodId;
    private Integer dispSn;
    private Date createDtime;
    private Date updateDtime;

    private ChannelDto channel;

}
