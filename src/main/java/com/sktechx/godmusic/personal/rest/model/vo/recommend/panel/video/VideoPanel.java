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

import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.domain.type.MediaRatingType;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.PanelContentVo;
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
public class VideoPanel extends Panel {

    @ApiModelProperty(value = "비디오 아이디")
    private Long videoId;

    @ApiModelProperty(value = "비디오 제목")
    private String videoNm;

    @ApiModelProperty(value = "영상등급")
    private MediaRatingType mediaRatingType;

    @ApiModelProperty(value = "재생시간")
    private String playTm;

    @ApiModelProperty(value = "썸네일 이미지 url")
    private String videoImgUrl;

    @ApiModelProperty(value = "동영상 연관 아티스트 정보")
    private List<ArtistDto> artistList;

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
