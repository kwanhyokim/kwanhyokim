package com.sktechx.godmusic.personal.common.domain.type;/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

import org.apache.ibatis.type.MappedTypes;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;

public enum VideoType implements CodeEnum {
    TEASER("TEASER", "티저"),
    MUSIC_VIDEO("MUSIC_VIDEO", "뮤직비디오"),
    LIVE("LIVE", "라이브"),
    INTERVIEW("INTERVIEW", "인터뷰"),
    MIX("MIX", "혼합")
    ;

    private final String code;
    private final String value;

    VideoType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @MappedTypes(VideoType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<VideoType> {
        public TypeHandler() {
            super(VideoType.class);
        }
    }

    @Override
    @JsonValue
    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    @Override
    public CodeEnum getDefault() {
        return null;
    }
}
