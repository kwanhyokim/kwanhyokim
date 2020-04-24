/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.chart;

import java.util.Date;
import java.util.List;

import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.common.util.DateUtil;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.PanelContentVo;
import lombok.Getter;
import lombok.Setter;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.CHART_PANEL_HOURLY_BASIS_PHRASES;

/**
 * 설명 : 개인화 FLO 차트 패널
 *
 */
public class PrivateFloChartPanel extends Panel {

    @Getter
    @Setter
    private ChartTitle priChartTitle;

    public PrivateFloChartPanel(
            RecommendPanelType panelType,
            ChartDto chart,
            List<ImageInfo> bgImgList) throws CommonBusinessException {

        super(panelType);

        this.imgList = bgImgList;
        this.title = chart.getChartNm();
        this.subTitle = DateUtil.dateToString(
                (chart.getDispStartDtime() == null ? new Date() : chart.getDispStartDtime()),
                "HH") + CHART_PANEL_HOURLY_BASIS_PHRASES;

        this.priChartTitle =
                ChartTitle.builder()
                    .prefix("FLO 차트")
                    .suffix("내 취향 MIX")
                .build();

        this.content = PanelContentVo.builder()
                .id(chart.getChartId())
                .type(RecommendPanelContentType.PRI_CHART)
                .createDtime(chart.getCreateDtime())
                .updateDtime(chart.getUpdateDtime())
                .trackList(chart.getTrackList())
                .trackCount(chart.getTrackCount())
                .chartTaste(chart.getChartTaste())
                .build();

    }
}
