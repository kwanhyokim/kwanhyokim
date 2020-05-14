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
import org.springframework.util.ObjectUtils;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.AfloChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.PanelContentVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelSignAssembly;
import lombok.extern.slf4j.Slf4j;

/**
 * 설명 : AFLO 홈 패널 생성기
 *
 * @author 김관효(Kwanhyo Kim)/Music사업팀/SKTECH(kwanhyo.kim@sk.com)
 * @since 2019. 5. 8.
 */
@Slf4j
@Service("afloPanelAssembly")
public class AfloPanelAssembly extends PanelSignAssembly {

    private final int AFLO_PANEL_HOME_LIMIT_SIZE = 5;
    private final int AFLO_PANEL_HOME_MAX_SIZE = 7;
    private final int AFLO_PANEL_DETAIL_LIMIT_SIZE = 4;
    private final int AFLO_PANEL_CHNL_TRACK_MAX_SIZE = 10;

    public AfloPanelAssembly(){}

    @Override
    protected List<Panel> appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta){

        return mergePanelList(
                appendAfloChannelPanelList(personalPhaseMeta.getCharacterNo(),
                    personalPhaseMeta.getOsType(), AFLO_PANEL_HOME_LIMIT_SIZE),
                appendPreferenceChartPanel(personalPhaseMeta),
                AFLO_PANEL_HOME_MAX_SIZE);
    }

    @Override
    public List<Panel> getRecommendPanelList(Long characterNo, OsType osType) {

        List<Panel> panelList =  appendAfloChannelPanelList(characterNo, osType,
                AFLO_PANEL_DETAIL_LIMIT_SIZE);

        // 패널의 트랙리스트에서 대표곡을 제외하고 정보 제거
        if(!CollectionUtils.isEmpty(panelList)){

            for(Panel panel : panelList){

                PanelContentVo panelContentVo = panel.getContent();

                panelContentVo.setTrackList(
                        panelContentVo.getTrackList().stream()
                                .filter(trackDto -> "Y".equals(trackDto.getTitleYn()))
                                .collect(Collectors.toList())
                );

                panelContentVo.setType(RecommendPanelContentType.AFLO);

            }
        }

        return panelList;
    }

    public List<Panel> appendAfloChannelPanelList(Long characterNo, OsType osType, int panelLimitSize) {

        final List<Panel> panelList = new ArrayList<>();

        List<ChnlDto> appendChannelList = Optional.ofNullable(
                channelService.getAfloChannelList(characterNo, panelLimitSize,
                        AFLO_PANEL_CHNL_TRACK_MAX_SIZE, osType))
                .orElseGet(Collections::emptyList);


        for(ChnlDto chnlDto : appendChannelList) {

            try {
                panelList.add(createAfloChannelPanel(chnlDto));
            } catch (Exception e) {
                log.error("appendPreferGenreChannelPanelList error : {}", e.getMessage());
            }
        }

        return panelList;
    }

    private Panel createAfloChannelPanel(final ChnlDto channel){

        AfloChannelPanel afloChannelPanel = new AfloChannelPanel(channel, channel.getImgList());

        if(!ObjectUtils.isEmpty(afloChannelPanel)){
            PanelContentVo panelContentVo = afloChannelPanel.getContent();

            if( !ObjectUtils.isEmpty(panelContentVo) &&
                    !CollectionUtils.isEmpty(panelContentVo.getTrackList())
            ) {
                if(panelContentVo.getTrackCount() != null && panelContentVo.getTrackCount() == 0) {
                    panelContentVo.setTrackCount(panelContentVo.getTrackList().size());
                }

                panelContentVo.setType(RecommendPanelContentType.AFLO);
            }

        }

        return afloChannelPanel;
    }

}

