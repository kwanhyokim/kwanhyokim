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
import org.springframework.util.CollectionUtils;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.client.PersonalMongoClient;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendPanelTrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendSimilarTrackDto;
import com.sktechx.godmusic.personal.rest.repository.RecommendReadMapper;
import com.sktechx.godmusic.personal.rest.repository.TrackMapper;

/**
 * 설명 : 오늘의 FLO 조회 서비스
 *
 */

@Service("rcmmdTodayReadService")
public class RcmmdTodayReadServiceImpl implements RcmmdReadService {

    @Override
    public RecommendPanelContentType getRecommendPanelContentType() {
        return RecommendPanelContentType.RC_SML_TR;
    }
    @Override
    public Optional<RecommendSimilarTrackDto> getRecommend(Long characterNo,
            Long rcmmdId) {

        RecommendSimilarTrackDto recommendSimilarTrackDto = null;

        if(checkUseMongo()){
            recommendSimilarTrackDto =
                        personalMongoClient.getRecommendTodayFlo(rcmmdId, characterNo).getData();
        }
        if(recommendSimilarTrackDto == null) {
            recommendSimilarTrackDto = recommendReadMapper.selectRecommendSimilarTrack(rcmmdId);
        }

        return Optional.ofNullable(recommendSimilarTrackDto);
    }

    @Override
    public List<RecommendSimilarTrackDto> getRecommendListByCharacterNo(Long characterNo) {
        return Optional.ofNullable(
                checkUseMongo() ?
                        personalMongoClient.getRecommendTodayFloListByCharacterNo(characterNo)
                                .getData().getList()
                        :
                        null
        ).orElseGet(
//                () -> recommendReadService.getRecommendTodayFloListByCharacterNo(characterNo)
                Collections::emptyList
        );
    }

    @Override
    public List<RecommendSimilarTrackDto> getRecommendListWithTrackByCharacterNoOrderByDispStartDtime
            (Long characterNo, int panelMaxSize, int trackMaxSize, OsType osType) {
        return Optional.ofNullable(
                checkUseMongo() ?
                personalMongoClient.getRecommendTodayFloListWithTrackByCharacterNo(characterNo).getData()
                        .getList()
                        :
                        null
        ).orElseGet( () ->
                    Optional.ofNullable(recommendReadMapper.selectRecommendSimilarTrackListByCharacterNo(
                                characterNo,
                                panelMaxSize,
                                trackMaxSize,
                                osType
                            )
                    ).map(
                            recommendTrackDtoList ->
                            recommendTrackDtoList.stream().map(RecommendSimilarTrackDto::from).collect(
                            Collectors.toList())
                    ).orElseGet(Collections::emptyList)
        );
    }

    @Override
    public List<RecommendPanelTrackDto> getRecommendTrackListByCharacterNoAndRcmmdId(
            Long characterNo, Long rcmmdId) {
        List<RecommendPanelTrackDto> recommendPanelTrackDtoList = null;

        if (checkUseMongo()) {
            recommendPanelTrackDtoList = getPersonalMongoClient()
                    .getRecommendTrackListByRcmmdTypeAndRcmmdId(getRecommendPanelContentType().getCode(), rcmmdId, characterNo, 50)
                    .getData().getList();
        }
        if (CollectionUtils.isEmpty(recommendPanelTrackDtoList)) {
            List<Long> trackIdList = Optional.ofNullable(
                    trackMapper.selectRecommendPanelSimilarTrackList(characterNo, rcmmdId))
                    .orElseThrow(() -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA));

            CommonApiResponse<ListDto<List<RecommendPanelTrackDto>>> response =
                    getMetaClient().recommendPanelTracks(trackIdList.toArray(new Long[0]));
            recommendPanelTrackDtoList = response.getData() != null ?
                    response.getData().getList() :
                    Collections.emptyList();
        }
        return recommendPanelTrackDtoList;
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
    public RedisService getRedisService() {
        return redisService;
    }

    // declaration

    private final RedisService redisService;
    private final RecommendReadMapper recommendReadMapper;
    private final TrackMapper trackMapper;
    private final PersonalMongoClient personalMongoClient;
    private final MetaClient metaClient;

    public RcmmdTodayReadServiceImpl(
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
