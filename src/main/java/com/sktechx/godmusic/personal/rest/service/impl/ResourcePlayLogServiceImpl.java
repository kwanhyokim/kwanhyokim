/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.amqp.service.AmqpService;
import com.sktechx.godmusic.personal.common.domain.type.ResourceLogType;
import com.sktechx.godmusic.personal.common.domain.type.SourceType;
import com.sktechx.godmusic.personal.common.exception.PersonalErrorDomain;
import com.sktechx.godmusic.personal.rest.model.dto.listen.SourcePlayLog;
import com.sktechx.godmusic.personal.rest.model.vo.listen.SettlementToken;
import com.sktechx.godmusic.personal.rest.model.vo.listen.SourcePlayLogGMContextVo;
import com.sktechx.godmusic.personal.rest.model.vo.video.ResourcePlayLogRequest;
import com.sktechx.godmusic.personal.rest.service.ResourcePlayLogService;
import com.sktechx.godmusic.personal.rest.service.SettlementService;
import com.sktechx.godmusic.personal.rest.service.UserEventService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 설명 : Resource 청취(재생) 로그 Service
 *
 * @author groot
 * @since 2019. 12. 19
 */
@Slf4j
@Service
public class ResourcePlayLogServiceImpl implements ResourcePlayLogService {

    @Autowired
    private SettlementService settlementService;

    @Autowired
    private AmqpService amqpService;

    @Autowired
    private UserEventService userEventService;

    /**
     * Resource 청취(재생) 로그
     */
    @Override
    public void deliverResourcePlayLog(SourcePlayLogGMContextVo sourcePlayLogGMContextVo,
                                       ResourcePlayLogRequest request) {

        SourcePlayLog sourcePlayLog = SourcePlayLog.builder()
                .playChnl(sourcePlayLogGMContextVo.getPlayChnl())
                .sessionId(request.getSessionId())
                .timeMillis(System.currentTimeMillis())
                .memberNo(sourcePlayLogGMContextVo.getMemberNo())
                .characterNo(sourcePlayLogGMContextVo.getCharacterNo())
                .sourceType(SourceType.fromCode(request.getSourceType()))
                .sourceId(String.valueOf(request.getResourceId()))
                .logType(request.getLogType())
                .bitrate(request.getQuality())
                .quality(request.getQuality())
                .trackTotTm(request.getRunningTimeSecs())
                .elapsedTm(request.getDuration())
                .osType(request.getOsTypeToStr())
                .dvcId(sourcePlayLogGMContextVo.getDeviceId())
                .chnlId(request.getChannelId())
                .chnlType(request.getChannelType())
                .memberRcmdId(null)
                .addTm(request.getAddDateTime())
                .free(request.isFree())
                .sessionToken(null)
                .userClientIp(sourcePlayLogGMContextVo.getUserClientIp())
                .ownerToken(request.getOwnerToken())
                .build();

        SourcePlayLog.SourcePlayLogBuilder sourcePlayLogBuilder = sourcePlayLog.toBuilder();

        ResourceLogType resourceLogType = ResourceLogType.fromCode(request.getLogType());
        if (ResourceLogType.ONEMIN == resourceLogType) {
            sourcePlayLogBuilder = this.buildOneMinVideoPlayLog(request, sourcePlayLogBuilder);
        }

        amqpService.deliverSourcePlay(sourcePlayLogBuilder.build());
        log.info("[RESOURCE 청취로그 MQ 발송] listen = {}", sourcePlayLogBuilder.toString());
        userEventService.deliverUserEventByVideoPlayLog(sourcePlayLogGMContextVo, request);
    }

    /**
     * 비디오 ONEMIN 청취 로그 만들기
     */
    private SourcePlayLog.SourcePlayLogBuilder buildOneMinVideoPlayLog(ResourcePlayLogRequest request,
                                                                       SourcePlayLog.SourcePlayLogBuilder sourcePlayLogBuilder) {
        SettlementToken sttToken = settlementService.parseSettlementToken(request.getSttToken());
        String serviceId = sttToken.getServiceId();
        SourceType sourceType = SourceType.fromCode(request.getSourceType());

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
