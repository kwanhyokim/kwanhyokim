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
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.RcmmdTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelSignAssembly;
import lombok.extern.slf4j.Slf4j;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.RCMMD_CF_TRACK_DISP_STANDARD_COUNT;
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
        appendRecommendCfTrackPanelList(personalPhaseMeta, panelList, 4);
        appendPreferenceChartPanel(personalPhaseMeta,panelList);

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

                            if(cfTrack.getTrackCount() >= RCMMD_CF_TRACK_DISP_STANDARD_COUNT){

                                panelList.add(createRecommendCfTrackPanel(personalPhaseMeta,cfTrack));
                            }

                        } catch (Exception e) {
                            log.error("RecommendPhasePanelAssembly appendRecommendCfTrackPanelList error : {}", e.getMessage());
                        }
                    });
        }

    }

    private Panel createRecommendCfTrackPanel(final PersonalPhaseMeta personalPhaseMeta,
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

