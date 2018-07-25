/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.impl.recommend.panel;

import com.sktechx.godmusic.personal.rest.model.dto.PreferGenreDto;
import com.sktechx.godmusic.personal.rest.model.dto.ServiceGenreDto;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelSignAssembly;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 설명 : 청취 단계 ( 2단계 ) 패널 생성기
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 14.
 */
@Slf4j
@Service("listenPhasePanelAssembly")
public class ListenPhasePanelAssembly extends PanelSignAssembly {

    @Override
    protected void defaultPanelSetting() {
    }
    @Override
    protected void appendPreferencePanel(){
    }
}

