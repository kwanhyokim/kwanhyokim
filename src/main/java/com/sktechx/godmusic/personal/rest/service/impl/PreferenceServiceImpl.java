/*
 *
 *  * Copyright (c) 2018 SK TECHX.
 *  * All right reserved.
 *  *
 *  * This software is the confidential and proprietary information of SK TECHX.
 *  * You shall not disclose such Confidential Information and
 *  * shall use it only in accordance with the terms of the license agreement
 *  * you entered into with SK TECHX.
 *
 */

package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.personal.common.domain.domain.HomeContentType;
import com.sktechx.godmusic.personal.common.domain.type.ChartType;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferGenreDto;
import com.sktechx.godmusic.personal.rest.model.dto.ChartDto;
import com.sktechx.godmusic.personal.rest.model.dto.ImageManagementDto;
import com.sktechx.godmusic.personal.rest.model.vo.preference.Artist;
import com.sktechx.godmusic.personal.rest.model.vo.preference.Chart;
import com.sktechx.godmusic.personal.rest.model.vo.preference.ChartResponse;
import com.sktechx.godmusic.personal.rest.repository.ArtistMapper;
import com.sktechx.godmusic.personal.rest.repository.CharacterPreferGenreMapper;
import com.sktechx.godmusic.personal.rest.repository.ChartMapper;
import com.sktechx.godmusic.personal.rest.repository.ImageManagementMapper;
import com.sktechx.godmusic.personal.rest.service.PreferenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 7. 19.
 */
@Slf4j
@Service
public class PreferenceServiceImpl implements PreferenceService {
    @Autowired
    private CharacterPreferGenreMapper characterPreferGenreMapper;

    @Autowired
    private ChartMapper chartMapper;

    @Autowired
    private ArtistMapper artistMapper;

    @Autowired
    private ImageManagementMapper imageManagementMapper;

    @Override
    public ChartResponse getPreferenceGenreList(Long characterNo) {
        List<CharacterPreferGenreDto> characterPreferGenreList = Collections.EMPTY_LIST;
        List<ChartDto> chartDtoList;

        if (characterNo != null) {
            characterPreferGenreList = characterPreferGenreMapper.selectCharacterPreferGenreList(characterNo);
        }

        if (characterNo == null || CollectionUtils.isEmpty(characterPreferGenreList)) {
            chartDtoList = chartMapper.selectChartListByDefaultGenre();
        } else {
            chartDtoList = chartMapper.selectChartListByPreferGenre(characterNo);
        }

        List<Chart> chartList = new ArrayList<>();
        for (ChartDto chartDto : chartDtoList) {
            String shortcutType = chartDto.getChartType() == ChartType.NEW ? "RECENT" : "POPULARITY";
            List<ImageManagementDto> imgMangList =  imageManagementMapper.selectImageManagementList(HomeContentType.GENRE.getCode(), chartDto.getSvcContentId(), shortcutType);

            if (!CollectionUtils.isEmpty(imgMangList)) { // 쇼컷 이미지가 없는 경우 목록에서 제외
                List<Chart.AlbumImg> albumImgList = new ArrayList<>();
                albumImgList.addAll(imgMangList.stream()
                        .map(imgMang -> Chart.AlbumImg.builder()
                                .size(imgMang.getImgSize())
                                .url(imgMang.getImgUrl())
                                .build())
                        .collect(Collectors.toList()));

                Chart chart = Chart.builder()
                        .chartId(chartDto.getChartId())
                        .chartNm(chartDto.getChartNm())
                        .albumImgList(albumImgList)
                        .build();

                chartList.add(chart);
            }
        }

        return new ChartResponse<>(chartList, HomeContentType.CHART);
    }

    @Override
    public ChartResponse getPreferenceArtistList(Long characterNo) {
        List<ArtistDto> artistDtoList = artistMapper.selectArtistListByPreferArtist(characterNo);

        return new ChartResponse<>(preferenceArtistListConvert(artistDtoList), HomeContentType.ARTIST);
    }

//    private List<Chart> preferenceGenreListConvert(List<ChartDto> chartDtoList) {
//        List<Chart> chartList = new ArrayList<>();
//
//        for (ChartDto chartDto : chartDtoList) {
//            List<Chart.AlbumImg> albumImgList = new ArrayList<>();
//
//            Optional.ofNullable(chartDto.getTrackList())
//                    .flatMap(trackList -> Optional.ofNullable(trackList.get(0)))
//                    .flatMap(track -> Optional.ofNullable(track.getAlbum()))
//                    .flatMap(album -> Optional.ofNullable(album.getImgList()))
//                    .ifPresent(imageInfos -> {
//                        albumImgList.addAll(imageInfos.stream()
//                                .map(imageInfo -> Chart.AlbumImg.builder()
//                                        .size(imageInfo.getSize())
//                                        .url(imageInfo.getUrl())
//                                        .build())
//                                .collect(Collectors.toList()));
//                    });
//
//            Chart chart = Chart.builder()
//                    .chartId(chartDto.getChartId())
//                    .chartNm(chartDto.getChartNm())
//                    .albumImgList(albumImgList)
//                    .build();
//
//            chartList.add(chart);
//        }
//
//        return chartList;
//    }

    private List<Artist> preferenceArtistListConvert(List<ArtistDto> artistDtoList) {
        List<Artist> artistList = new ArrayList<>();

        for (ArtistDto artistDto : artistDtoList) {
            List<Artist.ArtistImg> artistImgList = new ArrayList<>();

            Optional.ofNullable(artistDto.getImgList())
                    .ifPresent(imageInfos -> {
                        artistImgList.addAll(
                                imageInfos.stream()
                                        .map(imageInfo -> Artist.ArtistImg.builder()
                                                .size(imageInfo.getSize())
                                                .url(imageInfo.getUrl())
                                                .build())
                                        .collect(Collectors.toList()));
                    });

            Artist artist = Artist.builder()
                    .artistId(artistDto.getArtistId())
                    .artistNm(artistDto.getArtistName())
                    .albumImgList(artistImgList)
                    .build();

            artistList.add(artist);
        }

        return artistList;
    }

}
