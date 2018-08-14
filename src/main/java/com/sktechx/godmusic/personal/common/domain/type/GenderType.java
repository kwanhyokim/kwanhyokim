package com.sktechx.godmusic.personal.common.domain.type;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 10.
 * @time PM 4:27
 */
public enum  GenderType implements CodeEnum {
	M("M", "남성"),
	F("F", "여성");

	private final String code;
	private final String value;

	GenderType(String code, String value) {
		this.code = code;
		this.value = value;
	}

	@MappedTypes(GenderType.class)
	public static class TypeHandler extends CodeEnumTypeHandler<GenderType> {
		public TypeHandler() {
			super(GenderType.class);
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
