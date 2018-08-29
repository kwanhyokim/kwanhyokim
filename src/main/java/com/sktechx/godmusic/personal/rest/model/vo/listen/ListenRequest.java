package com.sktechx.godmusic.personal.rest.model.vo.listen;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 8.
 * @time PM 7:18
 */
@Data
public class ListenRequest {
	@NotNull
	@ApiModelProperty(name = "listenType", value = "청취 타입(CHNL: 일반채널(DJ), ALBUM: 앨범, MY_CHNL: 마이채널, CHART: 차트, " +
			"RC_ATST_TR: 추천(선호/유사 아티스트 인기곡), RC_SML_TR: 추천(유사곡), RC_GR_TR: 추천(선호 장르 유사곡), RC_CF_TR: 추천(CF 추천 알고리즘)",
			allowableValues = "CHNL, ALBUM, MY_CHNL, CHART, RC_ATST_TR, RC_SML_TR, RC_GR_TR, RC_CF_TR")
	private String listenType;

	@Max(20)
	@NotNull
	@ApiModelProperty(name = "listenTypeId", value = "좋아하는 타입에 맞는 ID의 리스트")
	private Long listenTypeId;
}
