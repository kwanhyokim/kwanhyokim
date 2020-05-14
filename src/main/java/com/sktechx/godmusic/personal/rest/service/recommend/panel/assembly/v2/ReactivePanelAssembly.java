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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.client.model.GetTrackListRequest;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.like.RcmmdLikeTrackDetailDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.reactive.RcmmdReactivePanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelSignAssembly;
import lombok.extern.slf4j.Slf4j;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.RCMMD_CF_TRACK_LIMIT_SIZE;

/**
 * 설명 : 나의 플로 생성기
 *
 * @author 김관효(Kwanhyo Kim)/Music사업팀/SKTECH(kwanhyo.kim@sk.com)
 * @date 2019. 5. 8.
 */
@Slf4j
@Service("reactivePanelAssembly")
public class ReactivePanelAssembly extends PanelSignAssembly {

    public static int REACTIVE_HOME_MAX_PANEL_SIZE = 6;
    public static int REACTIVE_LIMIT_PANEL_SIZE = 4;

    @Autowired
    MetaClient metaClient;

    public ReactivePanelAssembly(){}

    @Override
    protected List<Panel> appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta){

        return mergePanelList(
                appendRecommendLikeTrackPanelList(personalPhaseMeta),
                appendPreferenceChartPanel(personalPhaseMeta),
                REACTIVE_HOME_MAX_PANEL_SIZE
        );

    }

    private List<Panel> appendRecommendLikeTrackPanelList(PersonalPhaseMeta personalPhaseMeta) {

        List<Panel> panelList = new ArrayList<>();

        List<RcmmdLikeTrackDetailDto> rcmmdLikeTrackDtoList =
                Optional.ofNullable(
                    recommendReadService.getRecommendReactiveTrackListByCharacterNo(
                            personalPhaseMeta.getCharacterNo(),
                            REACTIVE_LIMIT_PANEL_SIZE,
                            RCMMD_CF_TRACK_LIMIT_SIZE,
                            personalPhaseMeta.getOsType()
                    )
                ).orElseGet( () -> {

                    List<RcmmdLikeTrackDetailDto> list = new ArrayList<>();
                    List<Long> trackIdList = new ArrayList<>();

                    trackIdList.add(3760L);
                    trackIdList.add(3822L);
                    trackIdList.add(7670L);
                    trackIdList.add(7718L);

                    list.add(
                            RcmmdLikeTrackDetailDto.builder()
                                    .rcmmdId(1L)
                                    .seedTrackId(2622L)
                                    .trackIdList(trackIdList)
                                    .dispStartDtime(new Date())
                                    .build()

                    );

                    return list;
                } );




        for(RcmmdLikeTrackDetailDto rcmmdLikeTrackDetailDto :
                rcmmdLikeTrackDtoList
                    .stream()
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparing(RcmmdLikeTrackDetailDto::getDispStartDtime).reversed()).collect(Collectors.toList())) {
            try {

                Long[] trackIds = new Long[5];

                List<ImageInfo> imageInfoList = new ArrayList<>(5);
                trackIds[0] = rcmmdLikeTrackDetailDto.getSeedTrackId();

                int i = 1;

                for(Long trackId :
                        rcmmdLikeTrackDetailDto.getTrackIdList().stream().limit(4).collect(Collectors.toList())){
                    trackIds[i++] = trackId;
                }

                final TrackDto[] seedTrackDto = new TrackDto[1];

                Optional.ofNullable(
                        metaClient.getTrackList(new GetTrackListRequest(Arrays.asList(trackIds))).getData()
                ).ifPresent(
                        recommendTrackDtoList ->
                        {
                                recommendTrackDtoList.getList().forEach(
                                    recommendPanelTrackDto ->
                                        imageInfoList.add(recommendPanelTrackDto.getAlbum().getImgList().get(0))
                                );

                            seedTrackDto[0] =recommendTrackDtoList.getList().get(0);
                        }

                );

                List<ImageInfo> seedTrackImgList = imageInfoList.subList(0,1);
                List<ImageInfo> rcmmdTrackImgList = imageInfoList.subList(1,5);

                panelList.add(
                        new RcmmdReactivePanel(
                                getDefaultBgImageList(new ArrayList<>(),
                                    personalPhaseMeta.getOsType()),
                                seedTrackImgList,
                                rcmmdTrackImgList,
                                seedTrackDto[0],
                                rcmmdLikeTrackDetailDto
                        )
                );

            } catch (Exception e) {
                e.printStackTrace();
                log.error("RecommendPhasePanelAssembly appendRecommendCfTrackPanelList error : {}", e.getMessage());
            }
        }

        return panelList;
    }

    @Override
    public List<Panel> getRecommendPanelList(Long characterNo, OsType osType){
        PersonalPhaseMeta personalPhaseMeta = new PersonalPhaseMeta();
        personalPhaseMeta.setCharacterNo(characterNo);
        personalPhaseMeta.setOsType(osType);

        return appendRecommendLikeTrackPanelList(personalPhaseMeta);

    }

}

