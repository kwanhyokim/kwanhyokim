/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.client.fallback;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.rest.client.ExternalClient;
import com.sktechx.godmusic.personal.rest.model.vo.listen.SendListenLogRequestVo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * 설명 : external 클라이언트 fallback
 *
 * @author Groot(조민국) / dev.mingood@sk.com
 * @since 2020. 04. 24
 */
@Slf4j
@Component
public class ExternalClientFallbackFactory implements FallbackFactory<ExternalClient> {

    @Override
    public ExternalClient create(Throwable throwable) {
        return new ExternalClient() {
            @Override
            public CommonApiResponse<Void> ping() {
                log.warn("[WARM-UP] ... external Ping 호출 실패. message={}", throwable.getMessage());
                return null;
            }

            @Override
            public CommonApiResponse<Void> sendListenLogRequest(@ModelAttribute SendListenLogRequestVo listenLogRequestVo) {
                log.error("[Listen-Pipeline] 호출 실패, message={}", throwable.getMessage());
                return null;
            }
        };
    }

}
