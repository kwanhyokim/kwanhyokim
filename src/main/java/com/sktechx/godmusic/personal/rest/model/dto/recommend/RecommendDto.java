package com.sktechx.godmusic.personal.rest.model.dto.recommend;/*
 * Copyright (c) 2020 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

import java.util.List;

import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;

public interface RecommendDto {
    List<TrackDto> getTrackDtoList();
    void setTrackDtoList(List<TrackDto> trackVoList);

    List<Long> getTrackIdList();
    void setTrackIdList(List<Long> trackIdList);
}
