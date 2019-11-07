/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.service.video;

import com.sktechx.godmusic.personal.rest.model.vo.video.MostWatchedVideoVo;
import com.sktechx.godmusic.personal.rest.model.vo.video.RangeResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 10. 13.
 */
public interface VideoService {

    /**
     * 캐릭터의 최근 본 영상 목록 조회
     */
    RangeResponse<MostWatchedVideoVo> getRecentWatchedVideos(Long characterNo, Pageable pageable);

    /**
     * 캐릭터의 최근 본 영상 목록 삭제
     */
    void deleteRecentWatchedVideos(Long characterNo, List<Long> videoIds);
}
