/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.model.dto.chart;

import java.util.List;

import lombok.Data;

/**
 * 설명 :
 *
 * @author N/A
 * @date 2020. 04. 07.
 */
@Data
public class ChartTrackTasteMixDto {

    private String mixYn;
    private String status;
    private String displayMessage;

    private boolean requireReordering;

    private List<ChartTrackTasteMixTrackDto> trackList;

}
