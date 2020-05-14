/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.recommend.panel.assembly.v2;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
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

    private final int FORME_FLO_PANEL_HOME_MAX_SIZE = 6;
    private final int FORME_FLO_PANEL_LIMIT_SIZE = 4;

    public ForMeFloPanelAssembly(){}

    @Override
    protected List<Panel> appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta){

        return mergePanelList(
                decorateImgPanelList (
                        appendRecommendCfTrackPanelList(personalPhaseMeta)
                ),
                appendPreferenceChartPanel(personalPhaseMeta),
                FORME_FLO_PANEL_HOME_MAX_SIZE);

    }

    private List<Panel> decorateImgPanelList(List<Panel> panelList){
        if(!CollectionUtils.isEmpty(panelList)){

            for(int i=0; i<panelList.size(); i++){

                Panel myPanel = panelList.get(i);

                if( !CollectionUtils.isEmpty(myPanel.getImgList()) &&
                        myPanel.getImgList().size() >=2 ) {
                    ImageInfo tempImageInfo;

                    if( (i%2) != 0) {
                        tempImageInfo = myPanel.getImgList().get(0);
                    }else{
                        tempImageInfo = myPanel.getImgList().get(1);
                    }

                    myPanel.setImgList(Collections.singletonList(tempImageInfo));
                }

            }
        }

        return panelList;
    }

    private List<Panel> appendRecommendCfTrackPanelList(PersonalPhaseMeta personalPhaseMeta) {

        List<Panel> panelList = new ArrayList<>();

        List<RecommendTrackDto> recommendCfTrackList =
                recommendReadService.getRecommendForMeFloListWithTrackByCharacterNo(
                        personalPhaseMeta.getCharacterNo(),
                        FORME_FLO_PANEL_LIMIT_SIZE,
                        RCMMD_CF_TRACK_LIMIT_SIZE,
                        personalPhaseMeta.getOsType()
                )
                ;

        for(RecommendTrackDto recommendTrackDto :
                recommendCfTrackList
                    .stream()
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparing(RecommendTrackDto::getRcmmdCreateDtime).reversed()).collect(Collectors.toList())) {
            try {
                panelList.add(createRecommendCfTrackPanel(personalPhaseMeta, recommendTrackDto));

            } catch (Exception e) {
                log.error("RecommendPhasePanelAssembly appendRecommendCfTrackPanelList error : {}", e.getMessage());
            }
        }

        return panelList;
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

        return appendRecommendCfTrackPanelList(personalPhaseMeta);


    }

}

