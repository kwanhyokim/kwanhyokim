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

package com.sktechx.godmusic.personal.common.domain.type;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 8. 6.
 */
public enum FixedSize {
    SEARCH_PAGE(1),
    SEARCH_SIZE(300),

    TRACK_ADD_LIKE_MAX_SIZE(300),
    TRACK_MOST_MAX_SIZE(300),
    TRACK_RECENT_MAX_SIZE(300),

    ARTIST_ADD_LIKE_MAX_SIZE(300),
    ALBUM_ADD_LIKE_MAX_SIZE(300),

    LIKE_CHANNEL_ALL(300),
    MY_CHANNEL(1000),
    MY_CHANNEL_TRACK(1000),

//	EVALUATION_DISPLAY_MIN_SIZE(3),
//	RECOMMEND_TRACK_DISPLAY_MIN_SIZE(8),

    MEMBER_BIRTH_YY_LENGTH(4);


    private int size;

    FixedSize(int size){
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}