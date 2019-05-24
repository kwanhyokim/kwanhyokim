/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service;

import java.util.List;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.LastListenHistoryDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.MoodPopularChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.PreferGenrePopularChnlDto;

/**
 * 설명 : 채널 서비스
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 23.
 */
public interface ChannelService {

    List<ChnlDto> getPopularChannelList(int channelLimitSize,int trackLimitSize, OsType osType,List<Long> filterChnlIdList);
    List<PreferGenrePopularChnlDto> getPreferGenrePopularChannelList(List<Long> preferGenreIdList, int trackLimitSize, OsType osType);
    List<MoodPopularChnlDto> getListenMoodPopularChannelIdList(List<Long> moodIdList , int trackLimitSize , OsType osType);
    List<LastListenHistoryDto> getLastListenHistory(Long memberNo, Long characterNo, OsType osType);

    List<ChnlDto> getPreferGenreThemeList(List<Long> preferGenreIdList, int trackLimitSize, OsType osType);
    void removeLastListenHistory(Long memberNo, Long characterNo, String listenType, Long listenId);
}
