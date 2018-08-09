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
import com.sktechx.godmusic.personal.CommonTest;
import com.sktechx.godmusic.personal.common.domain.type.*;
import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferGenreDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.artist.ArtistPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.ListenMoodPopularChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.PopularChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.chart.ChartPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.PreferGenreSimilarTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.track.PreferSimilarTrackPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPanel;
import com.sktechx.godmusic.personal.rest.repository.ChannelMapper;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendPanelService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * 설명 :  테스트
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 10.
 */
@Slf4j
@Tag("dev")
@Tag("recommendPanel")
public class RecommendPanelRealTests extends CommonTest {


    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    private RecommendPanelService recommendPanelService;
    @Test
    public void 전체_패널_대체_테스트() {
//        List<Panel> panelList = recommendPanelService.createRecommendPanelList(52L, OsType.AOS);
//
//        log.info("panelList : {}",panelList);
//        log.info("panelList size : {}",panelList.size());

        channelMapper.selectPreferGenrePopularChannelIdList(Arrays.asList(2L,3L),OsType.AOS);
    }

}
