/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data;

import lombok.Builder;
import lombok.Data;

/**
 * 설명 : 차트 전시를 위한 추가정보
 */

@Builder
@Data
public class ChartTitle {
    private String prefix;
    private String suffix;
}
