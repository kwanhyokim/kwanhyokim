/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.common.domain.type;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;

import java.util.Arrays;

/**
 * 설명 : XXXXXXXXXXX
 *
 * @author groot
 * @since 2019. 12. 18
 */
public enum ResourceLogType implements CodeEnum {
    STRT("STRT", "청취 및 시청 시작"),
    ONEMIN("1MIN", "1분 청취 또는 1분 시청"),
    MEND("MEND", "곡 종료 또는 시청 종료"),
    USKP("USKP", "유저 스킵")
    ;

    private final String code;
    private final String value;

    ResourceLogType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @MappedTypes(ResourceLogType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<ResourceLogType> {
        public TypeHandler() {
            super(ResourceLogType.class);
        }
    }

    public static ResourceLogType fromCode(String code) {
        return Arrays.stream(ResourceLogType.values())
                .filter(e -> e.getCode().equalsIgnoreCase(code))
                .findFirst().orElse(null);
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
