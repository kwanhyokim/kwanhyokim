/*
 * Copyright (c) 2018 SK Planet.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK Planet.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK Planet.
 */

package com.sktechx.godmusic.personal.rest.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.rest.model.dto.member.CharacterType;
import com.sktechx.godmusic.personal.rest.repository.DevToolMapper;
import com.sktechx.godmusic.personal.rest.service.DevToolService;

/**
 * 설명 :
 *
 * @author 김인우(Inwoo Kim)/Music사업팀/SKTECH(addio3305@sk.com)
 * @date 2018. 10. 24.
 */

@Service
public class DevToolServiceImpl implements DevToolService {
	@Autowired
	DevToolMapper devToolMapper;
	
	@Autowired
	RedisService redisService;
	
	@Override
	public void updateCharacterType(Long characterNo, CharacterType type) {

		devToolMapper.updateCharacterType(characterNo,type);

		if(CharacterType.AFLO.equals(type)) {
			devToolMapper.insertAfloCharacter(characterNo);
		}

		if(CharacterType.DEFAULT.equals(type)) {
			devToolMapper.deleteAfloCharacter(characterNo);
		}


	}
	
}
