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

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.client.DisplayClient;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.vo.ChannelListResponse;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.TPOChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.PanelContentVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
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

    private final DisplayClient displayClient;

    public OperationTpoPanelAssembly(DisplayClient displayClient){
        this.displayClient = displayClient;
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
        CommonApiResponse<ChannelListResponse> chnlDtoCommonApiResponse = displayClient.getOperationTpoList();

        Optional.ofNullable(chnlDtoCommonApiResponse)
                .ifPresent(channelListResponseCommonApiResponse -> {
                    if ("2000000".equals(chnlDtoCommonApiResponse.getCode())) {
                        Optional.ofNullable(channelMapper.selectChannelByIds(Optional.ofNullable(
                                Optional.ofNullable(channelListResponseCommonApiResponse.getData())
                                        .orElseThrow(() -> new CommonBusinessException(
                                                CommonErrorDomain.EMPTY_DATA)).getList())
                                .orElseThrow(() -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA)).stream()
                                .map(ChnlDto::getChnlId).collect(Collectors.toList())))
                                .orElseThrow(() -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA)).stream()
                                .filter(Objects::nonNull)
                                .sorted(Comparator.comparing(ChnlDto::getCreateDtime).reversed())
                                .collect(Collectors.toList())
                                .forEach(channel -> {
                                    try {
                                        panelList.add(createTPOChannelPanel(channel,
                                                personalPhaseMeta));
                                    } catch (Exception e) {
                                        log.error("TPO Panel defaultPanelSetting Exception : {}", e.getMessage());
                                    }
                                });
                    }

                });
        return panelList;
    }

    private Panel createTPOChannelPanel(final ChnlDto channel,final PersonalPhaseMeta personalPhaseMeta){

        TPOChannelPanel tpoChannelPanel = new TPOChannelPanel(channel,

                getDefaultBgImageList(
                getTpoAndThemeBackgroundImageList(personalPhaseMeta.getOsType())
                        ,
                        personalPhaseMeta.getOsType()
                )
        );
        tpoChannelPanel.setType(RecommendPanelType.POPULAR_CHANNEL);
        PanelContentVo panelContentVo = tpoChannelPanel.getContent();

        if( !ObjectUtils.isEmpty(panelContentVo) && !CollectionUtils.isEmpty(panelContentVo.getTrackList())) {
            panelContentVo.setTrackCount(panelContentVo.getTrackList().size());
        }

        return tpoChannelPanel;
    }


}

