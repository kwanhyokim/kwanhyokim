/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.vo.recommend.phase;

import com.sktechx.godmusic.personal.common.domain.type.OsType;
import com.sktechx.godmusic.personal.common.domain.type.PersonalPhaseType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferGenreDto;
import com.sktechx.godmusic.personal.rest.model.dto.PreferGenreDto;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * 설명 : 개인화 콘텐츠 메타 캐쉬 데이터 ( 단계, 추천 패널 )
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 18.
 */
@Data
public class PersonalPhaseMeta {
    protected OsType osType;
    protected Long characterNo;

    private List<PreferGenreDto> preferGenreList;
    private List<PersonalPhase> phaseList;
    private List<PersonalPanel> rcmmdPanelList;

    public PersonalPhaseType getFirstPhaseType(){
        if(!CollectionUtils.isEmpty(phaseList)){
            PersonalPhase firstPhase =  phaseList.stream()
                    .sorted(Comparator.comparing( PersonalPhase::getPhaseType , (phase1,phase2)->{
                        return phase2.compareTo(phase1);
                    }))
                    .findFirst()
                    .orElse(null);
            if(firstPhase != null)
                return firstPhase.getPhaseType();
        }
        return PersonalPhaseType.GUEST;
    }



    public List<Long> getRecommendPersonlPanelRcmmdIdList(RecommendPanelContentType recommendPanelContentType) {
        if (!CollectionUtils.isEmpty(rcmmdPanelList)) {
            PersonalPanel personalPanel = getRecommendPersonalPanel(recommendPanelContentType);
            if (personalPanel != null && !CollectionUtils.isEmpty(personalPanel.getRecommendIdList())) {
                return personalPanel.getRecommendIdList();
            }
        }
        return null;
    }
    public Long getRecommendPersonalPanelRcmmdId(RecommendPanelContentType recommendPanelContentType){
        if(!CollectionUtils.isEmpty(rcmmdPanelList)){
            PersonalPanel personalPanel = getRecommendPersonalPanel(recommendPanelContentType);
            if(personalPanel != null && !CollectionUtils.isEmpty(personalPanel.getRecommendIdList()) ){
                return personalPanel.getRecommendIdList().get(0);
            }
        }
        return null;
    }

    private PersonalPanel getRecommendPersonalPanel(RecommendPanelContentType recommendPanelContentType){
        if(!CollectionUtils.isEmpty(rcmmdPanelList)){
            return rcmmdPanelList.stream()
                    .filter(personalPanel -> recommendPanelContentType.equals(personalPanel.getRecommendPanelContentType()))
                    .findFirst().orElse(null);
        }
        return null;
    }

}
