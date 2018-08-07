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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.util.StringUtils;

/**
 * App 인입 채널 타입
 * 
 * @author 오경무/SKTECH (sanghyun.park.tx@sk.com)
 * @data 2017. 7. 25.
 */
public enum AppNameType implements CodeEnum {
	MMATE("MMATE","뮤직메이트"),
	MUSIC_MALL("MUSIC_MALL","뮤직몰"),
	NUGU("NUGU","NUGU")
	;
	
	final String code;
	final String desc;
	

	private AppNameType(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public CodeEnum getDefault() {
		return null;
	}

	@MappedTypes(AppNameType.class)
	public static class TypeHandler extends CodeEnumTypeHandler<AppNameType> {
		public TypeHandler() {
			super(AppNameType.class);
		}
	}
	
	@JsonCreator
	public static AppNameType fromCode(String code) {
		if (!StringUtils.isEmpty(code)) {
			for (AppNameType type : AppNameType.values()) {
				if (type.code.equals(code)){
					return type;
				}
			}
		}
		
		return null;		
	}
}
