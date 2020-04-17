package com.sktechx.godmusic.personal.rest.model.dto.chart;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import lombok.Data;

/**
 * 차트용 이미지 dto
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChartImageInfo extends ImageInfo {
	private String newUrl;

	public void replaceUrlByNew(Boolean useNewUrl){

		if(useNewUrl && this.newUrl != null){
			this.setUrl(this.newUrl);
			this.newUrl = null;
		}

		this.newUrl = null;
	}
}
