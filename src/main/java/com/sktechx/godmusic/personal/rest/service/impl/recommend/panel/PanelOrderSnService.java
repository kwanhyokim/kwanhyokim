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

import com.sktechx.godmusic.personal.common.domain.type.PersonalPhaseType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;

import java.util.HashMap;
import java.util.Map;

/**
 * 설명 : 추천 단계별 패널 우선 순위
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 31.
 */
public class PanelOrderSnService {

    private static Map<PersonalPhaseType,Map<RecommendPanelType, Integer>> panelOrderSnMap;

    static {
        panelOrderSnMap = new HashMap<>();

        //손님
        Map<RecommendPanelType, Integer> orderSnMap = new HashMap<>();
        orderSnMap.put(RecommendPanelType.LIVE_CHART,Integer.MIN_VALUE);
        orderSnMap.put(RecommendPanelType.RCMMD_TRACK,1);
        orderSnMap.put(RecommendPanelType.ARTIST_POPULAR_TRACK,2);
        orderSnMap.put(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK,3);
        orderSnMap.put(RecommendPanelType.PREFER_SIMILAR_TRACK,4);
        orderSnMap.put(RecommendPanelType.LISTEN_MOOD_POPULAR_CHANNEL,5);
        orderSnMap.put(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL,6);
        orderSnMap.put(RecommendPanelType.POPULAR_CHANNEL,7);
        orderSnMap.put(RecommendPanelType.KIDS_CHART,Integer.MAX_VALUE);
        panelOrderSnMap.put(PersonalPhaseType.GUEST, orderSnMap);

        //방문 단계
        orderSnMap = new HashMap<>();
        orderSnMap.put(RecommendPanelType.LIVE_CHART,Integer.MIN_VALUE);
        orderSnMap.put(RecommendPanelType.RCMMD_TRACK,1);
        orderSnMap.put(RecommendPanelType.ARTIST_POPULAR_TRACK,2);
        orderSnMap.put(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK,3);
        orderSnMap.put(RecommendPanelType.PREFER_SIMILAR_TRACK,4);
        orderSnMap.put(RecommendPanelType.LISTEN_MOOD_POPULAR_CHANNEL,5);
        orderSnMap.put(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL,6);
        orderSnMap.put(RecommendPanelType.POPULAR_CHANNEL,7);
        orderSnMap.put(RecommendPanelType.KIDS_CHART,Integer.MAX_VALUE);

        panelOrderSnMap.put(PersonalPhaseType.VISIT, orderSnMap);

        //청취단계
        orderSnMap = new HashMap<>();
        orderSnMap.put(RecommendPanelType.LIVE_CHART,Integer.MIN_VALUE);
        orderSnMap.put(RecommendPanelType.RCMMD_TRACK,1);
        orderSnMap.put(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK,2);
        orderSnMap.put(RecommendPanelType.PREFER_SIMILAR_TRACK,3);
        orderSnMap.put(RecommendPanelType.ARTIST_POPULAR_TRACK,4);
        orderSnMap.put(RecommendPanelType.LISTEN_MOOD_POPULAR_CHANNEL,5);
        orderSnMap.put(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL,6);
        orderSnMap.put(RecommendPanelType.POPULAR_CHANNEL,7);
        orderSnMap.put(RecommendPanelType.KIDS_CHART,Integer.MAX_VALUE);

        panelOrderSnMap.put(PersonalPhaseType.LISTEN, orderSnMap);

        //추천단계
        orderSnMap = new HashMap<>();
        orderSnMap.put(RecommendPanelType.LIVE_CHART,Integer.MIN_VALUE);
        orderSnMap.put(RecommendPanelType.RCMMD_TRACK,1);
        orderSnMap.put(RecommendPanelType.ARTIST_POPULAR_TRACK,2);
        orderSnMap.put(RecommendPanelType.PREFER_GENRE_SIMILAR_TRACK,3);
        orderSnMap.put(RecommendPanelType.PREFER_SIMILAR_TRACK,4);
        orderSnMap.put(RecommendPanelType.LISTEN_MOOD_POPULAR_CHANNEL,5);
        orderSnMap.put(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL,6);
        orderSnMap.put(RecommendPanelType.POPULAR_CHANNEL,7);
        orderSnMap.put(RecommendPanelType.KIDS_CHART,Integer.MAX_VALUE);

        panelOrderSnMap.put(PersonalPhaseType.RECOMMEND, orderSnMap);

    }

    public static Integer getPanelOrderSn(PersonalPhaseType personalPhaseType, RecommendPanelType recommendPanelType){
        Map<RecommendPanelType, Integer> orderSnMap = panelOrderSnMap.get(personalPhaseType);
        if(orderSnMap != null){
            Integer orderSn = orderSnMap.get(recommendPanelType);
            if(orderSn != null){
                return orderSn;
            }
        }
        return Integer.MAX_VALUE;
    }
}
