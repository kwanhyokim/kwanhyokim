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

import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelSignAssembly;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 설명 : 추천 단계 ( 3단계 ) 패널
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 14.
 */

@Slf4j
@Service("recommendPhasePanelAssembly")
public class RecommendPhasePanelAssembly extends PanelSignAssembly {
    int rcmmdCfTrackPanelSize = 2;
    int similarTrackPanelSize = 1;

    int preferGenreSimilarTrackPanelSize = 2;

    @Override
    protected List<Panel> defaultPanelSetting(PersonalPhaseMeta personalPhaseMeta) {
        final List<Panel> panelList = new ArrayList();

        panelAppender.appendRecommendCfTrackPanelList(personalPhaseMeta, panelList, rcmmdCfTrackPanelSize);

        boolean isFillRecommendPanel = false;
        if(rcmmdCfTrackPanelSize > panelList.size()){
            isFillRecommendPanel = true;
        }
        panelAppender.appendPreferGenreSimilarTrackPanelList(personalPhaseMeta, panelList, preferGenreSimilarTrackPanelSize);


        int panelDefaultSize = rcmmdCfTrackPanelSize+similarTrackPanelSize;

        if( panelDefaultSize > panelList.size()){
            panelAppender.appendSimilarTrackPanelList(personalPhaseMeta , panelList ,panelDefaultSize  - panelList.size() );
            if(panelDefaultSize > panelList.size()){
                panelAppender.appendPreferGenreChannelPanelList(personalPhaseMeta, panelList, panelDefaultSize - panelList.size() );
            }
        }else{
            if(isFillRecommendPanel){
                int recommendPanelCount = panelCount(RecommendPanelType.RCMMD_TRACK,panelList);
                int recommendPanelAppendCount = rcmmdCfTrackPanelSize - recommendPanelCount;
                if(recommendPanelAppendCount > 0){
                    panelAppender.appendSimilarTrackPanelList(personalPhaseMeta,panelList,recommendPanelAppendCount);
                    if(panelDefaultSize >= panelList.size()){
                        panelAppender.appendPreferGenreChannelPanelList(personalPhaseMeta, panelList, recommendPanelAppendCount );
                        if(panelDefaultSize >= panelList.size()){
                            panelAppender.appendDefaultPopularChannelPanel(personalPhaseMeta , panelList ,recommendPanelAppendCount);
                        }
                    }
                }
            }
        }
        if(panelDefaultSize > panelList.size()){
            panelAppender.appendDefaultPopularChannelPanel(personalPhaseMeta , panelList ,panelDefaultSize - panelList.size());
        }

        return panelList;
    }

    @Override
    protected void appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta ,final List<Panel> panelList) {
        panelAppender.appendPreferArtistPopularTrackPanel(personalPhaseMeta,panelList);
        panelAppender.appendPreferenceChartPanel(personalPhaseMeta,panelList);

        sort(personalPhaseMeta, panelList);
    }


}
