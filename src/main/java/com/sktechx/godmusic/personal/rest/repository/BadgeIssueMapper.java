/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.repository;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.mybatis.annotation.BaseMapper;
import com.sktechx.godmusic.personal.rest.domain.badge.BadgeIssueDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 설명 : 칭찬(배지) 관련 mapper
 *
 * @author Groot(조민국) / dev.mingood@sk.com
 * @since 2020. 03. 26
 */
@BaseMapper
public interface BadgeIssueMapper {

    BadgeIssueDto findByBadgeIssueId(@Param("badgeIssueId") int badgeIssueId);

    List<BadgeIssueDto> findAllBadgeIssueDtimeByCharacterNo(@Param("characterNo") Long characterNo);

    void updateConfirmDtimeAndOsType(@Param("badgeIssueId") int badgeIssueId,
                                     @Param("characterNo") Long characterNo,
                                     @Param("osType") OsType osType);

    List<BadgeIssueDto> findAllNewBadgeListByCharacterNo(@Param("characterNo") Long characterNo);

    List<BadgeIssueDto> findAllReceivedBadgeListByCharacterNo(@Param("characterNo") Long characterNo);

}
