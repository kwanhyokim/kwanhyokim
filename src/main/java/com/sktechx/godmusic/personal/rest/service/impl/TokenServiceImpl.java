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

import com.sktechx.godmusic.personal.common.domain.type.SourceType;
import com.sktechx.godmusic.personal.rest.model.vo.drm.OwnerTokenClaim;
import com.sktechx.godmusic.personal.rest.model.vo.listen.CachedToken;
import com.sktechx.godmusic.personal.rest.model.vo.listen.FreeCachedStreamingToken;
import com.sktechx.godmusic.personal.rest.model.vo.listen.SettlementToken;
import com.sktechx.godmusic.personal.rest.service.SettlementService;
import com.sktechx.godmusic.personal.rest.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 설명 : Token 관련 Service (sttToken, ownerToken, cachedToken 등)
 *
 * @author groot
 * @since 2019. 12. 23
 */
@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    @Value("${gd.settlement.jwt.secret-key}")
    private String JWT_SECRET_KEY;

    @Value("${token.drm.owner.key}")
    private String CACHED_AND_DRM_TOKEN_KEY;

    private final SettlementService settlementService;

    public TokenServiceImpl(SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    @Override
    public SettlementToken parseSettlementToken(String sttToken) {
        try {
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

        } catch (Exception e) {
            log.error("sttToken Parse Error : {}", e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public OwnerTokenClaim parseOwnerToken(String ownerToken) {
        try {
            Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(CACHED_AND_DRM_TOKEN_KEY.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(ownerToken);

            Map<String, Object> claims = (Map<String, Object>) jws.getBody().get("ownerInfo");
            if (ObjectUtils.isEmpty(claims)) {
                return null;
            }

            OwnerTokenClaim ownerTokenClaim = OwnerTokenClaim.builder()
                    .memberNo(Long.valueOf((Integer) claims.get("memberNo")))
                    .goodsId(Long.valueOf((Integer) claims.get("goodsId")))
                    .purchaseId(Long.valueOf((Integer) claims.get("purchaseId")))
                    .pssrlCode((String) claims.get("pssrlCode"))
                    .serviceId((String) claims.get("serviceId"))
                    .build();

            // todo 2019.05월 이후 불필요 코드 이므로 아래 method와 함께 삭제 필요
            this.addServiceIdToOwnerToken(ownerTokenClaim);
            return ownerTokenClaim;

        } catch (Exception e) {
            log.error("Owner Token Parse 에러 : {}", e.getMessage());
            return null;
        }
    }

    @Override
    public CachedToken parseCachedToken(String cachedToken) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(CACHED_AND_DRM_TOKEN_KEY.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(cachedToken);

            String serviceId = claims.getBody().get("svcId", String.class);
            Long purchaseId = claims.getBody().get("prchsId", Long.class);
            Long goodsId = claims.getBody().get("goodsId", Long.class);

            log.debug("[캐시드 토큰(cachedToken) 정보] serviceId={}, purchaseId={}, goodsId={}", serviceId, purchaseId, goodsId);

            return CachedToken.builder()
                    .svdId(serviceId)
                    .prchsId(purchaseId)
                    .goodsId(goodsId)
                    .build();

        } catch (Exception e) {
            log.error("cachedToken Parse Error : {}", e.getMessage());
            return null;
        }
    }

    @Override
    public FreeCachedStreamingToken parseFreeCachedStreamingToken(String freeCachedStreamingToken) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(freeCachedStreamingToken);

            String serviceId = claims.getBody().get("serviceId", String.class);

            log.debug("[무료 캐시드 토큰(freeCachedStreamingToken) 정보] serviceId={}", serviceId);

            return FreeCachedStreamingToken.builder()
                    .serviceId(serviceId)
                    .build();

        } catch (Exception e) {
            log.error("cachedToken Parse Error : {}", e.getMessage());
            return null;
        }
    }

    private void addServiceIdToOwnerToken(OwnerTokenClaim ownerTokenClaim) {
        if (!ObjectUtils.isEmpty(ownerTokenClaim.getPurchaseId())
                && StringUtils.isEmpty(ownerTokenClaim.getServiceId())) {

            // todo 테스크 코드 이 로그가 찍히지 않는 걸 확인 하고 해당 메소드 삭제 처리
            log.info("Service Id Extract, token={}", ownerTokenClaim);
            String serviceId = settlementService.getServiceCodeByPrchsId(ownerTokenClaim.getPurchaseId(), SourceType.DN.getPlayType());
            ownerTokenClaim.setServiceId(serviceId);
        }
    }

}
