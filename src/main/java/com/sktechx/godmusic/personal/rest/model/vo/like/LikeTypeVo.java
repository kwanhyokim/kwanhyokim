package com.sktechx.godmusic.personal.rest.model.vo.like;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 9. 6.
 * @time AM 10:53
 */
@Data
public class LikeTypeVo {

	@NotBlank
	@ApiModelProperty(name = "likeType", value = "좋아요 대상 타입 - CHNL: 채널, ALBUM: 앨범, CHART: 차트, ARTIST: 아티스트, TRACK: 곡, FLAC: FLAC, AFLO: Artist&FLO, VIDEO: 영상",
			allowableValues = "CHNL, ALBUM, CHART, ARTIST, TRACK, FLAC, AFLO, VIDEO")
	private String likeType;

	@Max(Long.MAX_VALUE)
	@NotNull
	@ApiModelProperty(name = "likeTypeId", value = "좋아요 대상 아이디")
	private Long likeTypeId;

}
