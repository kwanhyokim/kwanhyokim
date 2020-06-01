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
import com.sktechx.godmusic.personal.rest.model.vo.badge.AllBadgeListResponseVo;
import com.sktechx.godmusic.personal.rest.model.vo.badge.BadgeDetailListResponseVo;
import com.sktechx.godmusic.personal.rest.model.dto.badge.BadgeDetailResponseDto;
import com.sktechx.godmusic.personal.rest.model.vo.badge.NewBadgeExistCheckVo;
import com.sktechx.godmusic.personal.rest.service.badge.BadgeService;
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

import java.util.List;

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

    private final BadgeService badgeService;

    @ApiOperation(value = "캐릭터별 배지 전체 리스트 조회(획득 + 미획득)")
    @GetMapping("/list/all")
    public CommonApiResponse<AllBadgeListResponseVo> getBadgeList() {
        Long characterNo = GMContext.getContext().getCharacterNo();
        return new CommonApiResponse<>(badgeService.getAllBadgeListByCharacterNo(characterNo));
    }

    @ApiOperation(value = "New 배지 리스트 조회 (상세 팝업 용도)")
    @GetMapping("/list/new")
    public CommonApiResponse<BadgeDetailListResponseVo> getNewBadgeList() {
        Long characterNo = GMContext.getContext().getCharacterNo();
        List<BadgeDetailResponseDto> allNewBadgeList = badgeService.getAllNewBadgeList(characterNo);
        return new CommonApiResponse<>(new BadgeDetailListResponseVo(allNewBadgeList));
    }

    @ApiOperation(value = "배지 상세 조회")
    @GetMapping("/{badgeIssueId}")
    public CommonApiResponse<BadgeDetailResponseDto> getBadgeDetail(@PathVariable("badgeIssueId") int badgeIssueId) {
        Long characterNo = GMContext.getContext().getCharacterNo();
        return new CommonApiResponse<>(badgeService.getBadgeDetailResponseDtoByBadgeIssueId(characterNo, badgeIssueId));
    }

    @ApiOperation(value = "배지 확인")
    @PutMapping("/confirm")
    public CommonApiResponse<?> confirmMyNewBadge(@RequestParam("badgeIssueId") int badgeIssueId) {
        GMContext gmContext = GMContext.getContext();
        Long characterNo = gmContext.getCharacterNo();
        OsType osType = gmContext.getOsType();

        badgeService.userBadgeConfirm(badgeIssueId, characterNo, osType);
        return CommonApiResponse.emptySuccess();
    }

    @ApiOperation(value = "New 배지 존재 여부 체크 (빈번한 호출용)")
    @GetMapping("/check/new")
    public CommonApiResponse<NewBadgeExistCheckVo> checkNewBadgeExist() {
        Long characterNo = GMContext.getContext().getCharacterNo();
        return new CommonApiResponse<>(badgeService.getNewBadgeExistCheckVoByCharacterNo(characterNo));
    }

}
