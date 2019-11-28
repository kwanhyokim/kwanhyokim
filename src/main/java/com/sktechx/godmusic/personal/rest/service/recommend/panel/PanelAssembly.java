/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.recommend.panel;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.ChartDto;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.PopularChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.chart.ChartPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.repository.*;
import com.sktechx.godmusic.personal.rest.service.ChannelService;
import com.sktechx.godmusic.personal.rest.service.ChartService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendPanelService;
import lombok.extern.slf4j.Slf4j;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.POPULAR_CHNL_TRACK_LIMIT_SIZE;

/**
 * 설명 : 추천 패널 생성기
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 14.
 */
@Slf4j
public abstract class PanelAssembly {

    @Autowired
    protected ChartService chartService;
    @Autowired
    protected ChannelService channelService;
    @Autowired
    protected RecommendPanelService recommendPanelService;
    @Autowired
    protected ChannelMapper channelMapper;
    @Autowired
    protected RecommendMapper recommendMapper;

    @Autowired
    public RecommendReadMapper recommendReadMapper;

    @Autowired
    protected ChartMapper chartMapper;
    @Autowired
    protected CharacterPreferGenreMapper characterPreferGenreMapper;

    public abstract List<Panel> assembleRecommendPanel(PersonalPhaseMeta personalPhaseMeta) throws Exception;
    protected abstract List<Panel> defaultPanelSetting(PersonalPhaseMeta personalPhaseMeta);

    public abstract List<Panel> getRecommendPanelList(Long characterNo, OsType osType);

    protected int panelCount(RecommendPanelType recommendPanelType ,final List<Panel> panelList){
        return (int)Optional.ofNullable(panelList).orElse(Collections.emptyList()).stream().filter(panel -> recommendPanelType.equals(panel.getType())).count();

    }
    protected void sort(final PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList){
        panelList.sort((panel1, panel2) -> {
            Integer panel1Sn = panel1.getPanelOrderSn(personalPhaseMeta.getFirstPhaseType());
            Integer panel2Sn = panel2.getPanelOrderSn(personalPhaseMeta.getFirstPhaseType());
            return panel1Sn < panel2Sn
                    ? -1 : panel1Sn == panel2Sn? 0 :1 ;
        });

        pullForwardKidsChartPanel(panelList);

    }


    protected void appendDefaultPopularChannelPanel(final PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList, int panelLimitSize , List<Long> filterChnlIdList) {
        List<ChnlDto> popularChannelList = channelService.getPopularChannelList(panelLimitSize,POPULAR_CHNL_TRACK_LIMIT_SIZE ,personalPhaseMeta.getOsType(),filterChnlIdList);

        if(!CollectionUtils.isEmpty(popularChannelList)){
            popularChannelList
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(channel -> {
                        try{
                            panelList.add( createPopularChannelPanel( channel,personalPhaseMeta ) );
                        }catch(Exception e){
                            log.error("GuestPhasePanel defaultPanelSetting Exception : {}",e.getMessage());
                        }
                    });

        }
    }

    protected List<ImageInfo> getDefaultBgImageList(final List<ImageInfo> imgList , OsType osType){
        if(CollectionUtils.isEmpty(imgList)){
            return recommendPanelService.getRecommendPanelDefaultImageList(osType);
        }else{
            return imgList;
        }
    }

    protected Panel createChartPanel(RecommendPanelType recommendPanelType, OsType osType, int trackLimitSize){

        ChartDto chart = null;

        if(RecommendPanelType.LIVE_CHART.equals(recommendPanelType)){
            chart = chartService.getRealTimeTrackChart(osType,trackLimitSize);
        }else if(RecommendPanelType.KIDS_CHART.equals(recommendPanelType)){
            chart = chartService.getKidsChart(osType,trackLimitSize);
        }
        if(chart != null){
            try{
                return new ChartPanel(recommendPanelType, chart, getDefaultBgImageList(chart.getImgList(),osType));
            }catch(Exception e){
                log.error("create chart panel failed.");
            }
        }
        return null;
    }


    private Panel createPopularChannelPanel(final ChnlDto channel,final PersonalPhaseMeta personalPhaseMeta){
        return new PopularChannelPanel(
                channel ,
                getDefaultBgImageList( channel.getImgList(),personalPhaseMeta.getOsType() )
        );
    }

    private void pullForwardKidsChartPanel(final List<Panel> panelList ){
        if(!CollectionUtils.isEmpty(panelList) && panelList.size() > 1){

            List<Panel> chartPanelList = panelList.stream().filter(panel -> {
                return panel instanceof ChartPanel;
            }).collect(Collectors.toList());

            if(!CollectionUtils.isEmpty(chartPanelList) && chartPanelList.size() == 1 ){
                Panel chartPanel = chartPanelList.get(0);
                if(RecommendPanelType.KIDS_CHART.equals(chartPanel.getType())){
                    if(panelList.removeIf(panel -> panel.equals(chartPanel))){
                        panelList.add(0,chartPanel);
                    }
                }
            }
        }
    }

    protected void mergePanelList(List<Panel> panelList, List<Panel> myPanelList,
            List<Panel> chartPanelList, int panelSize) {
        Optional<Panel> liveChartPanel = Optional.ofNullable(chartPanelList)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(panel -> RecommendPanelType.LIVE_CHART.equals(panel.getType()))
                .findFirst();
        Optional<Panel> kidsChartPanel = Optional.ofNullable(chartPanelList)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(panel -> RecommendPanelType.KIDS_CHART.equals(panel.getType()))
                .findFirst();

        if(liveChartPanel.isPresent()){
            panelSize--;
        }

        if(kidsChartPanel.isPresent()){
            panelSize--;
        }

        if(myPanelList.size() > panelSize){
            myPanelList = myPanelList.subList(0, panelSize - 1);
        }

        panelList.addAll(myPanelList);

        if(liveChartPanel.isPresent()) {
            panelList.add(0, liveChartPanel.get());
        }

        if(kidsChartPanel.isPresent()){
            panelList.add(kidsChartPanel.get());
        }
    }

    protected void putTpoAndThemeImageList(PersonalPhaseMeta personalPhaseMeta,
            List<Panel> myPanelList) {

        List<ImageInfo> imageInfoList =
                Optional.ofNullable(
                        recommendReadMapper.selectTpoAndThemeImageList(personalPhaseMeta.getOsType())
                )
                        .orElseGet(Collections::emptyList)
                        .stream()
                        .collect(Collectors.toList());

        if(CollectionUtils.isEmpty(imageInfoList)){
            return;
        }

        for (int i = 0; i < myPanelList.size(); i++) {
            ImageInfo imageInfo = imageInfoList.get(i);
            myPanelList.get(i).setImgList(Arrays.asList(imageInfo));
        }
    }

}
