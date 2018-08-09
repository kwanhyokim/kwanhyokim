package com.sktechx.godmusic.personal.rest.model.vo.like;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 8.
 * @time AM 11:40
 */
@Data
public class LikeListRequest {

	@Length(max = 10)
	@NotNull
	@ApiModelProperty(name = "likeType", value = "좋아하는 타입(CHNL: 채널, ALBUM: 앨범, CHART: 차트, ARTIST: 아티스트, TRACK: 곡)",
			allowableValues = "CHNL, ALBUM, CHART, ARTIST, TRACK")
	private String likeType;

	@NotNull
	@ApiModelProperty(name = "likeTypeIds", value = "좋아하는 타입에 맞는 ID의 리스트")
	private List<Long> likeTypeIds;

}
