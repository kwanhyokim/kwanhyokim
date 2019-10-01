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

import java.util.Date;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.rest.client.fallback.MetaClientFallbackFactory;
import com.sktechx.godmusic.personal.rest.model.vo.video.VideoVo;

/**
 * 설명 : 메타 연동
 *
 * @author 남재우(Peter)/njw0619@sk.com
 * @date 2018.07.25
 */

@FeignClient(value = "meta-api", fallbackFactory = MetaClientFallbackFactory.class)
public interface MetaClient {

    @GetMapping("/meta/v1/videos/{videoIds}")
    CommonApiResponse<List<VideoVo>> getVideos(
            @PathVariable("videoIds") List<Long> videoIdList,
            @RequestParam("from")Date from,
            @RequestParam("to")Date to
    );

}
