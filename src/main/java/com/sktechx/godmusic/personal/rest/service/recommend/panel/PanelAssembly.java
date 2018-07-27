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

import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.repository.ChannelMapper;
import com.sktechx.godmusic.personal.rest.repository.CharacterPreferGenreMapper;
import com.sktechx.godmusic.personal.rest.repository.ChartMapper;
import com.sktechx.godmusic.personal.rest.repository.RecommendMapper;
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

    @Autowired
    protected ChannelMapper channelMapper;
    @Autowired
    protected RecommendMapper recommendMapper;

    @Autowired
    protected ChartMapper chartMapper;
    @Autowired
    protected CharacterPreferGenreMapper characterPreferGenreMapper;

    public abstract List<Panel> assembleRecommendPanel(PersonalPhaseMeta personalPhaseMeta);



    protected abstract List<Panel> defaultPanelSetting(PersonalPhaseMeta personalPhaseMeta);


}
