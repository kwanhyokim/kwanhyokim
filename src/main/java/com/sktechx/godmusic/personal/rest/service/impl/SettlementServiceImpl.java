package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.personal.common.domain.type.BitrateType;
import com.sktechx.godmusic.personal.common.domain.type.SourceType;
import com.sktechx.godmusic.personal.rest.model.dto.listen.SettlementInfoDto;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenTrackRequest;
import com.sktechx.godmusic.personal.rest.model.vo.listen.SettlementToken;
import com.sktechx.godmusic.personal.rest.model.vo.video.ResourcePlayLogRequest;
import com.sktechx.godmusic.personal.rest.repository.SettlementMapper;
import com.sktechx.godmusic.personal.rest.service.SettlementService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * 정산 서비스 구현체
 * 향후 MSA 정산 서버로 변경 필요
 *
 * @author M.Ryan (sanghyun.park.tx@sk.com)
 * @date 2019. 6. 24.
 */
@Slf4j
@Service
public class SettlementServiceImpl implements SettlementService {

    private final String FLAC_ALTERTIVE_STREAMING_SERVCIE_ID = "TS2";
    private final String FLAC_ALTERTIVE_DRM_SERVCIE_ID = "TD2";
    private final String FLAC_ALTERTIVE_MUSIC_VIDEOD_SERVCIE_ID = "TM2";

    @Value("${gd.settlement.jwt.secret-key}")
    private String JWT_SECRET_KEY;

    private final SettlementMapper settlementMapper;

    public SettlementServiceImpl(SettlementMapper settlementMapper) {
        this.settlementMapper = settlementMapper;
    }

    @Override
    public String getServiceCode(Long memberNo, String settlementTypeCode) {
        return getServiceCode(this.getSettlementInfo(memberNo, settlementTypeCode));
    }

    @Override
    public String getServiceCodeByPrchsId(Long prchsId, String settlementTypeCode) {

        return getServiceCode(settlementMapper.selectSettlementInfoByPrchsId(prchsId, settlementTypeCode));
    }

    @Override
    public SettlementInfoDto getSettlementInfo(Long memberNo, String settlementTypeCode) {
        return settlementMapper.selectSettlementInfo(memberNo, settlementTypeCode);
    }

    @Override
    public SettlementToken parseSettlementToken(String sttToken) {
        if (StringUtils.isEmpty(sttToken)) {
            return null;
        }

        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(sttToken);

        Integer version = claims.getBody().get("version", Integer.class);
        String serviceId = claims.getBody().get("serviceId", String.class);
        Long purchaseId = claims.getBody().get("purchaseId", Long.class);
        Long goodsId = claims.getBody().get("goodsId", Long.class);

        log.debug("[정산 토큰(sttToken) 정보] version={}, serviceId={}, purchaseId={}, goodsId={}", version, serviceId, purchaseId, goodsId);
        return SettlementToken.builder()
                .version(version)
                .serviceId(serviceId)
                .purchaseId(purchaseId)
                .goodsId(goodsId)
                .build();
    }

    @Override
    public String evaluateServiceId(ListenTrackRequest request, SettlementInfoDto settlement) {
        if (ObjectUtils.isEmpty(request.getBitrate())) {
            log.info("1분 청취 요청 Bitrate 없음");
            return settlement.getSvcId();
        }

        if (ObjectUtils.isEmpty(request.getSourceType())) {
            log.info("1분 청취 요청 SourceType 없음");
            return settlement.getSvcId();
        }

        // flac 요청일 경우 대체
        if (request.getBitrate() == BitrateType.BITRATE_FLAC16 || request.getBitrate() == BitrateType.BITRATE_FLAC24) {
            switch (request.getSourceType()) {
                case STRM:
                    return FLAC_ALTERTIVE_STREAMING_SERVCIE_ID;
                case DN:
                    return FLAC_ALTERTIVE_DRM_SERVCIE_ID;
                case MV:
                    return FLAC_ALTERTIVE_MUSIC_VIDEOD_SERVCIE_ID;
            }
        }
        return settlement.getSvcId();
    }

    @Override
    public String evaluateServiceIdByResourcePlayLogRequest(ResourcePlayLogRequest request,
                                                            SettlementInfoDto settlementInfo) {
        if (ObjectUtils.isEmpty(request.getQuality())) {
            log.info("1분 청취 요청 Bitrate 없음");
            return settlementInfo.getSvcId();
        }

        if (ObjectUtils.isEmpty(request.getSourceType())) {
            log.info("1분 청취 요청 SourceType 없음");
            return settlementInfo.getSvcId();
        }

        // flac 요청일 경우 대체
        String quality = request.getQuality();
        if (BitrateType.BITRATE_FLAC16.getCode().equalsIgnoreCase(quality)
                || BitrateType.BITRATE_FLAC24.getCode().equalsIgnoreCase(quality)) {

            switch (SourceType.fromCode(request.getSourceType())) {
                case STRM:
                    return FLAC_ALTERTIVE_STREAMING_SERVCIE_ID;
                case DN:
                    return FLAC_ALTERTIVE_DRM_SERVCIE_ID;
                case MV:
                    return FLAC_ALTERTIVE_MUSIC_VIDEOD_SERVCIE_ID;
            }

        }
        return settlementInfo.getSvcId();
    }

    private String getServiceCode(SettlementInfoDto settlement) {
        return Optional.ofNullable(settlement)
                .map(SettlementInfoDto::getSvcId)
                .orElse(null);
    }

}
