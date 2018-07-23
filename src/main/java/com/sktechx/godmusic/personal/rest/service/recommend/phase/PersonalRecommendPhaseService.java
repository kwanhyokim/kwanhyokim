/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.recommend.phase;

import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;

/**
 * 설명 : 캐릭터별 추천 단계 및 추천 메타 정보 서비스
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 19.
 */
public interface PersonalRecommendPhaseService {
    PersonalPhaseMeta getPersonalRecommendPhaseMeta(Long characterNo);
}
