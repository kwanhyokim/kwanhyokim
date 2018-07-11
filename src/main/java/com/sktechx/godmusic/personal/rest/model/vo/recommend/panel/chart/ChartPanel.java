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

import com.sktechx.godmusic.personal.rest.model.dto.ChannelDto;
import com.sktechx.godmusic.personal.rest.model.dto.ImageDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 설명 : 차트형 추천 패널
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
public class ChartPanel extends Panel {

    @Getter
    @ApiModelProperty(required = true, example = "", value = "차트 정보")
    private ChannelDto channel;

    public ChartPanel(RecommendPanelType panelType , List<ImageDto> imgList, ChannelDto channel) throws Exception{
        super(panelType , neverNull(channel).getChnlNm() , "",imgList);
        this.channel = channel;
    }

    private static ChannelDto neverNull( ChannelDto channel) throws Exception {
        if(channel == null || StringUtils.isEmpty(channel.getChnlNm()))
            throw new IllegalAccessException("channel is null.");
        return channel;
    }
}
