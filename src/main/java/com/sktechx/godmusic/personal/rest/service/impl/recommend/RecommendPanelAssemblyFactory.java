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
    VisitPhasePanelAssembly visitPhasePanelAssembly;

    @Autowired
    ListenPhasePanelAssembly listenPhasePanelAssembly;

    @Autowired
    RecommendPhasePanelAssembly recommendPhasePanelAssembly;

    @Autowired
    GuestPhasePanelAssembly guestPhasePanelAssembly;

    @Autowired
    ForMeFloPanelAssembly forMeFloPanelAssembly;

    @Autowired
    ArtistFloPanelAssembly artistFloPanelAssembly;

    @Autowired
    TodayFloPanelAssembly todayFloPanelAssembly;

    @Autowired
    AfloPanelAssembly afloPanelAssembly;

    @Autowired
    PreferGenreThemePanelAssembly preferGenreThemePanelAssembly;

    @Autowired
    OperationTpoPanelAssembly operationTpoPanelAssembly;

    public PanelAssembly getRecommendPanelAssembly(PersonalPhaseType personalPhaseType ){
        if(PersonalPhaseType.VISIT.equals(personalPhaseType)){
            return visitPhasePanelAssembly;
        }else if(PersonalPhaseType.LISTEN.equals(personalPhaseType)){
            return listenPhasePanelAssembly;

        }else if(PersonalPhaseType.RECOMMEND.equals(personalPhaseType)) {
            return recommendPhasePanelAssembly;
        }

        return getRecommendPanelAssembly();
    }

    public PanelAssembly getRecommendPanelAssembly(){
        return guestPhasePanelAssembly;
    }

    public PanelAssembly getV2RecommendPanelAssembly(PersonalPhaseMeta personalPhaseMeta){

        PersonalPanel personalPanel = personalPhaseMeta.getRecommendPersonalPanelTopItem();

        if(!ObjectUtils.isEmpty(personalPanel)) {

            PanelAssembly panelAssembly = getV2RecommendPanelAssembly(personalPanel.getRecommendPanelContentType());

            if(panelAssembly != null){
                return panelAssembly;
            }

        }

        // 선호 장르 테마
        if( personalPhaseMeta.isPreferGenreListPresent()){
            return preferGenreThemePanelAssembly;
        }

        return operationTpoPanelAssembly;

    }

    public PanelAssembly getV2RecommendPanelAssembly(RecommendPanelContentType recommendPanelContentType){

        switch (recommendPanelContentType){
            case AFLO:
                return afloPanelAssembly;
            // 나를 위한 새로운 발견
            case RC_CF_TR:
                return forMeFloPanelAssembly;
            // 오늘의 발견
            case RC_SML_TR:
                return todayFloPanelAssembly;
            // 좋아할만한 아티스트 MIX
            case RC_ATST_TR:
                return artistFloPanelAssembly;
            default:
                return null;
        }

    }

}
