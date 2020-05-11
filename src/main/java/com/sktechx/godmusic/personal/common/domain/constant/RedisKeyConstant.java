/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.common.domain.constant;

/**
 * @author : Donghwan Kim(skillz@sk.com)
 * @since on 2018. 7. 13..
 */
public class RedisKeyConstant {
	public static final String PREFIX = "godmusic";
	//추천
	public static final String PERSONAL_RECOMMEND_PHASE_KEY =
			"godmusic.personalapi.recommend.phase:%s";
	//삭제 필요 키
	public static final String PREFER_GENRE_POPULAR_CHNL_KEY =
			"godmusic.personalapi.recommend.prefer.genre.popular.chnllist";
	public static final String MOOD_POPULAR_CHNL_KEY =
			"godmusic.personalapi.recommend.mood.popular.chnllist";
	public static final String ALL_POPULAR_CHNL_KEY =
			"godmusic.personalapi.recommend.all.popular.chnllist";

	/** 차트 **/

	// 실시간 차트 캐쉬
	public static final String REALTIME_CHART_KEY =
			"godmusic.personalapi.recommend.chart.realtime";
	// 키즈 차트 캐쉬
	public static final String KIDS_CHART_KEY =
			"godmusic.personalapi.recommend.chart.kids";

	// 차트 전시용 정보 캐쉬 ( 차트명, 차트 이미지 캐쉬)
	public static final String CHART_DISPLAY_PROPERTIES_KEY =
			"godmusic.personalapi.recommend.chart.dispprops";

	// 개인화 차트 캐쉬 ( Mgo의 차트 순서 정보 캐쉬 )
	public static final String PERSONAL_CHART_KEY =
			"godmusic.personalapi.recommend.chart:%s.%s";

	//선호
	public static final String PERSONAL_PREFERENCE_GENRE_DEFAULT_KEY =
			"godmusic.personalapi.preference.genre.default";
	public static final String PERSONAL_PREFERENCE_ARTIST_KEY =
			"godmusic.personalapi.perference.artist:%s";

	/** 이미지 **/

	//이미지관리
	public static final String PERSONAL_IMAGE_MANAGEMENT_KEY =
			"godmusic.personalapi.image.management:{}.{}.{}";

	// 추천 패널 디폴트 이미지 ( 배경 이미지 없을 경우 대체)
	public static final String RECOMMEND_PANEL_DEFAULT_IMGLIST_KEY =
			"godmusic.personalapi.recommend.home.panel.default.imglist";
	// 일반 차트 배경 이미지
	public static final String PERSONAL_CHART_BACKGROUND_IMAGE_KEY =
			"godmusic.personalapi.chart.background.image:{}.{}";
	// 추천 차트 기본 배경 이미지
	public static final String PERSONAL_PRICHART_DEFAULT_BACKGROUND_IMAGE_KEY =
			"godmusic.personalapi.prichart.default.background.image:{}";
	// 추천 차트 장르별 배경 이미지
	public static final String PERSONAL_PRICHART_BACKGROUND_IMAGE_KEY =
			"godmusic.personalapi.prichart.background.image:{}.{}";

	/** 선호와 유사한 아티스트 **/

	//유사
	public static final String PERSONAL_SIMILAR_ARTIST_KEY =
			"godmusic.personalapi.similar.artist:%s";

	//유사이력
	public static final String PERSONAL_SIMILAR_ARTIST_HISTORY_KEY =
			"godmusic.personalapi.similar.artist.history:%s";


	/** 홈 영상 **/

	// 홈 선호 아티스트 최신 영상
	public static final String PERSONAL_PREFERENCE_VIDEO_ARTIST_NEW_LIST=
			"godmusic.personalapi.preference.video.artist.new.list:%s";

	// 홈 선호 장르 최신 영상
	public static final String PERSONAL_PREFERENCE_VIDEO_GENRE_NEW_LIST=
			"godmusic.personalapi.preference.video.genre.new.list:%s";

	// 홈 선호 장르 최신 디폴트 영상
	public static final String PERSONAL_PREFERENCE_VIDEO_GENRE_NEW_DEFAULT_LIST=
			"godmusic.personalapi.preference.video.genre.new.default.list";

}
