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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.GenreVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.PanelContentVo;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 설명 : 곡 유형 추천 패널
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
public abstract class TrackPanel extends Panel {

    @JsonIgnore
    private RecommendTrackDto recommendTrackDto;

    public TrackPanel(RecommendPanelType panelType ,String title, String subTitle, RecommendTrackDto recommendTrackDto, List<ImageInfo> bgImgList) throws CommonBusinessException {
        super(panelType);
        this.recommendTrackDto = recommendTrackDto;
        this.imgList = bgImgList;
        this.title = title;
        this.subTitle = subTitle;
        initialPanel();
    }

    @Override
    protected void initialPanel() {
        this.content = createPanelContent();
    }

    @Override
    protected PanelContentVo createPanelContent() {
        PanelContentVo content = new PanelContentVo();

        content.setId(recommendTrackDto.getRcmmdId());
        content.setType(RecommendPanelContentType.getRecommendPanelContentByPanelType(type));
        content.setTrackList(recommendTrackDto.getTrackList());
        content.setTrackCount(recommendTrackDto.getTrackList().size());
        content.setGenre(new GenreVo(recommendTrackDto.getSvcGenreDto()));
        content.setCreateDtime(recommendTrackDto.getCreateDtime());

        return content;
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
