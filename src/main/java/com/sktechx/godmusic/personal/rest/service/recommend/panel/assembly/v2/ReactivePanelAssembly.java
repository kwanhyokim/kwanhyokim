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
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.exception.PersonalErrorDomain;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.client.model.GetTrackListRequest;
import com.sktechx.godmusic.personal.rest.model.dto.AlbumDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.like.RcmmdLikeTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.reactive.RcmmdReactivePanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendImageManagementService;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelSignAssembly;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.RCMMD_CF_TRACK_LIMIT_SIZE;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

/**
 * 설명 : 좋아요 트랙(반응형 레이더) 플로 패널 생성기
 */

@Slf4j
@Service("reactivePanelAssembly")
public class ReactivePanelAssembly extends PanelSignAssembly {
    public static int REACTIVE_HOME_MAX_PANEL_SIZE = 7;

    public static int REACTIVE_LIMIT_PANEL_SIZE = 5;

    @Autowired
    MetaClient metaClient;

    @Autowired
    RecommendImageManagementService recommendImageManagementService;

    public ReactivePanelAssembly(){ }

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

    private List<Panel> appendRecommendLikeTrackPanelListV2(PersonalPhaseMeta personalPhaseMeta) {

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
                )
                .orElseGet( Collections::emptyList)
                .stream()
                .map( recommendDto -> (RcmmdLikeTrackDto) recommendDto)
                .collect(toList());

        if(rcmmdLikeTrackDtoList.isEmpty()){
            throw new CommonBusinessException(PersonalErrorDomain.HOME_PANNEL_CREATION_FAILED
                            , RecommendPanelContentType.RC_LKSM_TR)
            ;
        }

        int rcmmdTrackCount = rcmmdLikeTrackDtoList.size();
        int displayTrackCount = 5;
        List<Panel> panelList = new ArrayList<>(rcmmdTrackCount);
        List<ImageInfo> backgroundImageList =
                recommendImageManagementService.getAdaptivePanelBgImageAtRandomlyByOsType(personalPhaseMeta.getOsType(), rcmmdTrackCount);

        for (int index = 0; index < rcmmdTrackCount; index++) {

            RcmmdLikeTrackDto rcmmdLikeTrackDto = rcmmdLikeTrackDtoList.get(index);

            try {

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
                                Collections.singletonList(backgroundImageList.get((index % backgroundImageList.size()))),
                                createPanelThumbnailImages(listResponse.getList()),
                                rcmmdLikeTrackDto,
                                expectedSeedTrackDto,
                                totalTrackCount)
                        );
                    }else{
                        throw new CommonBusinessException(PersonalErrorDomain.HOME_PANNEL_CREATION_FAILED
                                , RecommendPanelContentType.RC_LKSM_TR);
                    }
                }
            }
            catch (CommonBusinessException cbe){
                if(cbe.getErrorDomain() == PersonalErrorDomain.HOME_PANNEL_CREATION_FAILED){
                    throw cbe;
                }else{
                    log.error("RecommendPhasePanelAssembly appendRecommendCfTrackPanelList error"
                            , cbe);
                }
            }
            catch (Exception e) {
                log.error("RecommendPhasePanelAssembly appendRecommendCfTrackPanelList error", e);
            }
        }

        return panelList;
    }

    private PanelImageHolder createPanelThumbnailImages(List<TrackDto> entireRecommendTrackDtoList) {

        TrackDto seedTrackDto = entireRecommendTrackDtoList.get(0);
        AlbumDto seedTrackAlbum = seedTrackDto.getAlbum();

        Set<AlbumDto> uniqueAlbumDtos =
                entireRecommendTrackDtoList
                        .stream()
                        .filter(trackDto -> !trackDto.getAlbum().getAlbumId().equals(seedTrackAlbum.getAlbumId()))
                        .map(TrackDto::getAlbum)
                        .collect(toCollection(LinkedHashSet::new));

        int panelGridThumbnailImageCount = 4;
        List<ImageInfo> gridThumbnailImageList;

        if (uniqueAlbumDtos.isEmpty()) {
            ImageInfo seedTrackAlbum200Image = seedTrackAlbum.getImageInfoBySize(200L);
            gridThumbnailImageList = Stream.generate(() -> seedTrackAlbum200Image).limit(panelGridThumbnailImageCount).collect(toList());
        } else {
            gridThumbnailImageList =
                    uniqueAlbumDtos
                            .stream()
                            .limit(panelGridThumbnailImageCount)
                            .map(albumDto -> albumDto.getImageInfoBySize(200L))
                            .collect(toList());

            if (uniqueAlbumDtos.size() < panelGridThumbnailImageCount) {
                int paddingCount = panelGridThumbnailImageCount - gridThumbnailImageList.size();
                ImageInfo paddingImageInfo = gridThumbnailImageList.get(gridThumbnailImageList.size() - 1);
                Stream.generate(() -> paddingImageInfo).limit(paddingCount).forEach(gridThumbnailImageList::add);
            }
        }

        return new PanelImageHolder(Collections.singletonList(seedTrackAlbum.getImageInfoBySize(500L)), gridThumbnailImageList);
    }

    @Deprecated
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
                                rcmmdLikeTrackDto
                        )
                );

            } catch (Exception e) {
                e.printStackTrace();
                log.error("RecommendPhasePanelAssembly appendRecommendCfTrackPanelList error : {}", e.getMessage());
            }
        }

        return panelList;
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PanelImageHolder {

        private final List<ImageInfo> seedThumbnailImageList;
        private final List<ImageInfo> gridThumbnailImageList;
    }
}

