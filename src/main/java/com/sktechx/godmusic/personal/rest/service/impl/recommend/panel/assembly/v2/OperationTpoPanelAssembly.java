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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.rest.client.DisplayClient;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.vo.ChannelListResponse;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.TPOChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.repository.RecommendReadMapper;
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

    private OperationTpoPanelAssembly(){}

    @Autowired
    DisplayClient displayClient;

    @Autowired
    private RecommendReadMapper recommendReadMapper;

    @Override
    protected List<Panel> defaultPanelSetting(PersonalPhaseMeta personalPhaseMeta) {

        final List<Panel> panelList = new ArrayList<>();

//        Panel chartPanel = createChartPanel(RecommendPanelType.LIVE_CHART,personalPhaseMeta.getOsType(),PREFER_DISP_CHART_TRACK_LIMIT_SIZE);
//        if(chartPanel != null){
//            panelList.add(0,chartPanel);
//        }

        appendTPOPanel(personalPhaseMeta, panelList, 5);

        return panelList;
    }
    @Override
    public List<Panel> getRecommendPanelList(Long characterNo, OsType osType) {
        return null;
    }

    private void appendTPOPanel(final PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList, int panelLimitSize) {

            CommonApiResponse<ChannelListResponse> chnlDtoCommonApiResponse =  displayClient.getOperationTpoList();
            List<ChnlDto> tpoChnlList = new ArrayList();
            if (chnlDtoCommonApiResponse != null && "2000000".equals(chnlDtoCommonApiResponse.getCode())) {
                if(chnlDtoCommonApiResponse.getData() != null && chnlDtoCommonApiResponse.getData().getList() != null) {
                    tpoChnlList = channelMapper.selectChannelByIds(
                            chnlDtoCommonApiResponse.getData().getList().stream().map( chnl -> chnl.getChnlId()).collect(
                                    Collectors.toList())
                    );

                }
            }

            if(!CollectionUtils.isEmpty(tpoChnlList)){
                tpoChnlList
                        .stream()
                        .filter(Objects::nonNull)
                        .forEach(channel -> {
                            try{
                                log.info("XXXXXXX {}", channel);
                                panelList.add( createPopularChannelPanel( channel,personalPhaseMeta ) );
                            }catch(Exception e){
                                log.error("TPO Panel defaultPanelSetting Exception : {}",e.getMessage());
                            }
                        });

            }
        }

        private Panel createPopularChannelPanel(final ChnlDto channel,final PersonalPhaseMeta personalPhaseMeta){

            List<ImageInfo> imageInfoList = recommendReadMapper.selectTpoAndThemeImageList(personalPhaseMeta.getOsType());

            if(CollectionUtils.isEmpty(imageInfoList)){
                imageInfoList = new ArrayList<>();
            }

            Collections.shuffle(imageInfoList);

            if( imageInfoList.size() > 5){
                imageInfoList = imageInfoList.subList(0,5);
            }

            return new TPOChannelPanel(
                    channel ,
                    imageInfoList
            );
        }

}

