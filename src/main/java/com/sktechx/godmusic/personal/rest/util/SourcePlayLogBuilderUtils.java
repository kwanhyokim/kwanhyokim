/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.util;

import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.domain.type.SourceType;
import com.sktechx.godmusic.personal.common.exception.PersonalErrorDomain;
import com.sktechx.godmusic.personal.rest.model.dto.listen.SettlementInfoDto;
import com.sktechx.godmusic.personal.rest.model.dto.listen.SourcePlayLog;
import com.sktechx.godmusic.personal.rest.model.vo.drm.OwnerTokenClaim;
import com.sktechx.godmusic.personal.rest.model.vo.listen.SettlementToken;
import com.sktechx.godmusic.personal.rest.model.vo.listen.play.SourcePlayLogGMContextVo;
import com.sktechx.godmusic.personal.rest.model.vo.listen.play.ResourcePlayLogRequest;
import com.sktechx.godmusic.personal.rest.service.McpService;
import com.sktechx.godmusic.personal.rest.service.SettlementService;
import com.sktechx.godmusic.personal.rest.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 설명 : SourcePlayLogBuilder 집합
 *
 * @author groot
 * @since 2019. 12. 23
 */
@Slf4j
@Component
public class SourcePlayLogBuilderUtils {

    private final SettlementService settlementService;
    private final McpService mcpService;
    private final TokenService tokenService;

    public SourcePlayLogBuilderUtils(SettlementService settlementService,
                                     McpService mcpService,
                                     TokenService tokenService) {
        this.settlementService = settlementService;
        this.mcpService = mcpService;
        this.tokenService = tokenService;
    }

    public SourcePlayLog buildBasicSourcePlayLogByTrack(SourcePlayLogGMContextVo gmContextVo,
                                                        ResourcePlayLogRequest request) {
        return SourcePlayLog.builder()
                .playChnl(gmContextVo.getPlayChnl())
                .sessionId(request.getSessionId())
                .timeMillis(System.currentTimeMillis())
                .memberNo(gmContextVo.getMemberNo())
                .characterNo(gmContextVo.getCharacterNo())
                .sourceType(SourceType.fromCode(request.getSourceType()))
                .trackId(request.getResourceId())
                .sourceId(String.valueOf(request.getResourceId()))
                .logType(request.getLogType())
                .bitrate(request.getQuality())
                .quality(request.getQuality())
                .trackTotTm(request.getRunningTimeSecs())
                .elapsedTm(request.getDuration())
                .osType(request.getOsTypeToStr())
                .dvcId(gmContextVo.getDeviceId())
                .albumId(request.getAlbumId())
                .chnlId(request.getChannelId())
                .chnlType(request.getChannelType())
                .memberRcmdId(request.getRecommendTrackId())
                .addTm(request.getAddDateTime())
                .free(request.isFree())
                .sessionToken("")
                .userClientIp(gmContextVo.getUserClientIp())
                .ownerToken(request.getOwnerToken())
                .metaCacheUpdateDtime(request.getMetaCacheUpdateDtime())        // 캐시드 스트리밍
                .offlineStartDtime(request.getOfflineStartDtime())              // 캐시드 스트리밍
                .playOfflineYn(request.getPlayOfflineYn())                      // 캐시드 스트리밍
                .playCacheYn(request.getPlayCacheYn())                          // 캐시드 스트리밍
                .build();
    }

    /**
     * 곡 ONEMIN 청취 로그 만들기 (sttToken == null || sttToken != null 2가지 케이스로 분기됨)
     */
    public SourcePlayLog.SourcePlayLogBuilder buildOneMinListenTrackLog(SourcePlayLogGMContextVo gmContextVo,
                                                                        ResourcePlayLogRequest request,
                                                                        SourcePlayLog.SourcePlayLogBuilder sourcePlayLogBuilder) {
        /**
         * TODO 스트리밍 상태에 따른 Token 넘어오는 케이스 정리해야 할 듯.
         * TODO 케이스 별로 사용되는 토큰이 1:1이면 각각 개별 메소드로 나눈다.
         */

        // 캐시드 스트리밍인 경우
        if (YnType.Y == request.getPlayCacheYn()) {
            // TODO tokenService.parseCachedToken(request.getCachedToken());
        }

        // 캐시드 스트리밍이 아닌 경우 SttToken 활용
        SettlementToken sttToken = tokenService.parseSettlementToken(request.getSttToken());
        if (null != sttToken) {
            log.debug("[TRACK 청취로그] sttToken 이용하여 serviceId 전달");
            return sourcePlayLogBuilder
                    .pssrlCd(sttToken.getServiceId())
                    .serviceId(sttToken.getServiceId())
                    .prchsId(sttToken.getPurchaseId())
                    .goodsId(sttToken.getGoodsId());
        }
        return this.checkFreeResourceWithAppendSttInfo(gmContextVo, request, sourcePlayLogBuilder);
    }

    /**
     * 무료곡인 경우, MCP를 조회하여 MCP 쪽 svcCd를 청취 로그의 serviceId로 넘긴다.
     * (무료곡인 경우는 정산쪽의 serviceId와 MCP의 serviceId(svcCd)가 다르기 때문에)
     */
    private SourcePlayLog.SourcePlayLogBuilder checkFreeResourceWithAppendSttInfo(SourcePlayLogGMContextVo gmContextVo,
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

        SettlementInfoDto settlementInfo = settlementService.getSettlementInfo(gmContextVo.getMemberNo(), sourceType);

        // TODO 캐시드일 때도 트랙은 트랙이니까 무료곡일 경우 체크해서 serviceId를 따로 채워야 하는지도 확인 필요
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
    public SourcePlayLog.SourcePlayLogBuilder buildDrmListenTrackLog(ResourcePlayLogRequest request,
                                                                     SourcePlayLog.SourcePlayLogBuilder sourcePlayLogBuilder) {
        String ownerToken = request.getOwnerToken();
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

    public SourcePlayLog buildBasicSourcePlayLogByVideo(SourcePlayLogGMContextVo gmContextVo,
                                                        ResourcePlayLogRequest request) {
        return SourcePlayLog.builder()
                .playChnl(gmContextVo.getPlayChnl())
                .sessionId(request.getSessionId())
                .timeMillis(System.currentTimeMillis())
                .memberNo(gmContextVo.getMemberNo())
                .characterNo(gmContextVo.getCharacterNo())
                .sourceType(SourceType.fromCode(request.getSourceType()))
                .sourceId(String.valueOf(request.getResourceId()))
                .logType(request.getLogType())
                .bitrate(request.getQuality())
                .quality(request.getQuality())
                .trackTotTm(request.getRunningTimeSecs())
                .elapsedTm(request.getDuration())
                .osType(request.getOsTypeToStr())
                .dvcId(gmContextVo.getDeviceId())
                .chnlId(request.getChannelId())
                .chnlType(request.getChannelType())
                .memberRcmdId(null)
                .addTm(request.getAddDateTime())
                .free(request.isFree())
                .sessionToken(null)
                .userClientIp(gmContextVo.getUserClientIp())
                .ownerToken(request.getOwnerToken())
                .metaCacheUpdateDtime(null)
                .offlineStartDtime(null)
                .playOfflineYn(null)
                .playCacheYn(null)
                .build();
    }

    /**
     * 비디오 ONEMIN 청취 로그 만들기
     */
    public SourcePlayLog.SourcePlayLogBuilder buildOneMinVideoPlayLog(ResourcePlayLogRequest request,
                                                                      SourcePlayLog.SourcePlayLogBuilder sourcePlayLogBuilder) {
        SettlementToken sttToken = tokenService.parseSettlementToken(request.getSttToken());
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
