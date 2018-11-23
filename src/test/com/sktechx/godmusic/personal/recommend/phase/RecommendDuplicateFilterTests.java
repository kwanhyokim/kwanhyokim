/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.recommend.phase;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.CommonTest;
import com.sktechx.godmusic.personal.common.domain.type.PersonalPhaseType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.repository.RecommendMapper;
import com.sktechx.godmusic.personal.rest.service.recommend.phase.PersonalRecommendPhaseService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

/**
 * 설명 : XXXXXXXX
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 08. 02.
 */
@Slf4j
@Tag("dev")
@Tag("recommendPhase")
public class PersonalPhaseMetaTests extends CommonTest {
    @MockBean
    private RecommendMapper recommendMapper;

    @Autowired
    private PersonalRecommendPhaseService personalRecommendPhaseService;

    @Test
    public void GUEST_테스트(){

    }

    private PersonalPhaseMeta makeMockPersonalPhaseMeta(){
        PersonalPhaseMeta personalPhaseMeta = new PersonalPhaseMeta();

        return personalPhaseMeta;
    }

    private PersonalPanel makeMockPersonalPanel(RecommendPanelContentType recommendPanelContentType , Long recommendId, int dispSn){
        PersonalPanel personalPanel = new PersonalPanel();
        personalPanel.setRecommendPanelContentType(recommendPanelContentType);
        personalPanel.setRecommendId(recommendId);
        personalPanel.setDispSn(dispSn);
        return personalPanel;
    }

}
