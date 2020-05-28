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

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.TPOChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.PanelContentVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.ChannelService;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelNonSignAssembly;
import lombok.extern.slf4j.Slf4j;

/**
 * 설명 : TPO 패널 생성기
 *
 * @author 김관효(Kwanhyo Kim)/Music사업팀/SKTECH(kwanhyo.kim@sk.com)
 * @date 2019. 5. 8.
 */
@Slf4j
@Service("operationTpoPanelAssembly")
public class OperationTpoPanelAssembly extends PanelNonSignAssembly {

    private final int OPERATION_TPO_PANEL_HOME_MAX_SIZE = 7;

    private final ChannelService channelService;

    public OperationTpoPanelAssembly(ChannelService channelService){
        this.channelService = channelService;
    }

    @Override
    public List<Panel> makeHomePanelListForMainTop(PersonalPhaseMeta personalPhaseMeta) {
        return mergePanelList(
                appendTPOPanel(personalPhaseMeta),
                appendPreferenceChartPanel.apply(personalPhaseMeta),
                OPERATION_TPO_PANEL_HOME_MAX_SIZE);
    }
    @Override
    public List<Panel> makeHomePanelListForMainMiddle(Long characterNo, OsType osType) {
        return null;
    }

    private List<Panel> appendTPOPanel(final PersonalPhaseMeta personalPhaseMeta) {
        List<Panel> panelList = new ArrayList<>();

        channelService.getOperationTpoChannelList()
        .forEach(channel -> {
            try {
                panelList.add(createTPOChannelPanel(channel, personalPhaseMeta));
            } catch (Exception e) {
                log.error("TPO Panel defaultPanelSetting Exception : {}", e.getMessage());
            }
        });

        return panelList;
    }

    private Panel createTPOChannelPanel(final ChnlDto channel,final PersonalPhaseMeta personalPhaseMeta){

        TPOChannelPanel tpoChannelPanel = new TPOChannelPanel(channel,
                getTpoAndThemeBackgroundImageList(personalPhaseMeta.getOsType())
        );
        tpoChannelPanel.setType(RecommendPanelType.POPULAR_CHANNEL);
        PanelContentVo panelContentVo = tpoChannelPanel.getContent();

        if( !ObjectUtils.isEmpty(panelContentVo) && !CollectionUtils.isEmpty(panelContentVo.getTrackList())) {
            panelContentVo.setTrackCount(panelContentVo.getTrackList().size());
        }

        return tpoChannelPanel;
    }


}

