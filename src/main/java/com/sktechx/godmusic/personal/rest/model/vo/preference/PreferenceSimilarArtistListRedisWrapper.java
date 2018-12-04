/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.vo.preference;

import java.util.List;
import java.util.Map;

import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import lombok.Builder;
import lombok.Data;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 김관효(Kwanhyo Kim)/Music사업팀/SKTECH(kwanhyo.kim@sk.com)
 * @date 2018. 12. 4.
 */

@Builder
@Data
public class PreferenceSimilarArtistListRedisWrapper {
	List<ArtistDto>[] artistDtoList;
	boolean cached;
}
