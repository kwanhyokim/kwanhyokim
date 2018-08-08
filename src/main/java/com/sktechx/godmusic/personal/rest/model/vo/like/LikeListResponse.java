package com.sktechx.godmusic.personal.rest.model.vo.like;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 8.
 * @time AM 11:21
 */
@Data
public class LikeListResponse<T> extends LikeVo {
	@ApiModelProperty("좋아요 타입별 항목")
	private T likeInfo;

	public LikeListResponse(T likeInfo, LikeVo likeVo) {
		this.likeInfo = likeInfo;
		this.setCharacterNo(likeVo.getCharacterNo());
		this.setLikeType(likeVo.getLikeType());
		this.setLikeTypeId(likeVo.getLikeTypeId());
		this.setDispSn(likeVo.getDispSn());
	}
}
