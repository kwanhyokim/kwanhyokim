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

import com.sktechx.godmusic.personal.rest.model.dto.CharacterDto;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.GenreDto;
import com.sktechx.godmusic.personal.rest.model.dto.PreferGenreDto;
import com.sktechx.godmusic.personal.rest.model.vo.preference.Genre;
import com.sktechx.godmusic.personal.rest.repository.CharacterMapper;
import com.sktechx.godmusic.personal.rest.repository.ChartMapper;
import com.sktechx.godmusic.personal.rest.repository.GenreMapper;
import com.sktechx.godmusic.personal.rest.service.PreferenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
    private CharacterMapper characterMapper;

    @Autowired
    private GenreMapper genreMapper;

    @Autowired
    private ChartMapper chartMapper;

    @Override
    public List<Genre> getPreferenceGenreList(Long characterNo) {
        List<GenreDto> genreList = Collections.EMPTY_LIST;
        List<PreferGenreDto> preferGenreList = Collections.EMPTY_LIST;

        if (characterNo != null) {
            CharacterDto character = characterMapper.selectCharacter(characterNo);

            if (character != null) {
                preferGenreList = character.getPreferGenreList();
                log.info("preferGenreList : {}", preferGenreList);
            }
        }

        if (characterNo == null || CollectionUtils.isEmpty(preferGenreList)) {
            genreList = genreMapper.selectGenreListByDefault();
        } else {
            genreList = genreMapper.selectGenreListByCharacterNo(characterNo);
        }

        log.info("genreList : {}", genreList);

        return genreList.stream().map(Genre::new).collect(Collectors.toList()); // FIXME: stream
    }

    @Override
    public ChnlDto getPreferenceGenre(Long chartId) {
        return chartMapper.selectChartContentList(chartId);
    }
}
