package com.sktechx.godmusic.personal.rest.model.dto.recommend;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 20.
 * @time PM 7:17
 */
@Data
@Builder
public class SimilaArtistDto {
	private Long artistId;
	@JsonProperty("id")
	private Long similarArtistId;
	private int dispSn;
}
