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

import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferGenreDto;
import com.sktechx.godmusic.personal.rest.model.dto.ChartDto;
import com.sktechx.godmusic.personal.rest.model.vo.preference.Chart;
import com.sktechx.godmusic.personal.rest.model.vo.preference.PreferGenreResponse;
import com.sktechx.godmusic.personal.rest.repository.CharacterPreferGenreMapper;
import com.sktechx.godmusic.personal.rest.repository.ChartMapper;
import com.sktechx.godmusic.personal.rest.service.PreferenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
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

    @Override
    public PreferGenreResponse getPreferenceGenreList(Long characterNo) {
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

        log.info("chartList : {}", chartDtoList);

        List<Chart> chartList = chartDtoList.stream().map(Chart::new).collect(Collectors.toList()); // FIXME
        return new PreferGenreResponse<>(chartList);
    }

    @Override
    public Chart getPreferenceGenre(Long chartId) {
        ChartDto chartDto = chartMapper.selectChartMusicContentList(chartId);

        Chart chart = new Chart();
        chart.setChartId(chartDto.getChartId());
        chart.setChartNm(chartDto.getChartNm());
        chart.setChartMusicCongtentList(chartDto.getTrackList().stream().map(Chart.ChartMusicContent::new).collect(Collectors.toList()));

        return chart;
    }
}
