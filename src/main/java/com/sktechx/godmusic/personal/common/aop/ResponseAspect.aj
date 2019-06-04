package com.sktechx.godmusic.personal.common.aop;

/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ResponseAspect {


	@AfterReturning(pointcut = "execution(* com.sktechx.godmusic.personal.rest.controller.*.*.recommendHomePanels(..))", returning = "result")
	public void doSomethingAfterReturning(JoinPoint joinPoint, Object result) {
//		RecommendPanelResponse recommendPanelResponse = ((CommonApiResponse<RecommendPanelResponse>)result).getData();
//
//		List<Panel> recommendPanelList = recommendPanelResponse.getList();

//		for(Panel recommendPanel : recommendPanelList){
//
////			recommendPanel.getContent().
//
//		}

		// 끼어들어 실행할 로직을 작성
	}
}
