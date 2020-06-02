/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.service.home;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.client.PersonalMongoClient;
import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferDispDto;
import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferGenreDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPanel;
import com.sktechx.godmusic.personal.rest.repository.CharacterPreferGenreMapper;
import com.sktechx.godmusic.personal.rest.repository.RecommendReadMapper;
import lombok.extern.slf4j.Slf4j;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.RCMMD_CF_TRACK_DISP_STANDARD_COUNT;
import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.SIMILAR_TRACK_DISP_STANDARD_COUNT;
import static com.sktechx.godmusic.personal.common.domain.constant.RedisKeyConstant.PERSONAL_USEMGO_KEY;

/**
 * 설명 : 홈 메타 조회 시, 비동기 처리가 필요한 DB 쿼리를 호출하는 서비스
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2019-09-05
 */

@Slf4j
@Service
public class HomeMetaServiceImpl implements HomeMetaService {

    @Autowired
    private CharacterPreferGenreMapper characterPreferGenreMapper;

    @Autowired
    private RecommendReadMapper recommendReadMapper;

    @Autowired
    private PersonalMongoClient personalMongoClient;

    @Autowired
    private RedisService redisService;

    @Async
    @Override
    public CompletableFuture<List<CharacterPreferGenreDto>> getCharacterPreferGenreList(
            Long characterNo) {
        return CompletableFuture.completedFuture(
                characterPreferGenreMapper.selectCharacterPreferGenreList(characterNo)
        );
    }

    @Async
    @Override
    public CompletableFuture<List<CharacterPreferDispDto>> getCharacterPreferDispList(
            Long characterNo) {
        return CompletableFuture.completedFuture(
                characterPreferGenreMapper.selectCharacterPreferDispList(characterNo)
        );
    }

    private Boolean checkUseMongo(RecommendPanelContentType recommendPanelContentType){
        boolean exists = redisService.existsWithPrefix(
                String.format(PERSONAL_USEMGO_KEY, recommendPanelContentType.getCode()));
        log.debug("checkUseMongo: {} {}", recommendPanelContentType, exists);
        return exists;
    }

    @Async
    @Override
    public CompletableFuture<List<PersonalPanel>> getPersonalRecommendPanelMeta(Long characterNo, Boolean checkDispEndDt) {
        return CompletableFuture.completedFuture(
                recommendReadMapper.selectPersonalRecommendPanelMeta(characterNo,
                        SIMILAR_TRACK_DISP_STANDARD_COUNT,
                        RCMMD_CF_TRACK_DISP_STANDARD_COUNT,
                        checkDispEndDt,
                        checkUseMongo(RecommendPanelContentType.RC_CF_TR),
                        checkUseMongo(RecommendPanelContentType.RC_SML_TR),
                        checkUseMongo(RecommendPanelContentType.RC_ATST_TR)
                )
        );
    }

    @Override
    public CompletableFuture<List<PersonalPanel>> getPersonalRecommendPanelMgoMeta(
            Long characterNo) {
        return CompletableFuture.completedFuture(Optional.ofNullable(
                personalMongoClient.getRcmmdPanelMetaByCharacterNo(characterNo).getData()
                ).orElseGet(()-> new ListDto<>(null))
                .getList()
        );

    }
}
