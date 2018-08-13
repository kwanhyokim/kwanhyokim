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
 * @time PM 4:28
 */
public enum ArtistGroupType implements CodeEnum {
	SINGLE("S", "솔로"),
	DUO("D" , "듀오"),
	GROUP("G" , "그룹");

	private final String code;
	private final String value;

	ArtistGroupType(String code, String value) {
		this.code = code;
		this.value = value;
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

	@MappedTypes(ArtistGroupType.class)
	public static class TypeHandler extends CodeEnumTypeHandler<ArtistGroupType> {
		public TypeHandler() {
			super(ArtistGroupType.class);
		}
	}
}
