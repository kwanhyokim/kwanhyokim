/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.dto;

import com.sktechx.godmusic.lib.domain.code.YnType;
import lombok.Data;

/**
 * 설명 : 차트 랭크 표현
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 08. 16.
 */
@Data
public class RankDto {
    private YnType newYn;
    private int rankBadge;
}
