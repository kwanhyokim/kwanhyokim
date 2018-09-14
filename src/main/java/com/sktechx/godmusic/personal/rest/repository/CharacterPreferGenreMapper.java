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
import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferDispDto;
import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferGenreDto;
import com.sktechx.godmusic.personal.rest.model.dto.ServiceGenreDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendArtistDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhase;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 7. 22.
 */
@BaseMapper
public interface CharacterPreferGenreMapper {
    List<CharacterPreferGenreDto> selectCharacterPreferGenreList(@Param("characterNo") Long characterNo);
    List<CharacterPreferDispDto> selectCharacterPreferDispList(@Param("characterNo") Long characterNo);
    List<CharacterPreferGenreDto> selectCharacterPreferDispMapGenre(@Param("characterNo") Long characterNo);
}
