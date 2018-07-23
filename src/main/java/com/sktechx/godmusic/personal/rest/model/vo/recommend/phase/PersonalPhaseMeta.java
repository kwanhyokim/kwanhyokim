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

import lombok.Data;

import java.util.List;

/**
 * 설명 : 개인화 콘텐츠 메타 캐쉬 데이터 ( 단계, 추천 패널 )
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 18.
 */
@Data
public class PersonalPhaseMeta {
    private List<PersonalPhase> personalPhaseList;
    private List<PersonalPanel> personalPanelList;
}
