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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.domain.type.PopularChnlType;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.LastListenHistoryDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.MoodPopularChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.MoodPopularChnlListDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.PreferGenrePopularChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.PreferGenrePopularChnlListDto;
import com.sktechx.godmusic.personal.rest.repository.AlbumMapper;
import com.sktechx.godmusic.personal.rest.repository.ChannelMapper;
import com.sktechx.godmusic.personal.rest.service.ChannelService;
import lombok.extern.slf4j.Slf4j;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.*;
import static com.sktechx.godmusic.personal.common.domain.constant.RedisKeyConstant.*;
/**
 * 설명 : 채널 (플레이리스트 ) 서비스
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 25.
 */
@Service
@Slf4j
public class ChannelServiceImpl implements ChannelService {

    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    private AlbumMapper albumMapper;

    @Autowired
    private RedisService redisService;


    public List<ChnlDto> getPopularChannelList(int channelLimitSize, int trackLimitSize ,OsType osType , List<Long> filterChnlIdList){
        List<ChnlDto> popularChannelList = null;
        try{
            popularChannelList = redisService.getListWithPrefix(ALL_POPULAR_CHNL_KEY,ChnlDto.class);
        }catch( Exception e){
            log.error("getPopularChannelList error : {}",e.getMessage());
        }finally {
            if(CollectionUtils.isEmpty(popularChannelList)){
                List<Long> popularChannelIdList = channelMapper.selectPopularChannelIdList();
                if(!CollectionUtils.isEmpty(popularChannelIdList)){
                    popularChannelIdList = slicePopularChannelIdLimit(popularChannelIdList);

                    popularChannelList = channelMapper.selectChannelListByIdList(popularChannelIdList,trackLimitSize, osType , PopularChnlType.ALL);
                    if(!CollectionUtils.isEmpty(popularChannelList)){
                        redisService.setWithPrefix(ALL_POPULAR_CHNL_KEY,popularChannelList,POPULAR_CHNL_EXPIRED_SECONDS);
                    }
                }
            }
        }

        filterDuplicatePopularChnlList(filterChnlIdList, popularChannelList);

        if(!CollectionUtils.isEmpty(popularChannelList) && popularChannelList.size() > channelLimitSize){
            popularChannelList = popularChannelList.subList(0,channelLimitSize);
        }

        return popularChannelList;
    }

    private List<Long> slicePopularChannelIdLimit( List<Long> popularChnlIdList) {
        if (!CollectionUtils.isEmpty(popularChnlIdList) && popularChnlIdList.size() > POPULAR_CHNL_CACHE_LIMIT_SIZE) {
            popularChnlIdList = popularChnlIdList.subList(0, POPULAR_CHNL_CACHE_LIMIT_SIZE);
        }
        return popularChnlIdList;
    }

    private void filterDuplicatePopularChnlList(List<Long> filterChnlIdList , List<ChnlDto> popularChnlList){
        if( !CollectionUtils.isEmpty(filterChnlIdList) && !CollectionUtils.isEmpty(popularChnlList) ){
            popularChnlList.removeIf(chnlDto -> {
                if(filterChnlIdList.contains(chnlDto.getChnlId()))
                    return true;
                return false;
            });
        }
    }
    @Override
    public List<PreferGenrePopularChnlDto> getPreferGenrePopularChannelList(List<Long> preferGenreIdList , int trackLimitSize, OsType osType) {
        List<PreferGenrePopularChnlListDto> preferGenrePopularChannelList = null;

        if(CollectionUtils.isEmpty(preferGenreIdList)){
            return null;
        }

        try{
            preferGenrePopularChannelList = redisService.getListWithPrefix(PREFER_GENRE_POPULAR_CHNL_KEY,PreferGenrePopularChnlListDto.class);
        }catch(Exception e){
            log.error("getPreferGenrePopularChannelIdList error : {}",e.getMessage());
        }finally {
            if(CollectionUtils.isEmpty(preferGenrePopularChannelList)){
                preferGenrePopularChannelList = channelMapper.selectAllPreferGenrePopularChannelIdList();
                if(!CollectionUtils.isEmpty(preferGenrePopularChannelList)){
                    redisService.setWithPrefix(PREFER_GENRE_POPULAR_CHNL_KEY, preferGenrePopularChannelList , PREFER_GENRE_POPULAR_CHNL_EXPIRED_SECONDS);
                }
            }
        }

        if(!CollectionUtils.isEmpty(preferGenrePopularChannelList)){
            return getPreferGenreUniqueChannelList(preferGenreIdList ,preferGenrePopularChannelList, trackLimitSize, osType);
        }
        return null;
    }

    @Override
    public List<ChnlDto> getPreferGenreThemeList(List<Long> preferGenreIdList , int trackLimitSize, OsType osType) {
        List<PreferGenrePopularChnlDto> preferGenrePopularChannelList = getPreferGenrePopularChannelList(preferGenreIdList, trackLimitSize, osType);

        if(CollectionUtils.isEmpty(preferGenrePopularChannelList)){
            return null;
        }

        List<ChnlDto> chnlDtoList = preferGenrePopularChannelList.stream().map(x -> x.getChannel())
                .collect(Collectors.toList());

        for(ChnlDto chnlDto : chnlDtoList){
            chnlDto.setChnlDispNm(null);
            chnlDto.setTrackCount(null);
            chnlDto.setTrackList(null);
            chnlDto.setUpdateDtime(null);
            chnlDto.setCreateDtime(null);
            chnlDto.setRenewTrackCnt(null);

            // 앨범 이미지가 있을 경우, 우선 적용
            if( !ObjectUtils.isEmpty(chnlDto.getAlbum()) && !CollectionUtils.isEmpty(chnlDto.getAlbum().getImgList())){
                chnlDto.setImgList(chnlDto.getAlbum().getImgList());
            }

            chnlDto.setAlbum(null);
        }

        return chnlDtoList;

    }


    public List<MoodPopularChnlDto> getListenMoodPopularChannelIdList(List<Long> moodIdList , int trackLimitSize , OsType osType){
        List<MoodPopularChnlListDto> moodPopularChannelList = null;
        if(CollectionUtils.isEmpty(moodIdList))
            return null;

        try{
            moodPopularChannelList = redisService.getListWithPrefix(MOOD_POPULAR_CHNL_KEY, MoodPopularChnlListDto.class);
        }catch(Exception e) {
            log.error("getListenMoodPopularChannelIdList error : {}", e.getMessage());
        }finally{
            if(CollectionUtils.isEmpty(moodPopularChannelList)){
                moodPopularChannelList = channelMapper.selectAllMoodPopularChannelIdList();
                if(!CollectionUtils.isEmpty(moodPopularChannelList)){
                    redisService.setWithPrefix(MOOD_POPULAR_CHNL_KEY, moodPopularChannelList,MOOD_POPULAR_CHNL_EXPIRED_SECONDS);
                }
            }
        }

        if(!CollectionUtils.isEmpty(moodPopularChannelList)){
            return getListenMoodUniqueChannelList(moodIdList,moodPopularChannelList ,trackLimitSize,osType);
        }

        return null;
    }

    @Override
    public List<LastListenHistoryDto> getLastListenHistory(Long memberNo, Long characterNo, OsType osType){

        List<LastListenHistoryDto> lastListenHistory = channelMapper.selectLastListenHistory(memberNo, characterNo, osType);
        List<LastListenHistoryDto> lastListenHistoryByChannel = channelMapper.selectLastListenHistoryByChannel(memberNo, characterNo, osType);
        List<LastListenHistoryDto> lastListenHistoryByAlbum = albumMapper.selectLastListenHistory(memberNo, characterNo);


        lastListenHistory.addAll(lastListenHistoryByChannel);
        lastListenHistory.addAll(lastListenHistoryByAlbum);
        lastListenHistory.sort((m1, m2) -> m1.getLastListenDtime().after(m2.getLastListenDtime()) ? -1 : 1);

        return lastListenHistory;
    }

    private List<PreferGenrePopularChnlDto> getPreferGenreUniqueChannelList(final List<Long> preferGenreIdList ,
                                                                            final List<PreferGenrePopularChnlListDto> preferGenrePopularChannelList,
                                                                            int trackLimitSize ,
                                                                            OsType osType){
        List<PreferGenrePopularChnlDto> uniquePopularChannelList = new ArrayList<>();

        if(!CollectionUtils.isEmpty(preferGenrePopularChannelList)){
            preferGenrePopularChannelList
                    .stream()
                    .filter(preferGenre -> Objects.nonNull(preferGenre) && !CollectionUtils.isEmpty(preferGenre.getChnlIdList()))
                    .filter(preferGenre -> preferGenreIdList.contains(preferGenre.getPreferGenreId()))
                    .forEach(preferGenre -> {
                        Long chnlId = preferGenre.getChnlIdList().
                                stream().
                                filter(id-> !uniquePopularChannelList.contains(id) ).
                                findFirst().
                                orElse(null);
                        if(chnlId!= null){
                            try{
                                uniquePopularChannelList.add(new PreferGenrePopularChnlDto(preferGenre.getPreferGenreId(),chnlId));
                            }catch(Exception e){
                                log.error("getPreferGenreUniqueChannelList error : {}",e.getMessage());
                            }
                        }
                    });

            attachPreferGenreChannelInfo(uniquePopularChannelList,trackLimitSize,osType);
        }


        return uniquePopularChannelList;
    }
    private void attachPreferGenreChannelInfo(final List<PreferGenrePopularChnlDto> popularChnlList, int trackLimitSize, OsType osType){
        List<Long> channelIdList = popularChnlList.stream().map(dto -> dto.getChnlId()).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty( channelIdList )){
            List<ChnlDto> channelList = channelMapper.selectChannelListByIdList(channelIdList , trackLimitSize, osType , PopularChnlType.GENRE);

            if(!CollectionUtils.isEmpty(channelList)) {

                for(PreferGenrePopularChnlDto popularChnlDto :  popularChnlList ){
                    ChnlDto channel= channelList
                            .stream()
                            .filter(chnlDto -> chnlDto.getChnlId().equals(popularChnlDto.getChnlId()))
                            .findFirst()
                            .orElse(null);
                    if(channel!= null){
                        popularChnlDto.setChannel(channel);
                    }
                }
            }
        }
    }


    private void attachListenMoodUniqueChannelList(final List<MoodPopularChnlDto> popularChnlList, int trackLimitSize, OsType osType){
        List<Long> channelIdList = popularChnlList.stream().map(dto -> dto.getChnlId()).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty( channelIdList )){
            List<ChnlDto> channelList = channelMapper.selectChannelListByIdList(channelIdList , trackLimitSize, osType, PopularChnlType.ALL);
            if(!CollectionUtils.isEmpty(channelList)) {

                for(MoodPopularChnlDto popularChnlDto :  popularChnlList ){
                    ChnlDto channel= channelList
                            .stream()
                            .filter(chnlDto -> chnlDto.getChnlId().equals(popularChnlDto.getChnlId()))
                            .findFirst()
                            .orElse(null);
                    if(channel!= null){
                        popularChnlDto.setChannel(channel);
                    }
                }
            }
        }
    }
    private List<MoodPopularChnlDto> getListenMoodUniqueChannelList(final List<Long> moodIdList ,
                                                                    final List<MoodPopularChnlListDto> moodPopularChannelList,
                                                                    int trackLimitSize ,
                                                                    OsType osType){

        List<MoodPopularChnlDto> uniquePopularChannelList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(moodPopularChannelList)){
            moodPopularChannelList
                    .stream()
                    .filter(dto-> Objects.nonNull(dto) && !CollectionUtils.isEmpty(dto.getChnlIdList()))
                    .filter(dto-> moodIdList.contains(dto.getCategoryId()))
                    .forEach(dto -> {
                        Long chnlId = dto.getChnlIdList()
                                .stream()
                                .filter(id->!uniquePopularChannelList.contains(id))
                                .findFirst()
                                .orElse(null);
                        if(chnlId!= null){
                            try{
                                uniquePopularChannelList.add(new MoodPopularChnlDto(dto.getCategoryId(),chnlId));
                            }catch(Exception e){
                                log.error("getListenMoodUniqueChannelList error : {}",e.getMessage());
                            }
                        }
                    });

            attachListenMoodUniqueChannelList(uniquePopularChannelList, trackLimitSize, osType);

        }
        return uniquePopularChannelList;
    }

    public void removeLastListenHistory(Long memberNo, Long characterNo, String listenType, Long listenId){
        channelMapper.deleteLastListenHistory(memberNo, characterNo, listenType, listenId);
    }
}
