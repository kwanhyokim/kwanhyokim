/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.recommend.panel;

import com.sktechx.godmusic.personal.common.domain.type.OsType;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.service.ChannelService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendPanelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 설명 : 추천 패널 생성기
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 14.
 */
@Slf4j
public abstract class PanelAssembly {

    @Autowired
    protected ChannelService channelService;
    @Autowired
    protected RecommendPanelService recommendPanelService;

    protected List<Panel> panelList;
    protected List<ChnlDto> hotplayChannelList;
    protected OsType osType;
    protected Long characterNo;

    public void setInitialData(Long characterNo , OsType osType){
        this.osType = osType;
        this.characterNo = characterNo;
    }
    public abstract List<Panel> assembleRecommendPanel();

    protected abstract void defaultPanelSetting();


}
