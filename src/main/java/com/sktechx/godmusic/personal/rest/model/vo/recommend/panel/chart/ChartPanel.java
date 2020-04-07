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

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.common.util.DateUtil;
import com.sktechx.godmusic.personal.rest.model.dto.ChartDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.ChartTitle;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.PanelContentVo;
import lombok.Getter;
import lombok.Setter;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.CHART_PANEL_HOURLY_BASIS_PHRASES;

/**
 * 설명 : 차트형 추천 패널
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
public class ChartPanel extends Panel {
    @JsonIgnore
    private ChartDto chart;

    @Getter
    @Setter
    private ChartTitle priChartTitle;

    public ChartPanel(RecommendPanelType panelType , ChartDto chart, List<ImageInfo> bgImgList) throws CommonBusinessException {
        super(panelType);
        this.chart = neverNullChart(chart);
        this.imgList = bgImgList;
        this.title = chart.getChartNm();
        this.subTitle = getBasedOnUpdate(chart , this.type);
        this.content = PanelContentVo.builder()
                .id(chart.getChartId())

                .type(RecommendPanelContentType.CHART)
                .createDtime(chart.getCreateDtime())
                .updateDtime(chart.getUpdateDtime())
                .trackList(chart.getTrackList())
                .trackCount(chart.getTrackCount())
                .build();
    }

    private static ChartDto neverNullChart(ChartDto chart) throws CommonBusinessException {
        if(chart == null || StringUtils.isEmpty(chart.getChartNm()))
            throw new CommonBusinessException("chart is null.");
        return chart;
    }

    private String getBasedOnUpdate(ChartDto chart , RecommendPanelType panelType){
        if(chart != null){
            if(RecommendPanelType.LIVE_CHART.equals(panelType)){
                return DateUtil.dateToString(chart.getDispStartDtime(), "HH")+CHART_PANEL_HOURLY_BASIS_PHRASES;
            }else if(RecommendPanelType.KIDS_CHART.equals(panelType)){
                return DateUtil.dateToString(chart.getUpdateDtime(), "yyyy.MM.dd");
            }
            return getChartUpdateHourly(chart.getUpdateDtime());
        }

        return null;
    }
    private String getChartUpdateHourly(Date updateDateTime){
        if(updateDateTime != null){

            return DateUtil.dateToString(updateDateTime, "yyyy.MM.dd");
        }
        return "";
    }
}
