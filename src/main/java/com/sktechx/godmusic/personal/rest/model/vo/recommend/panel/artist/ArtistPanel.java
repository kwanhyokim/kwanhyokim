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
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendArtistDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.PanelContentVo;
import org.springframework.util.CollectionUtils;

/**
 * 설명 : 아티스트형 추천 패널
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
public class ArtistPanel extends Panel{

    @JsonIgnore
    private RecommendArtistDto recommendArtistDto;

    public ArtistPanel(RecommendPanelType panelType, RecommendArtistDto recommendArtist ,Integer dispSn)  throws Exception{
        super(panelType , dispSn);
        this.recommendArtistDto = recommendArtist;
        this.initialPanel();
    }


    @Override
    protected void initialPanel() throws Exception{
        ArtistDto representationArtist = neverNullArtist(recommendArtistDto);

        this.title = "Musician focus";
        this.subTitle = representationArtist.getArtistNm();
        this.imgList = representationArtist.getImgList();
        this.content = createPanelContent();

    }

    @Override
    public PanelContentVo createPanelContent() {
        PanelContentVo content = new PanelContentVo();

        content.setId(recommendArtistDto.getRcmmdArtistId());
        content.setArtistCount(recommendArtistDto.getArtistList().size());
        content.setArtistList(recommendArtistDto.getArtistList());
        content.setContentType(RecommendPanelType.ARTIST_POPULAR_TRACK);
        content.setCreateDtime(recommendArtistDto.getCreateDtime());
        content.setUpdateDtime(recommendArtistDto.getUpdateDtime());

        return content;
    }

    private static ArtistDto neverNullArtist(RecommendArtistDto recommendArtist) throws Exception {
        if(recommendArtist == null || CollectionUtils.isEmpty(recommendArtist.getArtistList())){
            throw new IllegalAccessException("artist is null.");
        }

        ArtistDto artist = recommendArtist.getArtistList().get(0);

        if(artist==null ||  CollectionUtils.isEmpty(artist.getImgList())){
            //대체 이미지 패널
            throw new IllegalAccessException("artist imageList is null.");
        }
        return artist;
    }


}
