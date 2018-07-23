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
 * 설명 : 개인화 단계
 *       VISIT : 방문
 *       LISTEN : 청취
 *       RECOMMEND : 추천
 *       ACCUMULATE : 누적
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 18.
 */
public enum PersonalPhaseType implements CodeEnum {
    VISIT("VISIT","방문"),
    LISTEN("LISTEN","청취"),
    RECOMMEND("RECOMMEND","추천"),
    ACCUMULATE("ACCUMULATE","누적");

    private final String code;
    private final String value;

    PersonalPhaseType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @MappedTypes(PersonalPhaseType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<PersonalPhaseType> {
        public TypeHandler() {
            super(PersonalPhaseType.class);
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
