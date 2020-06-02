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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.model.dto.AlbumDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendSimilarTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.RcmmdTodayTrackPanel;
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

    public TodayFloPanelAssembly(){ }

    private List<Panel> appendTodayFloPanelList(final PersonalPhaseMeta personalPhaseMeta,
            int panelLimitSize) {

        List<Panel> panelList = new ArrayList<>();

        Optional.ofNullable(
                rcmmdReadServiceFactory.getRcmmdReadService(RecommendPanelContentType.RC_SML_TR)
                        .getRecommendListWithTrackByCharacterNoOrderByDispStartDtime(
                                personalPhaseMeta.getCharacterNo(),
                                panelLimitSize,
                                SIMILAR_TRACK_LIMIT_SIZE,
                                personalPhaseMeta.getOsType()
                        )
        )
        .ifPresent(
                similarTrackList -> {
                    List<RecommendSimilarTrackDto> currentSimilarTrackList =
                    similarTrackList.stream()
                            .map(similarTrack -> (RecommendSimilarTrackDto)similarTrack)
                            .filter(similarTrack ->
                                    similarTrack.getTrackCount() >= SIMILAR_TRACK_DISP_STANDARD_COUNT
                            )
                            .limit(panelLimitSize).collect(Collectors.toList());

                    currentSimilarTrackList
                            .forEach(similarTrack -> {
                                try {
                                    RcmmdTodayTrackPanel panel =
                                            new RcmmdTodayTrackPanel(similarTrack,
                                                    getDefaultBgImageList(similarTrack.getImgList(),
                                                            personalPhaseMeta.getOsType()
                                                    )
                                            );

                                    Optional
                                            .ofNullable(panel.getContent().getTrackList())
                                    .ifPresent(
                                            // 홈 노출 시, 중복 앨범 이미지 제거 작업
                                            trackDtoList -> {
                                                List<List<ImageInfo>> imageInfosList =
                                                        trackDtoList.stream()
                                                                .map(TrackDto::getAlbum)
                                                                .map(AlbumDto::getImgList)
                                                                .distinct()
                                                                .limit(3)
                                                        .collect(Collectors.toList());
                                                if (imageInfosList.size() == 3) {
                                                    for (int i = 0; i < imageInfosList.size(); i++) {
                                                        TrackDto trackDto = trackDtoList.get(i);
                                                        trackDto.getAlbum().setImgList(imageInfosList.get(i));
                                                    }
                                                }
                                            }
                                    );

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
    public List<Panel> makeHomePanelListForMainTop(PersonalPhaseMeta personalPhaseMeta){

        return mergePanelList(
                appendTodayFloPanelList(personalPhaseMeta, TODAY_FLO_LIMIT_PANEL_SIZE),
                appendPreferenceChartPanel.apply(personalPhaseMeta),
                TODAY_FLO_HOME_MAX_PANEL_SIZE);
    }

    @Override
    public List<Panel> makeHomePanelListForMainMiddle(Long characterNo, OsType osType){
        PersonalPhaseMeta personalPhaseMeta = new PersonalPhaseMeta();
        personalPhaseMeta.setCharacterNo(characterNo);
        personalPhaseMeta.setOsType(osType);

        return appendTodayFloPanelList(personalPhaseMeta, 4);

    }

}

