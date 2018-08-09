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

import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import lombok.Data;

/**
 * 설명 : 선호 장르 인기채널
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 27.
 */
@Data
public class PreferGenrePopularChnlDto {

    private Long characterNo;
    private Long preferGenreId;
    private Long chnlId;

}
