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
import java.util.List;

import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import lombok.Builder;
import lombok.Data;

/**
 * 설명 : 오늘의 추천 dto
 */

@Builder
@Data
public class RecommendSimilarTrackDto implements RecommendDto{
	private Long rcmmdSimilarTrackId;
	private String seedTrackNm;
	private String seedArtistNm;
	private Integer dispSn;
	private Date createDtime;

	private Date dispStdStartDt;

	private int trackCount;
	private List<Long> trackIdList;
	private List<TrackDto> trackDtoList;
	private List<ImageInfo> imgList;

	public static RecommendSimilarTrackDto from (RecommendTrackDto recommendTrackDto){
		return RecommendSimilarTrackDto.builder()
				.rcmmdSimilarTrackId(recommendTrackDto.getRcmmdId())
				.createDtime(recommendTrackDto.getRcmmdCreateDtime())
				.dispStdStartDt(recommendTrackDto.getDispStdStartDt())
				.dispSn(recommendTrackDto.getDispSn())
				.trackDtoList(recommendTrackDto.getTrackList())
				.trackCount(recommendTrackDto.getTrackCount())
				.imgList(recommendTrackDto.getImgList())
				.build();
	}
}
