package com.sktechx.godmusic.personal.rest.model.dto.recommend;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RecommendArtistTrackListDto {
	private Long rcmmdArtistId;
	private Long artistId;
	private Long trackId;
	private String trackNm;
	private int dispSn;

}
