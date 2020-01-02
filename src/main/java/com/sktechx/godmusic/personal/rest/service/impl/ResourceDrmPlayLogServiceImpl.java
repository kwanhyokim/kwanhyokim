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

import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.amqp.service.AmqpService;
import com.sktechx.godmusic.personal.common.domain.type.ResourceLogType;
import com.sktechx.godmusic.personal.common.domain.type.SourceType;
import com.sktechx.godmusic.personal.rest.model.dto.listen.SourcePlayLog;
import com.sktechx.godmusic.personal.rest.model.vo.listen.play.SourcePlayLogGMContextVo;
import com.sktechx.godmusic.personal.rest.model.vo.listen.play.ResourcePlayLogRequest;
import com.sktechx.godmusic.personal.rest.service.ResourcePlayLogService;
import com.sktechx.godmusic.personal.rest.service.UserEventService;
import com.sktechx.godmusic.personal.rest.util.SourcePlayLogBuilderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 설명 : 곡 (DRM) 청취 로그 Service
 *
 * @author groot
 * @since 2019. 12. 23
 */
@Slf4j
@Service
public class ResourceDrmPlayLogServiceImpl implements ResourcePlayLogService {

    private final AmqpService amqpService;
    private final UserEventService userEventService;
    private final SourcePlayLogBuilderUtils sourcePlayLogBuilderUtils;

    public ResourceDrmPlayLogServiceImpl(AmqpService amqpService,
                                         UserEventService userEventService,
                                         SourcePlayLogBuilderUtils sourcePlayLogBuilderUtils) {
        this.amqpService = amqpService;
        this.userEventService = userEventService;
        this.sourcePlayLogBuilderUtils = sourcePlayLogBuilderUtils;
    }

    @Override
    public SourceType shouldHandle() {
        return SourceType.DN;
    }

    @Override
    public void deliverResourcePlayLog(SourcePlayLogGMContextVo gmContextVo, ResourcePlayLogRequest request) {
        SourcePlayLog sourcePlayLog = sourcePlayLogBuilderUtils.buildBasicSourcePlayLogByTrack(gmContextVo, request);

        SourcePlayLog.SourcePlayLogBuilder sourcePlayLogBuilder = sourcePlayLog.toBuilder();

        log.info("[DRM TRACK 청취 로그] makeTrackListenLog START");
        if (ResourceLogType.ONEMIN == ResourceLogType.fromCode(request.getLogType())) {
            sourcePlayLogBuilder = sourcePlayLogBuilderUtils.buildOneMinListenTrackLog(gmContextVo, request, sourcePlayLogBuilder);
        }

        sourcePlayLogBuilder = sourcePlayLogBuilderUtils.buildDrmListenTrackLog(request, sourcePlayLogBuilder);

        if (YnType.Y == request.getFreeYn()) {
            sourcePlayLogBuilder.free(true);
        }

        amqpService.deliverSourcePlay(sourcePlayLogBuilder.build());
        log.info("[DRM TRACK 청취로그][MQ 발송] {}", sourcePlayLogBuilder.toString());
        userEventService.deliverUserEventByTrackListenLog(gmContextVo, request);
    }

}
