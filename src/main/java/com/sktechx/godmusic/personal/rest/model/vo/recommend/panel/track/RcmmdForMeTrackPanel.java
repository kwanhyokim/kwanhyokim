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

import java.util.List;
import java.util.Optional;

import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendForMeDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.GenreVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.PanelContentVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.SeedGenreVo;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.*;

/**
 * 설명 : 나를 위한 새로운 발견 패널
 * RecommendPanelType : RC_CF_TR
 */
public class RcmmdForMeTrackPanel extends Panel {
    public RcmmdForMeTrackPanel( RecommendForMeDto recommendForMeDto, List<ImageInfo> bgImgList
            )throws CommonBusinessException {

        super(RecommendPanelType.RCMMD_TRACK);
        this.title = RCMMD_TRACK_PANEL_TITLE;
        this.subTitle = String.format(RCMMD_TRACK_PANEL_SUB_TITLE,recommendForMeDto.getSvcGenreDto().getSvcGenreNm());
        this.playListTitle = RCMMD_TRACK_PANEL_PLAYLIST_TITLE;
        this.content = PanelContentVo.builder()
                .id(recommendForMeDto.getRcmmdMforuId())
                .type(RecommendPanelContentType.getRecommendPanelContentByPanelType(type))
                .trackList(recommendForMeDto.getTrackDtoList())
                .trackCount(recommendForMeDto.getTrackCount())
                .genre(new GenreVo(recommendForMeDto.getSvcGenreDto()))
                .createDtime(recommendForMeDto.getCreateDtime())
                .updateDtime(recommendForMeDto.getCreateDtime())
                .build();

        this.imgList = bgImgList;

        Optional.ofNullable(this.content.getGenre())
                .ifPresent(
                        genreVo -> {
                    this.subTitle = String.format(RCMMD_TRACK_PANEL_SUB_TITLE_NEW,
                            this.content.getGenre().getName()
                    );
                    this.seedGenreVo = SeedGenreVo.builder()
                            .name(this.content.getGenre().getName())
                            .suffix(RCMMD_TRACK_PANEL_SEED_SUFFIX)
                            .build();
                }
        );
    }
}
