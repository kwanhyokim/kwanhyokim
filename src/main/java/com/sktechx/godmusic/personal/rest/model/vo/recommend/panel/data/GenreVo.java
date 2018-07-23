/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data;

import com.sktechx.godmusic.personal.rest.model.dto.ServiceGenreDto;
import lombok.Data;

/**
 * 설명 : 추천 패널에서 사용할 공통 장르
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 19.
 */
@Data
public class GenreVo {
    private Long id;
    private String name;

    public GenreVo(){
    }

    public GenreVo(ServiceGenreDto svcGenreDto){
        if(svcGenreDto != null){
            this.id = svcGenreDto.getSvcGenreId();
            this.name = svcGenreDto.getSvcGenreNm();
        }
    }
}
