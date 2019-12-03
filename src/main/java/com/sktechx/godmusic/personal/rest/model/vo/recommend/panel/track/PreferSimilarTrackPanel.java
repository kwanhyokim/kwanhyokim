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

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.PanelContentVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.SeedTrackVo;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.SIMILAR_TRACK_PANEL_SEED_SUFFIX;
import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.SIMILAR_TRACK_PANEL_SUB_TITLE;
import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.SIMILAR_TRACK_PANEL_TITLE;
/**
 * 설명 : 유사곡 패널
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 10.
 */
public class PreferSimilarTrackPanel extends TrackPanel {

    public PreferSimilarTrackPanel(RecommendTrackDto recommendTrackDto,List<ImageInfo> bgImgList) throws CommonBusinessException {
        super( RecommendPanelType.PREFER_SIMILAR_TRACK ,
                SIMILAR_TRACK_PANEL_TITLE ,
                SIMILAR_TRACK_PANEL_SUB_TITLE,
                neverRecommdnTrackNull(recommendTrackDto) ,
                bgImgList
        );

        Optional.ofNullable(
                Optional.ofNullable(this.content)
                        .orElse(PanelContentVo.builder().build())
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
