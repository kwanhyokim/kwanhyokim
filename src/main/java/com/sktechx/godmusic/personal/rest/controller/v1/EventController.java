/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.controller.v1;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.RequestGMContext;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.model.vo.event.BestNineVo;
import com.sktechx.godmusic.personal.rest.service.event.BestNineEvent;
import com.sktechx.godmusic.personal.rest.validate.Validator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 설명 : 나의 이벤트 or Promotion 관련 정보 제공 API
 *       note. 향후 이벤트 서버로 분리 필요
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 11. 14.
 */
@Api(value = "MY 이벤트 관련 정보 제공 API", description = "나의 이벤트 or Promotion 관련 정보 제공 API")
@Slf4j
@Validated
@RestController
@RequestMapping(Naming.serviceCode+"/v1/events")
public class EventController {

    @Autowired
    BestNineEvent bestNineEvent;

    /**
     * 베스트 나인 이벤트는 회원을 대상으로한 이벤트 이며, 해당 API는 회원의 eventDate 에 해당하는
     * 베스트 나인 청취 트랙의 목록을 제공한다.
     */
    @ApiOperation(value = "베스트나인 이벤트 - 앨범 이미지 + 트랙 목록 조회", httpMethod = "GET")
    @GetMapping("/bestnines/{eventDate}")
    public CommonApiResponse<BestNineVo> getBestNineTracks(
            @PathVariable(name = "eventDate") @Length(min=6, max=6) String eventDate,
            @ApiIgnore @RequestGMContext GMContext gmContext) {

        Long characterNo = gmContext.getCharacterNo();
        Validator.loginValidate(gmContext);

        BestNineVo result = bestNineEvent.getBestNineTracks(characterNo, eventDate);
        if (result == null)
            return CommonApiResponse.emptySuccess();

        return new CommonApiResponse(result);
    }
}
