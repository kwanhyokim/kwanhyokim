/*
 *
 *  * Copyright (c) 2018 SK TECHX.
 *  * All right reserved.
 *  *
 *  * This software is the confidential and proprietary information of SK TECHX.
 *  * You shall not disclose such Confidential Information and
 *  * shall use it only in accordance with the terms of the license agreement
 *  * you entered into with SK TECHX.
 *
 */

package com.sktechx.godmusic.personal.rest.service;

import com.sktechx.godmusic.personal.rest.model.vo.preference.Chart;
import com.sktechx.godmusic.personal.rest.model.vo.preference.PreferenceResponse;

/**
 * 설명 : 선호 장르 서비스
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 7. 19.
 */
public interface PreferenceService {
    /**
     * 선호장르 목록 조회
     * @param characterNo
     * @return
     */
    PreferenceResponse getPreferenceGenreList(Long characterNo);

    /**
     * 선호장르별 상세(차트내 트랙 목록)
     * @param chartId
     * @return
     */
    Chart getPreferenceGenre(Long chartId);

    /**
     * 선호아티스트 목록 조회
     * @param characterNo
     * @return
     */
    PreferenceResponse getPreferenceArtistList(Long characterNo);
}
