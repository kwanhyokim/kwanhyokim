/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.vo.recommend.header;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.SeedArtistVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.SeedGenreVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.SeedTrackVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 설명 : 추천 상세 페이지 헤더 구성용 Vo
 *
 * @author 김관효(Kwanhyo Kim)/Music사업팀/SKTECH(kwanhyo.kim@sk.com)
 * @date 2018. 8. 16.
 */

@Data
@Builder
@ApiModel(value="RecommendPanelHeaderVo")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class RecommendPanelHeaderVo {


	@ApiModelProperty(required = true, value = "패널 제목")
	private String title;
	@ApiModelProperty(required = true, value = "패널 부제목")
	private String subTitle;
	@ApiModelProperty(required = true, value = "패널 배경 이미지 리스트")
	private List<ImageInfo> imgList;

	@ApiModelProperty(value = "총 곡수")
	private Integer trackCount;

	@ApiModelProperty(value = "아티스트 목록")
	private List<ArtistDto> artistList;

	@ApiModelProperty(value = "아티스트 수")
	private Integer artistCount;

	@ApiModelProperty(value = "생성일")
	@JsonProperty("createDateTime")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
	private Date createDtime;

	@ApiModelProperty(value = "갱신일")
	@JsonProperty("renewDateTime")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
	private Date renewDtime;

	@ApiModelProperty(value = "업데이트 여부")
	private YnType newYn;

	@JsonProperty("seedGenre")
	private SeedGenreVo seedGenreVo;

	@JsonProperty("seedArtist")
	private SeedArtistVo seedArtistVo;

	@JsonProperty("seedTrack")
	private SeedTrackVo seedTrackVo;

}
