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

import java.util.List;

import com.sktechx.godmusic.personal.rest.model.vo.preference.ChartResponse;
import com.sktechx.godmusic.personal.rest.model.vo.video.VideoVo;

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
    ChartResponse getPreferenceGenreList(Long characterNo);

    /**
     * 선호아티스트 목록 조회
     * @param characterNo
     * @return
     */
    ChartResponse getPreferenceArtistList(Long characterNo);


	/**
	 * 유사 아티스트 목록 조회
	 * @param characterNo
	 * @return
	 */
	ChartResponse getPreferSimilarArtistList(Long characterNo, Integer sectionNumber);

	/**
	 * 유사 시드 아티스트 이름 조회
	 * @param characterNo
	 * @return
	 */
	String getPreferSimilarArtistName(Long characterNo, Integer sectionNumber);


	void deletePreferSimilarArtistName(Long characterNo);

	/**
	 * 선호 아티스트 최신 비디오 목록 가져오기
	 * @param characterNo
	 * @return
	 */
	List<VideoVo> getPreferenceVideoArtistNewList(Long characterNo);

	/**
	 * 선호 장르 최신 비디오 목록 가져오기
	 * @param characterNo
	 * @return
	 */
	List<VideoVo> getPreferenceVideoGenreNewList(Long characterNo);

	List<VideoVo> getLimitedShuffledVideoList(List<VideoVo> videoVoList, Integer limitSize);

	/**
	 * 선호 아티스트 최신 비디오 캐쉬 제거
	 * @param characterNo
	 * @return
	 */
	void clearCachePreferenceVideoArtistNewList(Long characterNo);

	/**
	 * 선호 장르 최신 비디오 캐쉬 제거
	 * @param characterNo
	 * @return
	 */

	void clearCachePreferenceVideoGenreNewList(Long characterNo);


}
