/*
 *
 *  * Copyright (c) 2018 SK TECHX.
 *  * All right reserved.
 *  *
 *  * This software is the confidential and proprietary information of SK TECHX.
 *  * You shall not disclose such Confidential Information and
 *  * shall use it only in accordance with the terms of the license agreement
 *  * you entered into with SK TECHX.
 *
 */

package com.sktechx.godmusic.personal.rest.controller.v1;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.RequestGMContext;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.service.MetaApiProxy;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 설명 : Feign 테스트 컨트롤러
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 7. 19.
 */
@Slf4j
@RestController
@RequestMapping(Naming.serviceCode + "/v1/feign")
public class FeignController {

    @Autowired
    private MetaApiProxy metaApiProxy;

    @ApiOperation(value = "아티스트 상세(meta-api)", httpMethod = "GET", notes = "선호 아티스트 추천 API", response = ArtistDto.class)
    @GetMapping(value = "/artists/{artistId}")
    public CommonApiResponse<ArtistDto> artists(@ApiIgnore @RequestGMContext GMContext ctx,
                                                @ApiParam(name = "artistId", required = true, value = "아티스트아이디", defaultValue = "583") @PathVariable("artistId") Long artistId) {
        return metaApiProxy.artists(artistId);
    }


}
