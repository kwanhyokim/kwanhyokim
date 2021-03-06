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

import java.util.Date;
import java.util.List;

import com.sktechx.godmusic.personal.common.domain.type.CreateStdType;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 설명 : 아티스트 추천 DTO
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 19.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendArtistDto implements RecommendDto{
    private Long characterNo;
    private Date dispStdStartDt;
    private Date dispStdEndDt;
    private Long rcmmdArtistId;
    private Integer dispSn;
    private Date createDtime;
    private Date updateDtime;
    private CreateStdType createStdType;

    private int trackCount;
    private List<Long> trackIdList;
    private List<TrackDto> trackDtoList;

    private List<ArtistDto> artistList;

}
