/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.controller.v1;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.common.domain.type.AppNameType;
import com.sktechx.godmusic.personal.common.domain.type.SourceType;
import com.sktechx.godmusic.personal.common.resolver.ResourcePlayLogResolver;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenRequest;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenTrackRequest;
import com.sktechx.godmusic.personal.rest.model.vo.listen.SourcePlayLogGMContextVo;
import com.sktechx.godmusic.personal.rest.model.vo.video.ResourcePlayLogRequest;
import com.sktechx.godmusic.personal.rest.service.ListenService;
import com.sktechx.godmusic.personal.rest.service.ResourcePlayLogService;
import com.sktechx.godmusic.personal.rest.validate.Validator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @author Daniel
 * @author Groot
 * @date 2018. 8. 8.
 * @time PM 7:15
 */
@SuppressWarnings("rawtypes")
@Slf4j
@Api(tags = "청취 API", value = "personal/v1/listen")
@RestController
@RequestMapping(Naming.serviceCode + "/v1/listen")
public class ListenController {

    private final ListenService listenService;
    private final ResourcePlayLogResolver resourcePlayLogResolver;

    public ListenController(ListenService listenService,
                            ResourcePlayLogResolver resourcePlayLogResolver) {
        this.listenService = listenService;
        this.resourcePlayLogResolver = resourcePlayLogResolver;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @ApiOperation(value = "Resource 청취 로그", notes = "Resource 재생(청취) (ex.영상) 로그를 MQ 로 남김")
    @PostMapping("/resource")
    public CommonApiResponse addListenHistByResource(@Valid @RequestBody ResourcePlayLogRequest request,
                                                     HttpServletRequest httpServletRequest) {
        GMContext currentContext = GMContext.getContext();
        SourceType sourceType = SourceType.fromCode(request.getSourceType());
        Validator.loginValidate(currentContext);
        log.debug("[RESOURCE 청취 로그] request={}", request);

        SourcePlayLogGMContextVo sourcePlayLogGMContextVo = SourcePlayLogGMContextVo.builder()
                .playChnl(Optional.ofNullable(AppNameType.fromCode(currentContext.getAppName())).map(AppNameType::getCode).orElse(""))
                .memberNo(currentContext.getMemberNo())
                .characterNo(currentContext.getCharacterNo())
                .deviceId(currentContext.getDeviceId())
                .userClientIp(Optional.ofNullable(httpServletRequest.getHeader("client_ip")).orElse(""))
                .build();

        // TODO Resolver 적용
        ResourcePlayLogService resolver = resourcePlayLogResolver.findResolver(sourceType).get();
        log.info("[RESOURCE] Resolver에 의해 DI된 Service={}", resolver);
        resolver.deliverResourcePlayLog(sourcePlayLogGMContextVo, request);

//        listenService.addPlayHistoryByResource(request, currentContext, httpServletRequest);    // 기존
        return CommonApiResponse.emptySuccess();
    }

    @ApiOperation(value = "곡 청취 로그 (기존 /v2/user/log/track POST)", notes = "곡 재생시점에 따른 재생 로그를 MQ로 남김")
    @PostMapping("/track")
    public CommonApiResponse addListenHistByTrack(HttpServletRequest httpServletRequest,
                                                  @Valid @RequestBody ListenTrackRequest request) {
        GMContext currentContext = GMContext.getContext();
        Validator.loginValidate(currentContext);
        SourcePlayLogGMContextVo sourcePlayLogGMContextVo = SourcePlayLogGMContextVo.builder()
                .playChnl(Optional.ofNullable(AppNameType.fromCode(currentContext.getAppName())).map(AppNameType::getCode).orElse(""))
                .memberNo(currentContext.getMemberNo())
                .characterNo(currentContext.getCharacterNo())
                .deviceId(currentContext.getDeviceId())
                .build();

        // TODO 신규 Service로 컨버팅 작업

        listenService.addListenHistByTrack(request, currentContext, httpServletRequest);
        return CommonApiResponse.emptySuccess();
    }

    @ApiOperation(value = "채널 청취 로그 (기존 /v2/user/log/channel POST)", notes = "채널 전체 재생시 로그를 DB로 남김")
    @PostMapping("/channel")
    public CommonApiResponse addListenHistByChannel(@RequestBody ListenRequest request) {
        GMContext currentContext = GMContext.getContext();
        Validator.loginValidate(currentContext);

        listenService.addListenHistByChannel(request, currentContext.getMemberNo(), currentContext.getCharacterNo());
        return CommonApiResponse.emptySuccess();
    }

}
