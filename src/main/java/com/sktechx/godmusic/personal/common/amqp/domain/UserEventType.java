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

package com.sktechx.godmusic.personal.common.amqp.domain;

import com.sktechx.godmusic.personal.common.domain.type.TrackLogType;

/**
 * 설명 : XXXXXXXXX
 * 
 * @author 정덕진(Deockjin Chung)/Server 개발팀/SKTECH(djin.chung@sk.com)
 * @date 2018. 3. 26.
 *
 */

public enum UserEventType {
	UNKNOWN,
	LIKE,
	UNLIKE,
	PICK,
	UNPICK,
	STRT_LISTEN,
	MIN_LISTEN,
	LISTEN_FULL,
	SKIP;
	
	public final static UserEventType	fromTrackLogType(TrackLogType logType)	{
		if( logType == null )
			return UNKNOWN;
		
		switch(logType)	{
		case STRT :	return STRT_LISTEN;
		case ONEMIN :	return MIN_LISTEN;
		case MEND :	return LISTEN_FULL;
		case USKP :	return SKIP;
		}
		return UNKNOWN;
	}
}
