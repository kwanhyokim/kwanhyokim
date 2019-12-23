package com.sktechx.godmusic.personal.rest.model.vo.listen;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
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
	@ApiModelProperty(name = "trackId", value = "곡 ID")
	private Long trackId;

	@NotNull
	@ApiModelProperty(
			name = "trackLogType",
			value = "로그 타입 (STRT:곡시작, 1MIN:1분청취, MEND:곡종료, USKP:유저스킵(미확정))",
			allowableValues = "STRT, 1MIN, MEND, USKP"
	)
	private TrackLogType trackLogType;

	@NotNull
	@ApiModelProperty(
			name = "bitrate",
			value = "재생할 비트레이트(aac, 192k, 320k, flac16bit, flac24bit)",
			allowableValues = "aac, 192k, 320k, flac16bit, flac24bit"
	)
	private BitrateType bitrate;

	@NotNull
	@ApiModelProperty(
			name = "osType",
			value = "OS Type(ALL, AOS, IOS, WEB)",
			allowableValues = "ALL, AOS, IOS, WEB"
	)
	private OsType osType;

	@NotNull
	@ApiModelProperty(
			name = "elapsedSec",
			value = "트랙 현재 들은 길이(초단위)"
	)
	private Long elapsedSec;

	@NotNull
	@Min(1)
	@ApiModelProperty(
			name = "trackTotalSec",
			value = "트랙 전체 길이(초단위), 못 구할 시 0 입력"
	)
	private Long trackTotalSec;

	@NotNull
	@ApiModelProperty(
			name = "freeYn",
			value = "무료곡 여부(Y, N)",
			allowableValues = "Y, N"
	)
	private YnType freeYn;

	@ApiModelProperty(name = "albumId", value = "앨범 ID")
	private Long albumId;

	@ApiModelProperty(name = "channelId", value = "채널 ID")
	private Long channelId;

	@ApiModelProperty(name = "channelType", value = "채널 Type")
	private String channelType;

	@ApiModelProperty(name = "recommendTrackId", value = "추천곡 ID")
	private Long recommendTrackId;

	@ApiModelProperty(name = "addDateTime", value = "추가 시간")
	private String addDateTime;

	@ApiModelProperty(name = "sourceType", value = "요청 타입")
	private SourceType sourceType;

	@ApiModelProperty(name = "ownerToken", value = "파일 원천 정보")
	private String ownerToken;

	@ApiModelProperty(name = "listenSessionId", value = "청취 세션 아이디")
	private String listenSessionId;

	@ApiModelProperty(name = "sttToken", value = "정산 토큰")
	private String sttToken;

	@ApiModelProperty(name = "cachedToken", value = "캐시드 토큰")
	private String cachedToken;

	@JsonProperty(defaultValue = "N")
	@ApiModelProperty(name = "playOfflineYn", value = "오프라인 여부(Y, N)")
	private YnType playOfflineYn;

	@JsonProperty(defaultValue = "N")
	@ApiModelProperty(name = "playCacheYn", value = "Cache 여부(Y, N)")
	private YnType playCacheYn;

	@ApiModelProperty(name = "offlineStartDtime", value = "오프라인 시작 일시")
	private String offlineStartDtime;

	@ApiModelProperty(name = "metaCacheUpdateDtime", value = "meta cache 업데이트 일시")
	private String metaCacheUpdateDtime;

	public String getTrackLogTypeToStr() {
		return Optional.ofNullable(trackLogType)
				.map(TrackLogType::getCode)
				.orElse("");
	}

	public String getBitrateToStr() {
		return Optional.ofNullable(bitrate)
				.map(BitrateType::getCode)
				.orElse(BitrateType.UNKNOWN.getCode());
	}

	public String getOsTypeToStr() {
		return Optional.ofNullable(osType).map(OsType::getCode).orElse("");
	}

	public String getSourceTypeToStr() {
		return Optional.ofNullable(sourceType).map(SourceType::getCode).orElse(null);
	}

	@JsonIgnore
	public boolean hasSttToken() {
		return !Strings.isNullOrEmpty(sttToken);
	}
}
