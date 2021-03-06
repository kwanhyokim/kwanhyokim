/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.recommend.panel.assembly;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelSignAssembly;
import lombok.extern.slf4j.Slf4j;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.PREFER_GENRE_POPULAR_CHNL_LIST_SIZE;
/**
 * 설명 : 방문 단계 패널 생성기
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 14.
 */
@Slf4j
@Service("visitPhasePanelAssembly")
public class VisitPhasePanelAssembly extends PanelSignAssembly {

    private VisitPhasePanelAssembly(){}

    @Override
    public List<Panel> makeHomePanelListForMainTop(final PersonalPhaseMeta personalPhaseMeta){
        final List<Panel> panelList = new ArrayList<>();

        if(!CollectionUtils.isEmpty(personalPhaseMeta.getPreferGenreList())){
            appendPreferGenreChannelPanelList(personalPhaseMeta,panelList,PREFER_GENRE_POPULAR_CHNL_LIST_SIZE);
            if(isDefaultPanelAppend(panelList.size())){

                List filterChnlIdList = panelList.stream()
                        .filter(panel ->
                                ( RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL.equals(panel.getType())
                                || RecommendPanelType.LISTEN_MOOD_POPULAR_CHANNEL.equals(panel.getType()) )
                                        && panel.getContent() != null)
                        .map(panel-> {
                            return panel.getContent().getId();
                        })
                        .collect(Collectors.toList());

                appendDefaultPopularChannelPanel(personalPhaseMeta, panelList, PREFER_GENRE_POPULAR_CHNL_LIST_SIZE - panelList.size(), filterChnlIdList);
            }
        }else{
            appendDefaultPopularChannelPanel(personalPhaseMeta, panelList, PREFER_GENRE_POPULAR_CHNL_LIST_SIZE, null);
        }

        return panelList;
    }
    @Override
    public List<Panel> makeHomePanelListForMainMiddle(Long characterNo, OsType osType) {
        return null;
    }

    private boolean isDefaultPanelAppend(int panelSize){
        return PREFER_GENRE_POPULAR_CHNL_LIST_SIZE > panelSize;
    }

}
