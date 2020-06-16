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
import com.sktechx.godmusic.personal.rest.client.PersonalMongoClient;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendPanelTrackDto;
import com.sktechx.godmusic.personal.rest.repository.TrackMapper;

/**
 * 설명 : 나를 위한 FLO 조회
 *
 */

@Service("rcmmdEmptyReadService")
public class RcmmdEmptyReadServiceImpl implements RcmmdReadService {

    @Override
    public Optional<RecommendDto> getRecommend(Long characterNo,
            Long rcmmdId) {
        return Optional.of(new RecommendDto() {
                @Override
                public List<TrackDto> getTrackDtoList() {
                    return null;
                }
                @Override
                public void setTrackDtoList(List<TrackDto> trackVoList) {
                }
                @Override
                public List<Long> getTrackIdList() {
                    return null;
                }
                @Override
                public void setTrackIdList(List<Long> trackIdList) {
                }
            }
        );
    }

    @Override
    public List<? extends RecommendDto> getRecommendListByCharacterNo(Long characterNo) {
        return Collections.emptyList();
    }

    @Override
    public List<? extends RecommendDto> getRecommendListWithTrackByCharacterNoOrderByDispStartDtime(Long characterNo,
            int panelMaxSize, int trackMaxSize, OsType osType) {
        return Collections.emptyList();
    }

    @Override
    public List<RecommendPanelTrackDto> getRecommendTrackListByCharacterNoAndRcmmdId(
            Long characterNo, Long rcmmdId) {
        return Collections.emptyList();
    }
    @Override
    public PersonalMongoClient getPersonalMongoClient() {
        return null;
    }
    @Override
    public MetaClient getMetaClient() {
        return null;
    }
    @Override
    public TrackMapper getTrackMapper() {
        return null;
    }

    @Override
    public RedisService getRedisService() {
        return null;
    }

    @Override
    public boolean checkUseMongo() {
        return false;
    }

    @Override
    public RecommendPanelContentType getRecommendPanelContentType() {
        return null;
    }
}
