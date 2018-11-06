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

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 설명 : 선호 노출 Dto
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 8. 17.
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CharacterPreferDispDto {
    private Long characterNo;
    private Long preferDispId;
    private String preferDispNm;
    private String dispType;
    private String dispPropsType;
}
