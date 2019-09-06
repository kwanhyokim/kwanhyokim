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
 * 설명 : 선호 장르 패널 생성기
 *
 * @author 김관효(Kwanhyo Kim)/Music사업팀/SKTECH(kwanhyo.kim@sk.com)
 * @date 2019. 5. 8.
 */
@Slf4j
@Service("afloPanelAssembly")
public class AfloPanelAssembly extends PanelSignAssembly {

    private AfloPanelAssembly(){}

    @Override
    protected List<Panel> defaultPanelSetting(PersonalPhaseMeta personalPhaseMeta) {
        return null;
    }

    @Override
    protected void appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta ,final List<Panel> panelList){

        List<Panel> myPanelList = new ArrayList<>();
        List<Panel> chartPanelList = new ArrayList<>();

        appendAfloChannelPanelList(personalPhaseMeta.getCharacterNo(), personalPhaseMeta.getOsType(), myPanelList, 5 );

        appendPreferenceChartPanel(personalPhaseMeta, chartPanelList);

        mergePanelList(panelList, myPanelList, chartPanelList, 7);

        sort(personalPhaseMeta , panelList);

    }

    @Override
    public List<Panel> getRecommendPanelList(Long characterNo, OsType osType) {

        List<Panel> panelList = new ArrayList<>();

        appendAfloChannelPanelList(characterNo, osType, panelList, 4);

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

    public List<Panel> appendAfloChannelPanelList(Long characterNo, OsType osType, final List<Panel> panelList, int panelLimitSize) {

        List<ChnlDto> appendChannelList = Optional.ofNullable(
                channelService.getAfloChannelList(characterNo, panelLimitSize,10, osType))
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

            if( !ObjectUtils.isEmpty(panelContentVo) && !CollectionUtils.isEmpty(panelContentVo.getTrackList())) {
                if(panelContentVo.getTrackCount() != null && panelContentVo.getTrackCount() == 0) {
                    panelContentVo.setTrackCount(panelContentVo.getTrackList().size());
                }

                panelContentVo.setType(RecommendPanelContentType.AFLO);
            }

        }

        return afloChannelPanel;
    }

}

