package com.sktechx.godmusic.personal.common.domain.type;

import org.apache.ibatis.type.MappedTypes;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 9.
 * @time AM 10:35
 */
public enum BitrateType implements CodeEnum {

	BITRATE_AAC("aac","aac"),
	BITRATE_192K("192k","192k"),
	BITRATE_320K("320k","320k"),
	BITRATE_FLAC16("flac16bit", "flac16bit"),
	BITRATE_FLAC24("flac24bit", "flac24bit"),
	UNKNOWN("unknown", "unknown")

	;

	private final String code;
	private final String value;

	BitrateType(String code, String value) {
		this.code = code;
		this.value = value;
	}

	@MappedTypes(BitrateType.class)
	public static class TypeHandler extends CodeEnumTypeHandler<BitrateType> {
		public TypeHandler() {
			super(BitrateType.class);
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
