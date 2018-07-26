/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.impl.recommend.panel;

import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.ImageDto;
import com.sktechx.godmusic.personal.rest.model.dto.PreferGenreDto;
import com.sktechx.godmusic.personal.rest.model.dto.ServiceGenreDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.PopularChannelPanel;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelSignAssembly;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 설명 : 방문 단계 패널 생성기
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 14.
 */
@Slf4j
@Service("visitPhasePanelAssembly")
public class VisitPhasePanelAssembly extends PanelSignAssembly {

    @Override
    protected void defaultPanelSetting() {
        List<ImageDto> bgImgList = recommendPanelService.getPanelBackgroundImageList(RecommendPanelType.POPULAR_CHANNEL , personalPhaseMeta.getOsType());

        //TODO : 인기채널 패널 개수 DB 메타로 관리
        this.hotplayChannelList = channelService.getHotplayChannelList(new Integer(3));

        hotplayChannelList.stream().forEach(channel -> {
            try{
                panelList.add(new PopularChannelPanel(RecommendPanelType.POPULAR_CHANNEL,channel,bgImgList));
            }catch(Exception e){
                log.error("VisitPhasePanel defaultPanelSetting error : {}",e.getMessage());
                e.printStackTrace();
            }
        });
    }
    @Override
    protected void appendPreferencePanel(){


    }

}
