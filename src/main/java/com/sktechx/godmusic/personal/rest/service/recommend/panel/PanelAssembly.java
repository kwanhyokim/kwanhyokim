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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.CollectionUtils;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.utils.ServiceUtils;
import com.sktechx.godmusic.personal.common.domain.PreferPropsType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartDto;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.PopularChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.chart.ChartPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.chart.PrivateFloChartPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.chart.PrivateKidsChartPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.repository.*;
import com.sktechx.godmusic.personal.rest.service.ChannelService;
import com.sktechx.godmusic.personal.rest.service.chart.ChartService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendReadService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.POPULAR_CHNL_TRACK_LIMIT_SIZE;
import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.PREFER_DISP_CHART_TRACK_LIMIT_SIZE;

/**
 * 설명 : 추천 패널 생성기
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 14.
 */
@Slf4j
public abstract class PanelAssembly {

    @Autowired
    @Qualifier("chartService")
    protected ChartService chartService;

    @Autowired
    @Qualifier("mongoChartService")
    protected ChartService mongoChartService;

    @Autowired
    protected ChannelService channelService;

    @Autowired
    @Lazy
    protected RecommendReadService recommendReadService;

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

    @Getter
    @Setter
    public String appVersion;

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
            return panel1Sn.compareTo(panel2Sn);
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
            return recommendReadService.getRecommendPanelDefaultImageList(osType);
        }else{
            return imgList;
        }
    }

    protected Panel createChartPanel(RecommendPanelType recommendPanelType, OsType osType, int trackLimitSize){

        ChartDto chart = null;

        if(RecommendPanelType.LIVE_CHART.equals(recommendPanelType)){
            chart = chartService.getChartByDispPropsTypeWithTrackList(null,
                    "TOP100",
                    osType,
                    trackLimitSize);

        }else if(RecommendPanelType.KIDS_CHART.equals(recommendPanelType)){
            chart = chartService.getChartByDispPropsTypeWithTrackList(null,
                    "KIDS100",
                    osType,
                    trackLimitSize);
        }
        if(chart != null){
            try{
                return new ChartPanel(recommendPanelType, chart,
                        getDefaultBgImageList(chart.getImgList(), osType));

            }catch(Exception e){
                log.error("create chart panel failed.");
            }
        }
        return null;
    }

    protected Panel createPrivateChartPanel(Long characterNo, RecommendPanelType recommendPanelType,
            OsType osType, int trackLimitSize){

        ChartDto chart;

        if(RecommendPanelType.PRI_LIVE_CHART.equals(recommendPanelType)){
            chart = mongoChartService.getChartByDispPropsTypeWithTrackList(characterNo,
                    "TOP100",
                    osType,
                    trackLimitSize);

            return new PrivateFloChartPanel(
                    recommendPanelType, chart,
                    getDefaultBgImageList(chart.getImgList(), osType)
            );
        }else if(RecommendPanelType.PRI_KIDS_CHART.equals(recommendPanelType)){
            chart = mongoChartService.getChartByDispPropsTypeWithTrackList(characterNo,
                    "KIDS100",
                    osType,
                    trackLimitSize);

            return new PrivateKidsChartPanel(
                    recommendPanelType, chart,
                    getDefaultBgImageList(chart.getImgList(), osType)
            );
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

            List<Panel> chartPanelList = panelList.stream().filter(panel -> panel instanceof ChartPanel).collect(Collectors.toList());

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

    protected void mergePanelList(List<Panel> panelList, List<Panel> myPanelList, List<Panel> chartPanelList, int panelMaxSize) {

        Optional<Panel> liveChartPanel = Optional.ofNullable(chartPanelList)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(
                        panel ->
                                RecommendPanelType.LIVE_CHART.equals(panel.getType()) ||
                                        RecommendPanelType.PRI_LIVE_CHART.equals(panel.getType())
                )
                .findFirst();
        Optional<Panel> kidsChartPanel = Optional.ofNullable(chartPanelList)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(
                        panel -> RecommendPanelType.KIDS_CHART.equals(panel.getType()) ||
                                RecommendPanelType.PRI_KIDS_CHART.equals(panel.getType())
                )
                .findFirst();

        if(liveChartPanel.isPresent()){
            panelMaxSize--;
        }

        if(kidsChartPanel.isPresent()){
            panelMaxSize--;
        }

        panelList.addAll(
                myPanelList.stream().limit(panelMaxSize).collect(Collectors.toList())
        );

        liveChartPanel.ifPresent(panel -> panelList.add(0, panel));
        kidsChartPanel.ifPresent(panelList::add);
    }

    protected List<ImageInfo> getTpoAndThemeBackgroundImageList(OsType osType) {

        return
                Optional.ofNullable(
                        recommendReadMapper.selectTpoAndThemeImageList(osType)
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
    }

    public void appendPreferenceChartPanel(final PersonalPhaseMeta personalPhaseMeta, final List<Panel> panelList) {
        if(!CollectionUtils.isEmpty(personalPhaseMeta.getPreferDispList())) {

            personalPhaseMeta.getPreferDispList()
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(characterPreferDisp -> {

                        Integer appVersion = ServiceUtils.getFormattedAppVersion(getAppVersion());
                        Panel panel;

                        // 4.15.0 이하
                        if(appVersion.compareTo(41500) < 0){

                            panel = createChartPanel(
                                    (
                                            PreferPropsType.TOP100.getCode().equals(
                                                    characterPreferDisp.getDispPropsType()
                                            )
                                                    ?
                                                    RecommendPanelType.LIVE_CHART
                                                    :
                                                    RecommendPanelType.KIDS_CHART
                                    ),

                                    personalPhaseMeta.getOsType(), PREFER_DISP_CHART_TRACK_LIMIT_SIZE
                            );


                        }else {
                            panel = createPrivateChartPanel(
                                    personalPhaseMeta.getCharacterNo(),
                                    (
                                            PreferPropsType.TOP100.getCode().equals(
                                                    characterPreferDisp.getDispPropsType()
                                            )
                                                    ?
                                                    RecommendPanelType.PRI_LIVE_CHART
                                                    :
                                                    RecommendPanelType.PRI_KIDS_CHART
                                    ),
                                    personalPhaseMeta.getOsType(), PREFER_DISP_CHART_TRACK_LIMIT_SIZE
                            );

                        }

                        panelList.add(panel);

                    });
        }
    }

}
