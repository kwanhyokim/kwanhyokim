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
 * 설명 :
 *
 * @author 김형진/SKTECHX (twinkle@sk.com)
 * @date 2018. 8. 2.
 */
public enum SvcContentType implements CodeEnum {
    GENRE("GENRE","장르")
    ,TOTAL("TOTAL" ,"종합")
    ,KIDS("KIDS","키즈")
    ;

    private final String value;
    private final String code;

    SvcContentType(String code, String value) {
        this.code = code;
        this.value = value;
    }
    @MappedTypes(SvcContentType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<SvcContentType> {
        public TypeHandler() {
            super(SvcContentType.class);
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
