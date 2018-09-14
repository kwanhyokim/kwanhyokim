/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.artist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendArtistDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.PanelContentVo;
import org.springframework.util.CollectionUtils;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.ARTIST_PANEL_TITLE;
/**
 * 설명 : 아티스트형 추천 패널
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
public class ArtistPanel extends Panel{

    @JsonIgnore
    private RecommendArtistDto recommendArtistDto;

    public ArtistPanel(RecommendArtistDto recommendArtist)  throws CommonBusinessException{
        super(RecommendPanelType.ARTIST_POPULAR_TRACK);
        this.recommendArtistDto = recommendArtist;
        this.initialPanel();
    }


    @Override
    protected void initialPanel() throws CommonBusinessException{
        ArtistDto representationArtist = neverNullArtist(recommendArtistDto);

        this.title = ARTIST_PANEL_TITLE;
        this.subTitle = representationArtist.getArtistName();
        this.imgList = representationArtist.getImgList();
        this.content = createPanelContent();

    }

    @Override
    public PanelContentVo createPanelContent() {
        PanelContentVo content = new PanelContentVo();

        content.setId(recommendArtistDto.getRcmmdArtistId());
        content.setArtistCount(recommendArtistDto.getArtistList().size());
        content.setArtistList(recommendArtistDto.getArtistList());
        content.setType(RecommendPanelContentType.RC_ATST_TR);
        content.setCreateDtime(recommendArtistDto.getCreateDtime());
        content.setUpdateDtime(recommendArtistDto.getUpdateDtime());

        return content;
    }

    private static ArtistDto neverNullArtist(RecommendArtistDto recommendArtist) throws CommonBusinessException {
        if(recommendArtist == null || CollectionUtils.isEmpty(recommendArtist.getArtistList())){
            throw new CommonBusinessException("artist is null.");
        }


        ArtistDto artist = recommendArtist.getArtistList()
                .stream()
                .filter(artistDto->!CollectionUtils.isEmpty(artistDto.getImgList()))
                .findFirst()
                .orElse(recommendArtist.getArtistList().get(0));

        if(artist==null){
            throw new CommonBusinessException("artist is null.");
        }
        return artist;
    }


}
