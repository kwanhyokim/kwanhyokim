/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.dto.recommend;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import lombok.Data;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 정덕진(Deockjin Chung)/Music사업팀/SKTECH(djin.chung@sk.com)
 * @date 2018.07.03
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyMostTrackDto extends TrackDto {
    private Long memberNo;

    private Integer listenCount;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date lastListenDateTime;

    private YnType likeYn;
}
