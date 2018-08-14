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
public class LikePlaylistDto {
	private Long playListId;
	private String playListName;
	@ApiModelProperty(value = "전체 곡수")
	private Long totalCount;
	@ApiModelProperty(value = "플레이리스트 이미지")
	private List<ImageInfo> imgList;
}
