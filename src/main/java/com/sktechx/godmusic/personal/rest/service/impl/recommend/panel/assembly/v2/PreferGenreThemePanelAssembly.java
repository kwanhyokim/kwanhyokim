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

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.PreferGenrePopularChnlDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.repository.RecommendReadMapper;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelSignAssembly;
import lombok.extern.slf4j.Slf4j;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.POPULAR_CHNL_TRACK_LIMIT_SIZE;

/**
 * 설명 : 선호 장르 패널 생성기
 *
 * @author 김관효(Kwanhyo Kim)/Music사업팀/SKTECH(kwanhyo.kim@sk.com)
 * @date 2019. 5. 8.
 */
@Slf4j
@Service("preferGenreThemePanelAssembly")
public class PreferGenreThemePanelAssembly extends PanelSignAssembly {

    public PreferGenreThemePanelAssembly(){}

    @Autowired
    private RecommendReadMapper recommendReadMapper;

    @Override
    protected List<Panel> defaultPanelSetting(PersonalPhaseMeta personalPhaseMeta) {
        return new ArrayList<>();
    }

    @Override
    protected void appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta ,final List<Panel> panelList){

        List<Panel> myPanelList = new ArrayList<>();
        List<Panel> chartPanelList = new ArrayList<>();

        this.appendPreferGenreChannelPanelList(personalPhaseMeta, myPanelList, 5 );

        List<ImageInfo> imageInfoList = Optional.ofNullable(recommendReadMapper.selectTpoAndThemeImageList(personalPhaseMeta.getOsType()))
                .orElseGet(Collections::emptyList);

        if(imageInfoList.size() > 0 && imageInfoList.size() < 5){
            List<ImageInfo> tempImageInfoList = new ArrayList<>(5);
            Collections.fill(tempImageInfoList, imageInfoList.get(0));
            imageInfoList = tempImageInfoList;
        }

        for(int i=0; i<myPanelList.size(); i++) {
            ImageInfo imageInfo = imageInfoList.get(i);
            myPanelList.get(i).setImgList(Arrays.asList(imageInfo));
        }

        appendPreferenceChartPanel(personalPhaseMeta, chartPanelList);

        mergePanelList(panelList, myPanelList, chartPanelList, 7);

        sort(personalPhaseMeta , panelList);

    }

    @Override
    public List<Panel> getRecommendPanelList(Long characterNo, OsType osType) {
        return null;
    }

    @Override
    protected void appendPreferGenreChannelPanelList(final PersonalPhaseMeta personalPhaseMeta,
            final List<Panel> panelList,
            int panelLimitSize) {

        List<Long> preferGenreIdList = personalPhaseMeta.getPreferGenreIdList(panelLimitSize);

        List<PreferGenrePopularChnlDto> appendChannelList =
                Optional.ofNullable(channelService.getPreferGenrePopularChannelListV2(
                        preferGenreIdList,
                        POPULAR_CHNL_TRACK_LIMIT_SIZE,
                        personalPhaseMeta.getOsType()))
                        .orElseGet(Collections::emptyList);

        Collections.shuffle(appendChannelList);

        for (PreferGenrePopularChnlDto preferGenrePopularChnlDto : appendChannelList.stream()
                .filter(Objects::nonNull).limit(panelLimitSize).collect(Collectors.toList())) {
            try {
                panelList.add(createPreferGenrePopularChannelPanel(personalPhaseMeta,
                        preferGenrePopularChnlDto));

            } catch (Exception e) {
                log.error("appendPreferGenreChannelPanelList error : {}", e.getMessage());
            }
        }

    }
}

