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

import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.ImageDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 설명 : 아티스트형 추천 패널
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
public class ArtistPanel extends Panel {

    @Getter
    @ApiModelProperty(required = true, example = "", value = "아티스트 리스트")
    List<ArtistDto> artistList;

    public ArtistPanel(RecommendPanelType panelType, List<ArtistDto> artistList)  throws Exception{
        super(panelType , "Musician focus" , neverNullList(artistList).getArtistNm(),neverNullList(artistList).getImgList());
        this.artistList = artistList;
    }

    private static ArtistDto neverNullList(List<ArtistDto> artistList) throws Exception {
        if(CollectionUtils.isEmpty(artistList))
            throw new IllegalAccessException("artistList is null.");
        return artistList.get(0);
    }
}
