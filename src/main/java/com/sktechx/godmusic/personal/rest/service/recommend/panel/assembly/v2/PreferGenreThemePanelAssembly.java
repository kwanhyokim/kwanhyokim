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

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.PreferGenrePopularChnlDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
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

    public static int PREFER_GENRE_CHANNEL_HOME_MAX_PANEL_SIZE = 7;

    public static int PREFER_GENRE_CHANNEL_LIMIT_PANEL_SIZE = 5;

    public PreferGenreThemePanelAssembly(){}

    @Override
    protected List<Panel> appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta){

       return mergePanelList(
                this.appendPreferGenreChannelPanelList(personalPhaseMeta, PREFER_GENRE_CHANNEL_LIMIT_PANEL_SIZE ),
                appendPreferenceChartPanel(personalPhaseMeta),
               PREFER_GENRE_CHANNEL_HOME_MAX_PANEL_SIZE);
    }

    @Override
    public List<Panel> getRecommendPanelList(Long characterNo, OsType osType) {
        return null;
    }

    private List<Panel> appendPreferGenreChannelPanelList(final PersonalPhaseMeta personalPhaseMeta,
            int panelLimitSize) {

        List<Panel> panelList = new ArrayList<>();

        Optional.ofNullable(
                personalPhaseMeta.getPreferGenreIdList(panelLimitSize)
        ).ifPresent(
                preferGenreIdList -> {

                    List<PreferGenrePopularChnlDto> appendChannelList = Optional.ofNullable(
                            channelService.getPreferGenrePopularChannelListV2(preferGenreIdList,
                                    POPULAR_CHNL_TRACK_LIMIT_SIZE, personalPhaseMeta.getOsType()))
                            .orElseGet(ArrayList::new);

                    Collections.shuffle(appendChannelList);

                    appendChannelList.stream()
                            .filter(Objects::nonNull)
                            .limit(panelLimitSize)
                            .collect(Collectors.toList())
                            .forEach(preferGenrePopularChnlDto -> {

                                try {
                                    panelList.add(
                                            createPreferGenrePopularChannelPanel(
                                                    personalPhaseMeta,
                                                    preferGenrePopularChnlDto)
                                    );
                                } catch (Exception e) {
                                    log.error("appendPreferGenreChannelPanelList error : {}", e.getMessage());
                                }
                            });
                }
        );

        return panelList;
    }
}

