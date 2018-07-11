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

import java.util.List;

/**
 * 설명 : XXXXXXXX
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 10.
 */
public class PreferGenreSimilarTrackPanel extends TrackPanel {

    public PreferGenreSimilarTrackPanel(RecommendPanelType panelType, List<ImageDto> imgList, List<TrackDto> trackList) throws Exception{
        super(panelType , "Like U", "많이 들었던 노래와\n 유사한 선곡" ,imgList, trackList);
    }

}
