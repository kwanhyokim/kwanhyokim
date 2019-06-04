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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
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

        return panelList;
    }

    @Override
    protected void appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta ,final List<Panel> panelList){

        List<Panel> myPanelList = new ArrayList<>();
        List<Panel> chartPanelList = new ArrayList<>();

        appendPreferGenreChannelPanelList(personalPhaseMeta, myPanelList, 7 );

        List<ImageInfo> imageInfoList = recommendReadMapper.selectTpoAndThemeImageList(personalPhaseMeta.getOsType());

        if(CollectionUtils.isEmpty(imageInfoList)){
            imageInfoList = new ArrayList<>();
        }

        Collections.shuffle(imageInfoList);

        if( imageInfoList.size() > 5){
            imageInfoList = imageInfoList.subList(0,5);
        }

        List<ImageInfo> finalImageInfoList = imageInfoList;

        myPanelList.stream().forEach(x-> {
            x.setImgList(finalImageInfoList);
        });

        appendPreferenceChartPanel(personalPhaseMeta, chartPanelList);

        int panelSize = 7;

        Optional<Panel> liveChartPanel = null;
        Optional<Panel> kidsChartPanel = null;

        if(!CollectionUtils.isEmpty(chartPanelList)){
            liveChartPanel = chartPanelList.stream().filter(panel -> RecommendPanelType.LIVE_CHART.equals(panel.getType())).findFirst();
            kidsChartPanel = chartPanelList.stream().filter(panel -> RecommendPanelType.KIDS_CHART.equals(panel.getType())).findFirst();
        }

        if(liveChartPanel.isPresent()){
            panelSize--;
        }

        if(kidsChartPanel.isPresent()){
            panelSize--;
        }

        if(myPanelList.size() > panelSize){
            myPanelList = myPanelList.subList(0, panelSize - 1);
        }

        panelList.addAll(myPanelList);

        if(liveChartPanel.isPresent()) {
            panelList.add(0, liveChartPanel.get());
        }

        if(kidsChartPanel.isPresent()){
            panelList.add(kidsChartPanel.get());
        }

        sort(personalPhaseMeta , panelList);

    }

    @Override
    public List<Panel> getRecommendPanelList(Long characterNo, OsType osType) {
        return null;
    }

}

