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

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.domain.type.DayType;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.LastListenHistoryDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.MoodPopularChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.MoodPopularChnlListDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.PreferGenrePopularChnlListDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.PreferGenrePopularChnlDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.ListenMoodPopularChannelPanel;
import com.sktechx.godmusic.personal.rest.repository.AlbumMapper;
import com.sktechx.godmusic.personal.rest.repository.ChannelMapper;
import com.sktechx.godmusic.personal.rest.repository.ChartMapper;
import com.sktechx.godmusic.personal.rest.service.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.PropertySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 설명 : 채널 (플레이리스트 ) 서비스
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 25.
 */
@Service
@Slf4j
public class ChannelServiceImpl implements ChannelService {
    public static final String PREFER_GENRE_POPULAR_CHNL_KEY ="godmusic.personalapi.recommend.prefer.genre.popular.chnllist";
    public static final String MOOD_POPULAR_CHNL_KEY = "godmusic.personalapi.recommend.mood.popular.chnllist";
    public static final String ALL_POPULAR_CHNL_KEY ="godmusic.personalapi.recommend.all.popular.chnllist";

    private final int popularChnlTrackLimitSize = 10;
    private final int popularChnlExpiredSeconds = 86400;

    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    private ChartMapper chartMapper;

    @Autowired
    private AlbumMapper albumMapper;

    @Autowired
    private RedisService redisService;

    public List<ChnlDto> getPopularChannelList(int limitSize, OsType osType){
        List<Long> popularChnlIdList = null;
        try{
            popularChnlIdList = redisService.getListWithPrefix(ALL_POPULAR_CHNL_KEY,Long.class);
        }catch( Exception e){
            log.error("getPopularChannelList error : {}",e.getMessage());
        }finally {
            if(CollectionUtils.isEmpty(popularChnlIdList)){
                popularChnlIdList = channelMapper.selectPopularChannelIdList();
                if(!CollectionUtils.isEmpty(popularChnlIdList)){
                    redisService.setWithPrefix(ALL_POPULAR_CHNL_KEY,popularChnlIdList,popularChnlExpiredSeconds);
                }
            }
        }

        if(!CollectionUtils.isEmpty(popularChnlIdList) && popularChnlIdList.size() > limitSize){
            popularChnlIdList = popularChnlIdList.subList(0,limitSize);
        }

        return channelMapper.selectChannelListByIdList(popularChnlIdList,popularChnlTrackLimitSize, osType);
    }

    @Override
    public List<PreferGenrePopularChnlDto> getPreferGenrePopularChannelIdList(List<Long> preferGenreIdList) {
        List<PreferGenrePopularChnlListDto> preferGenrePopularChannelList = null;

        try{
            preferGenrePopularChannelList = redisService.getListWithPrefix(PREFER_GENRE_POPULAR_CHNL_KEY,PreferGenrePopularChnlListDto.class);
        }catch(Exception e){
            log.error("getPreferGenrePopularChannelIdList error : {}",e.getMessage());
        }finally {
            if(CollectionUtils.isEmpty(preferGenrePopularChannelList)){
                preferGenrePopularChannelList = channelMapper.selectAllPreferGenrePopularChannelIdList();
                if(!CollectionUtils.isEmpty(preferGenrePopularChannelList)){
                    redisService.setWithPrefix(PREFER_GENRE_POPULAR_CHNL_KEY, preferGenrePopularChannelList , popularChnlExpiredSeconds);
                }
            }
        }

        if( !CollectionUtils.isEmpty(preferGenreIdList) && !CollectionUtils.isEmpty(preferGenrePopularChannelList)){
            List<PreferGenrePopularChnlDto> filterChnlList = new ArrayList<>();
            preferGenrePopularChannelList
                    .stream()
                    .filter(dto -> Objects.nonNull(dto) && !CollectionUtils.isEmpty(dto.getChnlIdList()))
                    .filter(dto -> preferGenreIdList.contains(dto.getPreferGenreId()))
                    .forEach(dto -> {
                        Long chnlId = dto.getChnlIdList().stream().filter(id->!filterChnlList.contains(id)).findFirst().orElse(null);
                        if(chnlId!= null){
                            filterChnlList.add(new PreferGenrePopularChnlDto(dto.getPreferGenreId(),chnlId));
                        }
                    });
            return filterChnlList;
        }
        return null;

    }

    public List<MoodPopularChnlDto> getListenMoodPopularChannelIdList(List<Long> moodIdList){
        List<MoodPopularChnlListDto> moodPopularChannelList = null;

        try{
            moodPopularChannelList = redisService.getListWithPrefix(MOOD_POPULAR_CHNL_KEY, MoodPopularChnlListDto.class);
        }catch(Exception e) {
            log.error("getListenMoodPopularChannelIdList error : {}", e.getMessage());
        }finally{
            if(CollectionUtils.isEmpty(moodPopularChannelList)){
                moodPopularChannelList = channelMapper.selectAllMoodPopularChannelIdList();
                if(!CollectionUtils.isEmpty(moodPopularChannelList)){
                    redisService.setWithPrefix(MOOD_POPULAR_CHNL_KEY, moodPopularChannelList,popularChnlExpiredSeconds);
                }
            }
        }

        if(!CollectionUtils.isEmpty(moodIdList) && !CollectionUtils.isEmpty(moodPopularChannelList)){
            List<MoodPopularChnlDto> filterChnlList = new ArrayList<>();
            moodPopularChannelList
                    .stream()
                    .filter(dto-> Objects.nonNull(dto) && !CollectionUtils.isEmpty(dto.getChnlIdList()))
                    .filter(dto-> moodIdList.contains(dto.getCategoryId()))
                    .forEach(dto -> {
                        Long chnlId = dto.getChnlIdList().stream().filter(id->!filterChnlList.contains(id)).findFirst().orElse(null);
                        if(chnlId!= null){
                            filterChnlList.add(new MoodPopularChnlDto(dto.getCategoryId(),chnlId));
                        }
                    });

            return filterChnlList;
        }

        return null;
    }

    @Override
    public List<LastListenHistoryDto> getLastListenHistory(long characterNo, DayType dayType, OsType osType){

        List<LastListenHistoryDto> lastListenHistory = channelMapper.selectLastListenHistory(characterNo, dayType, osType);
        List<LastListenHistoryDto> lastListenAlbumHistory = albumMapper.selectLastListenHistory(characterNo);

        lastListenHistory.addAll(lastListenAlbumHistory);
        lastListenHistory.sort((m1, m2) -> m1.getLastListenDtime().after(m2.getLastListenDtime()) ? -1 : 1);

        return lastListenHistory;
    }
}
