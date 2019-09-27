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

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sktechx.godmusic.lib.mybatis.annotation.ReadOnlyMapper;
import com.sktechx.godmusic.personal.rest.model.dto.VideoDto;


@ReadOnlyMapper
public interface PreferenceMapper {

    List<VideoDto> selectPreferArtistVideoListByCharacterNo(@Param("characterNo") Long characterNo,
            @Param("isTop") Boolean isTop);

    List<VideoDto> selectPreferGenreVideoListByCharacterNo(@Param("characterNo") Long characterNo);
}
