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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.MappedTypes;

/**
 * App 인입 채널 타입
 * 
 * @author 오경무/SKTECH (sanghyun.park.tx@sk.com)
 * @data 2017. 7. 25.
 */
public enum AppNameType implements CodeEnum {
	MMATE("MMATE","뮤직메이트"),
	MUSIC_MALL("MUSIC_MALL","뮤직몰"),
	NUGU("NUGU","NUGU"),
	TMAP_APP("TMAP_APP","TMAP_APP"),
	FLO_APP("FLO", "FLO"),
	FLO_WEB("FLO_WEB", "FLO_WEB"),
	BIXBY("BIXBY", "BIXBY"),
	BIXBY_SPEAKER("BIXBY_SPEAKER", "BIXBY_SPEAKER")
	;
	
	final String code;
	final String desc;
	

	AppNameType(String code, String desc) {
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

	@JsonCreator
	public static String parseToString(String code) {
		if (!StringUtils.isEmpty(code)) {
			for (AppNameType type : AppNameType.values()) {
				if (type.code.equals(code)){
					return type.code;
				}
			}
		}
		return StringUtils.EMPTY;
	}
}
