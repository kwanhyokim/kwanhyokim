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
import com.sktechx.godmusic.personal.rest.model.vo.listen.SettlementToken;
import com.sktechx.godmusic.personal.rest.model.vo.listen.play.ResourcePlayLogRequestParam;
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
        return SourceType.VIDEO_MV;
    }

    /**
     * 비디오 재생(청취) 로그 MQ 발송
     */
    @Override
    public void deliverResourcePlayLog(GMContext gmContext, ResourcePlayLogRequestParam param) {
        SourcePlayLog sourcePlayLog = this.buildBasicSourcePlayLogByVideo(gmContext, param);

        SourcePlayLog.SourcePlayLogBuilder sourcePlayLogBuilder = sourcePlayLog.toBuilder();

        ResourceLogType resourceLogType = ResourceLogType.fromCode(param.getLogType());
        if (ResourceLogType.ONEMIN == resourceLogType) {
            sourcePlayLogBuilder = this.buildOneMinVideoPlayLog(param, sourcePlayLogBuilder);
        }

        amqpService.deliverSourcePlay(sourcePlayLogBuilder.build());
        log.info("[RESOURCE 청취로그 MQ 발송] listen = {}", sourcePlayLogBuilder.toString());
    }

    /**
     * 비디오 재생(청취) UserEvent MQ 발송
     */
    @Override
    public void deliverResourceUserEvent(GMContext gmContext, ResourcePlayLogRequestParam param) {
        UserEventType userEventType = UserEventType.fromPlayLogType(ResourceLogType.fromCode(param.getLogType()));
        if (UserEventType.UNKNOWN != userEventType) {
            UserEvent userEvent = UserEvent.newBuilder()
                    .playChnl(AppNameType.parseToString(gmContext.getAppName()))
                    .event(userEventType)
                    .memberNo(gmContext.getMemberNo())
                    .charactorNo(gmContext.getCharacterNo())
                    .targetId(String.valueOf(param.getResourceId()))
                    .targetType(UserEventTarget.VIDEO)
                    .sourceType(SourceType.fromCode(param.getSourceType()))
                    .trackTotTm(param.getRunningTimeSecs())
                    .elapsedTm(param.getDuration())
                    .timeMillis(System.currentTimeMillis())
                    .build();

            amqpService.deliverUserEvent(userEvent);
            log.info("[VIDEO 재생로그][UserEvent MQ 발송] {}", userEvent.toString());
        }
    }

    /**
     * 비디오 재생(청취)로그 기본 규격 build
     */
    private SourcePlayLog buildBasicSourcePlayLogByVideo(GMContext gmContext, ResourcePlayLogRequestParam param) {
        return SourcePlayLog.builder()
                .playChnl(AppNameType.parseToString(gmContext.getAppName()))
                .sessionId(param.getSessionId())
                .timeMillis(System.currentTimeMillis())
                .memberNo(gmContext.getMemberNo())
                .characterNo(gmContext.getCharacterNo())
                .sourceType(SourceType.fromCode(param.getSourceType()))
                .sourceId(String.valueOf(param.getResourceId()))
                .logType(param.getLogType())
                .bitrate(param.getQuality())
                .quality(param.getQuality())
                .trackTotTm(param.getRunningTimeSecs())
                .elapsedTm(param.getDuration())
                .osType(param.getOsTypeToStr())
                .dvcId(gmContext.getDeviceId())
                .chnlId(param.getChannelId())
                .chnlType(param.getChannelType())
                .memberRcmdId(null)
                .addTm(param.getAddDateTime())
                .free(param.isFree())
                .sessionToken(null)
                .userClientIp(param.getClientIp())
                .metaCacheUpdateDtime(null)
                .offlineStartDtime(null)
                .playOfflineYn(null)
                .playCacheYn(null)
                .build();
    }

    /**
     * 비디오 ONEMIN 재생(청취) 로그 만들기
     */
    private SourcePlayLog.SourcePlayLogBuilder buildOneMinVideoPlayLog(ResourcePlayLogRequestParam param,
                                                                       SourcePlayLog.SourcePlayLogBuilder sourcePlayLogBuilder) {
        SettlementToken sttToken = tokenService.parseSettlementToken(param.getSttToken());
        String serviceId = sttToken.getServiceId();
        SourceType sourceType = SourceType.fromCode(param.getSourceType());

        if (SourceType.VIDEO_MV == sourceType && StringUtils.isEmpty(serviceId)) {
            log.warn("[1분 리소스 청취로그] 정산정보 없음");
            throw new CommonBusinessException(PersonalErrorDomain.USER_PSSRL_NOT_FOUND);
        }

        return sourcePlayLogBuilder
                .pssrlCd(serviceId)
                .serviceId(serviceId)
                .prchsId(sttToken.getPurchaseId())
                .goodsId(sttToken.getGoodsId());
    }

}
