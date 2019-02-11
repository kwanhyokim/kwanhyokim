package com.sktechx.godmusic.personal.rest.model.vo.listen;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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
	@ApiModelProperty(name = "trackLogType", value = "로그 타입 (STRT:곡시작 , 1MIN:1분청취, MEND:곡종료, USKP:유저스킵(미확정))",
			allowableValues = "STRT, 1MIN, MEND, USKP")
	private TrackLogType trackLogType;

	@NotNull
	@ApiModelProperty(name = "bitrate", value = "재생할 비트레이트(aac, 192k, 320k)",
			allowableValues = "aac, 192k, 320k")
	private BitrateType bitrate;

	@NotNull
	@ApiModelProperty(name = "osType", value = "OS Type(ALL, AOS, IOS)",
			allowableValues = "ALL, AOS, IOS")
	private OsType osType;

	@NotNull
	@ApiModelProperty(name = "elapsedSec", value = "트랙 현재 들은 길이(초단위)")
	private Long elapsedSec;

	@NotNull
	@Min(1)
	@ApiModelProperty(name = "trackTotalSec", value = "트랙 전체 길이(초단위), 못 구할 시 0 입력")
	private Long trackTotalSec;

	@NotNull
	@ApiModelProperty(name = "freeYn", value = "무료곡 여부(Y, N)", allowableValues = "Y, N")
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
}
