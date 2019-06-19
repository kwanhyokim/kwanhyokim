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

package com.sktechx.godmusic.personal.rest.repository;

import com.sktechx.godmusic.lib.mybatis.annotation.BaseMapper;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 8. 6.
 */
@BaseMapper
public interface MemberChannelTrackMapper {
    List<TrackDto> selectMemberChannelTrackList(@Param("memberNo") Long memberNo, @Param("characterNo") Long characterNo, @Param("channelId") Long memberChannelId, @Param("pageable") Pageable pageable);

    int selectMemberChannelTrackListCount(@Param("memberNo") Long memberNo, @Param("characterNo") Long characterNo, @Param("channelId") Long memberChannelId);

    void deleteMapMemberChannelTrack(@Param("memberNo")Long memberNo, @Param("characterNo") Long characterNo, @Param("channelIdList") List<Long> memberChannelIdList);

    void deleteTrack(@Param("channelId")Long channelId, @Param("trackIdList")List<Long> trackIdList);

    int selectTrackTotalCount(@Param("channelId")Long memberChannelId);

    int selectMaxTrackViewPriority(@Param("channelId")Long memberChannelId);

    int insertTrackMemberChannel(@Param("channelId")Long memberChannelId, @Param("trackId")Long trackId, @Param("viewPriority")Integer viewPriority);

    List<Long> selectMemberChannelTrackIdList(@Param("memberNo")Long memberNo, @Param("characterNo") Long characterNo, @Param("channelId")Long memberChannelId);

    void insertSelectMemberChannelTrack(@Param("fromMemberChannelId") Long fromMemberChannelId, @Param("toMemberChannelId") Long toMemberChannelId);
}
