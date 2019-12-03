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

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.IntStream;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendArtistDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.PanelContentVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.SeedArtistVo;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.ARTIST_PANEL_TITLE;
import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.ARTIST_POPULAR_ARTIST_NAME_COUNT;
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

        ArtistDto representationArtist = neverNullArtist(recommendArtistDto);

        this.title = ARTIST_PANEL_TITLE;

        this.subTitle = getArtistSubTitle(recommendArtistDto);

        this.imgList = representationArtist.getImgList();
        this.content = PanelContentVo.builder()
                    .id(recommendArtistDto.getRcmmdArtistId())
                    .artistCount(recommendArtistDto.getArtistList().size())
                    .artistList(recommendArtistDto.getArtistList())
                    .type(RecommendPanelContentType.RC_ATST_TR)
                    .createDtime(recommendArtistDto.getDispStdStartDt())
                    .updateDtime(recommendArtistDto.getUpdateDtime())
                .build();

        this.seedArtistVo = Optional.ofNullable(this.content).isPresent() ?
                SeedArtistVo.builder()
                .name(this.subTitle)
                .suffix("")
                .build()
                : null
                ;
    }


    private String getArtistSubTitle(RecommendArtistDto recommendArtistDto){
        List<ArtistDto> artistList = recommendArtistDto.getArtistList();
        StringJoiner joiner = new StringJoiner(",");

        IntStream.range(0, getArtistNameDispCount(artistList.size()))
                .forEach(index ->{
                    ArtistDto artist = artistList.get(index);
                    if(!StringUtils.isEmpty(artist.getArtistName())){
                        if(index == 0){
                            joiner.add(artist.getArtistName());
                        }else{
                            joiner.add(" " +artist.getArtistName());
                        }
                    }
                });

        return joiner.toString();
    }

    private int getArtistNameDispCount(int artistListSize ){
        if( artistListSize < ARTIST_POPULAR_ARTIST_NAME_COUNT ){
            return artistListSize;
        }
        return ARTIST_POPULAR_ARTIST_NAME_COUNT;
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
