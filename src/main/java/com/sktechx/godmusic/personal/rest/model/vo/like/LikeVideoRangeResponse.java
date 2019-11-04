/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.model.vo.like;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.rest.model.vo.video.VideoVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder({"totalCount", "currentPage", "lastPageYn", "list"})
public class LikeVideoRangeResponse {

	@ApiModelProperty(value = "좋아요 한 총 영상 수")
	private long totalCount;

	@ApiModelProperty(value = "현재 요청한 페이지")
	private int currentPage;

	@ApiModelProperty(value = "마지막 페이지 여부")
	private YnType lastPageYn;

	@ApiModelProperty(value = "Video 정보 Object 목록")
	private List<VideoVo> list;

	public static LikeVideoRangeResponse of(Page<VideoVo> page){
		return LikeVideoRangeResponse.builder()
				.totalCount(page.getTotalElements())
				.currentPage(page.getNumber() + 1)
				.lastPageYn(page.isLast() ? YnType.Y : YnType.N)
				.list(page.getContent())
				.build();
	}
}
