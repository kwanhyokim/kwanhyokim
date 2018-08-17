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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 김관효(Kwanhyo Kim)/Music사업팀/SKTECH(kwanhyo.kim@sk.com)
 * @date 2018. 8. 16.
 */

@Data
@ApiModel(value="Panel")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class RecommendPanelInfoDto {


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

//	@JsonProperty("createDateTime")
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
//	private Date createDtime;
//
//	@JsonProperty("updateDateTime")
//	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
//	private Date updateDtime;

	@ApiModelProperty(value = "갱신일")
	@JsonProperty("renewDateTime")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
	private Date renewDtime;

	@ApiModelProperty(value = "업데이트 여부")
	private YnType newYn;


	public static class Builder {


		private String title;
		private String subTtitle;
		private List<ImageInfo> imgList;
		private Integer trackCount;
		private List<ArtistDto> artistList;
		private Integer artistCount;
		private Date renewDtime;
		private YnType newYn;

		public Builder title(String val){
			title = val;
			return this;
		}

		public Builder subTitle(String val){
			subTtitle = val;
			return this;
		}
		public Builder imgList(List<ImageInfo> val){
			imgList = val;
			return this;
		}

		public Builder trackCount(Integer val){
			trackCount = val;
			return this;
		}

		public Builder artistList(List<ArtistDto> val){
			artistList = val;
			return this;
		}

		public Builder artistCount(Integer val){
			artistCount = val;
			return this;
		}

		public Builder renewDtime(Date val){
			renewDtime = val;
			return this;
		}

		public Builder newYn(YnType val){
			newYn = val;
			return this;
		}

		public RecommendPanelInfoDto build(){
			return new RecommendPanelInfoDto(this);
		}

	}

	private RecommendPanelInfoDto(Builder builder){

		title = builder.title;
		subTitle = builder.subTtitle;
		imgList = builder.imgList;
		trackCount = builder.trackCount;
		artistList = builder.artistList;
		artistCount = builder.artistCount;
		renewDtime = builder.renewDtime;
		newYn = builder.newYn;

	}

}
