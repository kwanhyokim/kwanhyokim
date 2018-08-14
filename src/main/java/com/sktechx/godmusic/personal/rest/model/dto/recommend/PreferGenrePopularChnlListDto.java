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
 * 설명 : 선호 장르 인기채널 아이디 리스트
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 08. 10.
 */
@Data
public class PreferGenrePopularChnlListDto {
    private Long preferGenreId;
    private Long svcGenreId;
    private List<Long> chnlIdList;
}
