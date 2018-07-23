/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.repository;

import com.sktechx.godmusic.lib.mybatis.annotation.BaseMapper;
import com.sktechx.godmusic.personal.rest.model.dto.ChartDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 설명 : 트랙
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 23.
 */
@BaseMapper
public interface TrackMapper {
    //TODO : 추후 meta api 호출
    List<TrackDto> selectTrackList(@Param("trackIdList") List<Long> trackIdList);
}
