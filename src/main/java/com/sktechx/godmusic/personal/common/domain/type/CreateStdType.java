package com.sktechx.godmusic.personal.common.domain.type;/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;

public enum CreateStdType implements CodeEnum {

    DF("DF", "Discovery Flow 에서 생성"),
    RCMMD("RCMMD", "추천 에서 생성")
    ;

    private final String code;
    private final String value;

    CreateStdType(String code, String value ){
        this.code = code;
        this.value = value;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public CodeEnum getDefault() {
        return null;
    }

    public String getValue() {
        return this.value;
    }
}
