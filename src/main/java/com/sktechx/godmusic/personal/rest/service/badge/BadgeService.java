/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.service.badge;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.rest.model.dto.badge.BadgeDetailResponseDto;
import com.sktechx.godmusic.personal.rest.model.vo.badge.AllBadgeListResponseVo;
import com.sktechx.godmusic.personal.rest.model.vo.badge.ChallengeBadgeResponseVo;
import com.sktechx.godmusic.personal.rest.model.vo.badge.NewBadgeExistCheckVo;

import java.util.List;

/**
 * 설명 : XXXXXXXXXXX
 *
 * @author Groot(조민국) / dev.mingood@sk.com
 * @since 2020. 03. 26
 */
public interface BadgeService {

    BadgeDetailResponseDto getBadgeDetailResponseDtoByBadgeIssueId(Long characterNo, int badgeIssueId);

    NewBadgeExistCheckVo getNewBadgeExistCheckVoByCharacterNo(Long characterNo);

    void userBadgeConfirm(int badgeIssueId, Long characterNo, OsType osType);

    List<BadgeDetailResponseDto> getAllNewBadgeList(Long characterNo);

    List<BadgeDetailResponseDto> getAllReceivedBadgeList(Long characterNo);

    List<ChallengeBadgeResponseVo> getAllChallengeBadgeList(Long characterNo,
                                                            List<BadgeDetailResponseDto> receivedBadgeList);

    AllBadgeListResponseVo getAllBadgeListByCharacterNo(Long characterNo);

}
