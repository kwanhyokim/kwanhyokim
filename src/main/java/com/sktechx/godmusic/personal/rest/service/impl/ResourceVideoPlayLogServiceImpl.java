/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.service.impl;

import com.google.common.base.Strings;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEvent;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEventTarget;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEventType;
import com.sktechx.godmusic.personal.common.amqp.service.AmqpService;
import com.sktechx.godmusic.personal.common.domain.type.AppNameType;
import com.sktechx.godmusic.personal.common.domain.type.ResourceLogType;
import com.sktechx.godmusic.personal.common.domain.type.SourceType;
import com.sktechx.godmusic.personal.common.exception.PersonalErrorDomain;
import com.sktechx.godmusic.personal.rest.model.dto.listen.SourcePlayLog;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ResourcePlayLogRequestParam;
import com.sktechx.godmusic.personal.rest.model.vo.listen.token.SettlementToken;
import com.sktechx.godmusic.personal.rest.service.ResourcePlayLogService;
import com.sktechx.godmusic.personal.rest.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 설명 : 비디오 재생 로그 Service
 *
 * @author groot
 * @since 2019. 12. 19
 */
@Slf4j
@Service
public class ResourceVideoPlayLogServiceImpl implements ResourcePlayLogService {

    private final AmqpService amqpService;
    private final TokenService tokenService;

    public ResourceVideoPlayLogServiceImpl(AmqpService amqpService,
                                           TokenService tokenService) {
        this.amqpService = amqpService;
        this.tokenService = tokenService;
    }

    @Override
    public SourceType handleSourceType() {
        return SourceType.VIDEO;
    }

    /**
     * 비디오 재생(청취) 로그 MQ 발송
     */
    @Override
    public void deliverResourcePlayLog(GMContext gmContext, ResourcePlayLogRequestParam logRequestParam) {
        SourcePlayLog.SourcePlayLogBuilder sourcePlayLogBuilder = this.createBasicSourcePlayLogBuilder(gmContext, logRequestParam);

        ResourceLogType resourceLogType = ResourceLogType.fromCode(logRequestParam.getLogType());
        if (ResourceLogType.ONEMIN == resourceLogType) {
            sourcePlayLogBuilder = this.buildOneMinVideoPlayLog(logRequestParam, sourcePlayLogBuilder);
        }

        amqpService.deliverSourcePlay(sourcePlayLogBuilder.build());
        log.info("[RESOURCE 청취로그 MQ 발송] listen = {}", sourcePlayLogBuilder.toString());
    }

    /**
     * 비디오 재생(청취) UserEvent MQ 발송
     */
    @Override
    public void deliverResourceUserEvent(GMContext gmContext, ResourcePlayLogRequestParam logRequestParam) {
        UserEventType userEventType = UserEventType.fromPlayLogType(ResourceLogType.fromCode(logRequestParam.getLogType()));

        if (UserEventType.UNKNOWN != userEventType) {
            UserEvent userEvent = UserEvent.newBuilder()
                    .playChnl(AppNameType.parseFromCodeToString(gmContext.getAppName()))
                    .event(userEventType)
                    .memberNo(gmContext.getMemberNo())
                    .charactorNo(gmContext.getCharacterNo())
                    .targetId(logRequestParam.getResourceId().toString())
                    .targetType(UserEventTarget.VIDEO)
                    .sourceType(SourceType.fromCode(logRequestParam.getSourceType()))
                    .trackTotTm(logRequestParam.getRunningTimeSecs())
                    .elapsedTm(logRequestParam.getDuration())
                    .timeMillis(System.currentTimeMillis())
                    .build();

            amqpService.deliverUserEvent(userEvent);
            log.info("[VIDEO 재생로그][UserEvent MQ 발송] {}", userEvent.toString());
        }
    }

    /**
     * 비디오 재생(청취)로그 기본 규격 build
     */
    private SourcePlayLog.SourcePlayLogBuilder createBasicSourcePlayLogBuilder(GMContext gmContext,
                                                                               ResourcePlayLogRequestParam logRequestParam) {
        return SourcePlayLog.builder()
                .playChnl(AppNameType.parseFromCodeToString(gmContext.getAppName()))
                .sessionId(logRequestParam.getSessionId())
                .timeMillis(System.currentTimeMillis())
                .memberNo(gmContext.getMemberNo())
                .characterNo(gmContext.getCharacterNo())
                .sourceType(SourceType.fromCode(logRequestParam.getSourceType()))
                .sourceId(logRequestParam.getResourceId().toString())
                .logType(logRequestParam.getLogType())
                .bitrate(logRequestParam.getQuality())
                .quality(logRequestParam.getQuality())
                .trackTotTm(logRequestParam.getRunningTimeSecs())
                .elapsedTm(logRequestParam.getDuration())
                .osType(logRequestParam.getOsTypeToStr())
                .dvcId(gmContext.getDeviceId())
                .chnlId(logRequestParam.getChannelId())
                .chnlType(logRequestParam.getChannelType())
                .memberRcmdId(null)
                .addTm(logRequestParam.getAddDateTime())
                .free(logRequestParam.isFree())
                .sessionToken(null)
                .userClientIp(logRequestParam.getClientIp())
                .metaCacheUpdateDtime(null)
                .offlineStartDtime(null)
                .playOfflineYn(null)
                .playCacheYn(null)
                .traceType(logRequestParam.getTraceType())
                .appVersion(gmContext.getAppVer());
    }

    /**
     * 비디오 ONEMIN 재생(청취) 로그 만들기
     */
    private SourcePlayLog.SourcePlayLogBuilder buildOneMinVideoPlayLog(ResourcePlayLogRequestParam logRequestParam,
                                                                       SourcePlayLog.SourcePlayLogBuilder sourcePlayLogBuilder) {

        SettlementToken sttToken = null;
        if (!Strings.isNullOrEmpty(logRequestParam.getSttToken())) {
            sttToken = tokenService.parseSettlementToken(logRequestParam.getSttToken());
        }

        String serviceId = null;
        Long purchaseId = null;
        Long goodsId = null;

        if (null != sttToken) {
            serviceId = sttToken.getServiceId();
            purchaseId = sttToken.getPurchaseId();
            goodsId = sttToken.getGoodsId();
        }

        SourceType sourceType = SourceType.fromCode(logRequestParam.getSourceType());

        if (SourceType.VIDEO_MV == sourceType && StringUtils.isEmpty(serviceId)) {
            log.warn("[1분 리소스 청취로그] 정산정보 없음");
            throw new CommonBusinessException(PersonalErrorDomain.USER_PSSRL_NOT_FOUND);
        }

        return sourcePlayLogBuilder
                .pssrlCd(serviceId)
                .serviceId(serviceId)
                .prchsId(purchaseId)
                .goodsId(goodsId);
    }

}
