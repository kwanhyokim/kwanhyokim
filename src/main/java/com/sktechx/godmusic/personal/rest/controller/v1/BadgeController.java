/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.controller.v1;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.model.dto.badge.BadgeDetailListResponseDto;
import com.sktechx.godmusic.personal.rest.model.dto.badge.BadgeDetailResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 설명 : 칭찬하기(배지) Controller
 *
 * @author Groot(조민국) / dev.mingood@sk.com
 * @since 2020. 03. 24
 */
@Slf4j
@Api(tags = "칭찬하기(배지) API")
@RequiredArgsConstructor
@RestController
@RequestMapping(Naming.serviceCode + "/v1/badge")
public class BadgeController {

    // TODO 캐릭터명은 노출하지 않는다.

    @ApiOperation(value = "캐릭터별 배지 전체 리스트 조회(획득 + 미획득)")
    @GetMapping("/list/all")
    public CommonApiResponse<?> getBadgeList() {
        Long characterNo = GMContext.getContext().getCharacterNo();

        // TODO 배지 전체 조회 로직 (획득 + 미획독 포함) - 아직 UI 미확정
        return new CommonApiResponse<>();
    }

    @ApiOperation(value = "배지 상세 조회")
    @GetMapping("/{badgeId}")
    public CommonApiResponse<BadgeDetailResponseDto> getBadgeDetail(@PathVariable("badgeId") String badgeId) {
        Long characterNo = GMContext.getContext().getCharacterNo();

        // TODO 배지 개별 조회 로직
        return new CommonApiResponse<>(new BadgeDetailResponseDto());
    }

    @ApiOperation(value = "배지 확인")
    @PutMapping("/confirm")
    private CommonApiResponse<?> confirmMyNewBadge(@RequestParam("badgeIssueId") String badgeIssueId) {
        GMContext gmContext = GMContext.getContext();
        Long characterNo = gmContext.getCharacterNo();
        OsType osType = gmContext.getOsType();

        // TODO tb_badge_issue update 로직 (confirmYn, osType, confirmDtime, updateDtime)
        return CommonApiResponse.emptySuccess();
    }

    @ApiOperation(value = "캐릭터별 New 배지 리스트 조회")
    @GetMapping("/list/new")
    public CommonApiResponse<BadgeDetailListResponseDto> getNewBadgeList() {
        Long characterNo = GMContext.getContext().getCharacterNo();

        // TODO confirmYn != Y인 배지 리스트 조회 (배지 생성일 30일이 넘지 않은 것만 해당)
        return new CommonApiResponse<>(new BadgeDetailListResponseDto());
    }

}
