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
import org.springframework.util.CollectionUtils;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.util.DateUtil;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.*;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.like.RcmmdLikeTrackDto;
import com.sktechx.godmusic.personal.rest.repository.RecommendReadMapper;
import com.sktechx.godmusic.personal.rest.repository.TrackMapper;
import com.sktechx.godmusic.personal.rest.client.PersonalMongoClient;
import lombok.extern.slf4j.Slf4j;

/**
 * 설명 : 추천 데이터 MySql 조회 서비스
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2020-03-12
 */

@Slf4j
@Service("recommendReadService")
public class RecommendReadServiceImpl implements RecommendReadService {

    private final RecommendReadMapper recommendReadMapper;

    private final RecommendImageManagementService recommendImageManagementService;

    private final TrackMapper trackMapper;

    private final MetaClient metaClient;

    private final RedisService redisService;

    public RecommendReadServiceImpl(RecommendReadMapper recommendReadMapper,
            RecommendImageManagementService recommendImageManagementService,
            TrackMapper trackMapper,
            MetaClient metaClient,
            RedisService redisService) {
        this.recommendReadMapper = recommendReadMapper;
        this.recommendImageManagementService = recommendImageManagementService;
        this.trackMapper = trackMapper;
        this.metaClient = metaClient;
        this.redisService = redisService;
    }

    @Override
    public RecommendForMeDto getRecommendForMeFlo(Long characterNo, Long rcmmdMforuId) {
        return recommendReadMapper.selectRecommendForMeFlo(rcmmdMforuId);
    }
    @Override
    public List<RecommendForMeDto> getRecommendForMeFloListByCharacterNo(Long characterNo) {
        return null;
    }

    @Override
    public RecommendSimilarTrackDto getRecommendTodayFlo(Long characterNo,
            Long rcmmdSimilarTrackId) {
        return recommendReadMapper.selectRecommendSimilarTrack(rcmmdSimilarTrackId);
    }
    @Override
    public RecommendArtistDto getRecommendArtistFlo(Long characterNo, Long rcmmdArtistId) {
        return recommendReadMapper.selectRecommendArtistById(rcmmdArtistId);
    }
    @Override
    public List<RecommendSimilarTrackDto> getRecommendTodayFloListByCharacterNo(Long characterNo) {
        return null;
    }
    @Override
    public List<RecommendArtistDto> getRecommendArtistFloListByCharacterNo(Long characterNo) {
        return recommendReadMapper.selectRecommendArtistByCharacterNo(
                characterNo,
                DateUtil.dateToString(
                        Optional.ofNullable(
                                recommendReadMapper
                                        .selectRecommendArtistMostRecentDispDateByCharacterNo(
                                                characterNo
                                        )

                        ).orElseThrow(
                                () -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA)
                        )
                        , "yyyyMMdd")

        );
    }
    @Override
    public List<RecommendTrackDto> getRecommendForMeFloListWithTrackByCharacterNo(Long characterNo,
            int panelMaxSize, int trackMaxSize, OsType osType) {
        return Optional.ofNullable(
                recommendReadMapper.selectRecommendCfTrackListByCharacterNo(
                        characterNo,
                        panelMaxSize,
                        trackMaxSize,
                        osType)
        ).orElseThrow( () -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA));
    }

    @Override
    public List<RecommendTrackDto> getRecommendTodayFloListWithTrackByCharacterNo(
            Long characterNo,
            int panelMaxSize,
            int trackMaxSize,
            OsType osType) {

        return Optional.ofNullable(
                recommendReadMapper.selectRecommendSimilarTrackListByCharacterNo(
                        characterNo,
                        panelMaxSize,
                        trackMaxSize,
                        osType
                )
        ).orElseThrow( () -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA));
    }
    @Override
    public List<RecommendTrackDto> getRecommendArtistFloListWithTrackByCharacterNo(Long characterNo,
            int panelMaxSize, int trackMaxSize, OsType osType) {
        return null;
    }

    @Override
    public List<RecommendPanelTrackDto> getRecommendForMeFloTrackListByCharacterNoAndRcmmdId(
            Long characterNo, Long rcmmdId) {

        return getTrackList(
                Optional.ofNullable(
                        trackMapper.selectRecommendPanelCfTrackList(characterNo, rcmmdId)
                ).orElseThrow( () -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA))
        );
    }

    @Override
    public List<RecommendPanelTrackDto> getRecommendTodayFloTrackListByCharacterNoAndRcmmdId(
            Long characterNo, Long rcmmdId) {

        return getTrackList(
                Optional.ofNullable(
                        trackMapper.selectRecommendPanelSimilarTrackList(characterNo, rcmmdId)
                ).orElseThrow( () -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA))
        );
    }
    @Override
    public List<RecommendPanelTrackDto> getRecommendByRealtimeTrackListByCharacterNoAndRcmmdId(
            Long characterNo, Long rcmmdId) {

        return getTrackList(
                Optional.ofNullable(
                        trackMapper.selectRecommendPanelGenreTrackList(characterNo, rcmmdId)
                ).orElseThrow( () -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA))
        );
    }

    @Override
    public List<RecommendPanelTrackDto> getRecommendArtistFloTrackListByCharacterNoAndRcmmdId(
            Long characterNo, Long rcmmdId) {
        return getTrackList(
                Optional.ofNullable(
                        trackMapper.selectRecommendPanelPopularTrackList(characterNo, rcmmdId)
                ).orElseThrow( () -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA))
        );
    }

    @Override
    public RcmmdLikeTrackDto getRecommendReactiveTrack(Long characterNo, Long rcmmdArtistId) {
        return null;
    }
    @Override
    public List<RcmmdLikeTrackDto> getRecommendReactiveTrackListByCharacterNo(Long characterNo) {
        return null;
    }
    @Override
    public List<RcmmdLikeTrackDto> getRecommendReactiveTrackListWithTrackByCharacterNo(
            Long characterNo, int panelMaxSize, int trackMaxSize, OsType osType) {
        return null;
    }

    private List<RecommendPanelTrackDto> getTrackList(List<Long> trackIdList){

        if(CollectionUtils.isEmpty(trackIdList)){
            return null;
        }

        CommonApiResponse<ListDto<List<RecommendPanelTrackDto>>> response = metaClient.recommendPanelTracks(trackIdList.toArray(new Long[0]));

        if("2000000".equals(response.getCode()) && response.getData() != null) {
            return response.getData().getList();
        }

        return null;
    }

}
