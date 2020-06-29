/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.client.fallback;

import org.springframework.stereotype.Component;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.rest.client.DisplayClient;
import com.sktechx.godmusic.personal.rest.model.vo.ChannelListResponse;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 남재우(Peter)/njw0619@sk.com
 * @date 2018.09.04
 */

@Component
@Slf4j
public class DisplayClientFallbackFactory implements FallbackFactory<DisplayClient>{

    @Override
    public DisplayClient create(Throwable e) {
        return new DisplayClient(){
            @Override
            public CommonApiResponse<ChannelListResponse> getOperationTpoList(){
                log.error(e.getMessage());
                return CommonApiResponse.<ChannelListResponse>builder()
                        .data(
                                ChannelListResponse.builder().build()
                        )
                        .build();
            }

            @Override
            public CommonApiResponse<Void> ping() {
                log.warn("[WARM-UP] ... DISPLAY Ping 호출 실패, message={}", e.getMessage());
                return null;
            }

        };
    }
}
