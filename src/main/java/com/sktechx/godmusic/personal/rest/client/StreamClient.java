/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.client;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.rest.client.fallback.StreamClientFallbackFactory;
import com.sktechx.godmusic.personal.rest.client.model.OneTimeUrlDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 11. 28.
 */
@FeignClient(value = "stream-api", fallbackFactory = StreamClientFallbackFactory.class)
public interface StreamClient {

    /**
     * 트랙의 OneTimeURL 조회 from MCP
     */
    @GetMapping("/stream/v1/internal/tracks/{trackId}/onetimeurl")
    CommonApiResponse<OneTimeUrlDto> getTrackStreamingUrl(
            @PathVariable("trackId") Long trackId,
            @RequestParam(name = "bitrate") String bitrate,
            @RequestParam(name = "osType", required = false) String osType,
            @RequestParam(name = "svcCd", required = false) String serviceCode,
            @RequestParam(name = "svcType", required = false) String serviceType);
}
