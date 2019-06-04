package com.sktechx.godmusic.personal.rest.model.vo.like;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 7. 31.
 * @time PM 3:56
 */
@Data
public class LikeRequest {

	@Length(max = 10)
	@NotNull
	@ApiModelProperty(name = "likeType", value = "좋아하는 타입(CHNL: 채널, ALBUM: 앨범, CHART: 차트, ARTIST: 아티스트, TRACK: 곡)",
			allowableValues = "CHNL, ALBUM, CHART, ARTIST, TRACK, FLAC")
	private String likeType;

	@Length(max = 20)
	@NotNull
	@ApiModelProperty(name = "likeTypeId", value = "좋아하는 타입에 맞는 ID")
	private Long likeTypeId;

}
