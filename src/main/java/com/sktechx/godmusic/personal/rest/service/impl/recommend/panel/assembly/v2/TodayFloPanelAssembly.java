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
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.PreferSimilarTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelSignAssembly;
import lombok.extern.slf4j.Slf4j;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.SIMILAR_TRACK_DISP_STANDARD_COUNT;
import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.SIMILAR_TRACK_LIMIT_SIZE;

/**
 * 설명 : 오늘의 플로 생성기
 *
 * @author 김관효(Kwanhyo Kim)/Music사업팀/SKTECH(kwanhyo.kim@sk.com)
 * @date 2019. 5. 8.
 */
@Slf4j
@Service("todayFloPanelAssembly")
public class TodayFloPanelAssembly extends PanelSignAssembly {

    private TodayFloPanelAssembly(){}

    @Override
    protected List<Panel> defaultPanelSetting(PersonalPhaseMeta personalPhaseMeta) {
        final List<Panel> panelList = new ArrayList<>();


        return panelList;
    }
    @Override
    protected void appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta ,final List<Panel> panelList){
        appendSimilarTrackPanelList(personalPhaseMeta, panelList, 7);
        appendPreferenceChartPanel(personalPhaseMeta,panelList);
        sort(personalPhaseMeta , panelList);

    }

    protected void appendSimilarTrackPanelList(final PersonalPhaseMeta personalPhaseMeta,
            final List<Panel> panelList,
            int panelLimitSize) {

        List<RecommendTrackDto> similarTrackList =
                recommendReadMapper.selectRecommendSimilarTrackListByCharacterNo(personalPhaseMeta.getCharacterNo(), panelLimitSize,
                        SIMILAR_TRACK_LIMIT_SIZE, personalPhaseMeta.getOsType());

        if(!CollectionUtils.isEmpty(similarTrackList)){
            similarTrackList
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(similarTrack ->{
                        try {
                            if(similarTrack.getTrackCount() >= SIMILAR_TRACK_DISP_STANDARD_COUNT){
                                PreferSimilarTrackPanel panel = (PreferSimilarTrackPanel) createSimilarTrackPanel (personalPhaseMeta, similarTrack);

                                panel.makeInfoV2();

                                panelList.add(panel);
                            }
                        } catch (Exception e) {
                            log.error("appendSimilarTrackPanelList error : {}", e.getMessage());
                        }
                    });
        }
    }

    @Override
    public List<Panel> getRecommendPanelList(Long characterNo, OsType osType){
        PersonalPhaseMeta personalPhaseMeta = new PersonalPhaseMeta();
        personalPhaseMeta.setCharacterNo(characterNo);
        personalPhaseMeta.setOsType(osType);

        List<Panel> panelList = new ArrayList<>();

        appendSimilarTrackPanelList(personalPhaseMeta, panelList, 7);

        return panelList;

    }

}

