/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.service.recommend;

import java.util.List;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.*;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2020-03-12
 */

public interface RecommendReadService {

    RecommendForMeDto getRecommendFormeFlo(Long characterNo, Long rcmmdMforuId);
    RecommendSimilarTrackDto getRecommendTodayFlo(Long characterNo, Long rcmmdSimilarTrackId);
    RecommendArtistDto getRecommendArtistFlo(Long characterNo, Long rcmmdArtistId);

    List<RecommendTrackDto> getRecommendTodayFloListWithTrackByCharacterNo(
            Long characterNo,
            int panelMaxSize,
            int trackMaxSize,
            OsType osType
    );

    List<RecommendPanelTrackDto> getRecommendTodayFloTrackListByCharacterNoAndRcmmdId(
            Long characterNo,
            Long rcmmdId
    );

}
