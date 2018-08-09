/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.common.domain.type;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;

/**
 *  * 설명 : 선호 장르 타입
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
public enum PreferGenreType implements CodeEnum {
    PREFER("PREFER", "선호 장르"),
    TOP100("TOP100", "실시간 인기 TOP100"),
    KIDS("KIDS" ,"키즈 인기 차트");

    private final String code;
    private final String value;

    PreferGenreType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @MappedTypes(PreferGenreType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<PreferGenreType> {
        public TypeHandler() {
            super(PreferGenreType.class);
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
