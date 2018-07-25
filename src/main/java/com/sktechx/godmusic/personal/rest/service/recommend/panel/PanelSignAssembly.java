/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.recommend.panel;

import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;

import java.util.ArrayList;
import java.util.List;

/**
 * 설명 : 로그인 사용자 패널 생성기
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 24.
 */
public abstract class PanelSignAssembly extends PanelAssembly{


    public List<Panel> assembleRecommendPanel(){
        panelList = new ArrayList();
        defaultPanelSetting();
        appendPreferencePanel();
        appendPreferenceChartPanel();
        return panelList;
    }


    protected abstract void appendPreferencePanel();
    private void appendPreferenceChartPanel(){

    }
}
