package com.sktechx.godmusic.personal.common.domain.type;

import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 27.
 * @time AM 11:13
 */
public enum SourceType implements CodeEnum {
	STRM("STRM", "스트리밍", "ST"),
	DN("DN", "다운로드", "DL"),
	MV("MV", "뮤직비디오", "MV")
	;

	private final String code;
	private final String value;
	@Getter
	private final String playType;

	SourceType(String code, String value, String playType) {
		this.code = code;
		this.value = value;
		this.playType = playType;
	}

	@MappedTypes(SourceType.class)
	public static class TypeHandler extends CodeEnumTypeHandler<SourceType> {
		public TypeHandler() {
			super(SourceType.class);
		}
	}

	@Override
	public String getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

	@Override
	public SourceType getDefault() {
		return null;
	}
}
