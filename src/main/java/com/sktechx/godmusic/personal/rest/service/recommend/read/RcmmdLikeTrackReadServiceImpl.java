/*
 * Copyright (c) 2020 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.service.recommend.read;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendPanelTrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.like.RcmmdLikeTrackDto;
import com.sktechx.godmusic.personal.rest.repository.RecommendReadMapper;
import com.sktechx.godmusic.personal.rest.repository.TrackMapper;
import com.sktechx.godmusic.personal.rest.service.mongo.PersonalMongoClient;

/**
 * 설명 : 반응형 레이더 조회 서비스
 */

@Service("rcmmdLikeTrackReadService")
public class RcmmdLikeTrackReadServiceImpl implements RcmmdReadService {
    @Override
    public RecommendPanelContentType getRecommendPanelContentType() {
        return RecommendPanelContentType.RC_LKSM_TR;
    }

    @Override
    public RcmmdLikeTrackDto getRecommend(Long characterNo,
            Long rcmmdId) {
        return Optional.ofNullable(
                checkUseMongo() ?
                        personalMongoClient.getRecommendLikeTrack(characterNo, rcmmdId)
                                .getData()
                        :
                        null
        ).orElseGet(
                () -> RcmmdLikeTrackDto.builder().build()
        );
    }

    @Override
    public List<RcmmdLikeTrackDto> getRecommendListByCharacterNo(Long characterNo) {
        return Optional.ofNullable(
                checkUseMongo() ?
                        personalMongoClient.getRecommendLikeTracksList(characterNo)
                                .getData().getList()
                        :
                        null
        ).orElseGet(
//                () -> recommendReadService.getRecommendTodayFloListByCharacterNo(characterNo)
                Collections::emptyList
        );
    }

    @Override
    public List<RcmmdLikeTrackDto> getRecommendListWithTrackByCharacterNoOrderByDispStartDtime(Long characterNo,
            int panelMaxSize, int trackMaxSize, OsType osType) {
        return Optional.ofNullable(
                personalMongoClient.getRecommendLikeTracksList(characterNo).getData().getList()
        ).orElseGet(
                Collections::emptyList
        );
    }

    @Override
    public List<RecommendPanelTrackDto> getRecommendTrackListByCharacterNoAndRcmmdId(
            Long characterNo, Long rcmmdId) {
        return Optional.ofNullable(
                personalMongoClient.getRecommendTrackListByRcmmdTypeAndRcmmdId(
                        getRecommendPanelContentType().getCode(), rcmmdId, characterNo, 50)
                        .getData().getList()
        ).orElseGet(Collections::emptyList);
    }

    @Override
    public PersonalMongoClient getPersonalMongoClient() {
        return this.personalMongoClient;
    }
    @Override
    public MetaClient getMetaClient() {
        return this.metaClient;
    }
    @Override
    public TrackMapper getTrackMapper() {
        return this.trackMapper;
    }

    @Override
    public RedisService getRedisService() {
        return redisService;
    }

    @Override
    public boolean checkUseMongo() {
        return true;
    }

    // declaration

    private final RedisService redisService;
    private final RecommendReadMapper recommendReadMapper;
    private final TrackMapper trackMapper;
    private final PersonalMongoClient personalMongoClient;
    private final MetaClient metaClient;

    public RcmmdLikeTrackReadServiceImpl(
            RedisService redisService,
            RecommendReadMapper recommendReadMapper,
            TrackMapper trackMapper,
            PersonalMongoClient personalMongoClient,
            MetaClient metaClient
    ){
        this.redisService = redisService;
        this.recommendReadMapper = recommendReadMapper;
        this.trackMapper = trackMapper;
        this.personalMongoClient = personalMongoClient;
        this.metaClient = metaClient;
    }

}
