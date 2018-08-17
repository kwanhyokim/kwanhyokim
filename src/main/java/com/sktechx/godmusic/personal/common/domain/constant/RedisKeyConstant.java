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
	//추천
	public static final String PERSONAL_RECOMMEND_PHASE_KEY ="godmusic.personalapi.recommend.phase:%s";
	public static final String PREFER_GENRE_POPULAR_CHNL_KEY ="godmusic.personalapi.recommend.prefer.genre.popular.chnllist";
	public static final String MOOD_POPULAR_CHNL_KEY = "godmusic.personalapi.recommend.mood.popular.chnllist";
	public static final String ALL_POPULAR_CHNL_KEY ="godmusic.personalapi.recommend.all.popular.chnllist";
	public static final String RECOMMEND_PANEL_DEFAULT_IMG_KEY ="godmusic.personalapi.recommend.home.panel.default.imglist";

}
