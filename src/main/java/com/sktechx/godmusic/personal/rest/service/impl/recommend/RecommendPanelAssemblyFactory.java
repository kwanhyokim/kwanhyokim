/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.impl.recommend;

import com.sktechx.godmusic.personal.common.domain.type.PersonalPhaseType;
import com.sktechx.godmusic.personal.common.service.ApplicationContextProvider;
import com.sktechx.godmusic.personal.rest.service.impl.recommend.panel.GuestPhasePanelAssembly;
import com.sktechx.godmusic.personal.rest.service.impl.recommend.panel.ListenPhasePanelAssembly;
import com.sktechx.godmusic.personal.rest.service.impl.recommend.panel.RecommendPhasePanelAssembly;
import com.sktechx.godmusic.personal.rest.service.impl.recommend.panel.VisitPhasePanelAssembly;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelAssembly;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 24.
 */
@Service
public class RecommendPanelAssemblyFactory {

    @Autowired
    private ApplicationContextProvider applicationContextProvider;

    public PanelAssembly getRecommendPanelAssembly(PersonalPhaseType personalPhaseType ){
        if(PersonalPhaseType.VISIT.equals(personalPhaseType)){
            return applicationContextProvider.getContext().getBean(VisitPhasePanelAssembly.class);
        }else if(PersonalPhaseType.LISTEN.equals(personalPhaseType)){
            return applicationContextProvider.getContext().getBean(ListenPhasePanelAssembly.class);
        }else if(PersonalPhaseType.RECOMMEND.equals(personalPhaseType)) {
            return applicationContextProvider.getContext().getBean(RecommendPhasePanelAssembly.class);
        }

        return applicationContextProvider.getContext().getBean(GuestPhasePanelAssembly.class);
    }
}
