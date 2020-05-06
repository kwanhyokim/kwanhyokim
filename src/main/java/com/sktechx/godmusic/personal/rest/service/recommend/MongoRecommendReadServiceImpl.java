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

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.*;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.repository.RecommendReadMapper;
import com.sktechx.godmusic.personal.rest.repository.TrackMapper;
import com.sktechx.godmusic.personal.rest.service.mongo.PersonalMongoClient;

/**
 * 설명 : 추천 데이터 몽고향 조회 서비스
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2020-03-12
 */

@Service("mongoRecommendReadService")
public class MongoRecommendReadServiceImpl implements RecommendReadService {

    private final PersonalMongoClient personalMongoClient;

    private final RecommendReadMapper recommendReadMapper;

    private final TrackMapper trackMapper;

    private final MetaClient metaClient;

    private final RecommendReadService recommendReadService;

    public MongoRecommendReadServiceImpl(PersonalMongoClient personalMongoClient,
            RecommendReadMapper recommendReadMapper,
            TrackMapper trackMapper,
            MetaClient metaClient,
            @Qualifier("recommendReadService") RecommendReadService recommendReadService) {
        this.personalMongoClient = personalMongoClient;
        this.recommendReadMapper = recommendReadMapper;
        this.trackMapper = trackMapper;
        this.metaClient = metaClient;
        this.recommendReadService = recommendReadService;
    }

    @Override
    public RecommendForMeDto getRecommendForMeFlo(Long characterNo, Long rcmmdMforuId) {

        return Optional.ofNullable(
                personalMongoClient.getRecommendFormeFlo(rcmmdMforuId, characterNo)
                        .getData()
        ).orElseGet(
                () -> recommendReadMapper.selectRecommendGenreByRcmmdId(rcmmdMforuId)
        );
    }

    @Override
    public RecommendSimilarTrackDto getRecommendTodayFlo(Long characterNo,
            Long rcmmdSimilarTrackId) {
        return Optional.ofNullable(
                personalMongoClient.getRecommendTodayFlo(rcmmdSimilarTrackId, characterNo)
                        .getData()
        ).orElseGet(
                () -> recommendReadMapper.selectRecommendSimilarTrack(rcmmdSimilarTrackId)
        );
    }
    @Override
    public RecommendArtistDto getRecommendArtistFlo(Long characterNo, Long rcmmdArtistId) {
        return Optional.ofNullable(
                personalMongoClient.getRecommendArtistFlo(rcmmdArtistId, characterNo).getData()
        ).orElseGet( () -> recommendReadMapper.selectRecommendArtistById(rcmmdArtistId));
    }
    @Override
    public List<RecommendForMeDto> getRecommendForMeFloListByCharacterNo(Long characterNo) {
        return recommendReadService.getRecommendForMeFloListByCharacterNo(characterNo);
    }
    @Override
    public List<RecommendSimilarTrackDto> getRecommendTodayFloListByCharacterNo(Long characterNo) {
        return recommendReadService.getRecommendTodayFloListByCharacterNo(characterNo);
    }
    @Override
    public List<RecommendArtistDto> getRecommendArtistFloListByCharacterNo(Long characterNo) {
        return recommendReadService.getRecommendArtistFloListByCharacterNo(characterNo);
    }
    @Override
    public List<RecommendTrackDto> getRecommendForMeFloListWithTrackByCharacterNo(
            Long characterNo, int panelMaxSize, int trackMaxSize, OsType osType) {
        return recommendReadService.getRecommendForMeFloListWithTrackByCharacterNo(
                characterNo, panelMaxSize, trackMaxSize, osType
        );
    }

    @Override
    public List<RecommendTrackDto> getRecommendTodayFloListWithTrackByCharacterNo(
            Long characterNo, int panelMaxSize, int trackMaxSize, OsType osType) {

        return Optional.ofNullable(
                personalMongoClient.getRecommendTodayFloListWithTrackByCharacterNo(characterNo).getData()
                .getList()
        ).orElseGet( () ->
            recommendReadMapper.selectRecommendSimilarTrackListByCharacterNo(
                    characterNo,
                    panelMaxSize,
                    trackMaxSize,
                    osType
            ));
    }

    @Override
    public List<RecommendTrackDto> getRecommendArtistFloListWithTrackByCharacterNo(
            Long characterNo, int panelMaxSize, int trackMaxSize, OsType osType) {
        return recommendReadService.getRecommendArtistFloListWithTrackByCharacterNo(
                characterNo, panelMaxSize, trackMaxSize, osType);
    }

    @Override
    public List<RecommendPanelTrackDto> getRecommendForMeFloTrackListByCharacterNoAndRcmmdId(
            Long characterNo, Long rcmmdId) {
        return recommendReadService.getRecommendForMeFloTrackListByCharacterNoAndRcmmdId(
                characterNo, rcmmdId
        );
    }

    @Override
    public List<RecommendPanelTrackDto> getRecommendArtistFloTrackListByCharacterNoAndRcmmdId(
            Long characterNo, Long rcmmdId) {
        return recommendReadService.getRecommendArtistFloTrackListByCharacterNoAndRcmmdId(
                characterNo, rcmmdId
        );
    }

    @Override
    public List<RecommendPanelTrackDto> getRecommendTodayFloTrackListByCharacterNoAndRcmmdId(Long characterNo,
            Long rcmmdId) {

        return Optional.ofNullable(
                personalMongoClient.getRecommendTrackListByRcmmdTypeAndRcmmdId("RC_SML_TR", rcmmdId, characterNo, 50)
                .getData()
        ).orElseGet( () -> {

            List<Long> trackIdList =
                    Optional.ofNullable(
                            trackMapper.selectRecommendPanelSimilarTrackList(characterNo, rcmmdId)
                    ).orElseThrow( () -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA));

            CommonApiResponse<ListDto<List<RecommendPanelTrackDto>>> response =
                    metaClient.recommendPanelTracks(trackIdList.toArray(new Long[0]));

            return response.getData().getList();
        });
    }
    @Override
    public List<RecommendPanelTrackDto> getRecommendByRealtimeTrackListByCharacterNoAndRcmmdId(
            Long characterNo, Long rcmmdId) {
        return recommendReadService.getRecommendByRealtimeTrackListByCharacterNoAndRcmmdId(
                characterNo, rcmmdId
        );
    }
    @Override
    public List<ImageInfo> getRecommendPanelDefaultImageList(OsType osType) {
        return recommendReadService.getRecommendPanelDefaultImageList(osType);
    }

}
