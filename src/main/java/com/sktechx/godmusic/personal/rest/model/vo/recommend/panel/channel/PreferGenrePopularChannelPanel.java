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

import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.ImageDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.GenreVo;

import java.util.List;

/**
 * 설명 : 선호 인기 장르 인기 채널
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 19.
 */
public class PreferGenrePopularChannelPanel extends ChannelPanel {

    public PreferGenrePopularChannelPanel(RecommendPanelType panelType, ChnlDto channel, GenreVo genre, List<ImageDto> bgImgList) throws CommonBusinessException {
        super(panelType, channel , genre , bgImgList);
    }
}
