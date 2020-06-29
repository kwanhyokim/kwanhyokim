/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.recommend.panel.assembly;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelNonSignAssembly;
import lombok.extern.slf4j.Slf4j;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.POPULAR_CHNL_LIST_SIZE;
import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.PREFER_DISP_CHART_TRACK_LIMIT_SIZE;
/**
 * 설명 : 비로그인 사용자 패널 생성기
 *       인기 채널 3종 제공 + 실시간 차트
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 24.
 */
@Slf4j
@Service("guestPhasePanelAssembly")
public class GuestPhasePanelAssembly extends PanelNonSignAssembly {

    private GuestPhasePanelAssembly(){}

    @Override
    public List<Panel> makeHomePanelListForMainTop(PersonalPhaseMeta personalPhaseMeta){

        final List<Panel> panelList = new ArrayList<>();

        if(personalPhaseMeta != null && personalPhaseMeta.getOsType() == null){
            personalPhaseMeta.setOsType(OsType.IOS);
        }

        appendDefaultPopularChannelPanel(personalPhaseMeta , panelList , POPULAR_CHNL_LIST_SIZE , null);

        Panel chartPanel = createChartPanel(RecommendPanelType.LIVE_CHART,personalPhaseMeta.getOsType(),PREFER_DISP_CHART_TRACK_LIMIT_SIZE);
        if(chartPanel != null){
            panelList.add(0,chartPanel);
        }
        return panelList;
    }
    @Override
    public List<Panel> makeHomePanelListForMainMiddle(Long characterNo, OsType osType) {
        return null;
    }

}
