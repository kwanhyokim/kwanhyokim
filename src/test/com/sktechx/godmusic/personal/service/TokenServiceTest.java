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

import com.sktechx.godmusic.personal.rest.model.vo.listen.token.CachedStreamingToken;
import com.sktechx.godmusic.personal.rest.model.vo.listen.token.OwnerTokenClaim;
import com.sktechx.godmusic.personal.rest.model.vo.listen.token.SettlementToken;
import com.sktechx.godmusic.personal.rest.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
    public void sttToken_Parsing_TEST() {
        SettlementToken settlementToken = tokenService.parseSettlementToken("eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vYXBpLm11c2ljLWZsby5jb20iLCJzdWIiOiIyMTAxMzAyQOqwgOyeheyekOydmOydtOyaqeq2jOygleuztCIsInZlcnNpb24iOjEsInNlcnZpY2VJZCI6Ik0xOVMwMjEyMSIsInB1cmNoYXNlSWQiOjIwMTkxNzQyLCJnb29kc0lkIjoxMTIxOTA5NTB9.f9LpZMXhkkFok_51VG1MUdTCJfB9bRbcqVOp9LgPkHY");
        log.info("## {}", settlementToken);
    }

    @Test
    public void ownerToken_DRM_Parsing_TEST() {
        OwnerTokenClaim ownerTokenClaim = tokenService.parseOwnerToken(null);
        log.info("## {}", ownerTokenClaim);
    }

    @Test
    public void cachedStreamingToken_Parsing_TEST() {
        try {
            /*String jwt = Jwts.builder()
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
                    .compact();*/

            CachedStreamingToken cachedStreamingToken = tokenService.parseCachedStreamingToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdmNJZCI6IlIyMEMwMjAwMyIsInByY2hzSWQiOjIwMTkyNDUwLCJvcmRlcklkIjoiMjAyMDAxMTMxMDU3ODY4NDUiLCJnb29kc0lkIjoxMTIyMDAxMjAsInVzZUVuZER0aW1lIjoiMjAyMDAyMTIxMDU0NTQiLCJpc3MiOiJodHRwczovL2Rldi1hcGkubXVzaWNtYXRlcy5jby5rciIsImV4cCI6MTU4MjY4MjA5NH0.pHmTLE9V9Sv_F21ZQHs-nl4SBLTUOh2nTg9v7GTHTXk");
            log.info("## {}", cachedStreamingToken);

        } catch (Exception e) {
            log.error("JWT Parsing Error", e);
        }
    }

}
