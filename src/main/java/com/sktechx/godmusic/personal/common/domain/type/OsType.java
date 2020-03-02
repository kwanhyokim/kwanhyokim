package com.sktechx.godmusic.personal.common.domain.type;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 9.
 * @time AM 10:37
 */
public enum OsType implements CodeEnum {
    ALL("ALL", "ALL", "ALL"),
    AOS("AOS", "A", "Android"),
    IOS("IOS", "I", "iOS"),
    WEB("WEB", "W", "WEB"),
    TIZEN("TIZEN", "T", "Tizen"),
    GOOGLE_DEVICE("GOOGLE_DEVICE", "G", "Google_Device");

    private final String code;
    private final String shortCode;
    private final String value;

    OsType(String code, String shortCode, String value) {
        this.code = code;
        this.shortCode = shortCode;
        this.value = value;
    }

    @MappedTypes(OsType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<OsType> {
        public TypeHandler() {
            super(OsType.class);
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

    public String getShortCode() {
        return shortCode;
    }

    public static OsType fromCode(String code) {
        for (OsType type : OsType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }

        return OsType.AOS;
    }
}
