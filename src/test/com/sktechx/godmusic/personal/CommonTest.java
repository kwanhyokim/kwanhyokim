/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@ActiveProfiles("local")
public class CommonTest {

    @Test
    public void 테스트() {
        log.info("테스트 시작..");
    }

}

