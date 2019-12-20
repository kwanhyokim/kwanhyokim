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
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.amqp.service.AmqpService;
import com.sktechx.godmusic.personal.common.domain.type.ResourceLogType;
import com.sktechx.godmusic.personal.common.domain.type.SourceType;
import com.sktechx.godmusic.personal.common.exception.PersonalErrorDomain;
import com.sktechx.godmusic.personal.rest.model.dto.listen.SettlementInfoDto;
import com.sktechx.godmusic.personal.rest.model.dto.listen.SourcePlayLog;
import com.sktechx.godmusic.personal.rest.model.vo.drm.OwnerTokenClaim;
import com.sktechx.godmusic.personal.rest.model.vo.listen.SettlementToken;
import com.sktechx.godmusic.personal.rest.model.vo.listen.SourcePlayLogGMContextVo;
import com.sktechx.godmusic.personal.rest.model.vo.video.ResourcePlayLogRequest;
import com.sktechx.godmusic.personal.rest.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * 설명 : 곡 청취 로그 Service
 *
 * @author groot
 * @since 2019. 12. 20
 */
@Slf4j
@Service
public class TrackResourcePlayLogServiceImpl implements ResourcePlayLogService {

    @Autowired
    private SettlementService settlementService;

    @Autowired
    private McpService mcpService;

    @Autowired
    private DrmService drmService;

    @Autowired
    private AmqpService amqpService;

    @Autowired
    private UserEventService userEventService;

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
                .trackId(request.getResourceId())
                .sourceId(String.valueOf(request.getResourceId()))
                .logType(request.getLogType())
                .bitrate(request.getQuality())
                .quality(request.getQuality())
                .trackTotTm(request.getRunningTimeSecs())
                .elapsedTm(request.getDuration())
                .osType(request.getOsTypeToStr())
                .dvcId(sourcePlayLogGMContextVo.getDeviceId())
                .albumId(request.getAlbumId())
                .chnlId(request.getChannelId())
                .chnlType(request.getChannelType())
                .memberRcmdId(request.getRecommendTrackId())
                .addTm(request.getAddDateTime())
                .free(request.isFree())
                .sessionToken("")
                .userClientIp(sourcePlayLogGMContextVo.getUserClientIp())
                .ownerToken(request.getOwnerToken())
                .metaCacheUpdateDtime(request.getMetaCacheUpdateDtime())        // 캐시드 스트리밍
                .offlineStartDtime(request.getOfflineStartDtime())              // 캐시드 스트리밍
                .playOfflineYn(request.getPlayOfflineYn())                      // 캐시드 스트리밍
                .playCacheYn(request.getPlayCacheYn())                          // 캐시드 스트리밍
                .build();

        // TODO cachedToken을 활용해서 캐시드 스트리밍 처리해야함

        SourcePlayLog.SourcePlayLogBuilder sourcePlayLogBuilder = sourcePlayLog.toBuilder();
        log.info("[TRACK 청취 로그] makeTrackListenLog START");
        if (ResourceLogType.ONEMIN == ResourceLogType.fromCode(request.getLogType())) {
            sourcePlayLogBuilder = this.buildOneMinListenTrackLog(sourcePlayLogGMContextVo, request, sourcePlayLogBuilder);
        }

        if (SourceType.DN == SourceType.fromCode(request.getSourceType())) {
            sourcePlayLogBuilder = this.buildDrmListenTrackLog(request, sourcePlayLogBuilder);
        }

        if (YnType.Y == request.getFreeYn()) {
            sourcePlayLogBuilder.free(true);
        }

        amqpService.deliverSourcePlay(sourcePlayLogBuilder.build());
        log.info("[TRACK 청취로그][MQ 발송] {}", sourcePlayLogBuilder.toString());
        userEventService.deliverUserEventByTrackListenLog(sourcePlayLogGMContextVo, request);
    }

    @Override
    public SourceType shouldHandle() {
        return SourceType.STRM;
    }

    /**
     * 곡 ONEMIN 청취 로그 만들기 (sttToken == null || sttToken != null 2가지 케이스로 분기됨)
     */
    private SourcePlayLog.SourcePlayLogBuilder buildOneMinListenTrackLog(SourcePlayLogGMContextVo sourcePlayLogGMContextVo,
                                                                         ResourcePlayLogRequest request,
                                                                         SourcePlayLog.SourcePlayLogBuilder sourcePlayLogBuilder) {
        SettlementToken sttToken = settlementService.parseSettlementToken(request.getSttToken());

        if (null != sttToken) {
            log.debug("[TRACK 청취로그] sttToken 이용하여 serviceId 전달");
            return sourcePlayLogBuilder
                    .pssrlCd(sttToken.getServiceId())
                    .serviceId(sttToken.getServiceId())
                    .prchsId(sttToken.getPurchaseId())
                    .goodsId(sttToken.getGoodsId());
        }
        return this.checkSourceFreeYnWithAppendSttInfo(sourcePlayLogGMContextVo, request, sourcePlayLogBuilder);
    }

    /**
     * 무료곡인 경우, MCP를 조회하여 MCP 쪽 svcCd를 청취 로그의 serviceId로 넘긴다.
     * (무료곡인 경우는 정산쪽의 serviceId와 MCP의 serviceId(svcCd)가 다르기 때문에)
     */
    private SourcePlayLog.SourcePlayLogBuilder checkSourceFreeYnWithAppendSttInfo(SourcePlayLogGMContextVo sourcePlayLogGMContextVo,
                                                                                  ResourcePlayLogRequest request,
                                                                                  SourcePlayLog.SourcePlayLogBuilder sourcePlayLogBuilder) {
        log.debug("[TRACK 청취로그][sttToken 없음]");

        String serviceId = null;
        Long purchaseId = null;
        Long goodsId = null;

        String sourceType = request.getSourceType();
        Long trackId = request.getResourceId();
        String bitrate = request.getQuality();
        String osType = request.getOsTypeToStr();

        SettlementInfoDto settlementInfo = settlementService.getSettlementInfo(sourcePlayLogGMContextVo.getMemberNo(), sourceType);

        if (YnType.Y == request.getFreeYn()) {
            serviceId = mcpService.getServiceCodeFromMCP(trackId, bitrate, osType);
            log.debug("[TRACK 청취로그] 무료곡 청취 로그. trackId={}, freeYn={}, serviceId={}", trackId, request.getFreeYn(), serviceId);

            if (null != settlementInfo) {
                purchaseId = settlementInfo.getPrchsId();
                goodsId = settlementInfo.getGoodsId();
            }

        } else if (YnType.N == request.getFreeYn()) {
            if (ObjectUtils.isEmpty(settlementInfo)) {
                log.warn("[TRACK 청취로그] 정산 정보 조회 실패. request={}", request);
                throw new CommonBusinessException(PersonalErrorDomain.USER_PSSRL_NOT_FOUND);
            }

            serviceId = settlementService.evaluateServiceIdByResourcePlayLogRequest(request, settlementInfo);
            purchaseId = settlementInfo.getPrchsId();
            goodsId = settlementInfo.getGoodsId();
            log.debug("[TRACK 청취로그] 유료곡 청취 로그. trackId={}, freeYn={}, serviceId={}", trackId, request.getFreeYn(), serviceId);
        }

        if (StringUtils.isEmpty(serviceId)) {
            log.warn("[TRACK 청취로그] Not Found serviceId. request={}", request);
            throw new CommonBusinessException(PersonalErrorDomain.USER_PSSRL_NOT_FOUND);
        }

        return sourcePlayLogBuilder
                .pssrlCd(serviceId)
                .serviceId(serviceId)
                .prchsId(purchaseId)
                .goodsId(goodsId);
    }

    /**
     * DRM 곡의 경우
     */
    private SourcePlayLog.SourcePlayLogBuilder buildDrmListenTrackLog(ResourcePlayLogRequest request,
                                                                      SourcePlayLog.SourcePlayLogBuilder sourcePlayLogBuilder) {
        String ownerToken = request.getOwnerToken();
        if (StringUtils.isEmpty(ownerToken)) {
            log.warn("OwnerToken 없음 (DRM 스트리밍)");
            return sourcePlayLogBuilder;
        }

        OwnerTokenClaim ownerTokenClaim = drmService.getOwnerTokenInfo(ownerToken);
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
