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
import com.sktechx.godmusic.personal.common.amqp.domain.UserEvent;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEventTarget;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEventType;
import com.sktechx.godmusic.personal.common.amqp.service.AmqpService;
import com.sktechx.godmusic.personal.common.domain.type.AppNameType;
import com.sktechx.godmusic.personal.common.domain.type.ResourceLogType;
import com.sktechx.godmusic.personal.common.domain.type.SourceType;
import com.sktechx.godmusic.personal.rest.model.dto.listen.SourcePlayLog;
import com.sktechx.godmusic.personal.rest.model.vo.listen.play.ResourcePlayLogRequestParam;
import com.sktechx.godmusic.personal.rest.service.TrackListenLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 설명 : 곡(STRM, DRM) 청취 로그 관련 공통 로직
 *
 * @author groot
 * @since 2020. 01. 02
 */
@Slf4j
@Service
public class TrackListenLogServiceImpl implements TrackListenLogService {

    private final AmqpService amqpService;

    public TrackListenLogServiceImpl(AmqpService amqpService) {
        this.amqpService = amqpService;
    }

    /**
     * 곡 청취로그 기본 규격 build
     */
    @Override
    public SourcePlayLog buildBasicSourcePlayLogByTrack(GMContext gmContext, ResourcePlayLogRequestParam param) {
        return SourcePlayLog.builder()
                .playChnl(AppNameType.parseToString(gmContext.getAppName()))
                .sessionId(param.getSessionId())
                .timeMillis(System.currentTimeMillis())
                .memberNo(gmContext.getMemberNo())
                .characterNo(gmContext.getCharacterNo())
                .sourceType(SourceType.fromCode(param.getSourceType()))
                .trackId(param.getResourceId())
                .sourceId(String.valueOf(param.getResourceId()))
                .logType(param.getLogType())
                .bitrate(param.getQuality())
                .quality(param.getQuality())
                .trackTotTm(param.getRunningTimeSecs())
                .elapsedTm(param.getDuration())
                .osType(param.getOsTypeToStr())
                .dvcId(gmContext.getDeviceId())
                .albumId(param.getAlbumId())
                .chnlId(param.getChannelId())
                .chnlType(param.getChannelType())
                .memberRcmdId(param.getRecommendTrackId())
                .addTm(param.getAddDateTime())
                .free(param.isFree())
                .sessionToken("")
                .userClientIp(param.getClientIp())
                .ownerToken(param.getOwnerToken())
                .metaCacheUpdateDtime(param.getMetaCacheUpdateDtime())        // 캐시드 스트리밍
                .offlineStartDtime(param.getOfflineStartDtime())              // 캐시드 스트리밍
                .playOfflineYn(param.getPlayOfflineYn())                      // 캐시드 스트리밍
                .playCacheYn(param.getPlayCacheYn())                          // 캐시드 스트리밍
                .build();
    }

    /**
     * 곡 청취 UserEvent MQ에 발송
     */
    @Override
    public void deliverUserEventByTrackListenLog(GMContext gmContext, ResourcePlayLogRequestParam param) {
        UserEventType userEventType = UserEventType.fromPlayLogType(ResourceLogType.fromCode(param.getLogType()));
        if (UserEventType.UNKNOWN != userEventType) {
            UserEvent userEvent = UserEvent.newBuilder()
                    .playChnl(AppNameType.parseToString(gmContext.getAppName()))
                    .event(userEventType)
                    .memberNo(gmContext.getMemberNo())
                    .charactorNo(gmContext.getCharacterNo())
                    .targetId(String.valueOf(param.getResourceId()))
                    .targetType(UserEventTarget.TRACK)
                    .sourceType(SourceType.fromCode(param.getSourceType()))
                    .trackTotTm(param.getRunningTimeSecs())
                    .elapsedTm(param.getDuration())
                    .timeMillis(System.currentTimeMillis())
                    .build();

            amqpService.deliverUserEvent(userEvent);
            log.info("[TRACK 청취로그][UserEvent MQ 발송] {}", userEvent.toString());
        }
    }

}
