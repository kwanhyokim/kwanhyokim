package com.sktechx.godmusic.personal.common.domain.type;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 20.
 * @time PM 6:57
 */
public enum ArtistType implements CodeEnum {
	REPRSNT("REPRSNT", "대표 아티스트"),
	SIMILAR("SIMILAR", "유사 아티스트"),
	;

	private final String code;
	private final String value;

	ArtistType(String code, String value) {
		this.code = code;
		this.value = value;
	}

	@MappedTypes(ArtistType.class)
	public static class TypeHandler extends CodeEnumTypeHandler<ArtistType> {
		public TypeHandler() {
			super(ArtistType.class);
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

	public ArtistType getDefault() {
		return null;
	}
}
