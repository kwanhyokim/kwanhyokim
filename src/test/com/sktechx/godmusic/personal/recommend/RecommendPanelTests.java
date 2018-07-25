/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.recommend;
import static org.hamcrest.CoreMatchers.instanceOf;
import com.sktechx.godmusic.personal.CommonTest;
import com.sktechx.godmusic.personal.common.domain.type.OsType;
import com.sktechx.godmusic.personal.common.domain.type.PersonalPhaseType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.ChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.PopularChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhase;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.repository.GenreMapper;
import com.sktechx.godmusic.personal.rest.service.ChannelService;
import com.sktechx.godmusic.personal.rest.service.impl.recommend.RecommendPanelAssemblyFactory;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendPanelService;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelAssembly;
import com.sktechx.godmusic.personal.rest.service.recommend.phase.PersonalRecommendPhaseService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * 설명 :
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 10.
 */
@Slf4j
public class RecommendPanelTests extends CommonTest{

    @Autowired
    private RecommendPanelService recommendPanelService;

    @MockBean
    private PersonalRecommendPhaseService personalRecommendPhaseService;

    @MockBean
    private GenreMapper genreMapper;

    @MockBean
    private ChannelService channelService;

    @Autowired
    RecommendPanelAssemblyFactory recommendPanelAssemblyFactory;

    @BeforeEach
    public void init(){

    }

    @Test
    public void GUEST_패널_생성_테스트(){
        given(channelService.getHotplayChannelList(anyInt())).willReturn(makeMockHotPlayChannels());
        given(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(anyLong())).willReturn(makeMockPersonalPhaseMeta(PersonalPhaseType.GUEST));
        List<Panel> panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);

        assertNotNull(panelList);
        assertEquals(3, panelList.size());
        panelList.stream().forEach(panel -> {
            assertThat(panel, instanceOf(PopularChannelPanel.class));
        });

    }


    private PersonalPhaseMeta makeMockPersonalPhaseMeta(PersonalPhaseType personalPhaseType){
        PersonalPhaseMeta phaseMeta = new PersonalPhaseMeta();
        phaseMeta.setPhaseList(Arrays.asList( new PersonalPhase(personalPhaseType,new Date())));
        return phaseMeta;
    }
    private List<ChnlDto> makeMockHotPlayChannels(){
        List<ChnlDto> hotplayList = new ArrayList<>();

        ChnlDto chnl = new ChnlDto();
        chnl.setChnlId(1L);
        chnl.setChnlNm("인기채널 1");
        chnl.setChnlDispNm("인기\n채널 1");

        hotplayList.add(chnl);
        chnl = new ChnlDto();
        chnl.setChnlId(2L);
        chnl.setChnlNm("인기채널 2");
        chnl.setChnlDispNm("인기\n채널 2");

        hotplayList.add(chnl);
        chnl = new ChnlDto();
        chnl.setChnlId(3L);
        chnl.setChnlNm("인기채널 3");
        chnl.setChnlDispNm("인기\n채널 3");
        hotplayList.add(chnl);

        return hotplayList;
    }

}
