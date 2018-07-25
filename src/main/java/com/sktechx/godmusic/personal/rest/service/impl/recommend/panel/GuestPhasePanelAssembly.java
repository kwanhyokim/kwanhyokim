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
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.PopularChannelPanel;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelNonSignAssembly;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 설명 : 비로그인 사용자 패널 생성기
 *       인기 채널 3종 제공
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 24.
 */
@Slf4j
@Service("guestPhasePanelAssembly")
public class GuestPhasePanelAssembly extends PanelNonSignAssembly {

    @Override
    protected void defaultPanelSetting() {
        // TODO : 1-A , 1-A' , 2-B 각각의 패널 이미지 필요 여부 확인
        List<ImageDto> bgImgList = recommendPanelService.getPanelBackgroundImageList(RecommendPanelType.POPULAR_CHANNEL , osType);

        //TODO : 인기채널 패널 개수 DB로 관리
        this.hotplayChannelList = channelService.getHotplayChannelList(new Integer(3));

        // 1-A 패널 셋팅
        hotplayChannelList.stream().forEach(channel -> {
            try{
                panelList.add(new PopularChannelPanel(RecommendPanelType.POPULAR_CHANNEL,channel,bgImgList));
            }catch(Exception e){
                log.error("GuestPhasePanel defaultPanelSetting error : {}",e.getMessage());
                e.printStackTrace();
            }
        });

    }
}
