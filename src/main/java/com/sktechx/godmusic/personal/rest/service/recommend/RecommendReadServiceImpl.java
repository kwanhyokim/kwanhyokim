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

import org.springframework.stereotype.Service;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.*;
import com.sktechx.godmusic.personal.rest.repository.RecommendReadMapper;
import com.sktechx.godmusic.personal.rest.repository.TrackMapper;
import com.sktechx.godmusic.personal.rest.service.mongo.PersonalMongoClient;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2020-03-12
 */

@Service("recommendReadService")
public class RecommendReadServiceImpl implements RecommendReadService {

    private final PersonalMongoClient personalMongoClient;

    private final RecommendReadMapper recommendReadMapper;

    private final TrackMapper trackMapper;

    private final MetaClient metaClient;

    public RecommendReadServiceImpl(PersonalMongoClient personalMongoClient,
            RecommendReadMapper recommendReadMapper,
            TrackMapper trackMapper,
            MetaClient metaClient) {
        this.personalMongoClient = personalMongoClient;
        this.recommendReadMapper = recommendReadMapper;
        this.trackMapper = trackMapper;
        this.metaClient = metaClient;
    }

    @Override
    public RecommendForMeDto getRecommendFormeFlo(Long characterNo, Long rcmmdMforuId) {

//        return Optional.ofNullable(
//                personalMongoClient.getRecommendFormeFlo(rcmmdMforuId, characterNo)
//                        .getData()
//        ).orElseGet(
//                () -> recommendReadMapper.selectRecommendGenreByRcmmdId(rcmmdMforuId)
//        );

        return recommendReadMapper.selectRecommendGenreByRcmmdId(rcmmdMforuId);
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
//        return Optional.ofNullable(
//                personalMongoClient.getRecommendArtistFlo(rcmmdArtistId, characterNo).getData()
//        ).orElseGet( () -> recommendReadMapper.selectRecommendArtistById(rcmmdArtistId));

        return recommendReadMapper.selectRecommendArtistById(rcmmdArtistId);
    }

    @Override
    public List<RecommendTrackDto> getRecommendTodayFloListWithTrackByCharacterNo(
            Long characterNo,
            int panelMaxSize,
            int trackMaxSize,
            OsType osType) {

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

}
