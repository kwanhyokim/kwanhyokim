/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.controller.v1;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2020. 01. 23.
 */
@Api(value = "Ping", description = "Ping API - Daniel")
@Slf4j
@RestController
@RequestMapping(value = Naming.serviceCode + "/v1/ping")
public class PingController {


    @ApiOperation(value = "API Ping", httpMethod = "GET", response = CommonApiResponse.class)
    @GetMapping
    public CommonApiResponse<Void> ping() {
        return new CommonApiResponse<>(null);
    }

}
