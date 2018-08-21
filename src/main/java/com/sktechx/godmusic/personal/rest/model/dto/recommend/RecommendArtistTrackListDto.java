package com.sktechx.godmusic.personal.rest.model.dto.recommend;

import lombok.Builder;
import lombok.Data;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 21.
 * @time PM 3:13
 */
@Data
@Builder
public class RecommendArtistTrackListDto {
	private Long rcmmdArtistId;
	private Long trackId;
	private int dispSn;
}
