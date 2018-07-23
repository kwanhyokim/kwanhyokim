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

import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 설명 : 개인화 패널 정보
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 18.
 */
@Data
public class PersonalPanel {
    private RecommendPanelType recommendPanelType;
    private Date avaliableDateTime;
    private int dispSn;
    private List<Long> recommendIdList;

}
