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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.common.exception.CommonErrorMessage;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.GenreVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.PanelContentVo;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 설명 : 채널형 추천 패널
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class ChannelPanel extends Panel{
    @JsonIgnore
    private ChnlDto channel;

    @Getter
    private GenreVo genre;

    public ChannelPanel(RecommendPanelType panelType, ChnlDto channel, GenreVo genre , List<ImageInfo> imgList) throws CommonBusinessException {
        super(panelType);
        this.channel = neverNullChannel(channel);
        this.genre = genre;
        this.imgList = imgList;
        this.initialPanel();
    }

    @Override
    protected void initialPanel(){
        this.title = channel.getChnlDispNm();
        this.subTitle = "";
        this.content = createPanelContent();
    }


    @Override
    public PanelContentVo createPanelContent() {
        PanelContentVo content = new PanelContentVo();

        content.setId(channel.getChnlId());
        content.setType(RecommendPanelContentType.CHNL);
        content.setTrackCount(channel.getTrackCount());
        content.setTrackList(channel.getTrackList());
        content.setCreateDtime(channel.getCreateDtime());
        content.setUpdateDtime(channel.getUpdateDtime());
        content.setRenewTrackCount(channel.getRenewTrackCnt());
        content.setRenewYn(channel.getRenewYn());
        return content;
    }

    private static ChnlDto neverNullChannel(ChnlDto channel) throws CommonBusinessException {
        if(channel == null || StringUtils.isEmpty(channel.getChnlNm()))
            throw new CommonBusinessException(CommonErrorMessage.INTERNAL_SERVER_ERROR);
        return channel;
    }
}
