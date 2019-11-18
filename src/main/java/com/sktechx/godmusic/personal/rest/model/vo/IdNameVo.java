/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.model.vo;

import lombok.*;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 11. 14.
 */
@Getter
public class IdNameVo {

    private Long id;
    private String name;

    public IdNameVo() {
    }

    public IdNameVo(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
