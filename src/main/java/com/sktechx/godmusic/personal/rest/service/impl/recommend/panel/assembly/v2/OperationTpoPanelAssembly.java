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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.code.OsType;
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

    private OperationTpoPanelAssembly(){}

    @Autowired
    DisplayClient displayClient;

    @Autowired
    private RecommendReadMapper recommendReadMapper;

    @Override
    protected List<Panel> defaultPanelSetting(PersonalPhaseMeta personalPhaseMeta) {

        final List<Panel> panelList = new ArrayList<>();

        List<Panel> myPanelList = new ArrayList<>();
        List<Panel> chartPanelList = new ArrayList<>();

        appendTPOPanel(personalPhaseMeta, myPanelList, 5);

        List<ImageInfo> imageInfoList = recommendReadMapper.selectTpoAndThemeImageList(personalPhaseMeta.getOsType());

        if(CollectionUtils.isEmpty(imageInfoList)){
            imageInfoList = new ArrayList<>();
        }

        if(imageInfoList.size() < 5){
            List<ImageInfo> tempImageInfoList = Arrays.asList(new ImageInfo[5]);
            Collections.fill(tempImageInfoList, imageInfoList.get(0));
            imageInfoList = tempImageInfoList;
        }

        for(int i=0; i<myPanelList.size(); i++) {
            ImageInfo imageInfo = imageInfoList.get(i);
            myPanelList.get(i).setImgList(Arrays.asList(imageInfo));
        }

        appendPreferenceChartPanel(personalPhaseMeta, chartPanelList);

        int panelSize = 7;

        Optional<Panel> liveChartPanel = null;
        Optional<Panel> kidsChartPanel = null;

        if(!CollectionUtils.isEmpty(chartPanelList)){
            liveChartPanel = chartPanelList.stream().filter(panel -> RecommendPanelType.LIVE_CHART.equals(panel.getType())).findFirst();
            kidsChartPanel = chartPanelList.stream().filter(panel -> RecommendPanelType.KIDS_CHART.equals(panel.getType())).findFirst();
        }

        if(!ObjectUtils.isEmpty(liveChartPanel) && liveChartPanel.isPresent()){
            panelSize--;
        }

        if(!ObjectUtils.isEmpty(kidsChartPanel) && kidsChartPanel.isPresent()){
            panelSize--;
        }

        if(myPanelList.size() > panelSize){
            myPanelList = myPanelList.subList(0, panelSize - 1);
        }

        panelList.addAll(myPanelList);

        if(!ObjectUtils.isEmpty(liveChartPanel) && liveChartPanel.isPresent()) {
            panelList.add(0, liveChartPanel.get());
        }

        if(!ObjectUtils.isEmpty(kidsChartPanel) && kidsChartPanel.isPresent()){
            panelList.add(kidsChartPanel.get());
        }


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
                        .sorted(Comparator.comparing(ChnlDto::getCreateDtime).reversed())
                        .forEach(channel -> {
                            try{
                                log.info("XXXXXXX {}", channel);
                                panelList.add( createTPOChannelPanel( channel,personalPhaseMeta ) );
                            }catch(Exception e){
                                log.error("TPO Panel defaultPanelSetting Exception : {}",e.getMessage());
                            }
                        });

            }
        }

    private Panel createTPOChannelPanel(final ChnlDto channel,final PersonalPhaseMeta personalPhaseMeta){

        List<ImageInfo> imageInfoList = recommendReadMapper.selectTpoAndThemeImageList(personalPhaseMeta.getOsType());

        if(CollectionUtils.isEmpty(imageInfoList)){
            imageInfoList = new ArrayList<>();
        }

        Collections.shuffle(imageInfoList);

        if( imageInfoList.size() > 1){
            imageInfoList = Arrays.asList(imageInfoList.get(0));
        }

        TPOChannelPanel tpoChannelPanel = new TPOChannelPanel(channel, imageInfoList);

        if(!ObjectUtils.isEmpty(tpoChannelPanel)){
            tpoChannelPanel.setType(RecommendPanelType.POPULAR_CHANNEL);

            PanelContentVo panelContentVo = tpoChannelPanel.getContent();

            if( !ObjectUtils.isEmpty(panelContentVo) && !CollectionUtils.isEmpty(panelContentVo.getTrackList())) {
                panelContentVo.setTrackCount(panelContentVo.getTrackList().size());
            }

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

