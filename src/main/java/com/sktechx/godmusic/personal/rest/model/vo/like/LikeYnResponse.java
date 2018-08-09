package com.sktechx.godmusic.personal.rest.model.vo.like;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 9.
 * @time AM 11:14
 */
@Data
@AllArgsConstructor
public class LikeYnResponse {
	@ApiModelProperty(name = "likeYn", value = "좋아요 여부(Y, N)", allowableValues = "Y, N")
	private String likeYn;
}
