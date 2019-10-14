/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.model.vo.video;

import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.domain.type.BitrateType;
import com.sktechx.godmusic.personal.common.domain.type.OsType;
import com.sktechx.godmusic.personal.common.domain.type.SourceType;
import com.sktechx.godmusic.personal.common.domain.type.TrackLogType;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

@Data
@Builder
@ToString
public class ResourcePlayLogRequest {

	public enum LogType {
		/**
		 * 영상 시청 시작
		 */
		STRT("STRT"),

		/**
		 * 영상 1분 시청
		 */
		ONEMIN("1MIN"),

		/**
		 * 영상 시청 종료
		 */
		MEND("MEND"),

		/**
		 * 사용자 스킵
		 */
		USKP("USKP")
		;

		@Getter
		private final String code;

		LogType(String code) {
			this.code = code;
		}

		public static LogType fromCode(String code) {
			return Arrays.stream(LogType.values())
					.filter(e -> e.getCode().equalsIgnoreCase(code))
					.findFirst().orElse(null);
		}
	}

	@NotNull
	@ApiModelProperty(name = "resourceId", value = "리소스 ID ex) Video Meta의 videoId")
	private Long resourceId;

	@NotBlank
	@ApiModelProperty(name = "logType", value = "로그 타입 - STRT | 1MIN | MEND", allowableValues = "STRT | 1MIN | MEND")
	private String logType;

	@NotBlank
	@ApiModelProperty(name = "quality", value = "영상 해상도 - 480p | 720p | 1080p", allowableValues = "480P | 720P | 1080P")
	private String quality;

	@NotNull
	@ApiModelProperty(name = "osType", value = "OS Type(ALL, AOS, IOS, WEB)", allowableValues = "ALL, AOS, IOS, WEB")
	private OsType osType;

	@Min(0)
	@Max(value = Long.MAX_VALUE)
	@ApiModelProperty(name = "duration", value = "리소스 Play Time - Duration(초단위)")
	private Long duration;

	@Min(0)
	@Max(value = Long.MAX_VALUE)
	@ApiModelProperty(name = "runningTimeSecs", value = "리소스 전체 Play 길이(초단위), 못 구할 시 0 입력")
	private Long runningTimeSecs;

	@NotNull
	@ApiModelProperty(name = "freeYn", value = "무료 여부(Y, N)", allowableValues = "Y | N")
	private YnType freeYn;

	@ApiModelProperty(name = "channelId", value = "채널 ID")
	private Long channelId;

	@ApiModelProperty(name = "channelType", value = "채널 Type")
	private String channelType;

	@ApiModelProperty(name = "addDateTime", value = "추가 시간")
	private String addDateTime;

	@ApiModelProperty(name = "sourceType", value = "소스 타입 - VIDEO_MV | VIDEO_TEASER | VIDEO_SPECIAL | VIDEO_LIVE | VIDEO_INTERVIEW | VIDEO_ETC")
	private String sourceType;

	@ApiModelProperty(name = "ownerToken", value = "파일 원천 정보")
	private String ownerToken;

	@ApiModelProperty(name = "sessionId", value = "세션 아이디")
	private String sessionId;

	@ApiModelProperty(name = "sttToken", value = "정산 토큰")
	private String sttToken;

	public boolean isFree() {
		return YnType.Y == freeYn;
	}
}
