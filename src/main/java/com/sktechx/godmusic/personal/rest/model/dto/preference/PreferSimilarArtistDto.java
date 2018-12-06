/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.dto.preference;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 김관효(Kwanhyo Kim)/Music사업팀/SKTECH(kwanhyo.kim@sk.com)
 * @date 2018. 11. 21.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PreferSimilarArtistDto extends ArtistDto {
	private Long seedArtistId;
	private Integer rank;
}
