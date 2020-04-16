/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.chart;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.rest.model.dto.ChartDto;
import com.sktechx.godmusic.personal.rest.model.vo.chart.ChartVo;

/**
 * 설명 : 차트 서비스
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 23.
 */
public interface ChartService {
    ChartVo getChartWithTrackList(Long characterNo, Long chartId, OsType osType,
            int trackLimitSize);

    ChartDto getChartByDispPropsTypeWithTrackList(Long characterNo, String dispPropsType,
            OsType osType,
            int trackLimitSize);
}
