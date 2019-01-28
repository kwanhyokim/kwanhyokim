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
import com.sktechx.godmusic.personal.common.domain.type.SourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 설명 : 사용자 이벤트
 * 
 * @author 정덕진(Deockjin Chung)/Server 개발팀/SKTECH(djin.chung@sk.com)
 * @date 2018. 3. 26.
 *
 */
@Builder(builderMethodName = "newBuilder")
@AllArgsConstructor
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

	@JsonProperty("track_tot_tm")
	private Long				trackTotTm;

	@JsonProperty("elapsed_tm")
	private Long				elapsedTm;
	
	private UserEvent()	{
		this.timeMillis = System.currentTimeMillis();
	}

	@Override
	public String toString() {
		return "UserEvent{" +
				"playChnl='" + playChnl + '\'' +
				", event=" + event +
				", memberNo=" + memberNo +
				", charactorNo=" + charactorNo +
				", timeMillis=" + timeMillis +
				", targetType=" + targetType +
				", targetId='" + targetId + '\'' +
				", sourceType=" + sourceType +
				'}';
	}
}
