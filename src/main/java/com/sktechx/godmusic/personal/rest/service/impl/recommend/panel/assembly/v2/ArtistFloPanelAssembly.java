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
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.common.util.BooleanComparator;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendArtistDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.artist.ArtistPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelSignAssembly;
import lombok.extern.slf4j.Slf4j;

/**
 * 설명 : 아티스트 플로 생성기
 *
 * @author 김관효(Kwanhyo Kim)/Music사업팀/SKTECH(kwanhyo.kim@sk.com)
 * @date 2019. 5. 8.
 */

@Slf4j
@Service("artistFloPanelAssembly")
public class ArtistFloPanelAssembly extends PanelSignAssembly {

    private ArtistFloPanelAssembly(){}

    @Override
    protected List<Panel> defaultPanelSetting(PersonalPhaseMeta personalPhaseMeta) {
        final List<Panel> panelList = new ArrayList<>();

        return panelList;
    }
    @Override
    protected void appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta ,final List<Panel> panelList){

        List<Panel> myPanelList = new ArrayList<>();
        List<Panel> chartPanelList = new ArrayList<>();

        appendPreferArtistPopularTrackPanel(personalPhaseMeta, myPanelList);
        appendPreferenceChartPanel(personalPhaseMeta, chartPanelList);

        int panelSize = 7;

        Optional<Panel> liveChartPanel = null;
        Optional<Panel> kidsChartPanel = null;

        if(!CollectionUtils.isEmpty(chartPanelList)){
            liveChartPanel = chartPanelList.stream().filter(panel -> RecommendPanelType.LIVE_CHART.equals(panel.getType())).findFirst();
            kidsChartPanel = chartPanelList.stream().filter(panel -> RecommendPanelType.KIDS_CHART.equals(panel.getType())).findFirst();
        }

        if(!ObjectUtils.isEmpty(liveChartPanel) && liveChartPanel.isPresent()){
            panelSize--;
        }

        if(!ObjectUtils.isEmpty(kidsChartPanel) && kidsChartPanel.isPresent()){
            panelSize--;
        }

        if(myPanelList.size() > panelSize){
            myPanelList = myPanelList.subList(0, panelSize - 1);
        }

        panelList.addAll(myPanelList);

        if(!ObjectUtils.isEmpty(liveChartPanel) && liveChartPanel.isPresent()) {
            panelList.add(0, liveChartPanel.get());
        }

        if(!ObjectUtils.isEmpty(kidsChartPanel) && kidsChartPanel.isPresent()){
            panelList.add(kidsChartPanel.get());
        }

        sort(personalPhaseMeta , panelList);

    }

    @Override
    protected void appendPreferArtistPopularTrackPanel(final PersonalPhaseMeta personalPhaseMeta, final List<Panel> panelList) {

        List<RecommendArtistDto> recommendArtistDtoList = recommendReadMapper.selectRecommendArtistByCharacterNo(personalPhaseMeta.getCharacterNo());

        recommendArtistDtoList.stream().forEach(

                recommendArtistDto ->
                {
        if (recommendArtistDto != null && !CollectionUtils.isEmpty(recommendArtistDto.getArtistList())) {
            try {

                recommendArtistDto.getArtistList().sort(
                        (ArtistDto a, ArtistDto b) -> (BooleanComparator.TRUE_HIGH.compare(a.hasDefaultImage(), b.hasDefaultImage()))
                );

                ArtistPanel artistPanel = new ArtistPanel(recommendArtistDto);
                artistPanel.makeSeedInfo();

                panelList.add(artistPanel);

            } catch (Exception e) {
                log.error("PanelSignAssembly appendPreferArtistPanel artistPanel create error : {}", e.getMessage());
            }
        }});
    }


    @Override
    public List<Panel> getRecommendPanelList(Long characterNo, OsType osType){
        PersonalPhaseMeta personalPhaseMeta = new PersonalPhaseMeta();
        personalPhaseMeta.setCharacterNo(characterNo);
        personalPhaseMeta.setOsType(osType);

        List<Panel> panelList = new ArrayList<>();

        appendPreferArtistPopularTrackPanel(personalPhaseMeta, panelList);

        return panelList;

    }
}

