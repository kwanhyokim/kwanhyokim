package com.sktechx.godmusic.personal.common.domain.type;

import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;

public enum AnalsStatusType implements CodeEnum {

    SUCCESS("SUCCESS", "분석 성공"),
    FAILURE("FAILURE", "분석 실패"),
    NO_PLAYLIST("NO_PLAYLIST", "플레이리스트 아님");

    private final String value;
    private final String code;

    AnalsStatusType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @MappedTypes(AnalsStatusType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<AnalsStatusType> {
        public TypeHandler() {
            super(AnalsStatusType.class);
        }
    }
    @Override
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