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

import org.apache.ibatis.type.MappedTypes;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;

/**
 * @author : Donghwan Kim(skillz@sk.com)
 * @since on 2018. 7. 9..
 */
public enum MemberType implements CodeEnum {
    ID("IDM", "ID 회원"),
    MDN("MDN", "MDN 회원"),
    TID("TID", "TID 회원"),
    NAVER("NAVER", "NAVER 회원"),
    KAKAO("KAKAO", "KAKAO 회원"),
    APPLEID("APPLEID", "APPLEID 회원"),
    ;

    private final String code;
    private final String value;

    MemberType(String code, String value) {
        this.code = code;
        this.value = value;
    }

	@MappedTypes(MemberType.class)
	public static class TypeHandler extends CodeEnumTypeHandler<MemberType> {
		public TypeHandler() {
			super(MemberType.class);
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

	public MemberType getDefault() {
		return null;
	}

}
