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

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.type.PersonalPhaseType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferDispDto;
import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferGenreDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import static com.sktechx.godmusic.personal.common.domain.type.PersonalPhaseType.*;
import static com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType.RC_CF_TR;
import static com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType.RC_SML_TR;
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

    // 선호 장르
    private List<CharacterPreferGenreDto> preferGenreList;
    // 선호 노출
    private List<CharacterPreferDispDto> preferDispList;
    // 추천 단계
    private List<PersonalPhase> personalPhaseList;
    // 개인 패널
    private List<PersonalPanel> rcmmdPanelList;
    // 추천 패널에 노출되는 채널 아이디 리스트
    private List<Long> rcmmdPanelDispChnlIdList;

    // aflo 캐릭터 만기일시
    private Date afloCharacterExpireDtime;
    // aflo 채널 최신생성일
    private Date afloChnlRecentCreateDtime;

    // 현재 앱 버전
    @JsonIgnore
    private String appVer;

    public void setRcmmdPanelList(List<PersonalPanel> rcmmdPanelList){
        this.rcmmdPanelList = rcmmdPanelList;
        initPersonalPhaseList(rcmmdPanelList);
    }

    public PersonalPhaseType getFirstPhaseType(){
        if(!CollectionUtils.isEmpty(personalPhaseList)){
            return  personalPhaseList
                    .stream()
                    .filter(personalPhaseTemp -> Objects.nonNull(personalPhaseTemp.getPhaseType()))
                    .map(PersonalPhase::getPhaseType)
                    .max(Comparator.comparing( PersonalPhaseType::getPhase))
                    .orElse(PersonalPhaseType.GUEST);
        }
        return PersonalPhaseType.GUEST;
    }

    public PersonalPanel getRecommendPersonalPanelTopItem() {

        if(!CollectionUtils.isEmpty(rcmmdPanelList) && rcmmdPanelList.size() > 0) {
            return rcmmdPanelList.get(0);
        }

        return null;
    }

    @JsonIgnore
    public boolean isPreferGenreListPresent(){
         return Optional.ofNullable(this.preferGenreList).isPresent();
    }

    public List<Long> getRecommendPersonalPanelRcmmdIdList(RecommendPanelContentType recommendPanelContentType) {
        if(!CollectionUtils.isEmpty(rcmmdPanelList)) {
            return rcmmdPanelList.stream()
                    .filter(personalPanel ->
                            Objects.nonNull(personalPanel) && recommendPanelContentType.equals(personalPanel.getRecommendPanelContentType()))
                    .sorted(
                            Comparator.comparing(PersonalPanel::getRecommendId,
                                    Comparator.naturalOrder()).reversed()
                                    .thenComparing(PersonalPanel::getDispSn,
                                            Comparator.naturalOrder()))
                    .map(PersonalPanel::getRecommendId)
                    .distinct()
                    .collect(Collectors.toList());
        }
        return null;
    }

    public Long getRecommendPersonalPanelRcmmdId(RecommendPanelContentType recommendPanelContentType){
        PersonalPanel personalPanel = getRecommendPersonalPanel(recommendPanelContentType);
        if(personalPanel != null && personalPanel.getRecommendId() != null){
            return personalPanel.getRecommendId();
        }
        return null;
    }

    public List<PersonalPanel> getRecommendPersonalPanelList(RecommendPanelContentType recommendPanelContentType){
        if(!CollectionUtils.isEmpty(rcmmdPanelList)){
            return rcmmdPanelList
                    .stream()
                    .filter(personalPanel -> Objects.nonNull(personalPanel)
                            && recommendPanelContentType.equals(personalPanel.getRecommendPanelContentType())
                    )
                    .collect(Collectors.toList());
        }

        return null;
    }

    public List<Long> getPreferGenreAllIdList(){

        if(!CollectionUtils.isEmpty(preferGenreList)){
            return preferGenreList
                    .stream()
                    .filter(Objects::nonNull)
                    .map(CharacterPreferGenreDto::getPreferGenreId)
                    .distinct()
                    .collect(Collectors.toList());
        }
        return null;
    }
    public List<Long> getPreferGenreIdList(int limitSize){
        if(!CollectionUtils.isEmpty(preferGenreList)){
            return preferGenreList
                    .stream()
                    .filter(Objects::nonNull)
                    .map(CharacterPreferGenreDto::getPreferGenreId)
                    .distinct()
                    .limit(limitSize)
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<Long> getListenMoodIdList(RecommendPanelContentType recommendPanelContentType , int limitSize){
        List<PersonalPanel> personalPanelList = getRecommendPersonalPanelList(recommendPanelContentType);
        if(!CollectionUtils.isEmpty(personalPanelList)){
            return personalPanelList
                    .stream()
                    .filter(Objects::nonNull)
                    .map(PersonalPanel::getMoodId)
                    .distinct()
                    .limit(limitSize)
                    .collect(Collectors.toList());
        }

        return null;
    }

    public void removeRecommendPersonalPanel(RecommendPanelContentType recommendPanelContentType ,Long rcmmdId){
        if(!CollectionUtils.isEmpty(rcmmdPanelList)){
            rcmmdPanelList.removeIf(personalPanel -> personalPanel.getRecommendPanelContentType()
                    .equals(recommendPanelContentType) && personalPanel.getRecommendId()
                    .equals(rcmmdId));


        }
    }
    private PersonalPanel getRecommendPersonalPanel(RecommendPanelContentType recommendPanelContentType){
        if(!CollectionUtils.isEmpty(rcmmdPanelList)){
            return rcmmdPanelList.stream().filter(personalPanel -> Objects.nonNull(personalPanel)
                    && recommendPanelContentType
                    .equals(personalPanel.getRecommendPanelContentType()))
                    .min(Comparator.comparing(PersonalPanel::getDispSn, Integer::compareTo))
                    .orElse(null);
        }

        return null;
    }
    private void initPersonalPhaseList(List<PersonalPanel> rcmmdPanelList){
        personalPhaseList = new ArrayList<>();
        personalPhaseList.add(new PersonalPhase( VISIT , new Date()));
        //추천 단계 설정
        if(!CollectionUtils.isEmpty(rcmmdPanelList)){
            PersonalPanel recommendPanel = getRecommendPersonalPanel(RC_CF_TR);
            if(recommendPanel != null){
                personalPhaseList.add(new PersonalPhase( RECOMMEND , recommendPanel.getAvaliableDateTime()));
            }
            PersonalPanel listenPanel = getRecommendPersonalPanel(RC_SML_TR);
            if(listenPanel != null){
                personalPhaseList.add(new PersonalPhase( LISTEN , listenPanel.getAvaliableDateTime()));
            }
        }
    }

}
