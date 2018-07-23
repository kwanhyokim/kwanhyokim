/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.recommend.panel;

import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.ServiceGenreDto;
import com.sktechx.godmusic.personal.rest.model.dto.ServiceGenreDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 설명 : 추천 패널 생성기
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 14.
 */
@Slf4j
public abstract class RecommendPanelAssembly {
    protected List<Panel> panelList;
    protected Long characterNo;

    //캐릭터별 선호 장르 ( 인기 채널 포함 )
    protected List<ServiceGenreDto> preferenceGenreList;

    //캐릭터별 선호 아티스트
    protected List<ArtistDto> preferenceArtistList;

    public RecommendPanelAssembly(Long characterNo , List<ServiceGenreDto> preferenceGenreList , List<ArtistDto> preferenceArtistList){
        this.characterNo = characterNo;
        this.preferenceGenreList = preferenceGenreList;
        this.preferenceArtistList = preferenceArtistList;

        this.panelList = new ArrayList();
    }

    public List<Panel> assembleRecommendPanel() throws Exception{
        init();
        defaultPanelSetting();
        appendPreferencePanel();
        appendPreferenceChartPanel();
        return panelList;
    }

    protected abstract void init();
    protected abstract void defaultPanelSetting();
    protected abstract void appendPreferencePanel();

    private void appendPreferenceChartPanel(){
    }

}
