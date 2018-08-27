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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sktechx.godmusic.personal.common.domain.type.AppNameType;
import com.sktechx.godmusic.personal.common.domain.type.SourceType;
import lombok.Getter;

/**
 * 설명 : 사용자 이벤트
 * 
 * @author 정덕진(Deockjin Chung)/Server 개발팀/SKTECH(djin.chung@sk.com)
 * @date 2018. 3. 26.
 *
 */

@Getter
public class UserEvent {
	@JsonProperty("play_chnl")
	private String				playChnl;
	
	@JsonProperty("event")
	private UserEventType		event;

	@JsonProperty("member_no")
	private Long				memberNo;
	
	@JsonProperty("charactor_no")
	private Long				charactorNo;

	@JsonProperty("time_millis")
	private long				timeMillis;

	@JsonProperty("target_type")
	private UserEventTarget		targetType;

	@JsonProperty("target_id")
	private String				targetId;

	@JsonProperty("source_type")
	private SourceType          sourceType;
	
	private UserEvent()	{
		this.timeMillis = System.currentTimeMillis();
	}

	public final static UserEventBuilder	newBuilder()	{
		return new UserEventBuilder();
	}
	
	public static class UserEventBuilder	{
		UserEvent instance = new UserEvent();
		
		private UserEventBuilder()	{}	

		public UserEventBuilder		setPlayChnl(AppNameType playChnl)	{
			if(playChnl != null )
				instance.playChnl = playChnl.getCode();
			return this;
		}

		public UserEventBuilder		setEvent(UserEventType event)	{
			instance.event = event;
			return this;
		}

		public UserEventBuilder		setMemberNo(long memberNo)	{
			instance.memberNo = memberNo;
			return this;
		}

		public UserEventBuilder		setCharactorNo(long charactorNo)	{
			instance.charactorNo = charactorNo;
			return this;
		}

		public UserEventBuilder		setTargetType(UserEventTarget target)	{
			instance.targetType = target;
			return this;
		}

		public UserEventBuilder		setTargetId(String targetId)	{
			instance.targetId = targetId;
			return this;
		}

		public UserEventBuilder		setTargetId(long targetId)	{
			instance.targetId = Long.toString(targetId);
			return this;
		}

		public UserEventBuilder		setSourceType(SourceType sourceType)	{
			instance.sourceType = sourceType;
			return this;
		}
		
		public UserEvent	build()	{
			if( instance.event == null )	throw new IllegalStateException("event is null");
			if( instance.memberNo == null )	throw new IllegalStateException("memberNo is null");
			if( instance.targetType == null )	throw new IllegalStateException("targetType is null");
			if( instance.targetId == null )	throw new IllegalStateException("targetId is null");

			return instance;
		}
	}
	
}
