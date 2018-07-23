/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.impl.recommend.panel;

import com.sktechx.godmusic.personal.rest.model.dto.*;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.RecommendPanelAssembly;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 설명 : 방문 단계 패널 생성기
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 14.
 */
@Slf4j
public class VisitPhasePanelAssembly extends RecommendPanelAssembly {

    public VisitPhasePanelAssembly(Long characterNo, List<ServiceGenreDto> preferenceGenreList , List<ArtistDto> preferenceArtistList) {
        super(characterNo , preferenceGenreList , preferenceArtistList);
    }

    @Override
    protected void init(){
    }
    @Override
    protected void defaultPanelSetting() {
    }
    @Override
    protected void appendPreferencePanel(){

    }

}
