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
 * 설명 : 비디오 재생 로그 Service
 *
 * @author groot
 * @since 2019. 12. 19
 */
@Slf4j
@Service
public class ResourceVideoPlayLogServiceImpl implements ResourcePlayLogService {

    private final AmqpService amqpService;
    private final UserEventService userEventService;
    private final SourcePlayLogBuilderUtils sourcePlayLogBuilderUtils;

    public ResourceVideoPlayLogServiceImpl(AmqpService amqpService,
                                           UserEventService userEventService,
                                           SourcePlayLogBuilderUtils sourcePlayLogBuilderUtils) {
        this.amqpService = amqpService;
        this.userEventService = userEventService;
        this.sourcePlayLogBuilderUtils = sourcePlayLogBuilderUtils;
    }

    @Override
    public SourceType shouldHandle() {
        return SourceType.VIDEO_MV;
    }

    /**
     * 비디오 재생 로그
     */
    @Override
    public void deliverResourcePlayLog(SourcePlayLogGMContextVo gmContextVo, ResourcePlayLogRequest request) {
        SourcePlayLog sourcePlayLog = sourcePlayLogBuilderUtils.buildBasicSourcePlayLogByVideo(gmContextVo, request);

        SourcePlayLog.SourcePlayLogBuilder sourcePlayLogBuilder = sourcePlayLog.toBuilder();

        ResourceLogType resourceLogType = ResourceLogType.fromCode(request.getLogType());
        if (ResourceLogType.ONEMIN == resourceLogType) {
            sourcePlayLogBuilder = sourcePlayLogBuilderUtils.buildOneMinVideoPlayLog(request, sourcePlayLogBuilder);
        }

        amqpService.deliverSourcePlay(sourcePlayLogBuilder.build());
        log.info("[RESOURCE 청취로그 MQ 발송] listen = {}", sourcePlayLogBuilder.toString());
        userEventService.deliverUserEventByVideoPlayLog(gmContextVo, request);
    }

}
