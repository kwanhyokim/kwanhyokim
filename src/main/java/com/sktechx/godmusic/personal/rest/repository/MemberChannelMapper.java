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
import com.sktechx.godmusic.personal.rest.model.dto.MemberChannelDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 8. 6.
 */
@BaseMapper
public interface MemberChannelMapper {

    List<Long> selectMemberChannelIdList(@Param("memberNo")Long characterNo, @Param("excludeChannelId")Long excludeChannelId, @Param("pageable")Pageable pageable);

    List<MemberChannelDto> selectMemberChannelList(@Param("channelIdList") List<Long> channelIdList);

    int selectMemberChannelTotalCount(@Param("memberNo") Long characterNod);

    MemberChannelDto selectMemberChannel(@Param("memberNo")Long characterNo, @Param("channelId")Long memberChannelId);

    int selectMemberChannelCount(@Param("memberNo")Long characterNo);

    int selectMemberChannelEqualsName(@Param("memberNo") Long characterNo, @Param("channelName")String memberChannelName);

    List<String> selectMemberChannelLikeNameList(@Param("memberNo") Long characterNo, @Param("channelName") String memberChannelName);

    void insertMemberChannel(@Param("memberNo")Long characterNo, @Param("memberChannelDto")MemberChannelDto memberChannelDto);

    void deleteMemberChannel(@Param("memberNo")Long characterNo, @Param("channelIdList") List<Long> memberChannelIdList);

    void updateMemberChannel(@Param("memberNo")Long characterNo, @Param("channelId")Long memberChannelId, @Param("channelName")String memberChannelName);

    void updateMemberChannelList(@Param("memberNo")Long characterNo,
            @Param("channelId")Long memberChannelId,
            @Param("viewPriority")Integer viewPriority,
            @Param("albumId")Long albumId,
            @Param("updateTrackCount")boolean updateTrackCount,
            @Param("updatePlayTime")boolean updatePlayTime,
            @Param("updateDateTime")Date updateDateTime);

    Long selectMemberChannelAlbumId(@Param("channelId")Long memberChannelId);

    void updateMemberChannelImg(@Param("channelId")Long memberChannelId, @Param("viewPriority")Integer viewPriority);
}

