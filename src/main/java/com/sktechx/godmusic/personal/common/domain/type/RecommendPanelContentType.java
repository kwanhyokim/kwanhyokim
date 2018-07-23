/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.common.domain.type;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;

/**
 * 설명 : 추천 패널 컨텐츠 타입
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
public enum RecommendPanelContentType implements CodeEnum{
    CHANNEL("CHANNEL" , "채널"),
    CHART("CHART" , "차트"),
    ARRIST_POPULAR_TRACK("ARRIST_POPULAR_TRACK" , "아티스트 인기곡"),
    PREFER_SIMILAR_TRACK("PREFER_SIMILAR_TRACK" , "선호 유사곡"),
    PREFER_GENRE_SIMILAR_TRACK("PREFER_GENRE_SIMILAR_TRACK" , "선호 장르 유사곡"),
    RCMMD_TRACK("RCMMD_TRACK" , "추천 유사곡");

    private final String code;
    private final String value;

    RecommendPanelContentType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @MappedTypes(RecommendPanelContentType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<RecommendPanelContentType> {
        public TypeHandler() {
            super(RecommendPanelContentType.class);
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

    @Override
    public CodeEnum getDefault() {
        return null;
    }
}
