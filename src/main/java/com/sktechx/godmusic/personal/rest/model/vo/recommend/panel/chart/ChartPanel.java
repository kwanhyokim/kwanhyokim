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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.common.util.DateUtil;
import com.sktechx.godmusic.personal.rest.model.dto.ChartDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.PanelContentVo;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 설명 : 차트형 추천 패널
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
public class ChartPanel extends Panel {
    @JsonIgnore
    private ChartDto chart;

    public ChartPanel(RecommendPanelType panelType , ChartDto chart, List<ImageInfo> bgImgList) throws CommonBusinessException {
        super(panelType);
        this.chart = neverNullChart(chart);
        this.imgList = bgImgList;
        this.initialPanel();
    }

    @Override
    protected void initialPanel() {
        this.title = chart.getChartNm();
        this.subTitle = getChartUpdateHourly(chart.getUpdateDtime())+"시 기준";
        this.content = createPanelContent();
    }

    @Override
    protected PanelContentVo createPanelContent() {
        PanelContentVo content = new PanelContentVo();

        content.setId(chart.getChartId());

        content.setType(RecommendPanelContentType.CHART);
        content.setCreateDtime(chart.getCreateDtime());
        content.setUpdateDtime(chart.getUpdateDtime());
        content.setTrackList(chart.getTrackList());
        content.setTrackCount(chart.getTrackCount());

        return content;
    }

    private static ChartDto neverNullChart(ChartDto chart) throws CommonBusinessException {
        if(chart == null || StringUtils.isEmpty(chart.getChartNm()))
            throw new CommonBusinessException("chart is null.");
        return chart;
    }

    private String getChartUpdateHourly(Date updateDateTime){
        return DateUtil.dateToString(updateDateTime, "HH");
    }
}
