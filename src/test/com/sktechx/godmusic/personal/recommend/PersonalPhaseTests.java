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

import com.sktechx.godmusic.personal.CommonTest;
import com.sktechx.godmusic.personal.common.domain.type.PersonalPhaseType;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhase;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 설명 : 개인화 단계 테스트
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 10.
 */
@Slf4j
public class PersonalPhaseTests extends CommonTest{

    @Test
    public void 개인화메타데이터_청취단계_GET(){
        PersonalPhaseMeta phaseMeta = new PersonalPhaseMeta();
        List<PersonalPhase> personalPhaseList = new ArrayList();

        personalPhaseList.add(new PersonalPhase(PersonalPhaseType.LISTEN , new Date()));
        phaseMeta.setPhaseList(personalPhaseList);


        assertEquals(PersonalPhaseType.LISTEN,phaseMeta.getFirstPhaseType());
        personalPhaseList.add(new PersonalPhase(PersonalPhaseType.VISIT , new Date()));
        phaseMeta.setPhaseList(personalPhaseList);

        assertEquals(PersonalPhaseType.LISTEN,phaseMeta.getFirstPhaseType());

        personalPhaseList.add(new PersonalPhase(PersonalPhaseType.GUEST , new Date()));
        phaseMeta.setPhaseList(personalPhaseList);
        assertEquals(PersonalPhaseType.LISTEN,phaseMeta.getFirstPhaseType());

        personalPhaseList.add(new PersonalPhase(PersonalPhaseType.RECOMMEND , new Date()));
        phaseMeta.setPhaseList(personalPhaseList);

        assertEquals(PersonalPhaseType.RECOMMEND,phaseMeta.getFirstPhaseType());

        phaseMeta.setPhaseList(null);
        assertEquals(PersonalPhaseType.GUEST,phaseMeta.getFirstPhaseType());
    }

}
