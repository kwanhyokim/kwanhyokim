package com.sktechx.godmusic.personal.rest.model.dto.recommend;

import lombok.Data;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 20.
 * @time PM 8:43
 */
@Data
public class CharacterPreferArtistDto {
	private Long characterNo;
	private String genreType;
	private Long genreId;
	private Long artistId;
}
