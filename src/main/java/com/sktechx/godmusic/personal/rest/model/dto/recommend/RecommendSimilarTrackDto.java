/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.dto.recommend;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 김관효(Kwanhyo Kim)/Music사업팀/SKTECH(kwanhyo.kim@sk.com)
 * @date 2019. 5. 21.
 */

@Builder
@Data
public class RecommendSimilarTrackDto {
	private Long rcmmdSimilarTrackId;
	private String seedTrackNm;
	private String seedArtistNm;
	private Integer dispSn;
	private Date createDtime;
}
