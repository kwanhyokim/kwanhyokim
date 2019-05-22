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
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
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
    }

    @Override
    public void makeInfoV2() {
        if(ObjectUtils.isEmpty(this.content) || CollectionUtils.isEmpty(this.content.getTrackList()) || this.content.getTrackCount() <= 0){
            return;
        }
        TrackDto trackDto = this.content.getTrackList().get(0);
        String artistName = null;

        if(!CollectionUtils.isEmpty(trackDto.getArtistList())){
            artistName = trackDto.getArtistList().stream().map(x->x.getArtistName()).collect(
                    Collectors.joining(","));
        }


        this.seedTrack = SeedTrackVo.builder()
                .id(trackDto.getTrackId())
                .name(trackDto.getTrackNm())
                .artistName(artistName)
                .suffix(SIMILAR_TRACK_PANEL_SEED_SUFFIX)
                .build();

    }
}
