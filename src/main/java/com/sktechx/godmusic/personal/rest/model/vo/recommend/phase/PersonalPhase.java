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

import java.util.Date;

/**
 * 설명 : 개인화 단계 정보
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 18.
 */
@Data
public class PersonalPhase {
    private PersonalPhaseType phaseType;
    private Date avaliableDateTime;

    public PersonalPhase(){
    }

    public PersonalPhase(PersonalPhaseType phaseType , Date avaliableDateTime){
        this.phaseType = phaseType;
        this.avaliableDateTime = avaliableDateTime;
    }
}
