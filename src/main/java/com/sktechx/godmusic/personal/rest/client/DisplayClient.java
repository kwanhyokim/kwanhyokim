/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.rest.client.fallback.DisplayClientFallbackFactory;
import com.sktechx.godmusic.personal.rest.model.vo.ChannelListResponse;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 남재우(Peter)/njw0619@sk.com
 * @date 2018.09.06
 */

@FeignClient(value = "display-api", fallbackFactory = DisplayClientFallbackFactory.class)
public interface DisplayClient {

    @GetMapping(value = "/display/v1/home/operationTpo")
    CommonApiResponse<ChannelListResponse> getOperationTpoList();

    @GetMapping("/display/v1/ping")
    CommonApiResponse<Void> ping();
}
