/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.service.mongo;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.personal.common.domain.ListResponse;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenDeleteTrackRequest;
import com.sktechx.godmusic.personal.rest.service.TrackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 08. 30.
 */
@Slf4j
@Service("trackMongoService")
public class TrackMongoServiceImpl implements TrackService {

    @Autowired
    PersonalMongoClient personalMongoClient;

    @Autowired
    @Qualifier("trackService")
    TrackService trackService;

    @Autowired
    MongoRedisService mongoRedisService;

    @Override
    public PageImpl<?> mostTrackList(Long characterNo, Pageable pageable) {
        return mongoRedisService.executeService(
                () -> {
                    CommonApiResponse<ListResponse> result = personalMongoClient.getMostListenedTracks(characterNo, pageable.getPageNumber(), pageable.getPageSize());
                    if (result.getData() == null || result.getData().getList() == null || result.getData().getList().isEmpty()) {
                        return trackService.mostTrackList(characterNo, pageable);
                    }
                    return new PageImpl<>(result.getData().getList(), pageable, result.getData().getTotalCount());
                },
                () -> trackService.mostTrackList(characterNo, pageable)
        );
    }

    @Override
    public PageImpl<?> getMyRecentTrackList(Long memberNo, Long characterNo, Pageable pageable) {
        return mongoRedisService.executeService(
                () -> {
                    CommonApiResponse<ListResponse> result = personalMongoClient.getRecentListenedTracks(memberNo, characterNo, pageable.getPageNumber(), pageable.getPageSize());
                    if (result.getData() == null || result.getData().getList() == null || result.getData().getList().isEmpty()) {
                        return trackService.getMyRecentTrackList(memberNo, characterNo, pageable);
                    }
                    return new PageImpl<>(result.getData().getList(), pageable, result.getData().getTotalCount());
                },
                () -> trackService.getMyRecentTrackList(memberNo, characterNo, pageable)
        );

    }

    /**
     * 2020.03.19
     * Notice. 몽고 2차 도입으로 인해 최근들은 곡 삭제 API는 Personal-Mgo 로 이관 및 직접 서비스 함
     *         데이타 싱크를 위해서 Personal-Mgo -> Personal 로 Feign 통신 하도록 변경 됨
     *         따라서 해당 호출 로직 주석 처리 함. 항후 이슈 없을 경우 메소드 삭제 처리
     */
    @Override
    public void deleteMyRecentTrackList(Long memberNo, Long characterNo, List<Long> trackIds) {

        log.warn("[최근들은곡][삭제] Personal -> PersonalMgo 호출 발생. characterNo ={}", characterNo);
//        mongoRedisService.executeService(
//                () -> personalMongoClient.deleteRecentListenTrack(memberNo, characterNo, ListenDeleteTrackRequest.builder().trackIds(trackIds).build())
//        );

    }
}
