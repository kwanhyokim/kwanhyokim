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

package com.sktechx.godmusic.personal.rest.repository;

import com.sktechx.godmusic.lib.mybatis.annotation.BaseMapper;
import com.sktechx.godmusic.personal.rest.model.dto.GenreDto;
import com.sktechx.godmusic.personal.rest.model.dto.PreferGenreDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 7. 19.
 */
@BaseMapper
public interface GenreMapper {
    List<GenreDto> selectGenreListByDefault();
    List<GenreDto> selectGenreListByCharacterNo(@Param("characterNo") Long characterNo);

    List<PreferGenreDto> selectPreferGenreListByCharacterNo(@Param("characterNo") Long characterNo);
}
