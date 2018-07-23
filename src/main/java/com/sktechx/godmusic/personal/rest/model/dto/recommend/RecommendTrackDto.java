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

import com.sktechx.godmusic.personal.rest.model.dto.ServiceGenreDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 설명 : 추천 트랙 DTO
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 19.
 */
@Data
public class RecommendTrackDto {
    private Long characterNo;
    private Date dispStdStartDt;
    private Date dispStdEndDt;
    private Long svcGenreId;
    private Long rcmmdId;
    private Integer dispSn;
    private Date createDtime;
    private Date updateDtime;

    private List<TrackDto> trackList;
    private ServiceGenreDto svcGenreDto;

}
