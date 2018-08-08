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
        // CASE : 비로그인
        // expected : PersonalPhaseType.GUEST
        given(recommendMapper.selectPersonalRecommendPanelMeta(anyLong())).willReturn(null);
        PersonalPhaseMeta  personalPhaseMeta = personalRecommendPhaseService.getPersonalRecommendPhaseMeta(null, OsType.AOS);

        assertNotNull(personalPhaseMeta);
        assertEquals(personalPhaseMeta.getFirstPhaseType(),PersonalPhaseType.GUEST);
        assertEquals(personalPhaseMeta.getOsType(), OsType.AOS);
        assertEquals(personalPhaseMeta.getRcmmdPanelList(), null);
        assertNotNull(personalPhaseMeta.getPersonalPhaseList());
        assertEquals(personalPhaseMeta.getPersonalPhaseList().get(0).getPhaseType(), PersonalPhaseType.GUEST);
    }

    @Test
    public void LISTEN_단건_테스트(){
        // CASE : 로그인 사용자, 2-A 패널 데이터 단건 존재
        // expected : PersonalPhaseType.LISTEN
        List<PersonalPanel> personalPanelList = new ArrayList<>();
        PersonalPanel personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_SML_TR, 1L , 1);
        personalPanelList.add(personalPanel);

        given(recommendMapper.selectPersonalRecommendPanelMeta(anyLong())).willReturn(personalPanelList);
        Long characterNo = 1L;
        OsType osType = OsType.AOS;
        PersonalPhaseMeta  personalPhaseMeta = personalRecommendPhaseService.getPersonalRecommendPhaseMeta(characterNo, osType);

        assertNotNull(personalPhaseMeta);
        assertEquals(personalPhaseMeta.getFirstPhaseType(),PersonalPhaseType.LISTEN);
        assertEquals(personalPhaseMeta.getCharacterNo(), characterNo);
        assertEquals(personalPhaseMeta.getOsType(), osType);
        assertNotNull(personalPhaseMeta.getRcmmdPanelList());
        assertEquals(personalPhaseMeta.getRcmmdPanelList().get(0).getRecommendPanelContentType(), RecommendPanelContentType.RC_SML_TR);
        assertNotNull(personalPhaseMeta.getPersonalPhaseList());
        assertEquals(personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RecommendPanelContentType.RC_SML_TR).get(0) , new Long(1L));

    }

    @Test
    public void LISTEN_복수_테스트(){
        // CASE : 로그인 사용자, 2-A 패널 데이터 2건 존재
        // expected : PersonalPhaseType.LISTEN
        List<PersonalPanel> personalPanelList = new ArrayList<>();
        PersonalPanel personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_SML_TR, 3L , 1);
        personalPanelList.add(personalPanel);
        personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_SML_TR, 2L , 2);
        personalPanelList.add(personalPanel);

        given(recommendMapper.selectPersonalRecommendPanelMeta(anyLong())).willReturn(personalPanelList);
        Long characterNo = 1L;
        OsType osType = OsType.AOS;
        PersonalPhaseMeta  personalPhaseMeta = personalRecommendPhaseService.getPersonalRecommendPhaseMeta(characterNo, osType);

        assertNotNull(personalPhaseMeta);
        assertEquals(personalPhaseMeta.getFirstPhaseType(),PersonalPhaseType.LISTEN);
        assertEquals(personalPhaseMeta.getCharacterNo(), characterNo);
        assertEquals(personalPhaseMeta.getOsType(), osType);
        assertNotNull(personalPhaseMeta.getRcmmdPanelList());
        assertEquals(personalPhaseMeta.getRcmmdPanelList().get(0).getRecommendPanelContentType(), RecommendPanelContentType.RC_SML_TR);
        assertNotNull(personalPhaseMeta.getPersonalPhaseList());
        assertEquals(personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RecommendPanelContentType.RC_SML_TR).get(0) , new Long(3L));
    }

    @Test
    public void RECOMMEND_테스트_1(){
        // CASE : 로그인 사용자, 3-A 패널 데이터 단건 존재
        // expected : PersonalPhaseType.RECOMMEND

        List<PersonalPanel> personalPanelList = new ArrayList<>();
        PersonalPanel personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_CF_TR, 1L , 1);
        personalPanelList.add(personalPanel);

        given(recommendMapper.selectPersonalRecommendPanelMeta(anyLong())).willReturn(personalPanelList);
        Long characterNo = 1L;
        OsType osType = OsType.AOS;
        PersonalPhaseMeta  personalPhaseMeta = personalRecommendPhaseService.getPersonalRecommendPhaseMeta(characterNo, osType);

        assertNotNull(personalPhaseMeta);
        assertEquals(personalPhaseMeta.getFirstPhaseType(),PersonalPhaseType.RECOMMEND);
        assertEquals(personalPhaseMeta.getCharacterNo(), characterNo);
        assertEquals(personalPhaseMeta.getOsType(), osType);
        assertNotNull(personalPhaseMeta.getRcmmdPanelList());
        assertEquals(personalPhaseMeta.getRcmmdPanelList().get(0).getRecommendPanelContentType(), RecommendPanelContentType.RC_CF_TR);
        assertNotNull(personalPhaseMeta.getPersonalPhaseList());
        assertEquals(personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RecommendPanelContentType.RC_CF_TR).get(0) , new Long(1L));
    }

    @Test
    public void RECOMMEND_테스트_2(){
        // CASE : 로그인 사용자, 3-A 패널 데이터 2건 존재
        // expected : PersonalPhaseType.RECOMMEND

        List<PersonalPanel> personalPanelList = new ArrayList<>();
        PersonalPanel personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_CF_TR, 3L , 1);
        personalPanelList.add(personalPanel);
        personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_CF_TR, 2L , 2);
        personalPanelList.add(personalPanel);

        given(recommendMapper.selectPersonalRecommendPanelMeta(anyLong())).willReturn(personalPanelList);
        Long characterNo = 1L;
        OsType osType = OsType.AOS;
        PersonalPhaseMeta  personalPhaseMeta = personalRecommendPhaseService.getPersonalRecommendPhaseMeta(characterNo, osType);

        assertNotNull(personalPhaseMeta);
        assertEquals(personalPhaseMeta.getFirstPhaseType(),PersonalPhaseType.RECOMMEND);
        assertEquals(personalPhaseMeta.getCharacterNo(), characterNo);
        assertEquals(personalPhaseMeta.getOsType(), osType);
        assertNotNull(personalPhaseMeta.getRcmmdPanelList());
        assertEquals(personalPhaseMeta.getRcmmdPanelList().get(0).getRecommendPanelContentType(), RecommendPanelContentType.RC_CF_TR);
        assertNotNull(personalPhaseMeta.getPersonalPhaseList());
        assertEquals(personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RecommendPanelContentType.RC_CF_TR).get(0) , new Long(3L));
    }
    @Test
    public void 복합_테스트_1() {
        // CASE : 로그인 사용자, 3-A 패널, 2-A패널 존재
        // expected : PersonalPhaseType.RECOMMEND

        List<PersonalPanel> personalPanelList = new ArrayList<>();
        PersonalPanel personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_CF_TR, 5L , 1);
        personalPanelList.add(personalPanel);
        personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_SML_TR, 4L , 1);
        personalPanelList.add(personalPanel);

        given(recommendMapper.selectPersonalRecommendPanelMeta(anyLong())).willReturn(personalPanelList);
        Long characterNo = 3L;
        OsType osType = OsType.IOS;
        PersonalPhaseMeta  personalPhaseMeta = personalRecommendPhaseService.getPersonalRecommendPhaseMeta(characterNo, osType);

        assertNotNull(personalPhaseMeta);
        assertEquals(personalPhaseMeta.getFirstPhaseType(),PersonalPhaseType.RECOMMEND);
        assertEquals(personalPhaseMeta.getCharacterNo(), characterNo);
        assertEquals(personalPhaseMeta.getOsType(), osType);
        assertNotNull(personalPhaseMeta.getRcmmdPanelList());
        assertEquals(personalPhaseMeta.getRcmmdPanelList().get(0).getRecommendPanelContentType(), RecommendPanelContentType.RC_CF_TR);
        assertNotNull(personalPhaseMeta.getPersonalPhaseList());
        assertEquals(personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RecommendPanelContentType.RC_CF_TR).get(0) , new Long(5L));
        assertEquals(personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RecommendPanelContentType.RC_SML_TR).get(0) , new Long(4L));
    }

    @Test
    public void 복합_테스트_2() {
        // CASE : 로그인 사용자, 3-A 패널 2개, 2-A패널 존재
        // expected : PersonalPhaseType.RECOMMEND

        List<PersonalPanel> personalPanelList = new ArrayList<>();
        PersonalPanel personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_CF_TR, 5L , 1);
        personalPanelList.add(personalPanel);
        personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_CF_TR, 6L , 2);
        personalPanelList.add(personalPanel);
        personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_SML_TR, 4L , 1);
        personalPanelList.add(personalPanel);

        given(recommendMapper.selectPersonalRecommendPanelMeta(anyLong())).willReturn(personalPanelList);
        Long characterNo = 3L;
        OsType osType = OsType.IOS;
        PersonalPhaseMeta  personalPhaseMeta = personalRecommendPhaseService.getPersonalRecommendPhaseMeta(characterNo, osType);

        assertNotNull(personalPhaseMeta);
        assertEquals(personalPhaseMeta.getFirstPhaseType(),PersonalPhaseType.RECOMMEND);
        assertEquals(personalPhaseMeta.getCharacterNo(), characterNo);
        assertEquals(personalPhaseMeta.getOsType(), osType);
        assertNotNull(personalPhaseMeta.getRcmmdPanelList());
        assertEquals(personalPhaseMeta.getRcmmdPanelList().get(0).getRecommendPanelContentType(), RecommendPanelContentType.RC_CF_TR);
        assertNotNull(personalPhaseMeta.getPersonalPhaseList());
        assertEquals(personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RecommendPanelContentType.RC_CF_TR).get(0) , new Long(5L));
        assertEquals(personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RecommendPanelContentType.RC_CF_TR).get(1) , new Long(6L));
        assertEquals(personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RecommendPanelContentType.RC_SML_TR).get(0) , new Long(4L));
    }

    @Test
    public void 복합_테스트_3() {
        // CASE : 로그인 사용자, 3-A 패널 2개, 2-A패널 2개 존재
        // expected : PersonalPhaseType.RECOMMEND

        List<PersonalPanel> personalPanelList = new ArrayList<>();
        PersonalPanel personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_CF_TR, 5L , 1);
        personalPanelList.add(personalPanel);
        personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_CF_TR, 6L , 2);
        personalPanelList.add(personalPanel);
        personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_SML_TR, 4L , 1);
        personalPanelList.add(personalPanel);
        personalPanel = makeMockPersonalPanel(RecommendPanelContentType.RC_SML_TR, 3L , 2);
        personalPanelList.add(personalPanel);

        given(recommendMapper.selectPersonalRecommendPanelMeta(anyLong())).willReturn(personalPanelList);
        Long characterNo = 3L;
        OsType osType = OsType.IOS;
        PersonalPhaseMeta  personalPhaseMeta = personalRecommendPhaseService.getPersonalRecommendPhaseMeta(characterNo, osType);

        assertNotNull(personalPhaseMeta);
        assertEquals(personalPhaseMeta.getFirstPhaseType(),PersonalPhaseType.RECOMMEND);
        assertEquals(personalPhaseMeta.getCharacterNo(), characterNo);
        assertEquals(personalPhaseMeta.getOsType(), osType);
        assertNotNull(personalPhaseMeta.getRcmmdPanelList());
        assertEquals(personalPhaseMeta.getRcmmdPanelList().get(0).getRecommendPanelContentType(), RecommendPanelContentType.RC_CF_TR);
        assertNotNull(personalPhaseMeta.getPersonalPhaseList());
        assertEquals(personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RecommendPanelContentType.RC_CF_TR).get(0) , new Long(5L));
        assertEquals(personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RecommendPanelContentType.RC_CF_TR).get(1) , new Long(6L));
        assertEquals(personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RecommendPanelContentType.RC_SML_TR).get(0) , new Long(4L));
        assertEquals(personalPhaseMeta.getRecommendPersonalPanelRcmmdIdList(RecommendPanelContentType.RC_SML_TR).get(1) , new Long(3L));
    }

    private PersonalPanel makeMockPersonalPanel(RecommendPanelContentType recommendPanelContentType , Long recommendId, int dispSn){
        PersonalPanel personalPanel = new PersonalPanel();
        personalPanel.setRecommendPanelContentType(recommendPanelContentType);
        personalPanel.setRecommendId(recommendId);
        personalPanel.setDispSn(dispSn);
        return personalPanel;
    }

}
