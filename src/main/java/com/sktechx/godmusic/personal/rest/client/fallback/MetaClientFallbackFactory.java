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

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.client.model.MetaVideoRequestVo;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.vo.video.VideoVo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * 설명 : 메타 클라이언트 폴백
 *
 * @author 김관효(Bob) / kwanhyo.kim@sk.com
 * @date 2019.10.07
 */

@Component
@Slf4j
public class MetaClientFallbackFactory implements FallbackFactory<MetaClient>{

    @Override
    public MetaClient create(Throwable throwable) {
        return new MetaClient() {
            @Override
            public CommonApiResponse<ListDto<List<VideoVo>>> getVideos(
                    MetaVideoRequestVo metaVideoRequestVo) {

                List<VideoVo> list = new ArrayList<>();
                list.add(VideoVo.mock());

                return CommonApiResponse.<ListDto<List<VideoVo>>>builder()
                        .data(new ListDto<>(list)).build();
            }
        };
    }
}

