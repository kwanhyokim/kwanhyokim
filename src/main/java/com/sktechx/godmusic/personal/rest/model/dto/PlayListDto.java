package com.sktechx.godmusic.personal.rest.model.dto;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.*;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.domain.type.ChartType;
import com.sktechx.godmusic.personal.common.domain.type.PlayListType;
import com.sktechx.godmusic.personal.common.domain.type.SvcContentType;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "id", "name", "imgList", "createDateTime", "updateDateTime", "renewDateTime", "likeYn", "renewYn", "totalCount", "renewTrackCount", "basedOnUpdate", "trackList"})
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

	private TasteMixDto tasteMix;

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

	@JsonIgnore
	private SvcContentType svcContentType;

	@ApiModelProperty(hidden = true)
	private Date dispStartDtime;

	public String getBasedOnUpdate(){
		if(chartType != null && dispStartDtime != null){
			Calendar c = Calendar.getInstance();
			c.setTime(dispStartDtime);

			switch (chartType) {
				case NEW:
					return String.format("%02d:00 업데이트" , c.get(Calendar.HOUR_OF_DAY));
				case HOURLY:
					return String.format("%02d:00 업데이트" , c.get(Calendar.HOUR_OF_DAY));
				case DAILY:
					c.add(Calendar.MINUTE , - 1);
					String end = String.format("%02d/%02d" , c.get(Calendar.MONTH) + 1, c.get(Calendar.DATE));
					c.add(Calendar.DATE , -7);
					String start = String.format("%02d/%02d" , c.get(Calendar.MONTH) + 1 , c.get(Calendar.DATE));
					return String.format("%s ~ %s" , start , end );
				case NOTABLE:
					return String.format("%02d:%02d 업데이트" , c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
			}
		}
		return null;
	}

}
