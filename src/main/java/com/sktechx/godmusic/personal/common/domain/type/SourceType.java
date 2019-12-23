/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.common.domain.type;

import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

import java.util.Arrays;

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
	MV("MV", "뮤직비디오", "MV"),
	VIDEO_MV("VIDEO_MV", "뮤직비디오", "LS"),
	VIDEO_TEASER("VIDEO_TEASER", "티저", "LS"),
	VIDEO_SPECIAL("VIDEO_SPECIAL", "영상심의등급을 못받은 티져", "LS"),
	VIDEO_LIVE("VIDEO_LIVE", "라이브", "LS"),
	VIDEO_INTERVIEW("VIDEO_INTERVIEW", "인터뷰", "LS"),
	VIDEO_ETC("VIDEO_ETC", "기타영상", "LS")
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

	public static SourceType fromCode(String code) {
		return Arrays.stream(SourceType.values())
				.filter(e -> e.getCode().equalsIgnoreCase(code))
				.findFirst().orElse(null);
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
