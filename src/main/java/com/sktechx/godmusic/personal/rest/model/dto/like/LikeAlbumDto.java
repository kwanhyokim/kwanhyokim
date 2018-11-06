package com.sktechx.godmusic.personal.rest.model.dto.like;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 2.
 * @time PM 3:39
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeAlbumDto {
	@JsonProperty("id")
	@ApiModelProperty(required = true, example = "2834", value = "아이디")
	private Long albumId;
	@JsonProperty("title")
	@ApiModelProperty(required = true, example = "김경호 6집 (The Life)", value = "제목")
	private String albumTitle;
	@ApiModelProperty(value = "아티스트 ID")
	private Long artistId;
	@ApiModelProperty(value = "아티스트 이름")
	private String artistName;
	@ApiModelProperty(required = true, example = "200108", value = "발매년월일")
	private String releaseYmd;
	@ApiModelProperty(required = true, example = "정규", value = "앨범 타입")
	private String albumTypeStr;
	@ApiModelProperty(value = "앨범 이미지")
	private List<ImageInfo> imgList;
}
