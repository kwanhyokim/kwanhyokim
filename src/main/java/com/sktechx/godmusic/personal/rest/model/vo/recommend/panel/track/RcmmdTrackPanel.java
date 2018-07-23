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
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.GenreVo;
import lombok.Getter;

import java.util.List;

/**
 * 설명 : 추천 CBF
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 10.
 */
public class RcmmdTrackPanel extends TrackPanel {
    public RcmmdTrackPanel(RecommendPanelType panelType , RecommendTrackDto recommendTrackDto, List<ImageDto> bgImgList , Integer dispSn)throws Exception {
        super(panelType , "Made for U" , neverRecommdnTrackNull(recommendTrackDto).getSvcGenreDto().getSvcGenreNm(),recommendTrackDto ,bgImgList,  dispSn);
    }
}
