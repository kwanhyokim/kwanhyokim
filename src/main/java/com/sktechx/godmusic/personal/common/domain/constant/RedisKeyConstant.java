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
	public static final String PERSONAL_RECOMMEND_PHASE_KEY ="godmusic.personalapi.recommend.phase:%s";
	public static final String RECOMMEND_PANEL_DEFAULT_IMGLIST_KEY ="godmusic.personalapi.recommend.home.panel.default.imglist";

	//삭제 필요 키
	public static final String PREFER_GENRE_POPULAR_CHNL_KEY ="godmusic.personalapi.recommend.prefer.genre.popular.chnllist";
	public static final String MOOD_POPULAR_CHNL_KEY = "godmusic.personalapi.recommend.mood.popular.chnllist";
	public static final String ALL_POPULAR_CHNL_KEY ="godmusic.personalapi.recommend.all.popular.chnllist";

	public static final String REALTIME_CHART_KEY = "godmusic.personalapi.recommend.chart.realtime";
	public static final String KIDS_CHART_KEY = "godmusic.personalapi.recommend.chart.kids";

	//선호
	public static final String PERSONAL_PREFERENCE_GENRE_DEFAULT_KEY = "godmusic.personalapi.preference.genre.default";
	public static final String PERSONAL_PREFERENCE_ARTIST_KEY = "godmusic.personalapi.perference.artist:%s";

	//이미지관리
	public static final String PERSONAL_IMAGE_MANAGEMENT_KEY = "godmusic.personalapi.image.management:{}.{}.{}";
	public static final String PERSONAL_MEMBERCHANNEL_IMAGE_MANAGEMENT_KEY = "godmusic.personalapi.memberchannel.image.management:{}.{}.{}";

	//추천이미지관리
	public static final String RECOMMEND_IMAGE_MANAGEMENT_KEY = "godmusic.personalapi.recommend.image:{}.{}.{}.{}";

	//유사
	public static final String PERSONAL_SIMILAR_ARTIST_KEY = "godmusic.personalapi.similar.artist:%s";
	//유사이력
	public static final String PERSONAL_SIMILAR_ARTIST_HISTORY_KEY = "godmusic.personalapi.similar.artist.history:%s";

	// 선호장르테마, 운영tpo 홈패널 배경 이미지
	public static final String PERSONAL_TPOANDTHEME_IMAGELIST_KEY = "personalapi.recommend.home.panel.tpoandtheme.imglist";
}
