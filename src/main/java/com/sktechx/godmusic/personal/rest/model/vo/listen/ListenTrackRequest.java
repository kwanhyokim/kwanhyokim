/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.model.vo.listen;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 9.
 * @time AM 10:25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListenTrackRequest {
	@NotNull
	@ApiModelProperty(
			name = "trackId",
			value = "곡 ID",
			example = "31228110",
			position = 1
	)
	private Long trackId;

	@NotNull
	@ApiModelProperty(
			name = "sourceType",
			value = "요청 타입 - STRM | DN",
			example = "STRM",
			position = 2
	)
	private SourceType sourceType;

	@NotNull
	@ApiModelProperty(
			name = "trackLogType",
			value = "로그 타입 (STRT:곡시작, 1MIN:1분청취, MEND:곡종료, USKP:유저스킵(미확정))",
			allowableValues = "STRT, 1MIN, MEND, USKP",
			example = "MEND",
			position = 3
	)
	private TrackLogType trackLogType;

	@NotNull
	@ApiModelProperty(
			name = "osType",
			value = "OS Type(ALL, AOS, IOS, WEB)",
			allowableValues = "ALL, AOS, IOS, WEB",
			example = "AOS",
			position = 4
	)
	private OsType osType;

	@NotNull
	@ApiModelProperty(
			name = "bitrate",
			value = "재생할 비트레이트(aac, 192k, 320k, flac16bit, flac24bit)",
			allowableValues = "aac, 192k, 320k, flac16bit, flac24bit",
			example = "aac",
			position = 5
	)
	private BitrateType bitrate;

	@NotNull
	@ApiModelProperty(
			name = "elapsedSec",
			value = "트랙 현재 들은 길이(초단위)",
			example = "101",
			position = 6
	)
	private Long elapsedSec;

	@NotNull
	@Min(0)
	@ApiModelProperty(
			name = "trackTotalSec",
			value = "트랙 전체 길이(초단위), 못 구할 시 0 입력",
			example = "101",
			position = 7
	)
	private Long trackTotalSec;

	@NotNull
	@ApiModelProperty(
			name = "freeYn",
			value = "무료곡 여부(Y, N)",
			allowableValues = "Y, N",
			example = "N",
			position = 8
	)
	private YnType freeYn;

	@JsonProperty(defaultValue = "N")
	@ApiModelProperty(
			name = "playOfflineYn",
			value = "오프라인 여부(Y, N)",
			example = "N",
			position = 9
	)
	private YnType playOfflineYn;

	@JsonProperty(defaultValue = "N")
	@ApiModelProperty(
			name = "playCachedYn",
			value = "Cache 여부(Y, N)",
			example = "N",
			position = 10
	)
	private YnType playCachedYn;

	@ApiModelProperty(
			name = "listenSessionId",
			value = "청취 세션 아이디",
			example = "2d78cd21-7a22-468c-96b6-19c310111da6",
			position = 11
	)
	private String listenSessionId;

	@ApiModelProperty(
			name = "sttToken",
			value = "정산 토큰",
			position = 12
	)
	private String sttToken;

	@ApiModelProperty(
			name = "cachedStreamingToken",
			value = "캐시드 스트리밍 토큰",
			position = 13
	)
	private String cachedStreamingToken;

	@ApiModelProperty(
			name = "ownerToken",
			value = "파일 원천 정보",
			position = 14
	)
	private String ownerToken;

	@ApiModelProperty(
			name = "albumId",
			value = "앨범 ID",
			example = "20193170",
			position = 15
	)
	private Long albumId;

	@ApiModelProperty(
			name = "channelId",
			value = "채널 ID",
			example = "20193170",
			position = 16
	)
	private Long channelId;

	@ApiModelProperty(
			name = "channelType",
			value = "채널 Type",
			example = "ALBUM",
			position = 17
	)
	private String channelType;

	@ApiModelProperty(
			name = "recommendTrackId",
			value = "추천곡 ID",
			position = 18
	)
	private Long recommendTrackId;

	@ApiModelProperty(
			name = "addDateTime",
			value = "추가 시간",
			example = "20191211161526",
			position = 19
	)
	private String addDateTime;

	@ApiModelProperty(
			name = "offlineStartDtime",
			value = "오프라인 시작 일시",
			example = "2019-11-12 12:10:10",
			position = 20
	)
	private String offlineStartDtime;

	@ApiModelProperty(
			name = "metaCachedUpdateDtime",
			value = "meta cached 업데이트 일시",
			example = "2019-11-12 12:10:10",
			position = 21
	)
	private String metaCachedUpdateDtime;

	@ApiModelProperty(
			name = "freeCachedStreamingToken",
			value = "캐시드 스트리밍 곡이 무료곡일 경우 serviceId 토큰",
			position = 22
	)
	private String freeCachedStreamingToken;

	@ApiModelProperty(
			name = "traceType",
			value = "재생목록에 담긴 화면(경로)",
			position = 23
	)
	private String traceType;

	@ApiModelProperty(
			name = "buildNumber",
			value = "",
			position = 24
	)
	private String buildNumber;

	@JsonIgnore
	public String getTrackLogTypeToStr() {
		return Optional.ofNullable(trackLogType)
				.map(TrackLogType::getCode)
				.orElse("");
	}

	@JsonIgnore
	public String getBitrateToStr() {
		return Optional.ofNullable(bitrate)
				.map(BitrateType::getCode)
				.orElse(BitrateType.UNKNOWN.getCode());
	}

	@JsonIgnore
	public String getSourceTypeToStr() {
		return Optional.ofNullable(sourceType).map(SourceType::getCode).orElse(null);
	}

}
