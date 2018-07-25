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
    CHNL("CHNL" , "채널"),
    CHART("CHART" , "차트"),
    RC_ATST_TR("RC_ATST_TR" , "아티스트 인기곡"),
    RC_SML_TR("RC_SML_TR" , "선호 유사곡"),
    RC_GR_TR("RC_GR_TR" , "선호 장르 유사곡"),
    RC_CF_TR("RC_CF_TR" , "추천 유사곡");

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

    public static RecommendPanelContentType getRecommendPanelContentByPanelType(RecommendPanelType recommendPanelType){
        if(RecommendPanelType.RCMMD_TRACK.equals(recommendPanelType)){
            return RC_CF_TR;
        }else if(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK.equals(recommendPanelType)){
            return RC_GR_TR;
        }else if(RecommendPanelType.PREFER_SIMILAR_TRACK.equals(recommendPanelType)){
            return RC_SML_TR;
        }
        return null;
    }
}
