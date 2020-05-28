/*
 * Copyright (c) 2020 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.service.recommend.read;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;

/**
 * 설명 : 추천 조회 서비스 제공 factory
 *
 **/

@Component
public class RcmmdReadServiceFactory {

    private final RcmmdReadService rcmmdForMeReadService;
    private final RcmmdReadService rcmmdTodayReadService;
    private final RcmmdReadService rcmmdArtistFloReadService;
    private final RcmmdReadService rcmmdLikeTrackReadService;
    private final RcmmdReadService rcmmdEmptyReadService;

    public RcmmdReadServiceFactory(
            @Qualifier("rcmmdForMeReadService") RcmmdReadService rcmmdForMeReadService,
            @Qualifier("rcmmdTodayReadService") RcmmdReadService rcmmdTodayReadService,
            @Qualifier("rcmmdArtistFloReadService") RcmmdReadService rcmmdArtistFloReadService,
            @Qualifier("rcmmdLikeTrackReadService") RcmmdReadService rcmmdLikeTrackReadService,
            @Qualifier("rcmmdEmptyReadService") RcmmdReadService rcmmdEmptyReadService
    ){
        this.rcmmdTodayReadService = rcmmdTodayReadService;
        this.rcmmdForMeReadService = rcmmdForMeReadService;
        this.rcmmdArtistFloReadService = rcmmdArtistFloReadService;
        this.rcmmdLikeTrackReadService = rcmmdLikeTrackReadService;
        this.rcmmdEmptyReadService = rcmmdEmptyReadService;
    }

    public RcmmdReadService getRcmmdReadService(RecommendPanelContentType recommendPanelContentType){
        switch(recommendPanelContentType){
            case RC_CF_TR: return rcmmdForMeReadService;
            case RC_SML_TR: return rcmmdTodayReadService;
            case RC_ATST_TR: return rcmmdArtistFloReadService;
            case RC_LKSM_TR: return rcmmdLikeTrackReadService;
        }

        return rcmmdEmptyReadService;
    }
}
