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

import org.apache.ibatis.type.MappedTypes;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;

/**
 * 설명 : 추천 패널 타입
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */

public enum RecommendPanelType implements CodeEnum{
    POPULAR_CHANNEL("POPULAR_CHANNEL" , "인기채널"),
    PREFER_GENRE_POPULAR_CHANNEL("PREFER_GENRE_POPULAR_CHANNEL" , "선호 장르 인기 채널"),
    LISTEN_MOOD_POPULAR_CHANNEL("LISTEN_MOOD_POPULAR_CHANNEL" , "청취무드 인기채널"),
    PREFER_SIMILAR_TRACK("PREFER_SIMILAR_TRACK" , "선호 유사곡"),

    ARTIST_POPULAR_TRACK("ARTIST_POPULAR_TRACK" , "아티스트 인기곡"),
    PREFER_GENRE_SIMILAR_TRACK("PREFER_GENRE_SIMILAR_TRACK" , "선호 장르 유사곡"),
    RCMMD_TRACK("RCMMD_TRACK" , "추천 유사곡"),

    LIVE_CHART("LIVE_CHART" , "실시간 차트" ),
    KIDS_CHART("KIDS_CHART" , "키즈 차트" ),

    TPO_CHANNEL("TPO_CHANNEL", "TPO 채널"),
    ARTIST_FLO_TRACK("ARTIST_FLO_TRACK", "AFLO 채널"),

    PRI_LIVE_CHART("PRI_LIVE_CHART", "FLO 차트 내 취향 MIX"),
    PRI_KIDS_CHART("PRI_KIDS_CHART", "키즈 차트 내 취향 MIX"),

    RCMMD_LIKE_TRACK("RCMMD_LIKE_TRACK", "방금 레이더")
    ;


    private final String code;
    private final String value;

    RecommendPanelType(String code, String value) {
        this.code = code;
        this.value = value;
    }

    @MappedTypes(RecommendPanelType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<RecommendPanelType> {
        public TypeHandler() {
            super(RecommendPanelType.class);
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


    public static RecommendPanelType fromCode(String code) {
        if (!StringUtils.isEmpty(code)) {
            for (RecommendPanelType type : RecommendPanelType.values()) {
                if (type.code.equals(code)){
                    return type;
                }
            }
        }
        return null;
    }

}
