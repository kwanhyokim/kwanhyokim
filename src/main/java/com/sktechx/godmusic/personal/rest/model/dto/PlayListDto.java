package com.sktechx.godmusic.personal.rest.model.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.domain.type.ChartType;
import com.sktechx.godmusic.personal.common.domain.type.PlayListType;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import lombok.Data;

/**
 *
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayListDto {
	@JsonProperty("id")
	private Long id;

	@JsonProperty("name")
	private String name;

	private String playTime;
	private Integer trackCount;

	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
	private Date createDateTime;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
	private Date updateDateTime;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
	private Date registDateTime;

	private List<ImageInfo> imgList;

	private List<TrackDto> trackList;

	private ChartType chartType;

	@JsonProperty("type")
	private PlayListType playListType;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
	private Date renewDateTime;

	private YnType renewYn;

	private Integer renewTrackCnt;

	@JsonProperty("renewTrackCount")
	public Integer getRenewTrackCnt() {
		return this.renewYn == YnType.N ? 0 : renewTrackCnt;
	}

	public void setImgList(List<ImageInfo> imgList) {

		if (imgList != null) {
			imgList.sort(null);
		}

		this.imgList = imgList;
	}

}
