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

import com.sktechx.godmusic.personal.common.domain.type.PersonalPhaseType;
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
    private List<PersonalPhase> phaseList;
    private List<PersonalPanel> panelList;

    public PersonalPhaseType getFirstPhaseType(){
        if(!CollectionUtils.isEmpty(phaseList)){
            PersonalPhase firstPhase =  phaseList.stream()
                    .sorted(Comparator.comparing( PersonalPhase::getPhaseType , (s1,s2)->{
                        return s2.compareTo(s1);
                    }))
                    .findFirst()
                    .orElse(null);
            if(firstPhase != null)
                return firstPhase.getPhaseType();
        }
        return PersonalPhaseType.GUEST;
    }

}
