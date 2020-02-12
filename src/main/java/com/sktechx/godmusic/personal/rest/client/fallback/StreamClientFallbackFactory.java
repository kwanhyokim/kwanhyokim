/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.client.fallback;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.rest.client.StreamClient;
import com.sktechx.godmusic.personal.rest.client.model.OneTimeUrlDto;
import com.sktechx.godmusic.personal.rest.model.dto.member.CharacterDto;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 11. 28.
 */
@Slf4j
@Component
public class StreamClientFallbackFactory implements FallbackFactory<StreamClient> {
    @Override
    public StreamClient create(Throwable throwable) {
        return new StreamClient() {

            @Override
            public CommonApiResponse<OneTimeUrlDto> getTrackStreamingUrl(Long trackId, String bitrate, String osType, String serviceCode, String serviceType) {
                log.warn("Failed to retrieve OnetimeURL infomation from streamClient@streamClientFallback - {}", throwable.getMessage());
                return null;
            }

            @Override
            public CommonApiResponse<Void> ping() {
                log.warn("[WARM-UP] ... 스트림 Ping 호출 실패, message={}", throwable.getMessage());
                return null;
            }
        };
    }
}
