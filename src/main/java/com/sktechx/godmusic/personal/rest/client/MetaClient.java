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

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.rest.client.fallback.MetaClientFallbackFactory;
import com.sktechx.godmusic.personal.rest.client.model.MetaVideoRequestVo;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.vo.video.VideoVo;

/**
 * 설명 : 메타 연동
 *
 * @author 김관효(Bob) kwanhyo.kim@sk.com
 * @date 2019.10.07
 */

@FeignClient(value = "meta-api", fallbackFactory = MetaClientFallbackFactory.class)
public interface MetaClient {

    @PostMapping(name="/meta/internal/videos", consumes = MediaType.APPLICATION_JSON_VALUE)
    CommonApiResponse<ListDto<List<VideoVo>>> getVideos(@RequestBody MetaVideoRequestVo metaVideoRequestVo);

}
