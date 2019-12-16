/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.vo.recommend;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import lombok.Builder;
import lombok.Data;

/**
 * 설명 : 추천 패널 API 응답
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecommendPanelResponse {
    List<Panel> list;
    Integer mostRecentPanelIndex;

    @JsonProperty("updateDateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date updateDtime;

    @JsonIgnore
    private OsType osType;

    public void setList(List<Panel> recommendPanelList){

        Optional.ofNullable(recommendPanelList)
                .ifPresent(
                    panels -> {

                        for(Panel panel : panels){
                            panel.getContent().setOsType(osType);
                        }

                        recommendPanelList.stream().max(
                                Comparator.comparing(o -> o.getContent().getCreateDtime()))
                                .ifPresent(

                                        panel -> {
                                            if(RecommendPanelType.POPULAR_CHANNEL.equals(panel.getType()) ||
                                                    RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL.equals(panel.getType())
                                            ){
                                                this.mostRecentPanelIndex = 0;
                                                updateDtime = new Date();

                                            }else{
                                                mostRecentPanelIndex = recommendPanelList.indexOf(panel);
                                                updateDtime = panel.getContent().getCreateDtime();
                                            }

                                        }

                                );

                        this.list = recommendPanelList;
                    }
        );
    }
}
