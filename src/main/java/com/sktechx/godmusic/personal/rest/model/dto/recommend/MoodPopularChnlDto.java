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

import lombok.Data;

import java.util.List;

/**
 * 설명 : 분위기 인기 채널 아이디 리스트
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 08. 11.
 */
@Data
public class MoodPopularChnlDto {
    private Long categoryId;
    private Long chnlId;

    public MoodPopularChnlDto(){

    }

    public MoodPopularChnlDto(Long categoryId, Long chnlId){
        this.categoryId = categoryId;
        this.chnlId = chnlId;
    }
}
