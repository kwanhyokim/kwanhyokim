/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.dto.member;

import org.apache.ibatis.type.MappedTypes;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 남재우(Peter)/njw0619@sk.com
 * @date 2018.11.02
 */

public enum CharacterType implements CodeEnum {

    DEFAULT("DEFAULT", "선호 아티스트"),
    KIDS("KIDS", "Kids"),
    NUGU("NUGU", "NUGU"),
    TMAP("TMAP", "TMap"),
    AFLO("AFLO","Aflo"),
    ;

    private final String code;
    private final String value;

    CharacterType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @MappedTypes(CharacterType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<CharacterType> {
        public TypeHandler() {
            super(CharacterType.class);
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

    public CharacterType getDefault() {
        return null;
    }
}
