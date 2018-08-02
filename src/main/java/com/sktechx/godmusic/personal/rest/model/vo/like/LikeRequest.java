package com.sktechx.godmusic.personal.rest.model.vo.like;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

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
	@ApiParam(name = "likeType", value = "좋아하는 타입(CHNL: 채널, ALBUM: 앨범, CHART: 차트, ARTIST: 아티스트, TRACK: 곡)",
			allowableValues = "CHNL, ALBUM, CHART, ARTIST, TRACK")
	private String likeType;

	@Length(max = 20)
	@NotNull
	@ApiParam(name = "likeTypeId", value = "좋아하는 타입에 맞는 ID")
	private Long likeTypeId;

}
