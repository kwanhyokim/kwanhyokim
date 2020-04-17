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

/**
 * 설명 : 개인화 키즈 차트 패널
 *
 */
public class PrivateKidsChartPanel extends Panel {

    @Getter
    @Setter
    private ChartTitle priChartTitle;

    public PrivateKidsChartPanel(
            RecommendPanelType panelType,
            ChartDto chart,
            List<ImageInfo> bgImgList) throws CommonBusinessException {

        super(panelType);

        this.imgList = bgImgList;
        this.title = chart.getChartNm();
        this.subTitle = DateUtil.dateToString(
                (chart.getUpdateDtime() == null ?
                        chart.getDispStartDtime() :
                        chart.getUpdateDtime()),
                "yyyy.MM.dd");
        this.priChartTitle =
                ChartTitle.builder()
                    .prefix("키즈 차트")
                    .suffix("내 취향 MIX")
                .build();

        this.content = PanelContentVo.builder()
                .id(chart.getChartId())
                .type(RecommendPanelContentType.PRI_CHART)
                .createDtime(chart.getCreateDtime())
                .updateDtime(chart.getUpdateDtime())
                .trackList(chart.getTrackList())
                .trackCount(chart.getTrackCount())
                .build();

    }
}
