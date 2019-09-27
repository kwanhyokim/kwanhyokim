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

public enum MediaRatingType implements CodeEnum {

    ALL("ALL", "전체"),
    OVER_7("7_OVER", "7세"),
    OVER_12("12_OVER", "12세"),
    OVER_15("15_OVER", "15세"),
    OVER_19("19_OVER", "19세"),
    ADULT("ADULT", "성인"),
    RESTRICT("RESTRICT", "제한"),
    NOT_RATING("NOT_RATING", "등급없음")

    ;
    private final String code;
    private final String value;

    MediaRatingType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @MappedTypes(MediaRatingType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<MediaRatingType> {

        public TypeHandler() {
            super(MediaRatingType.class);
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
