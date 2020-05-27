/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.service;

import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEvent;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEventTarget;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEventType;
import com.sktechx.godmusic.personal.common.amqp.service.AmqpService;
import com.sktechx.godmusic.personal.common.amqp.service.ListenLogAmqpService;
import com.sktechx.godmusic.personal.common.domain.type.AppNameType;
import com.sktechx.godmusic.personal.common.domain.type.ResourceLogType;
import com.sktechx.godmusic.personal.common.domain.type.SourceType;
import com.sktechx.godmusic.personal.rest.model.dto.listen.SourcePlayLog;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ResourcePlayLogRequestParam;

import java.util.Optional;

/**
 * 설명 : Track에만 국한되는 공통 메소드를 갖고 있는 추상 클래스
 *
 * @author groot
 * @since 2020. 01. 09
 */
public abstract class AbstractRelatedTrackResourcePlayLogService implements ResourcePlayLogService {

    protected AmqpService amqpService;
    protected ListenLogAmqpService listenLogAmqpService;

    public AbstractRelatedTrackResourcePlayLogService(AmqpService amqpService,
                                                      ListenLogAmqpService listenLogAmqpService) {
        this.amqpService = amqpService;
        this.listenLogAmqpService = listenLogAmqpService;
    }

    /**
     * 곡 청취로그 기본 규격 builder
     */
    protected final SourcePlayLog.SourcePlayLogBuilder createBasicSourcePlayLogBuilder(GMContext gmContext,
                                                                                       ResourcePlayLogRequestParam logRequestParam) {
        return SourcePlayLog.builder()
                .playChnl(AppNameType.parseFromCodeToString(gmContext.getAppName()))
                .sessionId(logRequestParam.getSessionId())
                .timeMillis(System.currentTimeMillis())
                .memberNo(gmContext.getMemberNo())
                .characterNo(gmContext.getCharacterNo())
                .sourceType(SourceType.fromCode(logRequestParam.getSourceType()))
                .trackId(logRequestParam.getResourceId())
                .sourceId(logRequestParam.getResourceId().toString())
                .logType(logRequestParam.getLogType())
                .bitrate(logRequestParam.getQuality())
                .quality(logRequestParam.getQuality())
                .trackTotTm(logRequestParam.getRunningTimeSecs())
                .elapsedTm(logRequestParam.getDuration())
                .osType(logRequestParam.getOsTypeToStr())
                .dvcId(gmContext.getDeviceId())
                .albumId(logRequestParam.getAlbumId())
                .chnlId(logRequestParam.getChannelId())
                .chnlType(logRequestParam.getChannelType())
                .memberRcmdId(logRequestParam.getRecommendTrackId())
                .addTm(logRequestParam.getAddDateTime())
                .free(logRequestParam.isFree())
                .sessionToken("")
                .userClientIp(logRequestParam.getClientIp())
                .metaCacheUpdateDtime(logRequestParam.getMetaCachedUpdateDtime())           // 캐시드 스트리밍
                .offlineStartDtime(logRequestParam.getOfflineStartDtime())                  // 캐시드 스트리밍
                .playOfflineYn(logRequestParam.getPlayOfflineYn())                          // 캐시드 스트리밍
                .playCacheYn(logRequestParam.getPlayCachedYn())                             // 캐시드 스트리밍
                .traceType(logRequestParam.getTraceType())
                .appVersion(gmContext.getAppVer())
                .buildNumber(logRequestParam.getBuildNumber());
    }

    /**
     * 곡(STRM) 청취 UserEvent MQ 발송
     */
    @Override
    public void deliverResourceUserEvent(GMContext gmContext, ResourcePlayLogRequestParam logRequestParam) {
        // playOfflineYn == N 이고 userEvent가 존재할 때만 UserEvent를 남긴다.
        if (YnType.Y != logRequestParam.getPlayOfflineYn()) {
            this.createUserEventByTrack(gmContext, logRequestParam).ifPresent(amqpService::deliverUserEvent);
        }
    }

    /**
     * 곡 UserEvent 생성
     */
    private Optional<UserEvent> createUserEventByTrack(GMContext gmContext, ResourcePlayLogRequestParam logRequestParam) {
        UserEventType userEventType = UserEventType.fromPlayLogType(ResourceLogType.fromCode(logRequestParam.getLogType()));
        return UserEventType.UNKNOWN == userEventType ?
                Optional.empty() :
                Optional.of(UserEvent.newBuilder()
                        .playChnl(AppNameType.parseFromCodeToString(gmContext.getAppName()))
                        .event(userEventType)
                        .memberNo(gmContext.getMemberNo())
                        .charactorNo(gmContext.getCharacterNo())
                        .targetId(logRequestParam.getResourceId().toString())
                        .targetType(UserEventTarget.TRACK)
                        .sourceType(SourceType.fromCode(logRequestParam.getSourceType()))
                        .trackTotTm(logRequestParam.getRunningTimeSecs())
                        .elapsedTm(logRequestParam.getDuration())
                        .timeMillis(System.currentTimeMillis())
                        .build());
    }

}
