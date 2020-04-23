/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.controller.test;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.domain.constant.RedisKeyConstant;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.common.util.DateUtil;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.client.model.MetaVideoRequestVo;
import com.sktechx.godmusic.personal.rest.model.vo.video.VideoVo;
import com.sktechx.godmusic.personal.rest.repository.PreferenceMapper;
import com.sktechx.godmusic.personal.rest.service.PreferenceService;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2020-04-23
 */
@Profile({"!prod"})
@Controller
@Slf4j
@RequestMapping(Naming.serviceCode + "/test/preference/video")
@ApiIgnore
public class PreferenceVideoController {

    @Autowired
    RedisService redisService;

    @Autowired
    PreferenceMapper preferenceMapper;

    @Autowired
    MetaClient metaClient;

    @Autowired
    PreferenceService preferenceService;

    private static String response = "{\"message\":\"%s\"}";

    @GetMapping(value = "")
    public ModelAndView testPreference() {
        return new ModelAndView("testPreference");
    }

    @GetMapping("/clearCache")
    @ResponseBody
    public String clearCache(@RequestParam Long characterNo){

        redisService.delWithPrefix(
                String.format(RedisKeyConstant.PERSONAL_PREFERENCE_VIDEO_ARTIST_NEW_LIST, characterNo)
        );

        redisService.delWithPrefix(
                String.format(RedisKeyConstant.PERSONAL_PREFERENCE_VIDEO_GENRE_NEW_LIST, characterNo)
        );

        return "true";
    }

    @GetMapping("/checkArtist")
    @ResponseBody
    public String validateNewVideoArtist(@RequestParam Long characterNo,
            @RequestParam Long videoId){

        List<Long> videoIdList = preferenceMapper.selectPreferArtistVideoIdListByCharacterNo(characterNo);

        if(CollectionUtils.isEmpty(videoIdList)){
            return String.format(response, "해당 회원의 선호 정보가 없음.");
        }

        if(!videoIdList.contains(videoId)){
            return String.format(response, "해당 회원의 선호 비디오에 이 비디오는 포함되지 않음.");
        }


        List<VideoVo> videoVoList =
                metaClient.getVideos(
                        MetaVideoRequestVo.builder().videoIds(videoIdList).build()
                )
                        .getData().getList();
        if(CollectionUtils.isEmpty(videoVoList)){
            return String.format(response, "해당 비디오가 메타에 존재하지 않음.");
        }

        VideoVo tempVideoVo = VideoVo.builder().build();
        videoVoList.stream().filter(videoVo -> videoVo.getVideoId().equals(videoId)).findFirst().ifPresent(
                videoVo -> tempVideoVo.setDispStartDtime(videoVo.getDispStartDtime())
        );

        // 선호 장르 있는 경우
        // 3일 전 부터 현 시각 사이의 전시시작일 분리
        Date to = new Date();
        Date from = DateUtil.getDate(to, -259200);

        videoVoList = videoVoList
                .stream()
                .filter(Objects::nonNull)
                .filter(videoVo -> videoVo.getDispStartDtime().after(from) && videoVo.getDispStartDtime().before(to))
                .collect(Collectors.toList());

        if(CollectionUtils.isEmpty(videoVoList) || !videoVoList.contains(VideoVo.builder().videoId(videoId).build())){
            return String.format(response, "해당 비디오 전시일("+
                    DateUtil.dateToString(tempVideoVo.getDispStartDtime(), "yyyy-MM-dd HH:mm:ss")
                    +")이 기준일 (" +
                    DateUtil.dateToString(from, "yyyy-MM-dd HH:mm:ss") + " ~ " +
                    DateUtil.dateToString(to, "yyyy-yyyy-MM-dd HH:mm:ss-dd") +
                    ")에 포함되지 않음");
        }

        return String.format(response, "정상 노출 대상.");
    }

    @GetMapping("/checkGenre")
    @ResponseBody
    public String validateNewVideoGenre(@RequestParam Long characterNo, @RequestParam Long videoId){

        List<Long> videoIdList =
                preferenceMapper.selectPreferGenreVideoIdListByCharacterNo(characterNo);

        if(CollectionUtils.isEmpty(videoIdList)){
            return String.format(response, "해당 회원의 선호 정보가 없어 기존 장르로 대체됨");
        }

        if(!videoIdList.contains(videoId)){
            return String.format(response, "해당 회원의 선호 비디오에 이 비디오는 포함되지 않음.");
        }

        List<VideoVo> videoVoList =
                metaClient.getVideos(
                        MetaVideoRequestVo.builder().videoIds(videoIdList).build()
                )
                        .getData().getList();
        if(CollectionUtils.isEmpty(videoVoList)){
            return String.format(response, "해당 비디오가 메타에 존재하지 않음.");
        }


        VideoVo tempVideoVo = VideoVo.builder().build();
        videoVoList.stream().filter(videoVo -> videoVo.getVideoId().equals(videoId)).findFirst().ifPresent(
                videoVo -> tempVideoVo.setDispStartDtime(videoVo.getDispStartDtime())
        );

        // 선호 장르 있는 경우
        Date to = new Date();
        Date from = DateUtil.getDate(to, -604800);

        videoVoList = videoVoList
                .stream()
                .filter(Objects::nonNull)
                .filter(videoVo -> videoVo.getDispStartDtime().after(from) && videoVo.getDispStartDtime().before(to))
                .collect(Collectors.toList());

        if(CollectionUtils.isEmpty(videoVoList) || !videoVoList.contains(VideoVo.builder().videoId(videoId).build())){
            return String.format(response, "해당 비디오 전시일("+
                    DateUtil.dateToString(tempVideoVo.getDispStartDtime(), "yyyy-MM-dd HH:mm:ss")
                    +")이 기준일 (" +
                    DateUtil.dateToString(from, "yyyy-MM-dd HH:mm:ss") + " ~ " +
                    DateUtil.dateToString(to, "yyyy-yyyy-MM-dd HH:mm:ss-dd") +
                    ")에 포함되지 않음");
        }


        List<VideoVo> preferArtistVideoVoList =
                preferenceService.getPreferenceVideoArtistNewList(characterNo);
        if (preferArtistVideoVoList != null && preferArtistVideoVoList
                .contains(VideoVo.builder().videoId(videoId).build())) {
            return String.format(response, "해당 비디오가 아티스트 최신 영상에 포함되어 노출되지 않음");
        }


        return String.format(response, "정상 노출 대상.");
    }

}
