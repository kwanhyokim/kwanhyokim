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
import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.*;

/**
 * 설명 : 추천 단계 ( 3단계 ) 패널
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 14.
 */

@Slf4j
@Service("recommendPhasePanelAssembly")
public class RecommendPhasePanelAssembly extends PanelSignAssembly {

    private RecommendPhasePanelAssembly(){}
    @Override
    protected List<Panel> defaultPanelSetting(PersonalPhaseMeta personalPhaseMeta) {
        final List<Panel> panelList = new ArrayList();

        appendRecommendCfTrackPanelList(personalPhaseMeta, panelList, RCMMD_CF_PANEL_DEFAULT_SIZE);

        boolean isFillRecommendPanel = false;
        if(RCMMD_CF_PANEL_DEFAULT_SIZE > panelList.size()){
            isFillRecommendPanel = true;
        }
        appendPreferGenreSimilarTrackPanelList(personalPhaseMeta, panelList, PREFER_GENRE_SIMILAR_TRACK_PANEL_APPEND_SIZE);

        int panelDefaultSize = RCMMD_CF_PANEL_DEFAULT_SIZE+SIMILAR_TRACK_PANEL_APPEND_SIZE;

        if( panelDefaultSize > panelList.size()){
            appendSimilarTrackPanelList(personalPhaseMeta , panelList ,panelDefaultSize  - panelList.size() );
            if(panelDefaultSize > panelList.size()){
                appendPreferGenreChannelPanelList(personalPhaseMeta, panelList, panelDefaultSize - panelList.size() );
            }
        }else{
            if(isFillRecommendPanel){
                int recommendPanelCount = panelCount(RecommendPanelType.RCMMD_TRACK,panelList);
                int recommendPanelAppendCount = RCMMD_CF_PANEL_DEFAULT_SIZE - recommendPanelCount;
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
                    recommendMapper.selectRecommendCfTrackListByIdList(rcmmdIdList, RCMMD_CF_PANEL_DEFAULT_SIZE, RCMMD_CF_TRACK_LIMIT_SIZE, personalPhaseMeta.getOsType());
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
                                panelList.add(new RcmmdTrackPanel(RecommendPanelType.RCMMD_TRACK, cfTrack, getDefaultBgImageList( cfTrack.getImgList() , personalPhaseMeta.getOsType())));
                            } catch (Exception e) {
                                log.error("RecommendPhasePanelAssembly appendRecommendCfTrackPanelList error : {}", e.getMessage());
                                e.printStackTrace();
                            }
                        });
            }


        }

    }


}
