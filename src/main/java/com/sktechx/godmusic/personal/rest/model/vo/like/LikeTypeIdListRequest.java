package com.sktechx.godmusic.personal.rest.model.vo.like;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 2.
 * @time PM 7:33
 */
@Data
public class LikeTypeIdListRequest {

	@NotEmpty
	@ApiModelProperty(name = "likeTypeList", value = "좋아하는 타입별 아이디(CHNL: 채널, ALBUM: 앨범, CHART: 차트, ARTIST: 아티스트, TRACK: 곡)")
	private List<LikeTypeVo> likeTypeList = Collections.emptyList();

	public List<Long> getLikeTargetIds() {
		return this.likeTypeList.stream()
				.map(LikeTypeVo::getLikeTypeId)
				.collect(Collectors.toList());
	}

	public String getLikeType() {
		LikeTypeVo vo = this.likeTypeList.get(0);
		return vo.getLikeType();
	}
}
