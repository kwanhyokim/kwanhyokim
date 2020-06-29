/*
 * Copyright (c) 2020 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.service.recommend.phase;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.sktechx.godmusic.lib.utils.ComparableVersion;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import lombok.extern.slf4j.Slf4j;

/**
 * 설명 : 퍼스널 전시 정보에 대한 제약 사항
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2020/06/23
 */

@Slf4j
@Component
public class PersonalPhaseMetaSupport {

        public PersonalPhaseMeta filterPanelByAppVer(PersonalPhaseMeta personalPhaseMeta){
            List<PersonalPanel> personalPanelList = personalPhaseMeta.getRcmmdPanelList();
            personalPhaseMeta.setRcmmdPanelList(
                    personalPanelList.stream()
                            .filter(personalPanel ->
                                    new ComparableVersion(personalPhaseMeta.getAppVer()).compareTo( new ComparableVersion(personalPanel.getExceptionalAppVersion())) < 0
            )
                            .collect(Collectors.toList())
            );

            return personalPhaseMeta;
        }

        public PersonalPhaseMeta filterPanelByRecommendContentType(
                PersonalPhaseMeta personalPhaseMeta,
                RecommendPanelContentType recommendPanelContentType) {

            List<PersonalPanel> personalPanelList = personalPhaseMeta.getRcmmdPanelList();
            personalPhaseMeta.setRcmmdPanelList(
                    personalPanelList.stream()
                            .filter(personalPanel ->
                                    personalPanel.getRecommendPanelContentType() != recommendPanelContentType
                            )
                            .collect(Collectors.toList())
            );

            return personalPhaseMeta;
        }

}
