package com.sktechx.godmusic.personal.rest.model.dto.recommend;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sktechx.godmusic.personal.common.domain.type.ArtistType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 20.
 * @time PM 6:54
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendArtistListDto {
	private Long rcmmdArtistId;
	@JsonProperty("id")
	private Long artistId;
	private ArtistType artistType;
	private int dispSn;
}
