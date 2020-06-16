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
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.client.PersonalMongoClient;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendForMeDto;
import com.sktechx.godmusic.personal.rest.repository.RecommendReadMapper;
import com.sktechx.godmusic.personal.rest.repository.TrackMapper;

/**
 * 설명 : 나를 위한 FLO 조회
 *
 */

@Service("rcmmdForMeReadService")
public class RcmmdForMeReadServiceImpl implements RcmmdReadService {
    @Override
    public RecommendPanelContentType getRecommendPanelContentType() {
        return RecommendPanelContentType.RC_CF_TR;
    }

    @Override
    public Optional<RecommendForMeDto> getRecommend(Long characterNo,
            Long rcmmdId) {

        RecommendForMeDto recommendForMeDto = null;

        if(checkUseMongo()){
            recommendForMeDto = personalMongoClient.getRecommendForMeFlo(rcmmdId, characterNo)
                    .getData();
        }

        if(recommendForMeDto == null) {
            recommendForMeDto = recommendReadMapper.selectRecommendForMeFlo(rcmmdId);
        }

        return Optional.ofNullable(recommendForMeDto);
    }

    @Override
    public List<RecommendForMeDto> getRecommendListByCharacterNo(Long characterNo) {
        return Optional.ofNullable(
                checkUseMongo() ?
                        personalMongoClient.getRecommendForMeFloListByCharacterNo(characterNo)
                                .getData().getList()
                        :
                        null
        ).orElseGet(
//                () -> recommendReadService.getRecommendTodayFloListByCharacterNo(characterNo)
                Collections::emptyList
        );
    }

    @Override
    public List<RecommendForMeDto> getRecommendListWithTrackByCharacterNoOrderByDispStartDtime(Long characterNo,
            int panelMaxSize, int trackMaxSize, OsType osType) {
        return Optional.ofNullable(
                checkUseMongo() ?
                    personalMongoClient.getRecommendForMeFloListWithTrackByCharacterNo(characterNo)
                            .getData()
                            .getList()
                        :
                        null
        ).orElseGet( () ->
                Optional.ofNullable(recommendReadMapper.selectRecommendCfTrackListByCharacterNo(
                        characterNo,
                        panelMaxSize,
                        trackMaxSize,
                        osType
                        )
                ).map(
                        recommendTrackDtoList ->
                                recommendTrackDtoList.stream().map(RecommendForMeDto::from).collect(
                                        Collectors.toList())
                ).orElseGet(Collections::emptyList)
        );
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

    // declaration

    private final RedisService redisService;
    private final RecommendReadMapper recommendReadMapper;
    private final TrackMapper trackMapper;
    private final PersonalMongoClient personalMongoClient;
    private final MetaClient metaClient;

    public RcmmdForMeReadServiceImpl(
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
