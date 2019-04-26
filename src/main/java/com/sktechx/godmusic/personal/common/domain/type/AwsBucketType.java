package com.sktechx.godmusic.personal.common.domain.type;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;

public enum AwsBucketType implements CodeEnum {
    OCR("OCR", "OCR bucket"),
    ;

    private final String code;
    private final String value;

    AwsBucketType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @MappedTypes(AwsBucketType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<AwsBucketType> {
        public TypeHandler() {
            super(AwsBucketType.class);
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

    public AwsBucketType getDefault() {
        return null;
    }
}
