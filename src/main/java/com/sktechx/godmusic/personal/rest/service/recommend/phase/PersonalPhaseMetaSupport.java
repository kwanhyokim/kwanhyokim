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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.client.MetaMgoClient;
import com.sktechx.godmusic.personal.rest.client.model.GetTrackListRequest;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;

/**
 * 설명 : 퍼스널 전시 정보에 대한 제약 사항
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2020/06/23
 */

@Component
public class PersonalPhaseMetaSupport {

        @Autowired
        private MetaMgoClient metaMgoClient;

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

        public PersonalPhaseMeta filterSeedTrackTakeDownedLikeTrack(
                PersonalPhaseMeta personalPhaseMeta
        ){

            List<PersonalPanel> personalPanelList = personalPhaseMeta.getRcmmdPanelList();

            if(CollectionUtils.isEmpty(personalPanelList)){
                return personalPhaseMeta;
            }

            List<Long> resultlikeTrackSeedTrackIdList =
                    Optional.ofNullable(
                            metaMgoClient.getTrackList(
                                    new GetTrackListRequest(
                                            personalPanelList.stream()
                                                    .filter(personalPanel ->
                                                            RecommendPanelContentType.RC_LKSM_TR ==
                                                                    personalPanel.getRecommendPanelContentType())
                                                    .map(PersonalPanel::getSeedTrackId)
                                                    .collect(Collectors.toList()))
                            ).getData().getList()
                    )
                            .orElseGet(Collections::emptyList)
                            .stream()
                            .map(TrackDto::getTrackId)
                            .collect(Collectors.toList());


            personalPhaseMeta.setRcmmdPanelList(
                    personalPanelList.stream()
                    .filter(personalPanel ->
                            RecommendPanelContentType.RC_LKSM_TR != personalPanel.getRecommendPanelContentType() ||
                            (RecommendPanelContentType.RC_LKSM_TR ==
                                    personalPanel.getRecommendPanelContentType() &&
                                    resultlikeTrackSeedTrackIdList.contains(personalPanel.getSeedTrackId())
                            )
                    )
                    .collect(Collectors.toList())
            );

            return personalPhaseMeta;
        }
}
