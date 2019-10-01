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
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.model.vo.video.VideoVo;
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
public class MetaClientFallbackFactory implements FallbackFactory<MetaClient>{
    @Override
    public MetaClient create(Throwable e) {
        return new MetaClient(){

            @Override
            public CommonApiResponse<List<VideoVo>> getVideos(List<Long> videoIdList, Date from,
                    Date to) {


                List<VideoVo> list = new ArrayList<>();

                list.add(VideoVo.VideoVoBuilder.newBuilderFromDto().build());

                return new CommonApiResponse<>(list);
            }
        };
    }
}
