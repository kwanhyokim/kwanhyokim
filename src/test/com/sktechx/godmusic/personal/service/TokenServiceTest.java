/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.service;

import com.sktechx.godmusic.personal.rest.model.vo.listen.token.CachedToken;
import com.sktechx.godmusic.personal.rest.model.vo.listen.token.OwnerTokenClaim;
import com.sktechx.godmusic.personal.rest.model.vo.listen.token.SettlementToken;
import com.sktechx.godmusic.personal.rest.service.TokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;

/**
 * 설명 : TokenService TEST
 *
 * @author groot
 * @since 2019. 12. 27
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("local")
public class TokenServiceTest {

    @Value("${gd.settlement.jwt.secret-key}")
    private String JWT_SECRET_KEY;

    @Value("${token.drm.owner.key}")
    private String CACHED_AND_DRM_TOKEN_KEY;

    @Autowired
    private TokenService tokenService;

    @Test
    public void 정산토큰_파싱_TEST() {
        SettlementToken settlementToken = tokenService.parseSettlementToken("eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vYXBpLm11c2ljLWZsby5jb20iLCJzdWIiOiIyMTAxMzAyQOqwgOyeheyekOydmOydtOyaqeq2jOygleuztCIsInZlcnNpb24iOjEsInNlcnZpY2VJZCI6Ik0xOVMwMjEyMSIsInB1cmNoYXNlSWQiOjIwMTkxNzQyLCJnb29kc0lkIjoxMTIxOTA5NTB9.f9LpZMXhkkFok_51VG1MUdTCJfB9bRbcqVOp9LgPkHY");
        log.info("## {}", settlementToken);
    }

    @Test
    public void DRM토큰_파싱_TEST() {
        OwnerTokenClaim ownerTokenClaim = tokenService.parseOwnerToken(null);
        log.info("## {}", ownerTokenClaim);
    }

    @Test
    public void 캐시드_TEST() {
        try {
            String jwt = Jwts.builder()
                    .claim("svcId", "TS1")
                    .claim("prchsId", 20192455L)
                    .claim("orderId", "20200113165786848")
                    .claim("goodsId", 112200110L)
                    .claim("useEndDtime", "20200212163609")
                    .claim("iss", "https://dev-api.musicmates.co.kr")
//                .claim("exp", 1582702569L)    // 성공
                    .claim("exp", 1582L)    // 실패
                    .signWith(
                            SignatureAlgorithm.HS256,
                            CACHED_AND_DRM_TOKEN_KEY.getBytes(StandardCharsets.UTF_8)
                    )
                    .compact();

//        CachedToken cachedToken = tokenService.parseCachedToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdmNJZCI6IlRTMSIsInByY2hzSWQiOjIwMTkyNDUyLCJvcmRlcklkIjoiMjAyMDAxMTMxNTU3ODY4NDYiLCJnb29kc0lkIjoxMTIyMDAxMjAsInVzZUVuZER0aW1lIjoiMjAyMDAyMTIxNTUyMjMiLCJpc3MiOiJodHRwczovL2Rldi1hcGkubXVzaWNtYXRlcy5jby5rciIsImV4cCI6MTU4MjY5OTk0M30.7X0HGKLxeNXvRL08QpZkCLCIZcnup6cKZGQqyAtIUh8");
            CachedToken cachedToken = tokenService.parseCachedToken(jwt);
            log.info("## {}", cachedToken);

        } catch (Exception e) {
            log.error("JWT Parsing Error", e);
        }
    }

}
