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

    public ArtistFloPanelAssembly(){}

    @Override
    protected List<Panel> defaultPanelSetting(PersonalPhaseMeta personalPhaseMeta) {
        return new ArrayList<>();
    }

    @Override
    protected void appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta ,final List<Panel> panelList){

        List<Panel> myPanelList = new ArrayList<>();
        List<Panel> chartPanelList = new ArrayList<>();

        appendPreferArtistPopularTrackPanel(personalPhaseMeta, myPanelList);
        appendPreferenceChartPanel(personalPhaseMeta, chartPanelList);

        mergePanelList(panelList, myPanelList, chartPanelList, 7);
        sort(personalPhaseMeta , panelList);

    }

    @Override
    protected void appendPreferArtistPopularTrackPanel(final PersonalPhaseMeta personalPhaseMeta, final List<Panel> panelList) {
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
        .limit(4)
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
    }


    @Override
    public List<Panel> getRecommendPanelList(Long characterNo, OsType osType){
        PersonalPhaseMeta personalPhaseMeta = new PersonalPhaseMeta();
        personalPhaseMeta.setCharacterNo(characterNo);
        personalPhaseMeta.setOsType(osType);

        List<Panel> panelList = new ArrayList<>();

        appendPreferArtistPopularTrackPanel(personalPhaseMeta, panelList);

        return panelList;

    }
}

