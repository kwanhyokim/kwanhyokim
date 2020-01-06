/*
 * Copyright (c) 2019 Dreamus.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus.
 *
 * @project personal-api
 * @author dave(djin.chung@sk.com)
 * @date 19. 12. 19. 오후 2:29
 */

package com.sktechx.godmusic.personal.rest.controller.v1;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.service.PushService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 설명 : 고객이 PUSH 받은 이후 서버로 여러가지 action 을 취할때 사용하는 controller
 *
 * @author dave(djin.chung@sk.com)
 * @date 2019. 12. 19.
 */


@Api(value = "PUSH 관련 API 제공")
@Slf4j
@Validated
@RestController
@RequestMapping(Naming.serviceCode+"/v1/push")
public class PushController {

    @Autowired
    PushService pushService;

    /**
     * 베스트 나인 이벤트는 회원을 대상으로한 이벤트 이며, 해당 API는 회원의 eventDate 에 해당하는
     * 베스트 나인 청취 트랙의 목록을 제공한다.
     */
    @ApiOperation(value = "PUSH Click 이벤트 수집", httpMethod = "POST", notes = "사용자가 PUSH를 받고 클릭했을 경우 서버에 클릭 이벤트를 기록함")
    @PostMapping("/click/{clickId}")
    public CommonApiResponse pushClickLog(
            @PathVariable(name = "clickId") Long clickId) {
        pushService.clickLog(clickId);
        return CommonApiResponse.emptySuccess();
    }
}

