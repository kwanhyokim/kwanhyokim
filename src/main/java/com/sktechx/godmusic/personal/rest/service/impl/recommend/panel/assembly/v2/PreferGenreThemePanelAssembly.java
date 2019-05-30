/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.impl.recommend.panel.assembly.v2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.repository.RecommendReadMapper;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelSignAssembly;
import lombok.extern.slf4j.Slf4j;

/**
 * 설명 : 선호 장르 패널 생성기
 *
 * @author 김관효(Kwanhyo Kim)/Music사업팀/SKTECH(kwanhyo.kim@sk.com)
 * @date 2019. 5. 8.
 */
@Slf4j
@Service("preferGenreThemePanelAssembly")
public class PreferGenreThemePanelAssembly extends PanelSignAssembly {

    private PreferGenreThemePanelAssembly(){}

    @Autowired
    private RecommendReadMapper recommendReadMapper;

    @Override
    protected List<Panel> defaultPanelSetting(PersonalPhaseMeta personalPhaseMeta) {
        final List<Panel> panelList = new ArrayList<>();

        appendPreferGenreChannelPanelList(personalPhaseMeta, panelList, 5 );

        List<ImageInfo> imageInfoList = recommendReadMapper.selectTpoAndThemeImageList(personalPhaseMeta.getOsType());

        for(int i =0; i<panelList.size(); i++){
            ImageInfo imageInfo;

            try {
                imageInfo = imageInfoList.get(i);
            }catch (Exception e){
                imageInfo = imageInfoList.get(0);
            }

            panelList.get(i).setImgList(new ArrayList<>(Arrays.asList(imageInfo)));

        }

        return panelList;
    }

    @Override
    protected void appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta ,final List<Panel> panelList){
        appendPreferenceChartPanel(personalPhaseMeta,panelList);

        sort(personalPhaseMeta , panelList);

    }

    @Override
    public List<Panel> getRecommendPanelList(Long characterNo, OsType osType) {
        return null;
    }

}

