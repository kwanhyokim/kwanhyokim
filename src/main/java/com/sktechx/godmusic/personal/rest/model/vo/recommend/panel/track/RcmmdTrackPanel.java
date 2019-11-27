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

import org.springframework.util.ObjectUtils;

import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.PanelContentVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.SeedGenreVo;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.*;

/**
 * 설명 : 나를 위한 새로운 발견 패널
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 10.
 */
public class RcmmdTrackPanel extends TrackPanel {
    public RcmmdTrackPanel( RecommendTrackDto recommendTrackDto, List<ImageInfo> bgImgList )throws CommonBusinessException {
        super(RecommendPanelType.RCMMD_TRACK ,
                RCMMD_TRACK_PANEL_TITLE ,
                String.format(RCMMD_TRACK_PANEL_SUB_TITLE,neverRecommdnTrackNull(recommendTrackDto).getSvcGenreDto().getSvcGenreNm()),
                recommendTrackDto,
                bgImgList);

        Optional.ofNullable(
                Optional.ofNullable(this.content)
                        .orElse(PanelContentVo.builder().build())
                        .getGenre()
        ).ifPresent(genreVo -> {
                    this.subTitle = String.format(RCMMD_TRACK_PANEL_SUB_TITLE_NEW, this.content.getGenre().getName());
                    this.seedGenreVo = SeedGenreVo.builder()
                            .name(this.content.getGenre().getName()).suffix(RCMMD_TRACK_PANEL_SEED_SUFFIX)
                            .build();
                }
        );

    }

}
