package com.sktechx.godmusic.personal.rest.model.dto.like;

import lombok.Data;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 2.
 * @time PM 3:39
 */
@Data
public class LikeDto {
	private Long characterNo;
	private String likeType;
	private Long likeTypeId;
	private int dispSn;
}
