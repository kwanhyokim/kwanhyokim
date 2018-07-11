/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.vo.recommend.panel;

import com.sktechx.godmusic.personal.rest.model.dto.ImageDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendPanelType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.List;

/**
 * 설명 : 추천 패널
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
@ApiModel(value="Panel")
public abstract class Panel {
    @Getter
    @ApiModelProperty(required = true, value = "추천 패널 타입(POPULAR_CHANNEL:선호 채널, PREFER_SIMILAR_TRACK:유사, PREFER_GENRE_SIMILAR_TRACK:선호장르 유사곡, " +
            "RCMMD_TRACK:청취 추천, ARRIST_POPULAR_TRACK:선호 아티스트 인기곡, LIVE_CHART:top100, KIDS_CHART:키즈 )")
    protected RecommendPanelType panelType;

    @Getter
    @ApiModelProperty(required = true, value = "패널 제목")
    protected String title;
    @Getter
    @ApiModelProperty(required = true, value = "패널 부제목")
    protected String subTitle;

    @Getter
    @ApiModelProperty(required = true, value = "패널 배경 이미지 리스트")
    protected List<ImageDto> imgList;

    public Panel(RecommendPanelType panelType , String title, String subTitle , List<ImageDto> imgList){
        this.panelType = panelType;
        this.title = title;
        this.subTitle = subTitle;
        this.imgList = imgList;
    }

}
