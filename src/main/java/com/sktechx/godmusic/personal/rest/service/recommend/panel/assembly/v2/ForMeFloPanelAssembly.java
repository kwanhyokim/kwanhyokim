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
import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendForMeDto;
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

    private final Function<PersonalPhaseMeta, List<Panel>> appendRecommendPanelList =
            personalPhaseMeta -> {
                List<Panel> panelList = new ArrayList<>();

                List<? extends RecommendDto> recommendForMeDtoList =
                        rcmmdReadServiceFactory.getRcmmdReadService(RecommendPanelContentType.RC_CF_TR)
                        .getRecommendListWithTrackByCharacterNoOrderByDispStartDtime(
                                personalPhaseMeta.getCharacterNo(), FORME_FLO_PANEL_LIMIT_SIZE,
                                RCMMD_CF_TRACK_LIMIT_SIZE, personalPhaseMeta.getOsType()
                        );

                recommendForMeDtoList.forEach(
                            recommendDto -> {
                                RecommendForMeDto recommendForMeDto =
                                        (RecommendForMeDto) recommendDto;

                                int dispSn =
                                        ((recommendForMeDtoList.indexOf(recommendDto)+1) %2 == 0
                                                    ? 2: 1);

                                panelList.add(new RcmmdForMeTrackPanel(
                                        recommendForMeDto,
                                        getDefaultBgImageList(recommendImageManagementService
                                                .selectRecommendPanelInfoBgImageUrl(
                                                        RecommendPanelContentType.RC_CF_TR,
                                                        recommendForMeDto.getRcmmdMforuId(),
                                                        personalPhaseMeta.getOsType(), dispSn
                                                        ),
                                                personalPhaseMeta.getOsType()
                                        )
                                    )
                                );

                            }
                );
                return panelList;
            }
    ;

    @Override
    public List<Panel> makeHomePanelListForMainTop(PersonalPhaseMeta personalPhaseMeta){
        return mergePanelList(
                appendRecommendPanelList.apply(personalPhaseMeta),
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

