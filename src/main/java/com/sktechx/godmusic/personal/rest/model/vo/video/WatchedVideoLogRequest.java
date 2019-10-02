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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class WatchedVideoLogRequest {

	public enum LogType {
		/**
		 * 영상 시청 시작
		 */
		STRT,

		/**
		 * 영상 1분 시청
		 */
		AMIN,

		/**
		 * 영상 시청 종료
		 */
		MEND
		;
	}

	@Min(1)
	@Max(value = Long.MAX_VALUE)
	@ApiModelProperty(name = "videoId", value = "비디오 ID")
	private Long videoId;

	@NotNull
	@ApiModelProperty(name = "logType", value = "로그 타입 - STRT(시청시작) | 1MIN(1분시청) | MEND(시청종료) | USKP(유저스킵)", allowableValues = "STRT | 1MIN | MEND | USKP")
	private String logType;

	@NotNull
	@ApiModelProperty(name = "resolution", value = "영상 해상도 - 480P | 720P | 1080P", allowableValues = "480P | 720P | 1080P")
	private String resolution;

	@NotNull
	@ApiModelProperty(name = "osType", value = "OS Type(ALL, AOS, IOS, WEB)", allowableValues = "ALL, AOS, IOS, WEB")
	private OsType osType;

	@Min(1)
	@Max(value = Long.MAX_VALUE)
	@ApiModelProperty(name = "duration", value = "영상 시청 Duration(초단위)")
	private Long duration;

	@Min(0)
	@Max(value = Long.MAX_VALUE)
	@ApiModelProperty(name = "runningTimeSecs", value = "영상 전체 길이(초단위), 못 구할 시 0 입력")
	private Long runningTimeSecs;

	@NotNull
	@ApiModelProperty(name = "freeYn", value = "무료 여부(Y, N)", allowableValues = "Y | N")
	private YnType freeYn;

	@ApiModelProperty(name = "addDateTime", value = "추가 시간")
	private String addDateTime;

	@ApiModelProperty(name = "sourceType", value = "소스 타입 - MV | TEASER | SPECIAL | LIVE | INTERVIEW | ETC")
	private String sourceType;

	@ApiModelProperty(name = "ownerToken", value = "파일 원천 정보")
	private String ownerToken;

	@ApiModelProperty(name = "sessionId", value = "세션 아이디")
	private String sessionId;
}
