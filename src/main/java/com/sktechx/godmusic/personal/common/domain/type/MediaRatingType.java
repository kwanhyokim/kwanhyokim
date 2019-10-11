package com.sktechx.godmusic.personal.common.domain.type;

import org.apache.ibatis.type.MappedTypes;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;

public enum MediaRatingType implements CodeEnum {
    ALL("전체", "전체"),
    AGE_7_OVER("7_OVER", "7세 이상 시청가능"),
    AGE_12_OVER("12_OVER", "12세 이상 시청가능"),
    AGE_15_OVER("15_OVER", "15세 이상 시청가능"),
    AGE_19_OVER("19_OVER", "19세 이상 시청가능"),
    ADULT("ADULT", "성인 시청가능"),
    RESTRICT("RESTRICT", "제한 시청가능"),
    NOT_RATING("NOT_RATING", "등급 없음"),
    ;

    private final String code;
    private final String value;

    MediaRatingType(String code, String value) {
        this.code = code;
        this.value = value;
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

    @MappedTypes(MediaRatingType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<MediaRatingType> {
        public TypeHandler() {
            super(MediaRatingType.class);
        }
    }

}