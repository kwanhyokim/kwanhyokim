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
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 9.
 * @time AM 10:37
 */
public enum ImageDisplayType implements CodeEnum {
	MAIN_TOP("MAIN_TOP", "메인 화면 상단(추천 사이즈)"),
	MAIN_SRT("MAIN_SRT", "메인 화면 바로가기(표준 사이즈)"),
	RCT_DTL("RCT_DTL", "유형별 목록(스몰 사이즈)");

	private final String code;
	private final String value;

	ImageDisplayType(String code, String value) {
		this.code = code;
		this.value = value;
	}

	@MappedTypes(ImageDisplayType.class)
	public static class TypeHandler extends CodeEnumTypeHandler<ImageDisplayType> {
		public TypeHandler() {
			super(ImageDisplayType.class);
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

	public static ImageDisplayType fromCode(String code) {
		for (ImageDisplayType type : ImageDisplayType.values()) {
			if (type.getCode().equals(code)){
				return type;
			}
		}

		return ImageDisplayType.MAIN_SRT;
	}
}
