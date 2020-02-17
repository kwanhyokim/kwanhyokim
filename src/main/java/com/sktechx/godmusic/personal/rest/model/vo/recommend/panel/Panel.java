/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.vo.recommend.panel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.sktechx.godmusic.personal.common.domain.type.PersonalPhaseType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.PanelContentVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.SeedArtistVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.SeedGenreVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.SeedTrackVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.video.VideoPanel;
import com.sktechx.godmusic.personal.rest.service.impl.recommend.panel.PanelOrderSnService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 설명 : 추천 패널
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
@JsonDeserialize(as = VideoPanel.class)
@ApiModel(value="Panel")
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public abstract class Panel {
    @Getter
    @Setter
    @ApiModelProperty(required = true, value = "추천 패널 타입(POPULAR_CHANNEL:인기 채널, PREFER_SIMILAR_TRACK:유사, PREFER_GENRE_SIMILAR_TRACK:선호장르 유사곡, " +
            "RCMMD_TRACK:청취 추천, ARRIST_POPULAR_TRACK:선호 아티스트 인기곡, LIVE_CHART:top100, KIDS_CHART:키즈 )")
    public RecommendPanelType type;

    @Getter
    @ApiModelProperty(required = true, value = "패널 제목")
    protected String title;
    @Getter
    @ApiModelProperty(required = true, value = "패널 부제목")
    protected String subTitle;

    @Getter
    @ApiModelProperty(required = true, value = "재생목록 표기용 제목")
    protected String playListTitle;

    @Getter
    @Setter
    @ApiModelProperty(required = true, value = "패널 배경 이미지 리스트")
    protected List<ImageInfo> imgList;

    @Getter
    @ApiModelProperty(required = true, value = "패널 컨텐츠")
    protected PanelContentVo content;

    @Getter
    @ApiModelProperty(value = "시드 아티스트 정보")
    @JsonProperty("seedArtist")
    protected SeedArtistVo seedArtistVo;

    @Getter
    @ApiModelProperty(value = "시드 트랙 정보")
    @JsonProperty("seedTrack")
    protected SeedTrackVo seedTrackVo;

    @Getter
    @ApiModelProperty(required = true, value = "시드 트랙 정보")
    @JsonProperty("seedGenre")
    protected SeedGenreVo seedGenreVo;


    public Integer getPanelOrderSn(PersonalPhaseType personalPhaseType){
        return PanelOrderSnService.getPanelOrderSn(personalPhaseType, this.type);
    }

    @JsonCreator
    public Panel(@JsonProperty("type") RecommendPanelType type){
        this.type = type;
    }

    @JsonCreator
    public Panel(){

    }

    public void setImgList(List<ImageInfo> imgList) {

        if (imgList != null) {
            imgList.sort(null);
        }

        this.imgList = imgList;
    }
}