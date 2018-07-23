/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.personal.rest.model.dto.ChartDto;
import com.sktechx.godmusic.personal.rest.repository.ChartMapper;
import com.sktechx.godmusic.personal.rest.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 설명 : 각종 차트 서비스
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 23.
 */
@Service
public class ChartServiceImpl implements ChartService {

    @Autowired
    private ChartMapper chartMapper;

    @Override
    public ChartDto getKidsChart() {
        //TODO : 추후 meta api 호출
        return chartMapper.selectLiveChart();
    }

    @Override
    public ChartDto getLiveChart() {
        //TODO : 추후 meta api 호출
        return chartMapper.selectLiveChart();
    }
}
