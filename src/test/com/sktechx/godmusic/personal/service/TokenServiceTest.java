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

import com.sktechx.godmusic.personal.rest.model.vo.listen.token.OwnerTokenClaim;
import com.sktechx.godmusic.personal.rest.model.vo.listen.token.CachedToken;
import com.sktechx.godmusic.personal.rest.model.vo.listen.token.SettlementToken;
import com.sktechx.godmusic.personal.rest.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
        CachedToken cachedToken = tokenService.parseCachedToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdmNJZCI6MjAxOTIzMzUsInByY2hzSWQiOjIwMTkyMzM1LCJnb29kc0lkIjoxMTIyMDAxMTAsInVzZUVuZER0aW1lIjoxNTgyMDk5NTE4LCJpc3MiOiJodHRwczovL2Rldi1hcGkubXVzaWNtYXRlcy5jby5rciIsImV4cCI6MTU4MjA5OTUxOH0.ExQjNNYwzc3VrHYqSCWMhC4spJvXZQ1EpCO94nCRAgA");
        log.info("## {}", cachedToken);
    }

}
