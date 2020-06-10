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

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.client.model.GetTrackListRequest;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.like.RcmmdLikeTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.reactive.RcmmdReactivePanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendImageManagementService;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelSignAssembly;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.RCMMD_CF_TRACK_LIMIT_SIZE;
import static java.util.stream.Collectors.toList;

/**
 * 설명 : 좋아요 트랙(반응형 레이더) 플로 패널 생성기
 */

@Slf4j
@Service("reactivePanelAssembly")
public class ReactivePanelAssembly extends PanelSignAssembly {

    public static int REACTIVE_HOME_MAX_PANEL_SIZE = 6;
    public static int REACTIVE_LIMIT_PANEL_SIZE = 4;

    @Autowired
    MetaClient metaClient;

    @Autowired
    RecommendImageManagementService recommendImageManagementService;

    public ReactivePanelAssembly(){ }

    private List<Panel> appendRecommendLikeTrackPanelListV2(PersonalPhaseMeta personalPhaseMeta) {

        List<Panel> panelList = new ArrayList<>(3);

        List<RcmmdLikeTrackDto> rcmmdLikeTrackDtoList =
                Optional.ofNullable(
                        rcmmdReadServiceFactory.getRcmmdReadService(
                                RecommendPanelContentType.RC_LKSM_TR
                        ).getRecommendListWithTrackByCharacterNoOrderByDispStartDtime(
                                personalPhaseMeta.getCharacterNo(),
                                REACTIVE_LIMIT_PANEL_SIZE,
                                RCMMD_CF_TRACK_LIMIT_SIZE,
                                personalPhaseMeta.getOsType()
                        )
                ).orElseGet( Collections::emptyList )
                        .stream()
                        .map( recommendDto -> (RcmmdLikeTrackDto) recommendDto)
                        .collect(toList());


        for (RcmmdLikeTrackDto rcmmdLikeTrackDto : rcmmdLikeTrackDtoList) {

            try {

                int displayTrackCount = 5;

                ListDto<List<TrackDto>> listResponse =
                        metaClient.getTrackList(new GetTrackListRequest(rcmmdLikeTrackDto.getTrackIdList())).getData();

                if (listResponse != null && listResponse.isNotEmpty()) {

                    int totalTrackCount = listResponse.getList().size();
                    List<TrackDto> recommendTrackDtoList =
                            listResponse.getList().subList(0, Math.min(displayTrackCount, totalTrackCount));
                    TrackDto expectedSeedTrackDto = recommendTrackDtoList.get(0);

                    // seed track takedown 되지 않았을 경우
                    if (expectedSeedTrackDto.getTrackId().equals(rcmmdLikeTrackDto.getSeedTrackId())) {
                        panelList.add(new RcmmdReactivePanel(
                                getDefaultBgImageList(
                                        recommendImageManagementService.getAdaptivePanelHomeImageList(personalPhaseMeta.getOsType()),
                                        personalPhaseMeta.getOsType()
                                ),
                                Collections.singletonList(expectedSeedTrackDto.getAlbum().getImgList().get(0)),
                                recommendTrackDtoList
                                        .stream()
                                        .skip(1)
                                        .map(trackDto -> trackDto.getAlbum().getImgList().get(0)).collect(toList()),
                                expectedSeedTrackDto,
                                rcmmdLikeTrackDto,
                                totalTrackCount)
                        );
                    }
                }
            } catch (Exception e) {
                log.error("RecommendPhasePanelAssembly appendRecommendCfTrackPanelList error", e);
            }
        }

        return panelList;
    }

    private List<Panel> appendRecommendLikeTrackPanelList(PersonalPhaseMeta personalPhaseMeta) {

        List<Panel> panelList = new ArrayList<>();

        List<RcmmdLikeTrackDto> rcmmdLikeTrackDtoList =
                Optional.ofNullable(
                        rcmmdReadServiceFactory.getRcmmdReadService(
                                RecommendPanelContentType.RC_LKSM_TR
                        ).getRecommendListWithTrackByCharacterNoOrderByDispStartDtime(
                            personalPhaseMeta.getCharacterNo(),
                            REACTIVE_LIMIT_PANEL_SIZE,
                            RCMMD_CF_TRACK_LIMIT_SIZE,
                            personalPhaseMeta.getOsType()
                    )
                ).orElseGet( Collections::emptyList )
                .stream()
                .map( recommendDto -> (RcmmdLikeTrackDto) recommendDto)
                .collect(toList());


        for(RcmmdLikeTrackDto rcmmdLikeTrackDto :
                rcmmdLikeTrackDtoList) {
            try {

                Long[] trackIds = new Long[5];

                List<ImageInfo> seedTrackImgList = new ArrayList<>(1);
                List<ImageInfo> rcmmdTrackImgList = new ArrayList<>(4);

                trackIds[0] = rcmmdLikeTrackDto.getSeedTrackId();

                int i = 1;

                for(Long trackId :
                        rcmmdLikeTrackDto.getTrackIdList().stream().limit(4).collect(toList())){
                    trackIds[i++] = trackId;
                }

                final TrackDto[] seedTrackDto = new TrackDto[1];

                Optional.ofNullable(
                        metaClient.getTrackList(new GetTrackListRequest(Arrays.asList(trackIds))).getData()
                ).ifPresent(
                        recommendTrackDtoList ->
                        {
                                recommendTrackDtoList.getList().forEach(
                                    recommendPanelTrackDto -> {

                                        ImageInfo imageInfo = recommendPanelTrackDto.getAlbum()
                                                .getImgList().get(0);

                                        if ( recommendTrackDtoList.getList().indexOf(
                                                recommendPanelTrackDto) == 0){
                                            seedTrackImgList.add(imageInfo);
                                        }else{
                                            rcmmdTrackImgList.add(imageInfo);
                                        }
                                    }
                                );

                            seedTrackDto[0] = recommendTrackDtoList.getList().get(0);
                        }
                );

                panelList.add(
                        new RcmmdReactivePanel(
                                getDefaultBgImageList(
                                        recommendImageManagementService
                                                .getAdaptivePanelHomeImageList(
                                                        personalPhaseMeta.getOsType()
                                                )
                                        ,
                                    personalPhaseMeta.getOsType()),
                                seedTrackImgList,
                                rcmmdTrackImgList,
                                seedTrackDto[0],
                                rcmmdLikeTrackDto,
                                rcmmdLikeTrackDto.getTrackIdList().size()
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
    public List<Panel> makeHomePanelListForMainTop(PersonalPhaseMeta personalPhaseMeta){

        return mergePanelList(
                appendRecommendLikeTrackPanelListV2(personalPhaseMeta),
                appendPreferenceChartPanel.apply(personalPhaseMeta),
                REACTIVE_HOME_MAX_PANEL_SIZE
        );
    }

    @Override
    public List<Panel> makeHomePanelListForMainMiddle(Long characterNo, OsType osType){
        PersonalPhaseMeta personalPhaseMeta = new PersonalPhaseMeta();
        personalPhaseMeta.setCharacterNo(characterNo);
        personalPhaseMeta.setOsType(osType);

        return appendRecommendLikeTrackPanelListV2(personalPhaseMeta);
    }
}

