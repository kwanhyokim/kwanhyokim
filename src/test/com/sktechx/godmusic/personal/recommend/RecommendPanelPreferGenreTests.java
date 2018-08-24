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

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.type.PersonalPhaseType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.PopularChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPanel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

/**
 * 설명 : 추천 단계 테스트
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 10.
 */
@Tag("dev")
@Tag("recommendPanel")
@Slf4j
public class RecommendPanelPreferGenreTests extends RecommendMockData {
    private List<PersonalPanel> personalPanelList = new ArrayList<>();

    //추천 단계 기본 패널 개수
    private int defaultPanelTotalSize = 3;

    //3-A 데이터 수
    private int cfTrackDataSize = 2;
    //2-A 데이터 수
    private int similarTrackDataSize = 1;
    //2-A' 데이터 수
    private int preferGenreSimilarTrackDataSize = 1;
    //1-A' 인기 채널 개수
    private int preferGenrePolularChnlDataSize = 1;

    @BeforeEach
    public void init(){
        personalPanelList.add(makeMockPersonalPanel(RecommendPanelContentType.RC_CF_TR, 1L));
        personalPanelList.add(makeMockPersonalPanel(RecommendPanelContentType.RC_GR_TR, 1L));
        personalPanelList.add(makeMockPersonalPanel(RecommendPanelContentType.RC_SML_TR, 1L));
        personalPanelList.add(makeMockPersonalPanel(RecommendPanelContentType.RC_ATST_TR, 1L));

        given(channelService.getPopularChannelList(anyInt(),anyInt(),anyObject(), anyObject())).willReturn(makeMockHotPlayChannels(3));

    }

    @Test
    public void 전체_패널_대체_테스트(){
        // CASE : 추천 데이터가 없을 경우
        // expected : 1-A, 1-A, 1-A
        given(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(anyLong() , anyObject())).willReturn(makeMockPersonalPhaseMeta(PersonalPhaseType.RECOMMEND , null , null));
        List<Panel> panelList = recommendPanelService.createRecommendPanelList(1L , OsType.AOS);

        assertNotNull(panelList);
        assertEquals(3, panelList.size());
        panelList.stream().forEach(panel -> {
            assertThat(panel, instanceOf(PopularChannelPanel.class));
            assertThat(panel.getType(), is(RecommendPanelType.POPULAR_CHANNEL));
        });
    }

}
