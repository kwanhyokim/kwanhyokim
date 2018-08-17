/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.common.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;

/**
 * 설명 : 선호 노출 타입
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 08. 17.
 */
public enum PreferDispType implements CodeEnum {
    TOP100("TOP100", "실시간 차트")
    , KIDS("KIDS" , "키즈 차트")
    ;

    private final String value;
    private final String code;

    PreferDispType(String code, String value) {
        this.code = code;
        this.value = value;
    }
    @MappedTypes(PreferDispType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<PreferDispType> {
        public TypeHandler() {
            super(PreferDispType.class);
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
