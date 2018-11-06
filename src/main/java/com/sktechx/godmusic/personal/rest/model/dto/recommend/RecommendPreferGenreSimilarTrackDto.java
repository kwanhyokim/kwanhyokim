package com.sktechx.godmusic.personal.rest.model.dto.recommend;

import lombok.Builder;
import lombok.Data;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 23.
 * @time PM 1:50
 */
@Builder
@Data
public class RecommendPreferGenreSimilarTrackDto {
	private Long characterNo;
	private Long svcGenreId;
	private Long rcmmdPreferGenreSimilarTrackId;
	private int dispSn;
	private Long trackId;
}
