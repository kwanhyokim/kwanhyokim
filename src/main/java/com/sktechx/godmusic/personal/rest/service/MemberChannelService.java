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

package com.sktechx.godmusic.personal.rest.service;

import com.sktechx.godmusic.personal.common.domain.type.AppNameType;
import com.sktechx.godmusic.personal.rest.model.dto.MemberChannelDto;
import com.sktechx.godmusic.personal.rest.model.vo.myplaylist.MyPlaylistRetriveAllResponse;
import com.sktechx.godmusic.personal.rest.model.vo.myplaylist.MyPlaylistTrackCreateResponse;
import com.sktechx.godmusic.personal.rest.model.vo.myplaylist.MyPlaylistTrackRetrieveAllResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 8. 6.
 */
public interface MemberChannelService {

    MyPlaylistRetriveAllResponse getMemberChannelPageImpl(Long memberNo, Long characterNo, Long channelId, Pageable pageable);

    MyPlaylistTrackRetrieveAllResponse getMemberChannelTrackList(Long memberNo, Long characterNo, Long memberChannelId , Pageable pageable);

    MemberChannelDto getMemberChannel(Long memberNo, Long characterNo, Long memberChannelId);

    MemberChannelDto createMemberChannel(Long memberNo, Long characterNo, String memberChannelName);

    void modifyMemberChannelList(Long memberNo, Long characterNo, List<Long> viewPriorityChannelIdList);

    void removeMemberChannel(Long memberNo, Long characterNo, List<Long> memberChannelIdList);

    void modifyMemberChannel(Long memberNo, Long characterNo, Long memberChannelId, String memberChannelName);

    MemberChannelDto removeTrackList(AppNameType appName, Long memberNo, Long characterNo, Long memberChannelId, List<Long> trackIdList);

    MyPlaylistTrackCreateResponse addTrackList(AppNameType appName, Long memberNo, Long characterNo, Long memberChannelId, List<Long> trackIdList);

    MemberChannelDto modifyTrackList(Long memberNo, Long characterNo,  Long memberChannelId, List<Long> trackIdList);
}
