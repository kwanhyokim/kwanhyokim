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

import com.google.common.primitives.Ints;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.exception.CommonErrorMessage;
import com.sktechx.godmusic.personal.rest.model.dto.MostListenedTrackDto;
import com.sktechx.godmusic.personal.rest.repository.TrackMapper;
import com.sktechx.godmusic.personal.rest.service.TrackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.NumberUtils;

import java.util.List;

@Slf4j
@Service
public class TrackServiceImpl implements TrackService {

    @Autowired
    private TrackMapper trackMapper;

    @Override
    public PageImpl<?> mostTrackList(Long characterNo, Pageable pageable) {

        List<MostListenedTrackDto> mostTrackList = trackMapper.selectMostListenedTrackList(characterNo, pageable);

        if(CollectionUtils.isEmpty(mostTrackList)){
            throw new CommonBusinessException(CommonErrorMessage.EMPTY_DATA);
        }

        long totalCount = trackMapper.selectMostListenedTrackTotalCount(characterNo);

        return new PageImpl<>(mostTrackList, pageable, totalCount);
    }

    @Override
    public PageImpl<?> getMyRecentTrackList(Long characterNo, Pageable pageable){

        List<MostListenedTrackDto> recentListenedTrackList = trackMapper.selectMyRecentTrackList(characterNo, pageable);

        if(CollectionUtils.isEmpty(recentListenedTrackList)){
            throw new CommonBusinessException(CommonErrorMessage.EMPTY_DATA);
        }

        int start = pageable.getPageNumber() * pageable.getPageSize();
        int end = (pageable.getPageNumber() + 1) * pageable.getPageSize();

        long totalCount = trackMapper.selectMyRecentTrackTotalCount(characterNo);
        if(totalCount > 500) totalCount = 500;
        if(end > 500) end = 500;
        if(end > totalCount) end = Ints.checkedCast(totalCount);

        return new PageImpl<>(recentListenedTrackList.subList(start, end), pageable, totalCount);
    }
}
