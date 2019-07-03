/*
 * Copyright (c) 2018 SK Planet.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK Planet.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK Planet.
 */

package com.sktechx.godmusic.personal.rest.repository;

import org.apache.ibatis.annotations.Param;

import com.sktechx.godmusic.lib.mybatis.annotation.BaseMapper;
import com.sktechx.godmusic.personal.rest.model.dto.member.CharacterType;

/**
 * 설명 :
 *
 * @author 김인우(Inwoo Kim)/Music사업팀/SKTECH(addio3305@sk.com)
 * @date 2018. 10. 24.
 */

@BaseMapper
public interface DevToolMapper {
	
	void updateCharacterType(@Param("characterNo") Long characterNo, @Param("characterType") CharacterType characterType);
	void insertAfloCharacter(@Param("characterNo") Long characterNo);
	void deleteAfloCharacter(@Param("characterNo") Long characterNo);
	void insertCharacterPreferArtist(@Param("characterNo") Long characterNo, @Param("preferGenreId") Long preferGenreId, @Param("preferArtistId") Long preferArtistId);
	Integer selectCharacterPreferArtist(@Param("characterNo") Long characterNo);
}
