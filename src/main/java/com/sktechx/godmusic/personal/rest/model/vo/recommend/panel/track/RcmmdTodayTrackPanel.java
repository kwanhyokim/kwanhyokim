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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendSimilarTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.PanelContentVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.SeedTrackVo;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.*;

/**
 * 설명 : 오늘의 추천 패널
 * RecommendPanelType : RC_SML_TR
 *
 */
public class RcmmdTodayTrackPanel extends Panel {
    public RcmmdTodayTrackPanel( RecommendSimilarTrackDto recommendSimilarTrackDto,
            List<ImageInfo> bgImgList) throws CommonBusinessException {

        super(RecommendPanelType.PREFER_SIMILAR_TRACK);
        this.title = SIMILAR_TRACK_PANEL_TITLE;
        this.subTitle = SIMILAR_TRACK_PANEL_SUB_TITLE;
        this.playListTitle = SIMILAR_TRACK_PANEL_TITLE;
        this.imgList = bgImgList;
        this.content = PanelContentVo.builder()
                .id(recommendSimilarTrackDto.getRcmmdSimilarTrackId())
                .type(RecommendPanelContentType.getRecommendPanelContentByPanelType(type))
                .trackList(recommendSimilarTrackDto.getTrackDtoList())
                .trackCount(recommendSimilarTrackDto.getTrackCount())
                .createDtime(recommendSimilarTrackDto.getCreateDtime())
                .updateDtime(recommendSimilarTrackDto.getCreateDtime())
                .build();

        Optional.ofNullable(
                Optional.ofNullable(this.content)
                        .orElseGet( () -> PanelContentVo.builder().build())
                        .getTrackList()
        ).orElseGet(Collections::emptyList)
                .stream()
                .findFirst()
                .ifPresent(
                        trackDto ->
                                this.seedTrackVo = SeedTrackVo.builder()
                                        .id(trackDto.getTrackId())
                                        .name(trackDto.getTrackNm())
                                        .artistName(
                                                Optional.ofNullable(trackDto.getArtistList())
                                                        .orElseGet(Collections::emptyList)
                                                        .stream()
                                                        .filter(Objects::nonNull)
                                                        .map(ArtistDto::getArtistName)
                                                        .collect(Collectors.joining(","))

                                        )
                                        .suffix(SIMILAR_TRACK_PANEL_SEED_SUFFIX)
                                        .build()
                );
    }
}
