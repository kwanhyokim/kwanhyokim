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

/**
 * 설명 : XXXXXXXXX
 * 
 * @author 정덕진(Deockjin Chung)/Server 개발팀/SKTECH(djin.chung@sk.com)
 * @date 2018. 3. 16.
 *
 */

public interface AmqpDeliver {
	/**
	 * 메시지 전송 요청
	 * @param message
	 */
	public	void		request(String exchangeName, Object message);
	
	/**
	 * 메시지 전송 시작
	 */
	public	void		start();
	
	/**
	 * 메시지 전송 중지
	 */
	public	void		stop();
}
