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
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.amqp.service.AmqpService;
import com.sktechx.godmusic.personal.common.amqp.service.ListenLogAmqpService;
import com.sktechx.godmusic.personal.common.domain.type.ResourceLogType;
import com.sktechx.godmusic.personal.common.domain.type.SourceType;
import com.sktechx.godmusic.personal.rest.model.dto.listen.SourcePlayLog;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ResourcePlayLogRequestParam;
import com.sktechx.godmusic.personal.rest.model.vo.listen.token.OwnerTokenClaim;
import com.sktechx.godmusic.personal.rest.service.AbstractRelatedTrackResourcePlayLogService;
import com.sktechx.godmusic.personal.rest.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * 설명 : 곡 (DRM) 청취 로그 Service
 *
 * @author groot
 * @since 2019. 12. 23
 */
@Slf4j
@Service
public class ResourceDrmPlayLogServiceImpl extends AbstractRelatedTrackResourcePlayLogService {

    private final TokenService tokenService;

    public ResourceDrmPlayLogServiceImpl(AmqpService amqpService,
                                         ListenLogAmqpService listenLogAmqpService,
                                         TokenService tokenService) {
        super(amqpService, listenLogAmqpService);
        this.amqpService = amqpService;
        this.tokenService = tokenService;
    }

    @Override
    public SourceType handleSourceType() {
        return SourceType.DN;
    }

    /**
     * 곡(DRM) 청취 로그 MQ 발송
     */
    @Override
    public void deliverResourcePlayLog(GMContext gmContext, ResourcePlayLogRequestParam logRequestParam) {
        SourcePlayLog.SourcePlayLogBuilder sourcePlayLogBuilder = this.createBasicSourcePlayLogBuilder(gmContext, logRequestParam);

        log.info("[DRM TRACK 청취 로그] makeTrackListenLog START");
        if (ResourceLogType.ONEMIN == ResourceLogType.fromCode(logRequestParam.getLogType())) {
            sourcePlayLogBuilder = this.buildDrmListenTrackLog(logRequestParam, sourcePlayLogBuilder);
        }

        if (YnType.Y == logRequestParam.getFreeYn()) {
            sourcePlayLogBuilder.free(true);
        }

        listenLogAmqpService.deliverSourcePlay(sourcePlayLogBuilder.build());
        log.info("[DRM TRACK 청취로그][MQ 발송] {}", sourcePlayLogBuilder.toString());
    }

    /**
     * DRM 곡의 경우
     */
    private SourcePlayLog.SourcePlayLogBuilder buildDrmListenTrackLog(ResourcePlayLogRequestParam logRequestParam,
                                                                      SourcePlayLog.SourcePlayLogBuilder sourcePlayLogBuilder) {
        String ownerToken = logRequestParam.getOwnerToken();
        if (StringUtils.isEmpty(ownerToken)) {
            log.warn("OwnerToken 없음 (DRM 스트리밍)");
            return sourcePlayLogBuilder;
        }

        OwnerTokenClaim ownerTokenClaim = tokenService.parseOwnerToken(ownerToken);
        if (ObjectUtils.isEmpty(ownerTokenClaim)) {
            log.warn("OwnerToken Parse 실패 (DRM 스트리밍)");
            return sourcePlayLogBuilder;
        }

        return sourcePlayLogBuilder
                .drmMemberNo(ownerTokenClaim.getMemberNo())
                .drmPssrlCd(ownerTokenClaim.getPssrlCode())
                .drmServiceId(ownerTokenClaim.getServiceId())
                .drmPrchsId(ownerTokenClaim.getPurchaseId())
                .drmGoodsId(ownerTokenClaim.getGoodsId());
    }

}
