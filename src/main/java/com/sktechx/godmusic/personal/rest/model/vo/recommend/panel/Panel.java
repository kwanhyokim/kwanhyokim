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
import lombok.Getter;

import java.util.List;

/**
 * 설명 : 추천 패널
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
public abstract class Panel {
    @Getter
    protected RecommendPanelType panelType;

    @Getter
    protected String title;
    @Getter
    protected String subTitle;

    @Getter
    protected List<ImageDto> imgList;

    public Panel(RecommendPanelType panelType , String title, String subTitle , List<ImageDto> imgList){
        this.panelType = panelType;
        this.title = title;
        this.subTitle = subTitle;
        this.imgList = imgList;
    }

}
