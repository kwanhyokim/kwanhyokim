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
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.type.CreateStdType;
import com.sktechx.godmusic.personal.common.util.BooleanComparator;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendArtistDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.artist.ArtistPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelSignAssembly;
import lombok.extern.slf4j.Slf4j;

/**
 * 설명 : 아티스트 플로 생성기
 *
 * @author 김관효(Kwanhyo Kim)/Music사업팀/SKTECH(kwanhyo.kim@sk.com)
 * @date 2019. 5. 8.
 */

@Slf4j
@Service("artistFloPanelAssembly")
public class ArtistFloPanelAssembly extends PanelSignAssembly {

    private final int ARTIST_FLO_PANEL_HOME_MAX_SIZE = 7;
    private final int ARTIST_FLO_PANEL_LIMIT_SIZE = 4;

    public ArtistFloPanelAssembly(){}

    @Override
    protected List<Panel> appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta){
        return mergePanelList(
                appendPreferArtistPopularTrackPanel(personalPhaseMeta),
                appendPreferenceChartPanel(personalPhaseMeta),
                ARTIST_FLO_PANEL_HOME_MAX_SIZE
        );

    }

    protected List<Panel> appendPreferArtistPopularTrackPanel(final PersonalPhaseMeta personalPhaseMeta) {

        final List<Panel> panelList = new ArrayList<>();

        List<RecommendArtistDto> recommendArtistDtoList =
                recommendReadService.getRecommendArtistFloListByCharacterNo(
                        personalPhaseMeta.getCharacterNo()
                );

        Stream.concat(
            Optional.ofNullable(recommendArtistDtoList).orElseGet(Collections::emptyList).stream()
                .filter(recommendArtistDtoDf -> CreateStdType.DF.equals(recommendArtistDtoDf.getCreateStdType()))
                .limit(1),
            Optional.ofNullable(recommendArtistDtoList).orElseGet(Collections::emptyList).stream()
                .filter(recommendArtistDtoRcmmd -> CreateStdType.RCMMD.equals(recommendArtistDtoRcmmd.getCreateStdType()))
        )
        .filter(Objects::nonNull)
        .filter(recommendArtistDto -> !recommendArtistDto.getArtistList().isEmpty())
        .sorted(Comparator.comparing(RecommendArtistDto::getDispStdStartDt).reversed())
        .limit(ARTIST_FLO_PANEL_LIMIT_SIZE)
        .collect(Collectors.toList())
        .forEach(
                recommendArtistDto -> {
                    try {

                        recommendArtistDto.getArtistList()
                                .sort(
                                        (ArtistDto a, ArtistDto b) -> (BooleanComparator.TRUE_HIGH.compare(a.hasDefaultImage(), b.hasDefaultImage()))
                                );

                        panelList.add(new ArtistPanel(recommendArtistDto));

                    } catch (Exception e) {
                        log.error("PanelSignAssembly appendPreferArtistPanel artistPanel create error : {}", e.getMessage());
                    }
                }

        );

        return panelList;
    }

    @Override
    public List<Panel> getRecommendPanelList(Long characterNo, OsType osType){
        PersonalPhaseMeta personalPhaseMeta = new PersonalPhaseMeta();
        personalPhaseMeta.setCharacterNo(characterNo);
        personalPhaseMeta.setOsType(osType);

        return appendPreferArtistPopularTrackPanel(personalPhaseMeta);

    }
}

