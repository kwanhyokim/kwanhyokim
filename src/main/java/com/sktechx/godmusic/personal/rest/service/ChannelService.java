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

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.type.DayType;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.LastListenHistoryDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.MoodPopularChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.MoodPopularChnlListDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.PreferGenrePopularChnlDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 설명 : 채널 서비스
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 23.
 */
public interface ChannelService {

    List<ChnlDto> getPopularChannelList(int channelLimitSize,int trackLimitSize, OsType osType);
    List<PreferGenrePopularChnlDto> getPreferGenrePopularChannelList(List<Long> preferGenreIdList, int trackLimitSize, OsType osType);
    List<MoodPopularChnlDto> getListenMoodPopularChannelIdList(List<Long> moodIdList , int trackLimitSize , OsType osType);
    List<LastListenHistoryDto> getLastListenHistory(long characterNo, DayType dayType, OsType osType);
}
