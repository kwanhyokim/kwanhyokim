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
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;
import org.apache.ibatis.type.MappedTypes;

/**
 * 설명 : 추천 패널 차트 타입
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 08. 23.
 */
public enum RecommendChartPanelType {
    TOP100(SvcContentType.ALL, ChartType.HOURLY , MusicContentType.TRACK , 0),
    KIDS(SvcContentType.KIDS, ChartType.HOURLY , MusicContentType.TRACK , 0);

    private final SvcContentType svcContentType;
    private final ChartType chartType;
    private final MusicContentType musicContentType;
    private final int svcContentId;

    RecommendChartPanelType(SvcContentType svcContentType, ChartType chartType,MusicContentType musicContentType, int svcContentId) {
        this.svcContentType = svcContentType;
        this.chartType = chartType;
        this.musicContentType = musicContentType;
        this.svcContentId = svcContentId;
    }

    @MappedTypes(RecommendChartPanelType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<RecommendChartPanelType> {
        public TypeHandler() {
            super(RecommendChartPanelType.class);
        }
    }
}
