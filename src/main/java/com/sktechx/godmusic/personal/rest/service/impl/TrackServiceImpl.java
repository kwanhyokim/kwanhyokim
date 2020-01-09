/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.personal.common.domain.constant.TrackConstant;
import com.sktechx.godmusic.personal.rest.model.dto.MostListenedTrackDto;
import com.sktechx.godmusic.personal.rest.repository.TrackMapper;
import com.sktechx.godmusic.personal.rest.service.TrackService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Slf4j
@Service("trackService")
public class TrackServiceImpl implements TrackService {

    @Autowired
    private TrackMapper trackMapper;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public PageImpl<?> mostTrackList(Long characterNo, Pageable pageable) {

        List<MostListenedTrackDto> mostTrackList = trackMapper.selectMostListenedTrackList(characterNo, pageable);

        if (CollectionUtils.isEmpty(mostTrackList)) {
            throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);
        }

        long totalCount = trackMapper.selectMostListenedTrackTotalCount(characterNo);
        // 1000곡 노출 (pagination 처리 하면 달라져야 함)
//        long totalCount = mostTrackList.size() == pageable.getPageSize() ? pageable.getPageSize() : mostTrackList.size();

        if (totalCount > TrackConstant.MOST_TRACK_LIST_MAX_COUNT) {
            totalCount = TrackConstant.MOST_TRACK_LIST_MAX_COUNT;
        }

        return new PageImpl<>(mostTrackList, pageable, totalCount);
    }

    @Override
    public PageImpl<?> getMyRecentTrackList(Long memberNo, Long characterNo, Pageable pageable) {

        List<MostListenedTrackDto> recentListenedTrackList = trackMapper.selectMyRecentTrackList(memberNo, characterNo, pageable);

        if (CollectionUtils.isEmpty(recentListenedTrackList)) {
            throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);
        }

        long totalCount = trackMapper.selectMyRecentTrackTotalCount(memberNo, characterNo);

        return new PageImpl<>(recentListenedTrackList, pageable, totalCount);
    }

    @Override
    public void deleteMyRecentTrackList(Long memberNo, Long characterNo, List<Long> trackIdList) {

        Map<String, Object> batchParam = new HashMap<>();

        if (trackIdList == null) {
            throw new CommonBusinessException(CommonErrorDomain.BAD_REQUEST);
        }

        try (
                SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(
                        ExecutorType.BATCH, false)) {
            IntStream.range(0, trackIdList.size())
                    .forEach(index ->
                            {
                                batchParam.clear();
                                batchParam.put("memberNo", memberNo);
                                batchParam.put("characterNo", characterNo);
                                batchParam.put("trackId", trackIdList.get(index));

                                log.info("deleteMyRecentTrackList batchParam : " + batchParam.toString());
                                sqlSession.update("deleteMyRecentTrackList", batchParam);
                            }
                    );

            sqlSession.flushStatements();
            sqlSession.commit();

        } catch (Exception e) {
            log.error("Track :: delete my recent track list :: Error Message", e);
            throw new CommonBusinessException(CommonErrorDomain.INTERNAL_SERVER_ERROR);
        }

    }

}
