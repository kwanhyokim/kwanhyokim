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

import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.PreferSimilarTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.RcmmdTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelSignAssembly;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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

    int rcmmdCfTrackLimitSize = 15;
    private RecommendPhasePanelAssembly(){}
    @Override
    protected List<Panel> defaultPanelSetting(PersonalPhaseMeta personalPhaseMeta) {
        final List<Panel> panelList = new ArrayList();

        appendRecommendCfTrackPanelList(personalPhaseMeta, panelList, rcmmdCfTrackPanelSize);

        boolean isFillRecommendPanel = false;
        if(rcmmdCfTrackPanelSize > panelList.size()){
            isFillRecommendPanel = true;
        }
        appendPreferGenreSimilarTrackPanelList(personalPhaseMeta, panelList, preferGenreSimilarTrackPanelSize);

        int panelDefaultSize = rcmmdCfTrackPanelSize+similarTrackPanelSize;

        if( panelDefaultSize > panelList.size()){
            appendSimilarTrackPanelList(personalPhaseMeta , panelList ,panelDefaultSize  - panelList.size() );
            if(panelDefaultSize > panelList.size()){
                appendPreferGenreChannelPanelList(personalPhaseMeta, panelList, panelDefaultSize - panelList.size() );
            }
        }else{
            if(isFillRecommendPanel){
                int recommendPanelCount = panelCount(RecommendPanelType.RCMMD_TRACK,panelList);
                int recommendPanelAppendCount = rcmmdCfTrackPanelSize - recommendPanelCount;
                if(recommendPanelAppendCount > 0){
                    appendSimilarTrackPanelList(personalPhaseMeta,panelList,recommendPanelAppendCount);
                    if(panelDefaultSize >= panelList.size()){
                        appendPreferGenreChannelPanelList(personalPhaseMeta, panelList, recommendPanelAppendCount );
                        if(panelDefaultSize >= panelList.size()){
                            appendDefaultPopularChannelPanel(personalPhaseMeta , panelList ,recommendPanelAppendCount);
                        }
                    }
                }
            }
        }
        if(panelDefaultSize > panelList.size()){
            appendDefaultPopularChannelPanel(personalPhaseMeta , panelList ,panelDefaultSize - panelList.size());
        }

        return panelList;
    }

    @Override
    protected void appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta ,final List<Panel> panelList) {
        appendPreferArtistPopularTrackPanel(personalPhaseMeta,panelList);
        appendPreferenceChartPanel(personalPhaseMeta,panelList);

        sort(personalPhaseMeta, panelList);
    }

    private void appendRecommendCfTrackPanelList(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList, int limitSize) {
        List<PersonalPanel> rcmmdPanelList = personalPhaseMeta.getRecommendPersonalPanelList(RecommendPanelContentType.RC_CF_TR);

        if (!CollectionUtils.isEmpty(rcmmdPanelList)) {
            List<Long> rcmmdIdList = rcmmdPanelList.stream().map(personalPanel -> personalPanel.getRecommendId()).collect(Collectors.toList());

            List<RecommendTrackDto> recommendCfTrackList =
                    recommendMapper.selectRecommendCfTrackListByIdList(rcmmdIdList, rcmmdCfTrackPanelSize, rcmmdCfTrackLimitSize, personalPhaseMeta.getOsType());
            if (!CollectionUtils.isEmpty(recommendCfTrackList)) {
                recommendCfTrackList
                        .stream()
                        .filter(Objects::nonNull)
                        .forEach(cfTrack -> {

                            Optional<PersonalPanel> personalPanel = rcmmdPanelList.stream()
                                    .filter(panel -> panel.getRecommendId().equals(cfTrack.getRcmmdId()))
                                    .findFirst();
                            if (personalPanel.isPresent()) {
                                cfTrack.setTrackCount(personalPanel.get().getTrackCount());
                            }
                            try {
                                panelList.add(new RcmmdTrackPanel(RecommendPanelType.RCMMD_TRACK, cfTrack, cfTrack.getImgList()));
                            } catch (Exception e) {
                                log.error("RecommendPhasePanelAssembly appendRecommendCfTrackPanelList error : {}", e.getMessage());
                                e.printStackTrace();
                            }
                        });
            }


        }

    }


}
