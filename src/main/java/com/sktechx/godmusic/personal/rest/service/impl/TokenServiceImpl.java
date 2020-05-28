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

import com.sktechx.godmusic.personal.rest.model.vo.listen.token.CachedStreamingToken;
import com.sktechx.godmusic.personal.rest.model.vo.listen.token.FreeCachedStreamingToken;
import com.sktechx.godmusic.personal.rest.model.vo.listen.token.OwnerTokenClaim;
import com.sktechx.godmusic.personal.rest.model.vo.listen.token.SettlementToken;
import com.sktechx.godmusic.personal.rest.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static java.util.Objects.requireNonNull;

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

    @Override
    public SettlementToken parseSettlementToken(String sttToken) {
        requireNonNull(sttToken);

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
            log.warn("sttToken Parse Error", e);
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

            return OwnerTokenClaim.builder()
                    .memberNo(Long.valueOf((Integer) claims.get("memberNo")))
                    .goodsId(Long.valueOf((Integer) claims.get("goodsId")))
                    .purchaseId(Long.valueOf((Integer) claims.get("purchaseId")))
                    .pssrlCode((String) claims.get("pssrlCode"))
                    .serviceId((String) claims.get("serviceId"))
                    .build();

        } catch (Exception e) {
            log.warn("Owner Token Parse Error", e);
            return null;
        }
    }

    @Override
    public CachedStreamingToken parseCachedStreamingToken(String cachedToken) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(CACHED_AND_DRM_TOKEN_KEY.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(cachedToken);

            String serviceId = claims.getBody().get("svcId", String.class);
            Long purchaseId = claims.getBody().get("prchsId", Long.class);
            Long goodsId = claims.getBody().get("goodsId", Long.class);

            log.debug("[캐시드 스트리밍 토큰(cachedStreamingToken) 정보] serviceId={}, purchaseId={}, goodsId={}", serviceId, purchaseId, goodsId);

            return CachedStreamingToken.builder()
                    .svdId(serviceId)
                    .prchsId(purchaseId)
                    .goodsId(goodsId)
                    .build();

        } catch (Exception e) {
            log.warn("cachedStreamingToken Parse Error", e);
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
            log.warn("freeCachedStreamingToken Parse Error", e);
            return null;
        }
    }

}
