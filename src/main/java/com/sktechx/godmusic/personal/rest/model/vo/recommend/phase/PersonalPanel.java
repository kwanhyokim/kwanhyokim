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

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import lombok.Data;

/**
 * 설명 : 개인화 패널 정보
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 18.
 */
@Data
public class PersonalPanel {
    private RecommendPanelContentType recommendPanelContentType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date avaliableDateTime;
    private int dispSn;
    //패널 별 추천 아이디
    private Long recommendId;

    //청취 무드 무드 아이디
    private Long moodId;


    @JsonIgnore
    private Date createDtime;
}
