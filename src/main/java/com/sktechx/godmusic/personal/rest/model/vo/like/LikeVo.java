package com.sktechx.godmusic.personal.rest.model.vo.like;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 8.
 * @time PM 1:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeVo {
	@ApiModelProperty(value = "캐릭터 번호")
	private Long characterNo;
	@ApiModelProperty(name = "likeType", value = "좋아하는 타입(CHNL: 채널, ALBUM: 앨범, CHART: 차트, ARTIST: 아티스트, TRACK: 곡)",
			allowableValues = "CHNL, ALBUM, CHART, ARTIST, TRACK")
	private String likeType;
	@ApiModelProperty(name = "likeTypeIdList", value = "좋아하는 타입 아이디 리스트")
	private Long likeTypeId;
	@ApiModelProperty(value = "정렬 순서")
	private int dispSn;
}
