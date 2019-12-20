/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.personal.common.amqp.domain.UserEvent;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEventTarget;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEventType;
import com.sktechx.godmusic.personal.common.amqp.service.AmqpService;
import com.sktechx.godmusic.personal.common.domain.type.ResourceLogType;
import com.sktechx.godmusic.personal.common.domain.type.SourceType;
import com.sktechx.godmusic.personal.rest.model.vo.listen.SourcePlayLogGMContextVo;
import com.sktechx.godmusic.personal.rest.model.vo.video.ResourcePlayLogRequest;
import com.sktechx.godmusic.personal.rest.service.UserEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 설명 : 청취(재생) 로그에 따른 UserEvent Service
 *
 * @author groot
 * @since 2019. 12. 20
 */
@Slf4j
@Service
public class UserEventServiceImpl implements UserEventService {

    private final AmqpService amqpService;

    public UserEventServiceImpl(AmqpService amqpService) {
        this.amqpService = amqpService;
    }

    /**
     * 곡 청취에 따른 UserEvent
     */
    @Override
    public void deliverUserEventByTrackListenLog(SourcePlayLogGMContextVo sourcePlayLogGMContextVo,
                                                 ResourcePlayLogRequest request) {
        UserEventType userEventType = UserEventType.fromPlayLogType(ResourceLogType.fromCode(request.getLogType()));
        if (UserEventType.UNKNOWN != userEventType) {
            UserEvent userEvent = UserEvent.newBuilder()
                    .playChnl(sourcePlayLogGMContextVo.getPlayChnl())
                    .event(userEventType)
                    .memberNo(sourcePlayLogGMContextVo.getMemberNo())
                    .charactorNo(sourcePlayLogGMContextVo.getCharacterNo())
                    .targetId(String.valueOf(request.getResourceId()))
                    .targetType(UserEventTarget.TRACK)
                    .sourceType(SourceType.fromCode(request.getSourceType()))
                    .trackTotTm(request.getRunningTimeSecs())
                    .elapsedTm(request.getDuration())
                    .timeMillis(System.currentTimeMillis())
                    .build();

            amqpService.deliverUserEvent(userEvent);
            log.info("[TRACK 청취로그][UserEvent MQ 발송] {}", userEvent.toString());
        }
    }

    /**
     * 비디오 재생에 따른 UserEvent
     */
    @Override
    public void deliverUserEventByVideoPlayLog(SourcePlayLogGMContextVo sourcePlayLogGMContextVo,
                                               ResourcePlayLogRequest request) {
        UserEventType userEventType = UserEventType.fromPlayLogType(ResourceLogType.fromCode(request.getLogType()));
        if (UserEventType.UNKNOWN != userEventType) {
            UserEvent userEvent = UserEvent.newBuilder()
                    .playChnl(sourcePlayLogGMContextVo.getPlayChnl())
                    .event(userEventType)
                    .memberNo(sourcePlayLogGMContextVo.getMemberNo())
                    .charactorNo(sourcePlayLogGMContextVo.getCharacterNo())
                    .targetId(String.valueOf(request.getResourceId()))
                    .targetType(UserEventTarget.VIDEO)
                    .sourceType(SourceType.fromCode(request.getSourceType()))
                    .trackTotTm(request.getRunningTimeSecs())
                    .elapsedTm(request.getDuration())
                    .timeMillis(System.currentTimeMillis())
                    .build();

            amqpService.deliverUserEvent(userEvent);
            log.info("[VIDEO 재생로그][UserEvent MQ 발송] {}", userEvent.toString());
        }
    }

}
