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

import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;
import com.sktechx.godmusic.personal.common.domain.PreferPropsType;
import org.apache.ibatis.type.MappedTypes;

/**
 * 설명 : 추천 패널 차트 타입
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 08. 23.
 */
public enum RecommendChartPanelType {
    TOP100(SvcContentType.ALL, ChartType.HOURLY , MusicContentType.TRACK , PreferPropsType.TOP100),
    KIDS(SvcContentType.GENRE, ChartType.DAILY , MusicContentType.TRACK , PreferPropsType.KIDS100);

    private final SvcContentType svcContentType;
    private final ChartType chartType;
    private final MusicContentType musicContentType;
    private final PreferPropsType dispPropsType;

    RecommendChartPanelType(SvcContentType svcContentType, ChartType chartType,MusicContentType musicContentType , PreferPropsType dispPropsType) {
        this.svcContentType = svcContentType;
        this.chartType = chartType;
        this.musicContentType = musicContentType;
        this.dispPropsType = dispPropsType;
    }

    @MappedTypes(RecommendChartPanelType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<RecommendChartPanelType> {
        public TypeHandler() {
            super(RecommendChartPanelType.class);
        }
    }

    public SvcContentType getSvcContentType() {
        return svcContentType;
    }

    public ChartType getChartType() {
        return chartType;
    }

    public MusicContentType getMusicContentType() {
        return musicContentType;
    }

    public PreferPropsType getDispPropsType() {
        return dispPropsType;
    }
}
