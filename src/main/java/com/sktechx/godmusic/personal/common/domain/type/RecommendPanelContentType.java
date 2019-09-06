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
 * 설명 : 추천 패널 컨텐츠 타입
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
public enum RecommendPanelContentType implements CodeEnum{
    CHNL("CHNL" , "채널", ""),
    CHART("CHART" , "차트", ""),

    AFLO("AFLO", "AFLO채널", "AfloPannelAssembly"),
    RC_CF_TR("RC_CF_TR" , "추천 유사곡", "ForMeFloPanelAssembly"),
    RC_SML_TR("RC_SML_TR" , "선호 유사곡", "TodayFloPanelAssembly"),
    RC_ATST_TR("RC_ATST_TR" , "아티스트 인기곡", "ArtistFloPanelAssembly"),
    RC_GR_TR("RC_GR_TR" , "선호 장르 유사곡", ""),
    RC_MD_CN("RC_MD_CN" , "청취 무드 인기채널", ""),
    ;

    private final String code;
    private final String value;
    private final String className;

    RecommendPanelContentType(String code, String value, String className) {
        this.code = code;
        this.value = value;
        this.className = className;
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

    public String getClassName() { return className; }

    @Override
    public CodeEnum getDefault() {
        return null;
    }

    public static RecommendPanelContentType fromCode(String code) {
        if (!StringUtils.isEmpty(code)) {
            for (RecommendPanelContentType type : RecommendPanelContentType.values()) {
                if (type.code.equals(code)){
                    return type;
                }
            }
        }
        return null;
    }

    public static RecommendPanelContentType getRecommendPanelContentByPanelType(RecommendPanelType recommendPanelType){
        if(RecommendPanelType.RCMMD_TRACK.equals(recommendPanelType)){
            return RC_CF_TR;
        }else if(RecommendPanelType.PREFER_SIMILAR_TRACK.equals(recommendPanelType)){
            return RC_SML_TR;
        }
        return null;
    }
}
