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
import com.sktechx.godmusic.personal.rest.model.dto.AlbumDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.PreferSimilarTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelSignAssembly;
import lombok.extern.slf4j.Slf4j;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.SIMILAR_TRACK_DISP_STANDARD_COUNT;
import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.SIMILAR_TRACK_LIMIT_SIZE;

/**
 * 설명 : 오늘의 플로 생성기
 *
 * @author 김관효(Kwanhyo Kim)/Music사업팀/SKTECH(kwanhyo.kim@sk.com)
 * @date 2019. 5. 8.
 */
@Slf4j
@Service("todayFloPanelAssembly")
public class TodayFloPanelAssembly extends PanelSignAssembly {

    public static int TODAY_FLO_HOME_MAX_PANEL_SIZE = 7;
    public static int TODAY_FLO_LIMIT_PANEL_SIZE = 7;

    public TodayFloPanelAssembly(){}

    @Override
    protected List<Panel> appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta){

        return mergePanelList(
                appendSimilarTrackPanelList(personalPhaseMeta, TODAY_FLO_LIMIT_PANEL_SIZE),
                appendPreferenceChartPanel(personalPhaseMeta),
                TODAY_FLO_HOME_MAX_PANEL_SIZE);
    }

    protected List<Panel> appendSimilarTrackPanelList(final PersonalPhaseMeta personalPhaseMeta,
            int panelLimitSize) {

        List<Panel> panelList = new ArrayList<>();

        Optional.ofNullable(
                        recommendReadService.getRecommendTodayFloListWithTrackByCharacterNo(
                                personalPhaseMeta.getCharacterNo(),
                                panelLimitSize,
                                SIMILAR_TRACK_LIMIT_SIZE,
                                personalPhaseMeta.getOsType())
        )
        .ifPresent(
                similarTrackList -> {
                    List<RecommendTrackDto> currentSimilarTrackList =
                    similarTrackList.stream()
                            .filter(similarTrack -> similarTrack.getTrackCount() >= SIMILAR_TRACK_DISP_STANDARD_COUNT)
                            .sorted(Comparator.comparing(RecommendTrackDto::getRcmmdCreateDtime).reversed())
                            .limit(panelLimitSize).collect(Collectors.toList());

                    currentSimilarTrackList
                            .forEach(similarTrack -> {
                                try {
                                    PreferSimilarTrackPanel panel = (PreferSimilarTrackPanel) createSimilarTrackPanel(
                                            personalPhaseMeta, similarTrack);
                                    List<TrackDto> trackDtoList = Optional
                                            .ofNullable(panel.getContent().getTrackList())
                                            .orElseGet(Collections::emptyList);

                                    List<List<ImageInfo>> imageInfosList = trackDtoList.stream()
                                            .map(TrackDto::getAlbum).map(AlbumDto::getImgList)
                                            .distinct().limit(3).collect(Collectors.toList());

                                    if (imageInfosList.size() == 3) {
                                        for (int i = 0; i < imageInfosList.size(); i++) {
                                            TrackDto trackDto = trackDtoList.get(i);
                                            trackDto.getAlbum().setImgList(imageInfosList.get(i));
                                        }
                                    }

                                    panelList.add(panel);

                                } catch (Exception e) {
                                    log.error("appendSimilarTrackPanelList error : {}", e.getMessage());
                                }
                            });
                }
        );

        return panelList;
    }

    @Override
    public List<Panel> getRecommendPanelList(Long characterNo, OsType osType){
        PersonalPhaseMeta personalPhaseMeta = new PersonalPhaseMeta();
        personalPhaseMeta.setCharacterNo(characterNo);
        personalPhaseMeta.setOsType(osType);

        List<Panel> panelList = new ArrayList<>();

        appendSimilarTrackPanelList(personalPhaseMeta, panelList, 4);

        return panelList;

    }

}

