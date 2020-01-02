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

import com.sktechx.godmusic.personal.rest.model.vo.drm.OwnerTokenClaim;
import com.sktechx.godmusic.personal.rest.model.vo.listen.SettlementToken;
import com.sktechx.godmusic.personal.rest.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
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
@ActiveProfiles("!prod")
public class TokenServiceTest {

    @Autowired
    private TokenService tokenService;

    @Test
    public void 정산토큰_파싱_TEST() {
        SettlementToken settlementToken = tokenService.parseSettlementToken(null);
        Assert.assertNull(settlementToken);
    }

    @Test
    public void DRM토큰_파싱_TEST() {
        OwnerTokenClaim ownerTokenClaim = tokenService.parseOwnerToken(null);
        Assert.assertNull(ownerTokenClaim);
    }

}
