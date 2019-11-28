/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.client.model;

import com.sktechx.godmusic.lib.domain.code.YnType;
import lombok.Data;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 11. 28.
 */
@Data
public class OneTimeUrlDto {

    private String url;
    private YnType fullYn;
    private YnType freeYn;
    private YnType adultYn;
    private Long updDt;
    private Long contentUpdateTime;
    private String nonce;
    private String signature;
    private String loudness;
    private String svcCd;
}
