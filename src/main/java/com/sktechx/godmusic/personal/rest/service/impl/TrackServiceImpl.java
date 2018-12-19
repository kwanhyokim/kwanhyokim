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

import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.personal.common.domain.ListResponse;
import com.sktechx.godmusic.personal.common.domain.constant.TrackConstant;
import com.sktechx.godmusic.personal.rest.model.dto.MostListenedTrackDto;
import com.sktechx.godmusic.personal.rest.repository.TrackMapper;
import com.sktechx.godmusic.personal.rest.service.TrackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
public class TrackServiceImpl implements TrackService {

    @Autowired
    private TrackMapper trackMapper;

    @Override
    public ListResponse mostTrackList(Long characterNo, Long page, Long size) {

        if(page <= 0) page = 1L;
        Long offset = (page - 1) * size;
        List<MostListenedTrackDto> mostTrackList = trackMapper.selectMostListenedTrackList(characterNo, offset, size);

        if(CollectionUtils.isEmpty(mostTrackList)){
            throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);
        }

        long totalCount = trackMapper.selectMostListenedTrackTotalCount(characterNo);
        // 1000곡 노출 (pagination 처리 하면 달라져야 함)
//        long totalCount = mostTrackList.size() == pageable.getPageSize() ? pageable.getPageSize() : mostTrackList.size();

        if(totalCount > TrackConstant.MOST_TRACK_LIST_MAX_COUNT)
            totalCount = TrackConstant.MOST_TRACK_LIST_MAX_COUNT;

        YnType lastPage = (int)Math.ceil(totalCount / size  + 0.5) <= page ?YnType.Y : YnType.N;

        return new ListResponse((int)totalCount, page.intValue(), lastPage,  mostTrackList);
    }

    @Override
    public PageImpl<?> getMyRecentTrackList(Long memberNo, Long characterNo, Pageable pageable){

        List<MostListenedTrackDto> recentListenedTrackList = trackMapper.selectMyRecentTrackList(memberNo, characterNo, pageable);

        if(CollectionUtils.isEmpty(recentListenedTrackList)){
            throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);
        }

        long totalCount = trackMapper.selectMyRecentTrackTotalCount(memberNo, characterNo);

        return new PageImpl<>(recentListenedTrackList, pageable, totalCount);
    }
}
