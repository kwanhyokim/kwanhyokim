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
 * 설명 : 추천 관련 상수
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 08. 17.
 */
public class RecommendConstant {
    public static final int POPULAR_CHNL_LIST_SIZE  = 3;
    public static final int POPULAR_CHNL_CACHE_LIMIT_SIZE= 20;
    public static final int POPULAR_CHNL_EXPIRED_SECONDS = 300;
    public static final int PREFER_GENRE_POPULAR_CHNL_EXPIRED_SECONDS = 86400;
    public static final int MOOD_POPULAR_CHNL_EXPIRED_SECONDS = 86400;
    public static final int REALTIME_CHART_EXPIRED_SECONDS = 300;
    public static final int KIDS_CHART_EXPIRED_SECONDS = 300;

    public static final int CHARACTER_PREFER_GENRE_VIEW_LIMIT_SIZE = 3;

    //청취 단계 패널
    public static final int SIMILAR_TRACK_PANEL_SIZE  = 2;
    public static final int PREFER_GENRE_SIMILAR_PANEL_SIZE  = 2;
    public static final int LISTEN_MOOD_POPULAR_PANEL_SIZE  = 1;


    //추천 단계 패널
    public static final int RCMMD_CF_PANEL_DEFAULT_SIZE  = 2;
    public static final int SIMILAR_TRACK_PANEL_APPEND_SIZE = 1;
    public static final int PREFER_GENRE_SIMILAR_TRACK_PANEL_APPEND_SIZE = 2;
    public static final int RCMMD_CF_TRACK_LIMIT_SIZE = 15;

    //방문 단계
    public static final int PREFER_GENRE_POPULAR_CHNL_LIST_SIZE = 3;

    public static final int POPULAR_CHNL_TRACK_LIMIT_SIZE = 10;
    public static final int PREFER_GENRE_SIMILAR_TRACK_LIMIT_SIZE = 10;
    public static final int SIMILAR_TRACK_LIMIT_SIZE = 10;
    public static final int PREFER_DISP_CHART_TRACK_LIMIT_SIZE = 12;

    //노출 가능한 최소 개수
    public static final int SIMILAR_TRACK_DISP_STANDARD_COUNT = 15;
    public static final int PREFER_GENRE_SIMILAR_TRACK_DISP_STANDARD_COUNT = 15;


    public static final int ARTIST_POPULAR_TRACK_DISP_STANDARD_COUNT = 30;
    public static final int ARTIST_POPULAR_TRACK_DISP_ONE_ARTIST_COUNT = 12;
    public static final int ARTIST_POPULAR_TRACK_DISP_TWO_ARTIST_COUNT = 24;
    public static final int ARTIST_POPULAR_ARTIST_NAME_COUNT = 5;


    public static final int RCMMD_CF_TRACK_DISP_STANDARD_COUNT = 50;

    public static final String ARTIST_PANEL_TITLE = "Musician focus";
    public static final String CHART_PANEL_HOURLY_BASIS_PHRASES = "시 기준";

    public static final String PREFER_GENRE_SIMILAR_TRACK_PANEL_TITLE = "Mix Tape";
    public static final String PREFER_GENRE_SIMILAR_TRACK_PANEL_SUB_TITLE = "내가 많이 들은 곡과\n비슷한 노래";
    public static final String RCMMD_TRACK_PANEL_TITLE = "Made for U";
    public static final String RCMMD_TRACK_PANEL_SUB_TITLE = "내가 많이 들은\n 장르의 추천 노래";

    public static final String SIMILAR_TRACK_PANEL_TITLE = "Like U";
    public static final String SIMILAR_TRACK_PANEL_SUB_TITLE = "많이 들었던 노래와\n 유사한 선곡";

}


