/*
 *  Copyright (c) 2018 SK TECHX.
 *  All right reserved.
 *
 *  This software is the confidential and proprietary information of SK TECHX.
 *  You shall not disclose such Confidential Information and
 *  shall use it only in accordance with the terms of the license agreement
 *  you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.common.domain.type;

import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

/**
 * 재생 가능 채널 유형
 *
 * @author 박상현/SKTECH (sanghyun.park.tx@sk.com)
 * @date 2019. 6. 24.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum PlayType implements CodeEnum {
	SINGLE("SINGLE", 10),
	MULTI("MULTI", 20)
	;
	
	private String code;
	@Getter private int grade;

	@MappedTypes(PlayType.class)
	public static class TypeHandler extends CodeEnumTypeHandler<PlayType> {
		public TypeHandler() {
			super(PlayType.class);
		}
	}

	@Override
	public String getCode() {
		return code;
	}
	
	@Override
	public CodeEnum getDefault() {
		return PlayType.SINGLE;
	}
}
