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

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.mybatis.annotation.BaseMapper;
import com.sktechx.godmusic.personal.common.domain.type.PopularChnlType;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.LastListenHistoryDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.MoodPopularChnlListDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.PreferGenrePopularChnlListDto;

/**
 * 설명 :  채널 Repository
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 19.
 */
@BaseMapper
public interface ChannelMapper {
    List<Long> selectPopularChannelIdList();
    List<ChnlDto> selectChannelListByIdList(@Param("channelIdList") List<Long> channelIdList,
                                            @Param("trackLimitSize") int trackLimitSize ,
                                            @Param("osType") OsType osType ,
                                            @Param("popularChnlType") PopularChnlType popularChnlType);

    List<PreferGenrePopularChnlListDto> selectAllPreferGenrePopularChannelIdList();
    List<MoodPopularChnlListDto> selectAllMoodPopularChannelIdList();

    List<LastListenHistoryDto> selectLastListenHistory(@Param("memberNo") Long memberNo, @Param("characterNo") Long characterNo, @Param("osType") OsType osType);
    List<LastListenHistoryDto> selectLastListenHistoryByChannel(@Param("memberNo") Long memberNo, @Param("characterNo") Long characterNo, @Param("osType") OsType osType, @Param("exceptFlacChnl") Boolean exceptFlacChnl);

//    List<PreferGenrePopularChnlDto> selectPreferGenrePopularChannelIdList(@Param("preferGenreIdList") List<Long> preferGenreIdList,@Param("osType") OsType osType);
//    List<PreferGenrePopularChnlDto> selectPreferGenrePopularChannel(@Param("preferGenrePopularChnlList") List<PreferGenrePopularChnlDto> preferGenrePopularChnlList,@Param("osType") OsType osType);

    ChnlDto selectChannelById(@Param("channelId") Long channelId);

    List<ChnlDto> selectChannelByIds(@Param("channelIdList") List<Long> channelIdList);

    void deleteLastListenHistory(@Param("memberNo") Long memberNo, @Param("characterNo") Long characterNo, @Param("listenType") String listenType, @Param("listenTypeId") Long listenTypeId);

    List<Long> selectFloAndDataChannelRecentId();
    ChnlDto selectFlacChannelById(@Param("channelId") Long channelId);

}
