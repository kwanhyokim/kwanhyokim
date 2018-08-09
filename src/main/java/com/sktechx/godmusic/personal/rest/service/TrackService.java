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

public interface TrackService {
    PageImpl<?> mostTrackList(Long memberNo, Pageable pageable);
}
