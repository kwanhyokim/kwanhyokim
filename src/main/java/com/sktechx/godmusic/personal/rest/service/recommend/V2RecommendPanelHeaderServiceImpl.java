/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.service.recommend;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.client.MetaMgoClient;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.*;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.like.RcmmdLikeTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.header.RecommendPanelHeaderVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.SeedArtistVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.SeedGenreVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.SeedTrackVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.recommend.phase.PersonalRecommendPhaseService;
import com.sktechx.godmusic.personal.rest.service.recommend.read.RcmmdReadServiceFactory;

/**
 * 설명 : 설명 : 추천 패널 상세 헤더 ( 4.6.0 부터 이용 )
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2020-03-19
 */

@Service("v2RecommendPanelHeaderService")
public class V2RecommendPanelHeaderServiceImpl implements RecommendPanelHeaderService{

    @Autowired
    private PersonalRecommendPhaseService personalRecommendPhaseService;

    @Autowired
    private RecommendPanelService recommendPanelService;

    @Autowired
    private RecommendImageManagementService recommendImageManagementService;

    @Autowired
    private RcmmdReadServiceFactory rcmmdReadServiceFactory;

    @Autowired
    private MetaMgoClient metaMgoClient;

    @Override
    public RecommendImageManagementService getRecommendImageManagementService() {
        return recommendImageManagementService;
    }

    @Override
    public RecommendPanelHeaderVo getRecommendPanelInfo(Long characterNo,
            RecommendPanelContentType recommendPanelContentType,
            Long panelContentId,
            OsType osType
            ) {

        return getRecommendPanelInfo(recommendPanelContentType,
                panelContentId,
                characterNo,
                osType,
                recommendPanelService.getRecommendPanelTrackList(
                        characterNo, recommendPanelContentType, panelContentId
                )
        );
    }

    private RecommendPanelHeaderVo getRecommendPanelInfo(
            RecommendPanelContentType recommendPanelContentType,
            Long panelContentId,
            Long characterNo,
            OsType osType,
            ListDto<List<RecommendPanelTrackDto>> trackListDto) {

        RecommendPanelHeaderVo panel = null;

        PersonalPhaseMeta personalPhaseMeta = personalRecommendPhaseService
                .getPersonalRecommendPhaseMeta(characterNo, osType, "4.6.0"
                );

        List<RecommendPanelTrackDto> trackList = Optional.ofNullable(trackListDto.getList())
                .orElseGet(Collections::emptyList);

        RecommendDto recommendDto = rcmmdReadServiceFactory
                .getRcmmdReadService(recommendPanelContentType)
                    .getRecommend(personalPhaseMeta.getCharacterNo(), panelContentId)
                .orElseThrow( () -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA));

        switch (recommendPanelContentType){
            // 좋아할만한 아티스트 MIX
            case RC_ATST_TR:
                panel = getArtistFloRecommendPanelInfoDto(recommendDto);
                break;
            // 오늘의 추천
            case RC_SML_TR:
                panel = getTodayFloRecommendPanelInfoDto(recommendPanelContentType,
                        panelContentId, osType, trackList, trackList.size(), recommendDto);
                break;

            // 나를 위한 새로운 발견
            case RC_GR_TR:
            case RC_CF_TR:
                panel = getForMeRecommendPanelInfoDto(recommendPanelContentType, panelContentId,
                        osType, personalPhaseMeta, trackList.size(), recommendDto);
                break;

            case RC_LKSM_TR:
                panel = getLikeTrackRecommendPanelInfoDto(recommendDto, trackList.size());
                break;
        }

        return panel;
    }

    private RecommendPanelHeaderVo getLikeTrackRecommendPanelInfoDto(RecommendDto recommendDto, int trackCount) {

        RcmmdLikeTrackDto rcmmdLikeTrackDto = (RcmmdLikeTrackDto) recommendDto;

        Date dispDate = rcmmdLikeTrackDto.getDispStartDtime();

        return RecommendPanelHeaderVo.builder()
                .title(RecommendConstant.RCMMD_REACTIVE_PANEL_TITLE)
                .subTitle(RecommendConstant.RCMMD_REACTIVE_PANEL_SUB_TITLE)
                .trackCount(trackCount)
                .newYn(this.getNewYn(dispDate))
                .renewDtime(dispDate)
                .seedTrackVo(
                        Optional.ofNullable(
                                metaMgoClient.track(rcmmdLikeTrackDto.getSeedTrackId()).getData()
                        ).map(
                                trackDto ->
                                        SeedTrackVo.builder()
                                                .id(trackDto.getTrackId())
                                                .name(trackDto.getTrackNm())
                                                .artistName(trackDto.getArtist().getArtistName())
                                        .build()
                        ).orElse(null)
                )
                .build();

    }

    private RecommendPanelHeaderVo getForMeRecommendPanelInfoDto(
            RecommendPanelContentType recommendPanelContentType, Long panelContentId, OsType osType,
            PersonalPhaseMeta personalPhaseMeta, int trackCount, RecommendDto recommendDto) {

        RecommendForMeDto recommendForMeDto = (RecommendForMeDto) recommendDto;

        Date dispStdStartDt = new Date();
        SeedGenreVo seedGenreVo = null;
        String subTitle = RecommendConstant.RCMMD_CF_TRACK_PANEL_SUB_TITLE;

        if(!ObjectUtils.isEmpty(recommendForMeDto)){
            String genreNm = recommendForMeDto.getSvcGenreNm();
            dispStdStartDt = recommendForMeDto.getDispStdStartDt();

            seedGenreVo = SeedGenreVo.builder()
                    .name(genreNm)
                    .suffix(RecommendConstant.RCMMD_TRACK_PANEL_SEED_SUFFIX)
                    .build();

            subTitle = String.format(RecommendConstant.RCMMD_TRACK_PANEL_SUB_TITLE_NEW, genreNm);

        }

        AtomicReference<Integer> dispSn = new AtomicReference<>(1);

        Optional.ofNullable(
                personalPhaseMeta.getRecommendPersonalPanelList(RecommendPanelContentType.RC_CF_TR)
        ).orElseGet(Collections::emptyList)
        .stream()
        .filter(
                panel -> panelContentId.equals(panel.getRecommendId())
        )
        .findFirst()
        .ifPresent(
                personalPanel -> {
                    if (personalPhaseMeta.getRecommendPersonalPanelList(RecommendPanelContentType.RC_CF_TR)
                            .indexOf(personalPanel) % 2 == 0) {
                        dispSn.set(2);
                    } else {
                        dispSn.set(1);
                    }
                }
        );

        return RecommendPanelHeaderVo.builder()
                .title(RecommendConstant.RCMMD_TRACK_PANEL_TITLE)
                .subTitle(subTitle)
                .imgList(
                        getRecommendPanelInfoBgImage(recommendPanelContentType, panelContentId,
                                osType, dispSn.get())
                ).trackCount(trackCount)
                .newYn(this.getNewYn(dispStdStartDt)).renewDtime(dispStdStartDt)
                .seedGenreVo(seedGenreVo).build();
    }

    private RecommendPanelHeaderVo getTodayFloRecommendPanelInfoDto(
            RecommendPanelContentType recommendPanelContentType,
            Long panelContentId,
            OsType osType,
            List<RecommendPanelTrackDto> trackList,
            int trackCount, RecommendDto recommendDto) {

        RecommendSimilarTrackDto recommendSimilarTrackDto = (RecommendSimilarTrackDto)recommendDto;

        Date dispDate = recommendSimilarTrackDto.getDispStdStartDt();

        RecommendPanelHeaderVo panel = RecommendPanelHeaderVo.builder()
                .title(RecommendConstant.SIMILAR_TRACK_PANEL_TITLE)
                .subTitle(RecommendConstant.SIMILAR_TRACK_PANEL_DETAIL_SUB_TITLE)
                .imgList(
                        getRecommendPanelInfoBgImage(recommendPanelContentType,
                                panelContentId,
                                osType,
                                recommendSimilarTrackDto.getDispSn()
                        )
                )
                .trackCount(trackCount)
                .newYn(this.getNewYn(dispDate))
                .renewDtime(dispDate)
                .build();

        trackList.stream()
                .findFirst()
                .ifPresent(
                        recommendPanelTrackDto ->
                                panel.setSeedTrackVo(
                                        SeedTrackVo.builder()
                                                .id(recommendPanelTrackDto.getTrackId())
                                                .name(recommendPanelTrackDto.getTrackName())
                                                .artistName(
                                                        Optional.ofNullable(
                                                                recommendPanelTrackDto.getArtistList()
                                                        ).orElseGet(Collections::emptyList)
                                                                .stream()
                                                                .map(ArtistDto::getArtistName)
                                                                .collect(Collectors.joining(",")
                                                                )
                                                )
                                                .suffix(RecommendConstant.SIMILAR_TRACK_PANEL_SEED_SUFFIX)
                                                .build()
                                )
                );

        return panel;
    }

    private RecommendPanelHeaderVo getArtistFloRecommendPanelInfoDto(RecommendDto recommendDto) {
        RecommendArtistDto recommendArtistDto = (RecommendArtistDto)recommendDto;

        List<ArtistDto> artistDtoList =

                Optional.ofNullable(
                        recommendArtistDto.getArtistList())
                .orElseThrow(() -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA))
                    .stream()
                    .sorted(Comparator.comparing(ArtistDto::hasDefaultImage))
                    .collect(Collectors.toList());

        String subTitle = artistDtoList.stream().map(ArtistDto::getArtistName).limit(5).collect(
                Collectors.joining(","));

        // 아티스트의 첫 이미지를 배경 이미지로 사용
        return RecommendPanelHeaderVo.builder()
                .title(RecommendConstant.ARTIST_PANEL_TITLE)
                .subTitle(subTitle)
                .imgList(artistDtoList.get(0) == null ? null : artistDtoList.get(0).getImgList())
                .artistList(artistDtoList)
                .artistCount(artistDtoList.size())
                .renewDtime(Objects.requireNonNull(recommendArtistDto).getDispStdStartDt())
                .newYn(this.getNewYn(recommendArtistDto.getDispStdStartDt()))
                .seedArtistVo(
                        SeedArtistVo.builder()
                                .name(subTitle)
                                .suffix("")
                                .build()
                ).build();
    }

    private YnType getNewYn(Date dispDate){
        if(dispDate != null) {
            Date stdDate = new Date((System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)));
            if (stdDate.before(dispDate)) {
                return YnType.Y;
            }
        }

        return YnType.N;

    }

}
