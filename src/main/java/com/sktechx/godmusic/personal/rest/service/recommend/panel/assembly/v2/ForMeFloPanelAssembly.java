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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendForMeDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.RcmmdForMeTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelSignAssembly;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.RCMMD_CF_TRACK_LIMIT_SIZE;

/**
 * 설명 : 나의 플로 생성기
 *
 * @author 김관효(Kwanhyo Kim)/Music사업팀/SKTECH(kwanhyo.kim@sk.com)
 * @date 2019. 5. 8.
 */

@Component("forMeFloPanelAssembly")
public class ForMeFloPanelAssembly extends PanelSignAssembly {

    private static final Logger log = org.slf4j.LoggerFactory
            .getLogger(ForMeFloPanelAssembly.class);
    private final int FORME_FLO_PANEL_HOME_MAX_SIZE = 6;
    private final int FORME_FLO_PANEL_LIMIT_SIZE = 4;

    public ForMeFloPanelAssembly(){ }

    // 배경 이미지 설정 function
    private final UnaryOperator<List<Panel>> decoratePanelBgImgList =
            panelList -> {
                if (!CollectionUtils.isEmpty(panelList)) {
                    for (int i = 0; i < panelList.size(); i++) {
                        Panel myPanel = panelList.get(i);
                        if (!CollectionUtils.isEmpty(myPanel.getImgList())
                                && myPanel.getImgList().size() >= 2) {
                            ImageInfo tempImageInfo;
                            if ((i % 2) != 0) {
                                tempImageInfo = myPanel.getImgList().get(0);
                            } else {
                                tempImageInfo = myPanel.getImgList().get(1);
                            }
                            myPanel.setImgList(Collections.singletonList(tempImageInfo));
                        }

                    }
                }
                return panelList;
            };

    private final Function<PersonalPhaseMeta, List<Panel>> appendRecommendPanelList =
            personalPhaseMeta -> {
                List<Panel> panelList = new ArrayList<>();
                rcmmdReadServiceFactory.getRcmmdReadService(RecommendPanelContentType.RC_CF_TR)
                        .getRecommendListWithTrackByCharacterNoOrderByDispStartDtime(
                                personalPhaseMeta.getCharacterNo(), FORME_FLO_PANEL_LIMIT_SIZE,
                                RCMMD_CF_TRACK_LIMIT_SIZE, personalPhaseMeta.getOsType()).forEach(
                        recommendDto -> panelList
                                .add(new RcmmdForMeTrackPanel((RecommendForMeDto) recommendDto)));
                return panelList;
            }
    ;

    @Override
    public List<Panel> makeHomePanelListForMainTop(PersonalPhaseMeta personalPhaseMeta){
        return mergePanelList(
                appendRecommendPanelList.andThen(decoratePanelBgImgList).apply(personalPhaseMeta),
                appendPreferenceChartPanel.apply(personalPhaseMeta),
                FORME_FLO_PANEL_HOME_MAX_SIZE
        );

    }

    @Override
    public List<Panel> makeHomePanelListForMainMiddle(Long characterNo, OsType osType){
        PersonalPhaseMeta personalPhaseMeta = new PersonalPhaseMeta();
        personalPhaseMeta.setCharacterNo(characterNo);
        personalPhaseMeta.setOsType(osType);

        return appendRecommendPanelList.apply(personalPhaseMeta);
    }

}

