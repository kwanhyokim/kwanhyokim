/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.reactive;

import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.like.RcmmdLikeTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.PanelContentVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.SeedTrackVo;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.assembly.v2.ReactivePanelAssembly.PanelImageHolder;

import java.util.List;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.RCMMD_REACTIVE_PANEL_SUB_TITLE;
import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.RCMMD_REACTIVE_PANEL_TITLE;

/**
 * 설명 : 반응형 홈 패널
 */

public class RcmmdReactivePanel extends Panel {

    @Deprecated
    public RcmmdReactivePanel(List<ImageInfo> bgImgList,
                              List<ImageInfo> seedTrackImgList,
                              List<ImageInfo> rcmmdTrackImgList,
                              TrackDto seedTrackDto,
                              RcmmdLikeTrackDto rcmmdLikeTrackDto) {

        super(RecommendPanelType.RCMMD_LIKE_TRACK);

        this.title = RCMMD_REACTIVE_PANEL_TITLE;
        this.subTitle = RCMMD_REACTIVE_PANEL_SUB_TITLE;
        this.playListTitle = RCMMD_REACTIVE_PANEL_TITLE;
        this.imgList = bgImgList;

        this.content = PanelContentVo.builder()
                .id(rcmmdLikeTrackDto.getRcmmdId())
                .type(RecommendPanelContentType.RC_LKSM_TR)
                .seedTrackImgList(seedTrackImgList)
                .rcmmdTrackImgList(rcmmdTrackImgList)
                .trackCount(rcmmdLikeTrackDto.getTrackIdList().size())
                .renewYn(YnType.Y)
                .createDtime(rcmmdLikeTrackDto.getDispStartDtime())
                .updateDtime(rcmmdLikeTrackDto.getDispStartDtime())
                .build();

        this.seedTrackVo = SeedTrackVo.builder()
                .id(seedTrackDto.getTrackId())
                .name(seedTrackDto.getTrackNm())
                .artistName(seedTrackDto.getArtist().getArtistName())
                .suffix("")
                .build();
    }

    public RcmmdReactivePanel(List<ImageInfo> bgImgList,
                              PanelImageHolder panelImageHolder,
                              RcmmdLikeTrackDto rcmmdLikeTrackDto,
                              TrackDto seedTrackDto,
                              int totalTrackCount) {

        super(RecommendPanelType.RCMMD_LIKE_TRACK);

        this.title = RCMMD_REACTIVE_PANEL_TITLE;
        this.subTitle = RCMMD_REACTIVE_PANEL_SUB_TITLE;
        this.playListTitle = RCMMD_REACTIVE_PANEL_TITLE;
        this.imgList = bgImgList;

        this.content =
                PanelContentVo.builder()
                        .id(rcmmdLikeTrackDto.getRcmmdId())
                        .type(RecommendPanelContentType.RC_LKSM_TR)
                        .seedTrackImgList(panelImageHolder.getSeedThumbnailImageList())
                        .rcmmdTrackImgList(panelImageHolder.getGridThumbnailImageList())
                        .trackCount(totalTrackCount)
                        .renewYn(YnType.Y)
                        .createDtime(rcmmdLikeTrackDto.getDispStartDtime())
                        .updateDtime(rcmmdLikeTrackDto.getDispStartDtime())
                        .build();

        this.seedTrackVo =
                SeedTrackVo.builder()
                        .id(seedTrackDto.getTrackId())
                        .name(seedTrackDto.getTrackNm())
                        .artistName(seedTrackDto.getArtist().getArtistName())
                        .suffix("")
                        .build();

    }
}
