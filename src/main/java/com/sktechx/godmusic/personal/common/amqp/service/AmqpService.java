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

package com.sktechx.godmusic.personal.common.amqp.service;

import com.sktechx.godmusic.personal.common.amqp.domain.UserEvent;

/**
 * 설명 : AMQP 서비스
 * 
 * @author 정덕진(Deockjin Chung)/Server 개발팀/SKTECH(djin.chung@sk.com)
 * @date 2018. 3. 15.
 *
 */

public interface AmqpService {
	/**
	 * 청취 로그 전달
	 * @param message
	 */
	public	void		deliverSourcePlay(Object message);
	
	/**
	 * 사용자 이벤트 전달
	 * @param data
	 */
	public	void		deliverUserEvent(UserEvent data);
}
