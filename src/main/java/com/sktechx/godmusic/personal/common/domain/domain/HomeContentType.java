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

package com.sktechx.godmusic.personal.common.domain.domain;

import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 8. 1.
 */
public enum HomeContentType implements CodeEnum {
    PERSONAL("PERSONAL", "개인화 추천"),
    TPO("TPO", "지금 들을 만한"),
    RECENT("RECENT", "최근 들은"),
    NEW("NEW", "최신 음악"),
    GENRE("GENRE", "선호 장르 추천"),
    SHORTCUT("SHORTCUT", "상황별"),
    ARTIST("ARTIST", "아티스트 추천"),
    EDITOR("EDITOR", "Editor's Pick"),
    DISCOVERY("DISCOVERY", "디스커버리"),
    BANNER("BANNER", "배너");

    private final String code;
    private final String title;

    HomeContentType (String code, String title) {
        this.code = code; this.title = title;
    }

    @Override
    public String getCode() {
        return code;
    }

    public String getTitle(){
        return title;
    }

    @Override
    public CodeEnum getDefault() {
        return null;
    }
}
