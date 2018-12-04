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
 * Created by Nick.
 *
 * 인기 채널 타입
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 11. 30.
 * @time AM 3:58
 */
public enum PopularChnlType implements CodeEnum {
	ALL("ALL", "전체"),
	GENRE("GENRE", "장르");

	private final String code;
	private final String value;

	PopularChnlType(String code, String value) {
		this.code = code;
		this.value = value;
	}

	@MappedTypes(PopularChnlType.class)
	public static class TypeHandler extends CodeEnumTypeHandler<PopularChnlType> {
		public TypeHandler() {
			super(PopularChnlType.class);
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
