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

import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;

/**
 * 설명 : 인기 채널 패널
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 19.
 */
public class TPOChannelPanel extends ChannelPanel{
    public TPOChannelPanel( ChnlDto channel , List<ImageInfo> imgList) throws CommonBusinessException {
        super(RecommendPanelType.TPO_CHANNEL, channel , null , imgList);
    }
    @Override
    public void makeInfoV2() {
    }
}
