package com.sktechx.godmusic.personal.rest.model.dto.like;

import com.sktechx.godmusic.personal.rest.model.dto.ImageDto;
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
public class LikeTrackDto {
	@ApiModelProperty(value = "곡 ID")
	private Long trackId;
	@ApiModelProperty(value = "곡 제목")
	private String trackName;
	@ApiModelProperty(value = "아티스트 ID")
	private Long artistId;
	@ApiModelProperty(value = "아티스트 이름")
	private String artistName;
	@ApiModelProperty(value = "앨범 이미지")
	private List<ImageDto> imgList;
}
