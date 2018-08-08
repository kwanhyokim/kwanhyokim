/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.impl.recommend.panel.assembly;

import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelSignAssembly;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 설명 : 방문 단계 패널 생성기
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 14.
 */
@Slf4j
@Service("visitPhasePanelAssembly")
public class VisitPhasePanelAssembly extends PanelSignAssembly {
    final int preferGenrePopularChannelListSize = 3;

    private VisitPhasePanelAssembly(){}
    @Override
    protected List<Panel> defaultPanelSetting(final PersonalPhaseMeta personalPhaseMeta) {
        final List<Panel> panelList = new ArrayList<>();

        if(!CollectionUtils.isEmpty(personalPhaseMeta.getPreferGenreList())){
            appendPreferGenreChannelPanelList(personalPhaseMeta,panelList,preferGenrePopularChannelListSize);
            if(preferGenrePopularChannelListSize > panelList.size()){
                appendDefaultPopularChannelPanel(personalPhaseMeta, panelList, preferGenrePopularChannelListSize - panelList.size());
            }
        }else{
            appendDefaultPopularChannelPanel(personalPhaseMeta, panelList, preferGenrePopularChannelListSize);
        }

        return panelList;
    }

    @Override
    protected void appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta ,final List<Panel> panelList){
        appendPreferArtistPopularTrackPanel(personalPhaseMeta,panelList);
        appendPreferenceChartPanel(personalPhaseMeta,panelList);
        sort(personalPhaseMeta, panelList);
    }

}
