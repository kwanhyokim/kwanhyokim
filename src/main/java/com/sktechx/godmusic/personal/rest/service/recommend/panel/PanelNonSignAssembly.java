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

import com.sktechx.godmusic.personal.common.domain.type.OsType;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;

import java.util.ArrayList;
import java.util.List;

/**
 * 설명 : 비로그인 사용자 추천 패널 생성기
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 24.
 */
public abstract class PanelNonSignAssembly extends PanelAssembly{

    @Override
    public List<Panel> assembleRecommendPanel(){
        panelList = new ArrayList();

        defaultPanelSetting();

        return panelList;
    }

}
