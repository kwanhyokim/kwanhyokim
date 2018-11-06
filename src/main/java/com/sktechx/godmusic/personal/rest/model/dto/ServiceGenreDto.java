/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.dto;

import lombok.Data;

import java.nio.channels.Channel;
import java.util.List;

/**
 * 설명 : 서비스 장르 DTO
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 14.
 */
@Data
public class ServiceGenreDto {
    private Long svcGenreId;
    private String svcGenreNm;

    public ServiceGenreDto(){
    }
    public ServiceGenreDto(Long svcGenreId , String svcGenreNm ){
        this.svcGenreId = svcGenreId;
        this.svcGenreNm = svcGenreNm;
    }
}
