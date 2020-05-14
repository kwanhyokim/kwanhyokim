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
import com.sktechx.godmusic.personal.common.util.DateUtil;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.client.model.MetaVideoRequestVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.model.vo.video.VideoVo;
import com.sktechx.godmusic.personal.rest.repository.PreferenceMapper;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelSignAssembly;
import lombok.extern.slf4j.Slf4j;

/**
 * 설명 : 오늘의 플로 생성기
 *
 * @author 김관효(Kwanhyo Kim)/Music사업팀/SKTECH(kwanhyo.kim@sk.com)
 * @date 2019. 5. 8.
 */
@Slf4j
@Service("preferArtistVideoPanelAssembly")
public class PreferArtistVideoPanelAssembly extends PanelSignAssembly {

    public static int PREFER_ARTIST_VIDEO_HOME_MAX_PANEL_SIZE = 7;

    public PreferArtistVideoPanelAssembly(PreferenceMapper preferMapper, MetaClient metaClient){
        this.preferMapper = preferMapper;
        this.metaClient = metaClient;
    }

    private final PreferenceMapper preferMapper;

    private final MetaClient metaClient;

    @Override
    protected List<Panel> appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta){

        return mergePanelList(
                appendPreferArtistVideoList(personalPhaseMeta, true),
                appendPreferenceChartPanel(personalPhaseMeta),
                PREFER_ARTIST_VIDEO_HOME_MAX_PANEL_SIZE
        );
    }

    private List<Panel> appendPreferArtistVideoList(final PersonalPhaseMeta personalPhaseMeta, Boolean isTop) {

        Date from;
        Date to;

        if(isTop){
            // 1 day
            from = DateUtil.toDate(DateUtil.toString(new Date()));
            to = DateUtil.getDate(from, 86400);

        }else{
            // 3 days
            from = new Date();
            to = DateUtil.getDate(from, 259200);
        }

        return Optional.ofNullable(
                        metaClient.getVideos(
                                MetaVideoRequestVo.builder()
                                        .videoIds(
                                                preferMapper.selectPreferArtistVideoIdListByCharacterNo(
                                                        personalPhaseMeta.getCharacterNo()
                                                )
                                        )
                                        .build()
                        ).getData().getList()

                ).orElseGet(Collections::emptyList)
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(videoVo ->
                                    videoVo.getDispStartDtime().after(from) &&
                                    videoVo.getDispStartDtime().before(to))
                        .map(VideoVo::convertToVideoPanel)
                        .collect(Collectors.toList());
    }

    @Override
    public List<Panel> getRecommendPanelList(Long characterNo, OsType osType){
        PersonalPhaseMeta personalPhaseMeta = new PersonalPhaseMeta();
        personalPhaseMeta.setCharacterNo(characterNo);
        personalPhaseMeta.setOsType(osType);

        List<Panel> panelList = appendPreferArtistVideoList(personalPhaseMeta, false);

        if(panelList.size() > 5){
            Collections.shuffle(panelList);
            panelList = panelList.stream().limit(5).collect(Collectors.toList());
        }

        return panelList;

    }

}

