/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.impl.recommend.panel.assembly.v2;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.personal.common.domain.PreferPropsType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.client.DisplayClient;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.vo.ChannelListResponse;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.TPOChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.PanelContentVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.repository.RecommendReadMapper;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelNonSignAssembly;
import lombok.extern.slf4j.Slf4j;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.PREFER_DISP_CHART_TRACK_LIMIT_SIZE;

/**
 * 설명 : TPO 패널 생성기
 *
 * @author 김관효(Kwanhyo Kim)/Music사업팀/SKTECH(kwanhyo.kim@sk.com)
 * @date 2019. 5. 8.
 */
@Slf4j
@Service("operationTpoPanelAssembly")
public class OperationTpoPanelAssembly extends PanelNonSignAssembly {

    public OperationTpoPanelAssembly(DisplayClient displayClient,
            RecommendReadMapper recommendReadMapper){
        this.displayClient = displayClient;
        this.recommendReadMapper = recommendReadMapper;
    }

    private final DisplayClient displayClient;

    private final RecommendReadMapper recommendReadMapper;

    @Override
    protected List<Panel> defaultPanelSetting(PersonalPhaseMeta personalPhaseMeta) {

        final List<Panel> panelList = new ArrayList<>();

        List<Panel> myPanelList = new ArrayList<>();
        List<Panel> chartPanelList = new ArrayList<>();

        appendTPOPanel(personalPhaseMeta, myPanelList);
        putTpoAndThemeImageList(personalPhaseMeta, myPanelList);
        appendPreferenceChartPanel(personalPhaseMeta, chartPanelList);

        mergePanelList(panelList, myPanelList, chartPanelList, 7);

        return panelList;
    }


    @Override
    public List<Panel> getRecommendPanelList(Long characterNo, OsType osType) {
        return null;
    }

    private void appendTPOPanel(final PersonalPhaseMeta personalPhaseMeta,
            final List<Panel> panelList) {
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
    }


    private Panel createTPOChannelPanel(final ChnlDto channel,final PersonalPhaseMeta personalPhaseMeta){

        List<ImageInfo> imageInfoList =
                Optional.ofNullable(
                        recommendReadMapper.selectTpoAndThemeImageList(personalPhaseMeta.getOsType())
                )
                .orElseGet(Collections::emptyList)
                .stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(ArrayList::new),

                        list -> {
                            Collections.shuffle(list);
                            return list.stream().limit(1).collect(Collectors.toList());
                        }

                ));

        TPOChannelPanel tpoChannelPanel = new TPOChannelPanel(channel, imageInfoList);
        tpoChannelPanel.setType(RecommendPanelType.POPULAR_CHANNEL);
        PanelContentVo panelContentVo = tpoChannelPanel.getContent();

        if( !ObjectUtils.isEmpty(panelContentVo) && !CollectionUtils.isEmpty(panelContentVo.getTrackList())) {
            panelContentVo.setTrackCount(panelContentVo.getTrackList().size());
        }

        return tpoChannelPanel;
    }


    private void appendPreferenceChartPanel(final PersonalPhaseMeta personalPhaseMeta, final List<Panel> panelList) {
        if(!CollectionUtils.isEmpty(personalPhaseMeta.getPreferDispList())) {

            personalPhaseMeta.getPreferDispList()
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(characterPreferDisp -> {

                        RecommendPanelType recommendPanelType = getPreferRecommendPanelType(characterPreferDisp.getDispPropsType());
                        if (recommendPanelType != null) {
                            Panel panel = createChartPanel(recommendPanelType, personalPhaseMeta.getOsType(), PREFER_DISP_CHART_TRACK_LIMIT_SIZE);
                            if (panel != null) {
                                panelList.add(panel);
                            }
                        }

                    });
        }
    }

    private RecommendPanelType getPreferRecommendPanelType(String preferPropsType ){

        if(PreferPropsType.TOP100.getCode().equals(preferPropsType)){
            return RecommendPanelType.LIVE_CHART;
        }else if(PreferPropsType.KIDS100.getCode().equals(preferPropsType)){
            return RecommendPanelType.KIDS_CHART;
        }
        return null;

    }

}

