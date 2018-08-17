/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.vo.recommend.phase;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.type.PersonalPhaseType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferDispDto;
import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferGenreDto;
import com.sktechx.godmusic.personal.rest.model.dto.PreferGenreDto;
import com.sktechx.godmusic.personal.rest.model.vo.preference.Chart;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 설명 : 개인화 콘텐츠 메타 캐쉬 데이터 ( 단계, 추천 패널 )
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 18.
 */
@Data
@Slf4j
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PersonalPhaseMeta {

    protected Long characterNo;
    protected OsType osType;

    //선호 장르
    private List<CharacterPreferGenreDto> preferGenreList;
    //선호 노출
    private List<CharacterPreferDispDto> preferDispList;

    //추천 단계
    private List<PersonalPhase> personalPhaseList;
    //개인화 추천 패널
    private List<PersonalPanel> rcmmdPanelList;


    public void setRcmmdPanelList(List<PersonalPanel> rcmmdPanelList){
        this.rcmmdPanelList = rcmmdPanelList;

        this.personalPhaseList = new ArrayList<>();
        personalPhaseList.add(new PersonalPhase(PersonalPhaseType.VISIT , new Date()));
        //추천 단계 설정
        if(!CollectionUtils.isEmpty(rcmmdPanelList)){
            PersonalPanel recommendPanel = getRecommendPersonalPanel(RecommendPanelContentType.RC_CF_TR);
            if(recommendPanel != null){
                personalPhaseList.add(new PersonalPhase(PersonalPhaseType.RECOMMEND , recommendPanel.getAvaliableDateTime()));
            }
            PersonalPanel listenPanel = getRecommendPersonalPanel(RecommendPanelContentType.RC_SML_TR);
            if(listenPanel != null){
                personalPhaseList.add(new PersonalPhase(PersonalPhaseType.LISTEN , listenPanel.getAvaliableDateTime()));
            }
        }
    }

    @ApiModelProperty(value = "개인화 단계")
    public PersonalPhaseType getFirstPhaseType(){

        if(!CollectionUtils.isEmpty(personalPhaseList)){
            PersonalPhase firstPhase = personalPhaseList
                    .stream()
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparing( PersonalPhase::getPhaseType , (phase1,phase2)->{
                        return phase2.compareTo(phase1);
                    }))
                    .findFirst()
                    .orElse(null);
            if(firstPhase != null){
                return firstPhase.getPhaseType();
            }
        }
        return PersonalPhaseType.GUEST;
    }


    public List<Long> getPreferGenreIdList(int limitSize){
        if(CollectionUtils.isEmpty(preferGenreList))
            return null;

        return preferGenreList
                    .stream()
                    .filter(Objects::nonNull)
                    .map(preferGenre->{
                        return preferGenre.getPreferGenreId();
                    })
                    .filter(Objects::nonNull)
                    .distinct()
                    .limit(limitSize)
                    .collect(Collectors.toList());
    }


    public List<Long> getRecommendPersonalPanelRcmmdIdList(RecommendPanelContentType recommendPanelContentType) {
        if(CollectionUtils.isEmpty(rcmmdPanelList))
            return null;

        return rcmmdPanelList.stream()
            .filter(personalPanel->{return Objects.nonNull(personalPanel) && recommendPanelContentType.equals(personalPanel.getRecommendPanelContentType()) ;})
            .sorted(Comparator.comparing( PersonalPanel::getDispSn , (dispSn1,dispSn2)->{
                return dispSn1.compareTo(dispSn2);
            })).map(personalPanel -> {
                return personalPanel.getRecommendId();
            })
            .filter(Objects::nonNull)
            .distinct()
            .collect(Collectors.toList());
    }

    public Long getRecommendPersonalPanelRcmmdId(RecommendPanelContentType recommendPanelContentType){
        PersonalPanel personalPanel = getRecommendPersonalPanel(recommendPanelContentType);
        if(personalPanel != null && personalPanel.getRecommendId() != null){
            return personalPanel.getRecommendId();
        }
        return null;
    }

    public List<PersonalPanel> getRecommendPersonalPanelList(RecommendPanelContentType recommendPanelContentType){
        if(CollectionUtils.isEmpty(rcmmdPanelList))
            return null;

        return rcmmdPanelList
                .stream()
                .filter(personalPanel -> Objects.nonNull(personalPanel) && recommendPanelContentType.equals(personalPanel.getRecommendPanelContentType()))
                .collect(Collectors.toList());
    }

    public List<Long> getListenMoodIdList(RecommendPanelContentType recommendPanelContentType){
        List<PersonalPanel> personalPanelList = getRecommendPersonalPanelList(recommendPanelContentType);
        if(!CollectionUtils.isEmpty(personalPanelList)){
            return rcmmdPanelList.stream().map(personalPanel -> personalPanel.getMoodId()).collect(Collectors.toList());
        }

        return null;
    }
    private PersonalPanel getRecommendPersonalPanel(RecommendPanelContentType recommendPanelContentType){
        if(CollectionUtils.isEmpty(rcmmdPanelList))
            return null;

        return rcmmdPanelList
                .stream()
                .filter(personalPanel -> Objects.nonNull(personalPanel) && recommendPanelContentType.equals(personalPanel.getRecommendPanelContentType()))
                .findFirst()
                .orElse(null);
    }
}
