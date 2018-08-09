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

import java.util.List;

/**
 * 설명 : 청취 무드 인기 채널
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 19.
 */
public class ListenMoodPopularChannelPanel extends ChannelPanel{
    public ListenMoodPopularChannelPanel(RecommendPanelType panelType, ChnlDto channel) throws CommonBusinessException {
        super(panelType, channel , null);
    }
}
