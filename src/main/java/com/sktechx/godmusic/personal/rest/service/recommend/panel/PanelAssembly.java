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

import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.ImageDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.PopularChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.repository.ChannelMapper;
import com.sktechx.godmusic.personal.rest.repository.CharacterPreferGenreMapper;
import com.sktechx.godmusic.personal.rest.repository.ChartMapper;
import com.sktechx.godmusic.personal.rest.repository.RecommendMapper;
import com.sktechx.godmusic.personal.rest.service.ChannelService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendPanelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * 설명 : 추천 패널 생성기
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 14.
 */
@Slf4j
public abstract class PanelAssembly {

    @Autowired
    protected ChannelService channelService;
    @Autowired
    protected RecommendPanelService recommendPanelService;
    @Autowired
    protected ChannelMapper channelMapper;
    @Autowired
    protected RecommendMapper recommendMapper;

    @Autowired
    protected ChartMapper chartMapper;
    @Autowired
    protected CharacterPreferGenreMapper characterPreferGenreMapper;

    public abstract List<Panel> assembleRecommendPanel(PersonalPhaseMeta personalPhaseMeta);


    protected abstract List<Panel> defaultPanelSetting(PersonalPhaseMeta personalPhaseMeta);

    protected int panelCount(RecommendPanelType recommendPanelType ,final List<Panel> panelList){
        return (int)Optional.ofNullable(panelList).orElse(Collections.emptyList()).stream().filter(panel -> {
            return recommendPanelType.equals(panel.getType());
        }).count();

    }
    protected void sort(final PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList){
        panelList.sort(new Comparator<Panel>() {
            @Override
            public int compare(Panel panel1, Panel panel2) {
                Integer panel1Sn = panel1.getPanelOrderSn(personalPhaseMeta.getFirstPhaseType());
                Integer panel2Sn = panel2.getPanelOrderSn(personalPhaseMeta.getFirstPhaseType());
                return panel1Sn < panel2Sn
                        ? -1 : panel1Sn == panel2Sn? 0 :1 ;
            }
        });

        //TODO : 선호장르를 KIDS만 선택했을 경우 처음에 노출
    }


    protected void appendDefaultPopularChannelPanel(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList, int limitSize) {
        //TODO : 배경 이미지 작업
        List<ImageDto> bgImgList = recommendPanelService.getPanelBackgroundImageList(RecommendPanelType.POPULAR_CHANNEL , personalPhaseMeta.getOsType());
        List<ChnlDto> popularChannelList = channelService.getEditorsPickChannelList(limitSize);

        popularChannelList.stream().forEach(channel -> {
            try{
                panelList.add(new PopularChannelPanel(RecommendPanelType.POPULAR_CHANNEL,channel,bgImgList));
            }catch(Exception e){
                log.error("GuestPhasePanel defaultPanelSetting Exception : {}",e.getMessage());
            }
        });

    }
}
