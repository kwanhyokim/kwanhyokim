/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track;

import com.sktechx.godmusic.personal.rest.model.dto.ImageDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import lombok.Getter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 설명 : 곡 유형 추천 패널
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
public abstract class TrackPanel extends Panel {
    @Getter
    protected List<TrackDto> trackList;
    @Getter
    protected Integer trackCount;

    public TrackPanel(RecommendPanelType panelType , String title, String subTitle, List<ImageDto> imgList , List<TrackDto> trackList) throws Exception{
        super(panelType , neverNull(title), neverNull(subTitle) , imgList);
        this.trackList = neverNullList(trackList);
        this.trackCount = trackList.size();

    }

    private static String neverNull( String str) throws Exception {
        if(StringUtils.isEmpty(str))
            throw new IllegalAccessException("title or subTitle is null.");
        return str;
    }

    private static List<TrackDto> neverNullList( List<TrackDto> trackList) throws Exception{
        if(CollectionUtils.isEmpty(trackList))
            throw new IllegalAccessException("trackList is null.");
        return trackList;
    }

}
