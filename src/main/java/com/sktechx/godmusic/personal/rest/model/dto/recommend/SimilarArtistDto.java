package com.sktechx.godmusic.personal.rest.model.dto.recommend;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 20.
 * @time PM 7:17
 */
@Data
@Builder
public class SimilarArtistDto {
	@JsonProperty("id")
	private Long artistId;
	private Long similarArtistId;
}
