/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.dto.recommend;

import org.apache.ibatis.type.MappedTypes;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 정덕진(Deockjin Chung)/Music사업팀/SKTECH(djin.chung@sk.com)
 * @date 2018.07.03
 */

public enum LyricsType implements CodeEnum {
    T("T", "시간"),
    N("N", "일반"),
    ;

    private final String code;
    private final String value;

    LyricsType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @MappedTypes(LyricsType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<LyricsType> {
        public TypeHandler() {
            super(LyricsType.class);
        }
    }

    @Override
    @JsonValue
    public String getCode() {
        return code;
    }

    @Override
    public CodeEnum getDefault() {
        return null;
    }

    public String getValue() {
        return value;
    }
}
