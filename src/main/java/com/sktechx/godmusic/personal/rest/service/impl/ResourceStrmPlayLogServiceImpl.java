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
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.amqp.service.AmqpService;
import com.sktechx.godmusic.personal.common.domain.type.ResourceLogType;
import com.sktechx.godmusic.personal.common.domain.type.SourceType;
import com.sktechx.godmusic.personal.common.exception.PersonalErrorDomain;
import com.sktechx.godmusic.personal.rest.model.dto.listen.SettlementInfoDto;
import com.sktechx.godmusic.personal.rest.model.dto.listen.SourcePlayLog;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ResourcePlayLogRequestParam;
import com.sktechx.godmusic.personal.rest.model.vo.listen.token.CachedToken;
import com.sktechx.godmusic.personal.rest.model.vo.listen.token.FreeCachedStreamingToken;
import com.sktechx.godmusic.personal.rest.model.vo.listen.token.SettlementToken;
import com.sktechx.godmusic.personal.rest.service.AbstractRelatedTrackResourcePlayLogService;
import com.sktechx.godmusic.personal.rest.service.McpService;
import com.sktechx.godmusic.personal.rest.service.SettlementService;
import com.sktechx.godmusic.personal.rest.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * 설명 : 곡(STRM) 청취 로그 Service
 *
 * @author groot
 * @since 2019. 12. 20
 */
@Slf4j
@Service
public class ResourceStrmPlayLogServiceImpl extends AbstractRelatedTrackResourcePlayLogService {

    private final TokenService tokenService;
    private final SettlementService settlementService;
    private final McpService mcpService;

    public ResourceStrmPlayLogServiceImpl(AmqpService amqpService,
                                          TokenService tokenService,
                                          SettlementService settlementService,
                                          McpService mcpService) {
        super(amqpService);
        this.tokenService = tokenService;
        this.settlementService = settlementService;
        this.mcpService = mcpService;
    }

    @Override
    public SourceType handleSourceType() {
        return SourceType.STRM;
    }

    /**
     * 곡(STRM) 청취 로그 MQ 발송
     */
    @Override
    public void deliverResourcePlayLog(GMContext gmContext, ResourcePlayLogRequestParam logRequestParam) {
        SourcePlayLog.SourcePlayLogBuilder sourcePlayLogBuilder = this.createBasicSourcePlayLogBuilder(gmContext, logRequestParam);

        log.info("[STRM TRACK 청취 로그] makeTrackListenLog START");
        if (ResourceLogType.ONEMIN == ResourceLogType.fromCode(logRequestParam.getLogType())) {
            sourcePlayLogBuilder = this.buildOneMinListenTrackLog(gmContext, logRequestParam, sourcePlayLogBuilder);
        }

        if (YnType.Y == logRequestParam.getFreeYn()) {
            sourcePlayLogBuilder.free(true);
        }

        amqpService.deliverSourcePlay(sourcePlayLogBuilder.build());
        log.info("[STRM TRACK 청취로그][MQ 발송] {}", sourcePlayLogBuilder.toString());
    }

    /**
     * 곡 ONEMIN 청취 로그 만들기
     * - cached면 cachedToken 활용, 아니면 sttToken 활용
     */
    private SourcePlayLog.SourcePlayLogBuilder buildOneMinListenTrackLog(GMContext gmContext,
                                                                         ResourcePlayLogRequestParam logRequestParam,
                                                                         SourcePlayLog.SourcePlayLogBuilder sourcePlayLogBuilder) {
        // 캐시드 스트리밍인 경우
        if (YnType.Y == logRequestParam.getPlayCacheYn()) {
            return this.buildSourcePlayLogByCachedTokenAndFreeYn(logRequestParam, sourcePlayLogBuilder);
        }

        // 캐시드 스트리밍이 아닌 경우 SttToken 활용
        SettlementToken sttToken = tokenService.parseSettlementToken(logRequestParam.getSttToken());
        if (null != sttToken) {
            log.debug("[TRACK 청취로그] sttToken 이용하여 serviceId 전달");
            return sourcePlayLogBuilder
                    .pssrlCd(sttToken.getServiceId())
                    .serviceId(sttToken.getServiceId())
                    .prchsId(sttToken.getPurchaseId())
                    .goodsId(sttToken.getGoodsId());
        }

        // sttToken == null이면 무료곡 여부 체크 후 MCP 조회
        return this.checkFreeTrackWithAppendSttInfo(gmContext, logRequestParam, sourcePlayLogBuilder);
    }

    /**
     * 캐시드 스트리밍인 경우 무료곡 여부를 포함해서 CachedToken을 통해 데이터를 채운다.
     */
    private SourcePlayLog.SourcePlayLogBuilder buildSourcePlayLogByCachedTokenAndFreeYn(ResourcePlayLogRequestParam logRequestParam,
                                                                                        SourcePlayLog.SourcePlayLogBuilder sourcePlayLogBuilder) {
        CachedToken cachedToken = tokenService.parseCachedToken(logRequestParam.getCachedToken());

        sourcePlayLogBuilder
                .prchsId(cachedToken.getPrchsId())
                .goodsId(cachedToken.getGoodsId());

        if (YnType.N == logRequestParam.getFreeYn()) {
            log.debug("[TRACK 청취로그] cachedToken 이용하여 serviceId 전달");
            return sourcePlayLogBuilder
                    .pssrlCd(cachedToken.getSvdId())
                    .serviceId(cachedToken.getSvdId());
        }

        FreeCachedStreamingToken freeCachedStreamingToken = tokenService.parseFreeCachedStreamingToken(logRequestParam.getFreeCachedStreamingToken());
        return sourcePlayLogBuilder
                .pssrlCd(freeCachedStreamingToken.getServiceId())
                .serviceId(freeCachedStreamingToken.getServiceId());
    }

    /**
     * [sttToken == null일 경우]
     * 무료곡인 경우, MCP를 조회하여 MCP 쪽 svcCd를 청취 로그의 serviceId로 넘긴다.
     * (무료곡인 경우는 정산쪽의 serviceId와 MCP의 serviceId(svcCd)가 다르기 때문에)
     */
    private SourcePlayLog.SourcePlayLogBuilder checkFreeTrackWithAppendSttInfo(GMContext gmContext,
                                                                               ResourcePlayLogRequestParam logRequestParam,
                                                                               SourcePlayLog.SourcePlayLogBuilder sourcePlayLogBuilder) {
        log.debug("[TRACK 청취로그][sttToken 없음]");
        String serviceId = null;
        Long purchaseId = null;
        Long goodsId = null;

        String sourceType = logRequestParam.getSourceType();
        Long trackId = logRequestParam.getResourceId();
        String bitrate = logRequestParam.getQuality();
        String osType = logRequestParam.getOsTypeToStr();

        SettlementInfoDto settlementInfo = settlementService.getSettlementInfo(gmContext.getMemberNo(), sourceType);

        if (YnType.Y == logRequestParam.getFreeYn()) {
            serviceId = mcpService.getServiceCodeFromMCP(trackId, bitrate, osType);
            log.debug("[TRACK 청취로그] 무료곡 청취 로그. trackId={}, freeYn={}, serviceId={}", trackId, logRequestParam.getFreeYn(), serviceId);

            if (null != settlementInfo) {
                purchaseId = settlementInfo.getPrchsId();
                goodsId = settlementInfo.getGoodsId();
            }

        } else if (YnType.N == logRequestParam.getFreeYn()) {
            if (ObjectUtils.isEmpty(settlementInfo)) {
                log.warn("[TRACK 청취로그] 정산 정보 조회 실패. logRequestParam={}", logRequestParam);
                throw new CommonBusinessException(PersonalErrorDomain.USER_PSSRL_NOT_FOUND);
            }

            serviceId = settlementService.evaluateServiceIdByResourcePlayLogRequest(logRequestParam, settlementInfo);
            purchaseId = settlementInfo.getPrchsId();
            goodsId = settlementInfo.getGoodsId();
            log.debug("[TRACK 청취로그] 유료곡 청취 로그. trackId={}, freeYn={}, serviceId={}", trackId, logRequestParam.getFreeYn(), serviceId);
        }

        if (StringUtils.isEmpty(serviceId)) {
            log.warn("[TRACK 청취로그] Not Found serviceId. logRequestParam={}", logRequestParam);
            throw new CommonBusinessException(PersonalErrorDomain.USER_PSSRL_NOT_FOUND);
        }

        return sourcePlayLogBuilder
                .pssrlCd(serviceId)
                .serviceId(serviceId)
                .prchsId(purchaseId)
                .goodsId(goodsId);
    }

}
