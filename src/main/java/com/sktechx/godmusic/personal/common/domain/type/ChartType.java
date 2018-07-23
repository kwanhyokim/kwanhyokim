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
 *  * 설명 : 차트  타입
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
public enum ChartType implements CodeEnum {
    LIVE("LIVE", "실시간"),
    KIDS("KIDS" , "어린이");

    private final String code;
    private final String value;

    ChartType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @MappedTypes(ChartType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<ChartType> {
        public TypeHandler() {
            super(ChartType.class);
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
