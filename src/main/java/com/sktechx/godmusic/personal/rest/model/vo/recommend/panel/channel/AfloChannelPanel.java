/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel;

import java.util.List;

import org.springframework.util.ObjectUtils;

import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 설명 : 인기 채널 패널
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 19.
 */
public class AfloChannelPanel extends ChannelPanel{

    @Getter
    @Setter
    @ApiModelProperty(required = true, value = "AFLO 패널 사인 이미지 리스트")
    private List<ImageInfo> signImgList;

    public AfloChannelPanel( ChnlDto channel , List<ImageInfo> imgList) throws CommonBusinessException {

        super(RecommendPanelType.ARTIST_FLO_TRACK, channel , null , imgList);

        this.signImgList = channel.getSignImgList();

        if (!ObjectUtils.isEmpty(this.content)) {
            this.content.setType(RecommendPanelContentType.AFLO);
        }

    }

    @Override
    public void makeSeedInfo() {
    }
}
