package com.sktechx.godmusic.personal.common.domain.type;

import org.apache.ibatis.type.MappedTypes;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 13.
 * @time PM 5:45
 */
public enum PlayListType implements CodeEnum {
	CHNL("CHNL", "체널")
	, CHART("CHART" , "차트")
	, RANK_CHART("RANK_CHART" , "랭크 차트")
	, FLAC ("FLAC", "FLAC 채널")
	;

	private final String value;
	private final String code;

	PlayListType(String code, String value) {
		this.code = code;
		this.value = value;
	}
	@MappedTypes(PlayListType.class)
	public static class TypeHandler extends CodeEnumTypeHandler<PlayListType> {
		public TypeHandler() {
			super(PlayListType.class);
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
