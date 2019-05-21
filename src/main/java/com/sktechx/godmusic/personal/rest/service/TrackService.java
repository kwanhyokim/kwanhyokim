/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.sktechx.godmusic.personal.common.domain.ListResponse;

public interface TrackService {
    ListResponse mostTrackList(Long characterNo, Long page, Long size);

    PageImpl<?> getMyRecentTrackList(Long memberNo, Long characterNo, Pageable pageable);

    void deleteMyRecentTrackList(Long memberNo, Long characterNo, Long trackId);
}
