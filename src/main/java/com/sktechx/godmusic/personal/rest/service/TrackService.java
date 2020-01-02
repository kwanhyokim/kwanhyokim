/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.service;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TrackService {

    PageImpl<?> mostTrackList(Long characterNo, Pageable pageable);

    PageImpl<?> getMyRecentTrackList(Long memberNo, Long characterNo, Pageable pageable);

    void deleteMyRecentTrackList(Long memberNo, Long characterNo, List<Long> trackIds);

}
