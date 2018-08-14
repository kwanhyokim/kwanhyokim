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

        long totalCount = trackMapper.selectMyRecentTrackTotalCount(characterNo);

        return new PageImpl<>(recentListenedTrackList, pageable, totalCount);
    }
}
