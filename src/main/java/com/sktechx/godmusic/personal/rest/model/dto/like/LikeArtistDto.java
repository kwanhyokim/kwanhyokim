package com.sktechx.godmusic.personal.rest.model.dto.like;

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
public class LikeArtistDto {
	@ApiModelProperty(value = "아티스트 ID")
	private Long artistId;
	@ApiModelProperty(value = "아티스트 이름")
	private String artistName;
	@ApiModelProperty(value = "성별 타입 코드 M: 남성, F: 여성", allowableValues = "M, F")
	private String genderCdStr;
	@ApiModelProperty(value = "아티스트 그룹 타입 코드 S: 솔로, D: 듀오, G:그룹", allowableValues = "S, D, G")
	private String artistGroupTypeStr;
	@ApiModelProperty(value = "아티스트 이미지")
	private List<ImageInfo> imgList;
}
