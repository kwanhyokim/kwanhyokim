/*
 *
 *  * Copyright (c) 2018 SK TECHX.
 *  * All right reserved.
 *  *
 *  * This software is the confidential and proprietary information of SK TECHX.
 *  * You shall not disclose such Confidential Information and
 *  * shall use it only in accordance with the terms of the license agreement
 *  * you entered into with SK TECHX.
 *
 */

package com.sktechx.godmusic.personal.common.domain.type;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;

/**
 * @author : Donghwan Kim(skillz@sk.com)
 * @since on 2017. 8. 4..
 */
public enum TrackLogType implements CodeEnum {

	STRT("STRT","곡 시작"),
	ONEMIN("1MIN","1분청취"),
	MEND("MEND","곡 종료"),
	USKP("USKP","유저 스킵");

	private final String code;
	private final String value;

	TrackLogType(String code, String value) {
		this.code = code;
		this.value = value;
	}

	@MappedTypes(TrackLogType.class)
	public static class TypeHandler extends CodeEnumTypeHandler<TrackLogType> {
		public TypeHandler() {
			super(TrackLogType.class);
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
