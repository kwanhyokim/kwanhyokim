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

import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.ChartDto;
import com.sktechx.godmusic.personal.rest.model.dto.ImageDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendArtistDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.artist.ArtistPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.chart.ChartPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 설명 : 로그인 사용자 패널 생성기
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 24.
 */
@Slf4j
public abstract class PanelSignAssembly extends PanelAssembly{

    public List<Panel> assembleRecommendPanel(PersonalPhaseMeta personalPhaseMeta){
        //STEP 1 : 기본 패널 생성
        final List<Panel> panelList = defaultPanelSetting(personalPhaseMeta);
        //STEP 2 : 선호 장르 및 선호 아티스트 인기곡 관련 패널
        appendPreferencePanel(personalPhaseMeta,panelList);
        //SETP 3 : TOP100, KIDS 패널
        appendPreferenceChartPanel(personalPhaseMeta,panelList);
        return panelList;
    }

    protected abstract void appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList);
    protected void appendPreferArtistPanel(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList , int index){
        Long rcmmdArtistId = personalPhaseMeta.getRecommendPersonalPanelRcmmdId(RecommendPanelContentType.RC_ATST_TR);
        if(rcmmdArtistId != null){
            RecommendArtistDto recommendArtistDto = recommendMapper.selectRecommendArtistById(rcmmdArtistId);
            if(recommendArtistDto != null){
                try{
                    panelList.add(index,new ArtistPanel(RecommendPanelType.ARTIST_POPULAR_TRACK, recommendArtistDto));
                }catch(Exception e){
                    log.error("VisitPhasePanel appendPreferencePanel artistPanel create error : {}",e.getMessage());
                }
            }
        }
    }
    private void appendPreferenceChartPanel(PersonalPhaseMeta personalPhaseMeta,final List<Panel> panelList){
        Optional.ofNullable(personalPhaseMeta.getPreferGenreList()).orElse(Collections.emptyList()).stream()
            .forEach(preferGenreDto -> {
                if("TOP100".equals(preferGenreDto.getPreferGenreType())){
                    //TODO : meta API호출 필요
                    List<ImageDto> bgImgList = recommendPanelService.getPanelBackgroundImageList(RecommendPanelType.LIVE_CHART , personalPhaseMeta.getOsType());
                    ChartDto liveChartDto = chartMapper.selectLiveChart();

                    if(liveChartDto != null){
                        try{
                            panelList.add(0,new ChartPanel(RecommendPanelType.LIVE_CHART,liveChartDto,bgImgList));
                        }catch(Exception e){
                            log.error("VisitPhasePanel liveChartPanel create error : {}",e.getMessage());
                        }
                    }
                }else if("KIDS".equals(preferGenreDto.getPreferGenreType())){
                    //TODO : meta API호출 필요
                    ChartDto kidsChartDto = chartMapper.selectKidsChart();
                    List<ImageDto> bgImgList = recommendPanelService.getPanelBackgroundImageList(RecommendPanelType.KIDS_CHART , personalPhaseMeta.getOsType());

                    if(kidsChartDto!= null){
                        try{
                            panelList.add(new ChartPanel(RecommendPanelType.KIDS_CHART, kidsChartDto ,bgImgList ));
                        }catch(Exception e){
                            log.error("VisitPhasePanel liveChartPanel create error : {}",e.getMessage());
                        }
                    }
                }
            });

    }
}
