/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.client;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.rest.client.fallback.ExternalClientFallbackFactory;
import com.sktechx.godmusic.personal.rest.model.vo.listen.SendListenLogRequestVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 설명 : External Client
 *
 *  OCR 관련된 {@link com.sktechx.godmusic.personal.rest.service.ExternalApiProxy} 라는 녀석이 이미 존재함.
 *
 * @author Groot(조민국) / dev.mingood@sk.com
 * @since 2020. 04. 24
 */
@FeignClient(value = "external-api", fallbackFactory = ExternalClientFallbackFactory.class)
public interface ExternalClient {

    @GetMapping("/external/v1/ping")
    CommonApiResponse<Void> ping();

    @PostMapping("/external/v1/pipe/listen/log")
    CommonApiResponse<Void> sendListenLogRequest(@ModelAttribute SendListenLogRequestVo listenLogRequestVo);

}
