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

import org.springframework.util.CollectionUtils;

import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.GenreVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.PanelContentVo;
import lombok.extern.slf4j.Slf4j;

/**
 * 설명 : 곡 유형 추천 패널
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
@Slf4j
public abstract class TrackPanel extends Panel {

    public TrackPanel(RecommendPanelType panelType ,String title, String subTitle, String playListTitle, RecommendTrackDto recommendTrackDto, List<ImageInfo> bgImgList) throws CommonBusinessException {
        super(panelType);
        this.imgList = bgImgList;
        this.title = title;
        this.subTitle = subTitle;
        this.playListTitle = playListTitle;
        
        this.content = PanelContentVo.builder()
                .id(recommendTrackDto.getRcmmdId())
                .type(RecommendPanelContentType.getRecommendPanelContentByPanelType(type))
                .trackList(recommendTrackDto.getTrackList())
                .trackCount(recommendTrackDto.getTrackCount())
                .genre(new GenreVo(recommendTrackDto.getSvcGenreDto()))
                .createDtime(recommendTrackDto.getRcmmdCreateDtime())
                .updateDtime(recommendTrackDto.getRcmmdCreateDtime())
            .build();
    }

    protected static RecommendTrackDto neverRecommdnTrackNull(RecommendTrackDto recommendTrackDto) throws CommonBusinessException {
        if(recommendTrackDto == null || CollectionUtils.isEmpty(recommendTrackDto.getTrackList()) )
            throw new CommonBusinessException("recommendTrackDto is null.");

        if(recommendTrackDto.getSvcGenreDto() == null){
            throw new CommonBusinessException("recommendTrackDto svcGenre is null.");
        }
        return recommendTrackDto;
    }

}
