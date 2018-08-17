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

import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;

import java.util.List;
import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.*;
/**
 * 설명 : 추천 CBF
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 10.
 */
public class RcmmdTrackPanel extends TrackPanel {
    public RcmmdTrackPanel(RecommendPanelType panelType , RecommendTrackDto recommendTrackDto, List<ImageInfo> bgImgList )throws CommonBusinessException {
        super(panelType , RCMMD_TRACK_PANEL_TITLE , neverRecommdnTrackNull(recommendTrackDto).getSvcGenreDto().getSvcGenreNm(),recommendTrackDto ,bgImgList);
    }
}
