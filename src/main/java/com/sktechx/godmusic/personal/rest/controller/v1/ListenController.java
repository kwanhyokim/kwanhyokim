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
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.common.domain.type.SourceType;
import com.sktechx.godmusic.personal.common.exception.PersonalErrorDomain;
import com.sktechx.godmusic.personal.common.resolver.ResourcePlayLogResolver;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenRequest;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenTrackRequest;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ResourcePlayLogRequestParam;
import com.sktechx.godmusic.personal.rest.service.ListenService;
import com.sktechx.godmusic.personal.rest.validate.Validator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 청취 로그 관련 Controller
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @author Daniel
 * @author Groot
 */
@SuppressWarnings("rawtypes")
@Slf4j
@Api(tags = "청취 API", value = "personal/v1/listen")
@RestController
@RequestMapping(Naming.serviceCode + "/v1/listen")
@Validated
public class ListenController {

    private final ListenService listenService;
    private final ResourcePlayLogResolver resourcePlayLogResolver;

    public ListenController(ListenService listenService,
                            ResourcePlayLogResolver resourcePlayLogResolver) {
        this.listenService = listenService;
        this.resourcePlayLogResolver = resourcePlayLogResolver;
    }

    @ApiOperation(value = "Resource Bulk 청취 로그", notes = "Resource 재생(청취) Bulk로 받는 api (For Cached Streaming)")
    @PostMapping("/resource/list")
    public CommonApiResponse addBulkCachedListenHistByResource(
            @RequestBody @Size(min = 1, max = 1000) List<ResourcePlayLogRequestParam> logRequestParamList) {
        GMContext gmContext = GMContext.getContext();
        Validator.loginValidate(gmContext);

        try {
            logRequestParamList.forEach(logRequestParam -> {
                resourcePlayLogResolver.findResolver(SourceType.fromCode(logRequestParam.getSourceType())).ifPresent(service -> {
                    log.debug("[RESOURCE] Resolver에 의해 DI된 Service={}", service.getClass().getName());
                    service.deliverResourcePlayLog(gmContext, logRequestParam);
                    service.deliverResourceUserEvent(gmContext, logRequestParam);
                });
            });

        } catch (Exception e) {
            log.error("Bulk Cached Streaming Log Fail", e);
            throw new CommonBusinessException(PersonalErrorDomain.FAIL_BULK_CACHED_STREAMING_PROCESS);
        }

        return CommonApiResponse.emptySuccess();
    }

    @ApiOperation(value = "Resource 청취 로그", notes = "Resource 재생(청취) (ex.영상) 로그를 MQ 로 남김")
    @PostMapping("/resource")
    public CommonApiResponse addListenHistByResource(@Valid @RequestBody ResourcePlayLogRequestParam logRequestParam) {
        GMContext gmContext = GMContext.getContext();
        Validator.loginValidate(gmContext);
        log.debug("[RESOURCE 청취 로그] logRequestParam={}", logRequestParam);

        // Resolver 적용
        resourcePlayLogResolver.findResolver(SourceType.fromCode(logRequestParam.getSourceType())).ifPresent(service -> {
            log.debug("[RESOURCE] Resolver에 의해 DI된 Service={}", service.getClass().getName());
            service.deliverResourcePlayLog(gmContext, logRequestParam);
            service.deliverResourceUserEvent(gmContext, logRequestParam);
        });

        return CommonApiResponse.emptySuccess();
    }

    @ApiOperation(value = "곡 청취 로그 (기존 /v2/user/log/track POST)", notes = "곡 재생시점에 따른 재생 로그를 MQ로 남김")
    @PostMapping("/track")
    public CommonApiResponse addListenHistByTrack(HttpServletRequest httpServletRequest,
                                                  @Valid @RequestBody ListenTrackRequest listenTrackRequest) {
        GMContext gmContext = GMContext.getContext();
        Validator.loginValidate(gmContext);

        ResourcePlayLogRequestParam playLogRequestParam = ResourcePlayLogRequestParam.builder()
                .resourceId(listenTrackRequest.getTrackId())
                .sourceType(listenTrackRequest.getSourceTypeToStr())
                .logType(listenTrackRequest.getTrackLogTypeToStr())
                .osType(listenTrackRequest.getOsType())
                .quality(listenTrackRequest.getBitrateToStr())
                .duration(listenTrackRequest.getTrackTotalSec())
                .runningTimeSecs(listenTrackRequest.getElapsedSec())
                .freeYn(listenTrackRequest.getFreeYn())
                .playOfflineYn(listenTrackRequest.getPlayOfflineYn())
                .playCachedYn(listenTrackRequest.getPlayCacheYn())
                .sessionId(listenTrackRequest.getListenSessionId())
                .albumId(listenTrackRequest.getAlbumId())
                .channelId(listenTrackRequest.getChannelId())
                .channelType(listenTrackRequest.getChannelType())
                .sttToken(listenTrackRequest.getSttToken())
                .ownerToken(listenTrackRequest.getOwnerToken())
                .cachedStreamingToken(listenTrackRequest.getCachedToken())
                .freeCachedStreamingToken(listenTrackRequest.getFreeCachedStreamingToken())
                .recommendTrackId(listenTrackRequest.getRecommendTrackId())
                .addDateTime(listenTrackRequest.getAddDateTime())
                .offlineStartDtime(listenTrackRequest.getOfflineStartDtime())
                .metaCachedUpdateDtime(listenTrackRequest.getMetaCacheUpdateDtime())
                .build();

        log.debug("{}", playLogRequestParam);
        return this.addListenHistByResource(playLogRequestParam);
    }

    @ApiOperation(value = "채널 청취 로그 (기존 /v2/user/log/channel POST)", notes = "채널 전체 재생시 로그를 DB로 남김")
    @PostMapping("/channel")
    public CommonApiResponse addListenHistByChannel(@RequestBody ListenRequest channelListenRequest) {
        GMContext gmContext = GMContext.getContext();
        Validator.loginValidate(gmContext);

        listenService.addListenHistByChannel(channelListenRequest, gmContext.getMemberNo(), gmContext.getCharacterNo());
        return CommonApiResponse.emptySuccess();
    }

}
