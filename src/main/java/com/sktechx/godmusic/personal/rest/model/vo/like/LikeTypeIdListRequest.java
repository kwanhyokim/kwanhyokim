package com.sktechx.godmusic.personal.rest.model.vo.like;

import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 2.
 * @time PM 7:33
 */
@Data
public class LikeTypeIdListRequest {
	@Length(max = 10)
	@NotNull
	@ApiParam(name = "likeType", value = "좋아하는 타입(CHNL: 채널, ALBUM: 앨범, CHART: 차트, ARTIST: 아티스트, TRACK: 곡)",
			allowableValues = "CHNL, ALBUM, CHART, ARTIST, TRACK")
	private String likeType;

	@NotNull
	@ApiParam(name = "likeTypeIdList", value = "좋아하는 타입 아이디 리스트")
	private List<Long> likeTypeIdList;
}
