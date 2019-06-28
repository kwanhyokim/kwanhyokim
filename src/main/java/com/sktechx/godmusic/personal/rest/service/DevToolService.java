/*
 * Copyright (c) 2018 SK Planet.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK Planet.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK Planet.
 */

package com.sktechx.godmusic.personal.rest.service;

import java.util.List;

import com.sktechx.godmusic.personal.rest.model.dto.member.CharacterType;
import com.sktechx.godmusic.personal.rest.model.dto.member.MemberDto;

/**
 * 설명 :
 *
 * @author 김인우(Inwoo Kim)/Music사업팀/SKTECH(addio3305@sk.com)
 * @date 2018. 10. 24.
 */

public interface DevToolService {

	void updateCharacterType(Long characterNo, CharacterType type);

}
