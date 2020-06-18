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
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.util.DateUtil;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.client.PersonalMongoClient;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendPanelTrackDto;
import com.sktechx.godmusic.personal.rest.repository.RecommendReadMapper;
import com.sktechx.godmusic.personal.rest.repository.TrackMapper;

/**
 * 설명 : 나를 위한 FLO 조회
 *
 */

@Service("rcmmdArtistFloReadService")
public class RcmmdArtistFloReadServiceImpl implements RcmmdReadService {

    @Override
    public RecommendPanelContentType getRecommendPanelContentType() {
        return RecommendPanelContentType.RC_SML_TR;
    }

    @Override
    public Optional<RecommendArtistDto> getRecommend(Long characterNo,
            Long rcmmdId) {
        RecommendArtistDto recommendArtistDto = null;

        if(checkUseMongo()){
            recommendArtistDto = personalMongoClient.getRecommendArtistFlo(rcmmdId, characterNo)
                    .getData();
        }

        if(recommendArtistDto == null){
            recommendArtistDto = recommendReadMapper.selectRecommendArtistById(rcmmdId);
        }

        return Optional.ofNullable(recommendArtistDto);
    }

    @Override
    public List<RecommendArtistDto> getRecommendListByCharacterNo(Long characterNo) {
        return Optional.ofNullable(
                checkUseMongo() ?
                        personalMongoClient.getRecommendArtistFloListByCharacterNo(characterNo)
                                .getData().getList()
                        :
                        null
        ).orElseGet(
//                () -> recommendReadService.getRecommendTodayFloListByCharacterNo(characterNo)
                Collections::emptyList
        );
    }

    @Override
    public List<RecommendArtistDto> getRecommendListWithTrackByCharacterNoOrderByDispStartDtime(Long characterNo,
            int panelMaxSize, int trackMaxSize, OsType osType) {
        return Optional.ofNullable(
                checkUseMongo() ?
                    personalMongoClient.getRecommendArtistFloListWithTrackByCharacterNo(characterNo)
                            .getData()
                            .getList()
                        :
                        null
        ).orElseGet( () -> {
                    Date recentDispStartDt =
                            recommendReadMapper.selectRecommendArtistMostRecentDispDateByCharacterNo(
                                    characterNo
                            );

                    if(recentDispStartDt == null){
                        return Collections.emptyList();
                    }
                    return Optional.ofNullable(
                            recommendReadMapper.selectRecommendArtistByCharacterNo(
                                    characterNo,
                                    DateUtil.dateToString(recentDispStartDt, "yyyyMMdd"))
                    ).orElseGet(Collections::emptyList);
                }
        );
    }

    @Override
    public List<RecommendPanelTrackDto> getRecommendTrackListByCharacterNoAndRcmmdId(
            Long characterNo, Long rcmmdId) {
        List<Long> trackIdList =
                Optional.ofNullable(
                        trackMapper.selectRecommendPanelPopularTrackList(characterNo,
                                rcmmdId)
                ).orElseThrow( () -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA));

        CommonApiResponse<ListDto<List<RecommendPanelTrackDto>>> response =
                getMetaClient().recommendPanelTracks(trackIdList.toArray(new Long[0]));

        return response.getData() != null ? response.getData().getList() : Collections.emptyList();
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

    public RcmmdArtistFloReadServiceImpl(
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
