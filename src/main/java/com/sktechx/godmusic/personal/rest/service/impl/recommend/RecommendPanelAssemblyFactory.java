/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.impl.recommend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.sktechx.godmusic.personal.common.domain.type.PersonalPhaseType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.service.ApplicationContextProvider;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.impl.recommend.panel.assembly.GuestPhasePanelAssembly;
import com.sktechx.godmusic.personal.rest.service.impl.recommend.panel.assembly.ListenPhasePanelAssembly;
import com.sktechx.godmusic.personal.rest.service.impl.recommend.panel.assembly.RecommendPhasePanelAssembly;
import com.sktechx.godmusic.personal.rest.service.impl.recommend.panel.assembly.VisitPhasePanelAssembly;
import com.sktechx.godmusic.personal.rest.service.impl.recommend.panel.assembly.v2.*;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelAssembly;

/**
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 24.
 */
@Service
public class RecommendPanelAssemblyFactory {

    @Autowired
    private ApplicationContextProvider applicationContextProvider;

    public PanelAssembly getRecommendPanelAssembly(PersonalPhaseType personalPhaseType ){
        if(PersonalPhaseType.VISIT.equals(personalPhaseType)){
            return applicationContextProvider.getContext().getBean(VisitPhasePanelAssembly.class);
        }else if(PersonalPhaseType.LISTEN.equals(personalPhaseType)){
            return applicationContextProvider.getContext().getBean(ListenPhasePanelAssembly.class);

        }else if(PersonalPhaseType.RECOMMEND.equals(personalPhaseType)) {
            return applicationContextProvider.getContext().getBean(RecommendPhasePanelAssembly.class);
        }

        return getRecommendPanelAssembly();
    }

    public PanelAssembly getRecommendPanelAssembly(){
        return applicationContextProvider.getContext().getBean(GuestPhasePanelAssembly.class);
    }

    public PanelAssembly getV2RecommendPanelAssembly(PersonalPhaseMeta personalPhaseMeta){

        PersonalPanel personalPanel = personalPhaseMeta.getRecommendPersonalPanelTopItem();

        if(!ObjectUtils.isEmpty(personalPanel)) {
            // 추천 패널 없이 AFLO만 존재하고, 선호 장르가 선택되어 있으면 선호 장르 테마 조립기 이용
            if ( RecommendPanelContentType.AFLO.equals(personalPanel.getRecommendPanelContentType())
                    && personalPhaseMeta.getRcmmdPanelList().stream()
                        .filter(personalPanel1 -> !RecommendPanelContentType.AFLO.equals(personalPanel1.getRecommendPanelContentType())).count() == 0
                    && personalPhaseMeta.isPreferGenreListPresent()){
                return applicationContextProvider.getContext().getBean(PreferGenreThemePanelAssembly.class);
            }

            PanelAssembly panelAssembly = getV2RecommendPanelAssembly(personalPanel.getRecommendPanelContentType());

            if(panelAssembly != null){
                return panelAssembly;
            }
        }

        // 선호 장르 테마
        if( personalPhaseMeta.isPreferGenreListPresent()){
            return applicationContextProvider.getContext().getBean(PreferGenreThemePanelAssembly.class);
        }

        return applicationContextProvider.getContext().getBean(OperationTpoPanelAssembly.class);

    }

    public PanelAssembly getV2RecommendPanelAssembly(RecommendPanelContentType recommendPanelContentType){

        switch (recommendPanelContentType){
            case AFLO:
                return applicationContextProvider.getContext().getBean(AfloPanelAssembly.class);
            // 나를 위한 FLO
            case RC_CF_TR:
                return applicationContextProvider.getContext().getBean(ForMeFloPanelAssembly.class);
            // 오늘의 FLO
            case RC_SML_TR:
                return applicationContextProvider.getContext().getBean(TodayFloPanelAssembly.class);
            // 아티스트 FLO
            case RC_ATST_TR:
                return applicationContextProvider.getContext().getBean(ArtistFloPanelAssembly.class);
            default:
                return null;
        }

    }

}
