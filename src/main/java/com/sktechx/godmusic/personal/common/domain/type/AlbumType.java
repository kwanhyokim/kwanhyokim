package com.sktechx.godmusic.personal.common.domain.type;

import org.apache.ibatis.type.MappedTypes;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 10.
 * @time PM 4:24
 */
public enum AlbumType implements CodeEnum {
	RL("RL", "정규"),
	SL("SL", "싱글"),
	EP("EP", "미니"),
	OS("OS", "OST"),
	CP("CP", "컴필"),
	BS("BS", "베스트"),
	LV("LV", "라이브"),
	RM("RM", "리마스터"),
	SP("SP", "스페셜"),
	MF("MF", "가상앨범"),
	CV("CV", "커버버젼"),
	DS("DS", "디지털싱글"),

	OM("OM", "컴필/참여"),
	PR("PR", "컴필/참여"),
	TR("TR", "컴필/참여"),
	DE("DE", "데모"),
	ST("ST", "Split")
	;

	private final String code;
	private final String value;

	AlbumType(String code, String value) {
		this.code = code;
		this.value = value;
	}

	@MappedTypes(AlbumType.class)
	public static class TypeHandler extends CodeEnumTypeHandler<AlbumType> {
		public TypeHandler() {
			super(AlbumType.class);
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
