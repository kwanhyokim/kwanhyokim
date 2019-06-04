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

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.RcmmdTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelSignAssembly;
import lombok.extern.slf4j.Slf4j;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.RCMMD_CF_TRACK_LIMIT_SIZE;

/**
 * 설명 : 나의 플로 생성기
 *
 * @author 김관효(Kwanhyo Kim)/Music사업팀/SKTECH(kwanhyo.kim@sk.com)
 * @date 2019. 5. 8.
 */
@Slf4j
@Service("forMeFloPanelAssembly")
public class ForMeFloPanelAssembly extends PanelSignAssembly {

    private ForMeFloPanelAssembly(){}

    @Override
    protected List<Panel> defaultPanelSetting(PersonalPhaseMeta personalPhaseMeta) {
        final List<Panel> panelList = new ArrayList<>();

        return panelList;
    }

    @Override
    protected void appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta ,final List<Panel> panelList){

        List<Panel> myPanelList = new ArrayList<>();
        List<Panel> chartPanelList = new ArrayList<>();

        appendRecommendCfTrackPanelList(personalPhaseMeta, myPanelList, 4);
        appendPreferenceChartPanel(personalPhaseMeta, chartPanelList);

        if(!CollectionUtils.isEmpty(myPanelList)){

            for(int i=0; i<myPanelList.size(); i++){

                if( !CollectionUtils.isEmpty(myPanelList.get(i).getImgList()) && myPanelList.get(i).getImgList().size() >=2 ) {
                    myPanelList.get(i).setImgList(
                            Arrays.asList(myPanelList.get(i).getImgList().get(i%2)));
                }

            }
        }

        Optional<Panel> liveChartPanel = null;
        Optional<Panel> kidsChartPanel = null;

        if(!CollectionUtils.isEmpty(chartPanelList)){
            liveChartPanel = chartPanelList.stream().filter(panel -> RecommendPanelType.LIVE_CHART.equals(panel.getType())).findFirst();
            kidsChartPanel = chartPanelList.stream().filter(panel -> RecommendPanelType.KIDS_CHART.equals(panel.getType())).findFirst();
        }

        panelList.addAll(myPanelList);

        if(!ObjectUtils.isEmpty(liveChartPanel) && liveChartPanel.isPresent()) {
            panelList.add(0, liveChartPanel.get());
        }

        if(!ObjectUtils.isEmpty(kidsChartPanel) &&kidsChartPanel.isPresent()){
            panelList.add(kidsChartPanel.get());
        }

        sort(personalPhaseMeta , panelList);

    }

    public void appendRecommendCfTrackPanelList(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList, int panelLimitSize) {

        List<RecommendTrackDto> recommendCfTrackList =
                recommendReadMapper.selectRecommendCfTrackListByCharacterNo(personalPhaseMeta.getCharacterNo(), panelLimitSize, RCMMD_CF_TRACK_LIMIT_SIZE, personalPhaseMeta.getOsType());

        if (!CollectionUtils.isEmpty(recommendCfTrackList)) {
            recommendCfTrackList
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(cfTrack -> {
                        try {

                            RcmmdTrackPanel rcmmdTrackPanel = createRecommendCfTrackPanel(personalPhaseMeta,cfTrack);
                            rcmmdTrackPanel.makeSeedInfo();

                            panelList.add(rcmmdTrackPanel);

                        } catch (Exception e) {
                            log.error("RecommendPhasePanelAssembly appendRecommendCfTrackPanelList error : {}", e.getMessage());
                        }
                    });
        }

    }

    private RcmmdTrackPanel createRecommendCfTrackPanel(final PersonalPhaseMeta personalPhaseMeta,
            final RecommendTrackDto cfTrack){

        return new RcmmdTrackPanel(cfTrack, getDefaultBgImageList( cfTrack.getImgList() , personalPhaseMeta.getOsType()));
    }

    @Override
    public List<Panel> getRecommendPanelList(Long characterNo, OsType osType){
        PersonalPhaseMeta personalPhaseMeta = new PersonalPhaseMeta();
        personalPhaseMeta.setCharacterNo(characterNo);
        personalPhaseMeta.setOsType(osType);

        List<Panel> panelList = new ArrayList<>();

        appendRecommendCfTrackPanelList(personalPhaseMeta, panelList, 7);

        return panelList;

    }

}

