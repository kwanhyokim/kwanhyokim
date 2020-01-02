/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.repository;


import com.sktechx.godmusic.lib.mybatis.annotation.BaseMapper;
import com.sktechx.godmusic.personal.rest.model.dto.event.BestNineTrackDto;
import org.apache.ibatis.annotations.Param;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 11. 14.
 */
@BaseMapper
public interface EventMapper {

    BestNineTrackDto findBestNineTracks(@Param("characterNo") Long characterNo, @Param("eventDate") String eventDate);

}
