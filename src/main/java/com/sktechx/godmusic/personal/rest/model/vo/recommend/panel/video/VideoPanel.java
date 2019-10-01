/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.video;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.domain.type.MediaRatingType;
import com.sktechx.godmusic.personal.common.domain.type.VideoType;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.PanelContentVo;
import com.sktechx.godmusic.personal.rest.model.vo.video.VideoArtistVo;
import com.sktechx.godmusic.personal.rest.model.vo.video.VideoThumbnailImageVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 설명 : 비디오 패널
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2019-09-27
 */

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoPanel extends Panel {

    @ApiModelProperty(value = "비디오 아이디")
    private Long videoId;

    @ApiModelProperty(value = "비디오 제목")
    private String videoNm;

    @ApiModelProperty(value = "영상등급")
    private MediaRatingType mediaRatingType;

    @ApiModelProperty(value = "재생시간")
    private String playTm;

    @ApiModelProperty(value = "동영상 타입 코드")
    private VideoType videoType;

    @ApiModelProperty(value = "동영상 타입 Description")
    private String videoTypeValue;

    @ApiModelProperty(value = "동영상 대표 아티스트")
    private VideoArtistVo representationArtist;

    @ApiModelProperty(value = "동영상 대표 아티스트 목록")
    private List<VideoArtistVo> artistList;

    @ApiModelProperty(value = "동영상 썸네일 목록")
    private List<VideoThumbnailImageVo> thumbnailImageList;

    @Override
    protected void initialPanel() throws CommonBusinessException {
    }
    @Override
    protected PanelContentVo createPanelContent() {
        return null;
    }
    @Override
    public void makeSeedInfo() {
    }
}
