/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.controller.inner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.CommonConstant;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.RequestGMContext;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.service.PreferenceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 설명 : MSA 내부 호출용 컨트롤러
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2019-10-15
 */

@Slf4j
@RestController
@RequestMapping(Naming.serviceCode + "/v1/preferences/internal")
@Api(value = "내부용 선호 관련")

public class InternalPreferenceController {

    @Autowired
    private PreferenceService preferenceService;

    @ApiOperation(value = "좋아하는 아티스트 최신영상 캐쉬 삭제")
    @GetMapping("/video/artist/new/clearCache")
    public CommonApiResponse clearCachePreferenceVideoArtistNewList(
            @ApiIgnore @RequestGMContext GMContext ctx,
            @RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO, required = false) Long characterNo
    ){

        if(!ObjectUtils.isEmpty(characterNo)){
            preferenceService.clearCachePreferenceVideoArtistNewList(characterNo);
        }

        return CommonApiResponse.emptySuccess();
    }

    @ApiOperation(value = "좋아하는 장르 최신영상 조회")
    @GetMapping("/video/genre/new/clearCache")
    public CommonApiResponse clearCachePreferenceVideoGenreNewList(
            @ApiIgnore @RequestGMContext GMContext ctx,
            @RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO, required = false) Long characterNo
    ){

        if(!ObjectUtils.isEmpty(characterNo)){
            preferenceService.clearCachePreferenceVideoGenreNewList(characterNo);
        }

        return CommonApiResponse.emptySuccess();
    }
}
