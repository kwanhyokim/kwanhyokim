/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.impl;

import com.netflix.discovery.converters.Auto;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.exception.CommonErrorMessage;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.PreferGenrePopularChnlDto;
import com.sktechx.godmusic.personal.rest.repository.ChannelMapper;
import com.sktechx.godmusic.personal.rest.repository.ChartMapper;
import com.sktechx.godmusic.personal.rest.service.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 설명 : 채널 (플레이리스트 ) 서비스
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 25.
 */
@Service
@Slf4j
public class ChannelServiceImpl implements ChannelService {

    public static final String HOME_PANEL_POPULAR_CHNL_KEY ="godmusic.personalapi.recommend.home.popular.chnllist";

    private final int popularChnlTrackLimitSize = 10;
    private final int popularChnlExpiredSeconds = 86400;

    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    private ChartMapper chartMapper;

    @Autowired
    private RedisService redisService;

    public List<ChnlDto> getPopularChannelList(int limitSize, OsType osType){
        List<Long> popularChnlIdList = null;
        try{
            popularChnlIdList = redisService.getListWithPrefix(HOME_PANEL_POPULAR_CHNL_KEY,Long.class);
        }catch( Exception e){
            log.error("getEditorsPickChannelList error : {}",e.getMessage());
        }finally {
            if(CollectionUtils.isEmpty(popularChnlIdList)){
                popularChnlIdList = channelMapper.selectPopularChannelIdList();
                if(!CollectionUtils.isEmpty(popularChnlIdList)){
                    redisService.setWithPrefix(HOME_PANEL_POPULAR_CHNL_KEY,popularChnlIdList,popularChnlExpiredSeconds);
                }
            }
        }

        if(!CollectionUtils.isEmpty(popularChnlIdList) && popularChnlIdList.size() > limitSize){
            popularChnlIdList = popularChnlIdList.subList(0,limitSize);
        }

        return channelMapper.selectPopularChannelList(popularChnlIdList,popularChnlTrackLimitSize, osType);
    }

    @Override
    public List<PreferGenrePopularChnlDto> getPreferGenrePopularChannel(List<Long> preferGenreIdList) {
        return null;
    }
}
