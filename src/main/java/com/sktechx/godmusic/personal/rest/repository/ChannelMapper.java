/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.repository;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.mybatis.annotation.BaseMapper;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.PreferGenrePopularChnlDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 설명 :  채널 Repository
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 19.
 */
@BaseMapper
public interface ChannelMapper {
    //TODO : meta api 호출
    ChnlDto selectChannelById(Long chnlId);
    Integer selectChannelTrackCount(Long chnlId);

    List<Long> selectPopularChannelIdList();
    List<ChnlDto> selectPopularChannelList(@Param("channelIdList") List<Long> channelIdList, @Param("trackLimitSize") int trackLimitSize ,@Param("osType") OsType osType);

    List<PreferGenrePopularChnlDto> selectPreferGenrePopularChannelIdList(@Param("preferGenreIdList") List<Long> preferGenreIdList,@Param("osType") OsType osType);
//    List<PreferGenrePopularChnlDto> selectPreferGenrePopularChannel(@Param("preferGenrePopularChnlList") List<PreferGenrePopularChnlDto> preferGenrePopularChnlList,@Param("osType") OsType osType);

}
