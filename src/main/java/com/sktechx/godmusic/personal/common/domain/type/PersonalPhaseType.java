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
 *       GUEST : 비로그인 방문
 *       VISIT : 방문
 *       LISTEN : 청취
 *       RECOMMEND : 추천
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 18.
 */
public enum PersonalPhaseType implements CodeEnum {
    GUEST("GUEST",0, "비로그인 방문"),
    VISIT("VISIT",1, "방문"),
    LISTEN("LISTEN",2,"청취"),
    RECOMMEND("RECOMMEND",3,"추천");

    private final String code;
    private final int phase;
    private final String value;

    PersonalPhaseType(String code,int phase, String value) {
        this.code = code;
        this.phase = phase;
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

    public int getPhase(){ return phase; }

    public String getValue() {
        return value;
    }


    @Override
    public CodeEnum getDefault() {
        return null;
    }

    public static PersonalPhaseType fromCode(String code) {
        for (PersonalPhaseType type : PersonalPhaseType.values()) {
            if (type.getCode().equals(code)){
                return type;
            }
        }
        return null;
    }
}
