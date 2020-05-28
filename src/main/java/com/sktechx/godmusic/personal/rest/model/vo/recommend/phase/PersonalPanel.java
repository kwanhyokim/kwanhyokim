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
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 설명 : 개인화 패널 정보
 */
@EqualsAndHashCode(of={"recommendId"})
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date createDtime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date dispStdStartDt;

    private OsType exceptionalOsType;
    private String exceptionalAppVersion;

}
