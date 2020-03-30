/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.service.badge.impl;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.util.DateUtil;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.badge.BadgeDetailResponseDto;
import com.sktechx.godmusic.personal.rest.model.dto.badge.BadgeIssueDto;
import com.sktechx.godmusic.personal.rest.model.vo.badge.NewBadgeExistCheckVo;
import com.sktechx.godmusic.personal.rest.repository.BadgeIssueMapper;
import com.sktechx.godmusic.personal.rest.service.badge.BadgeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 설명 : XXXXXXXXXXX
 *
 * @author Groot(조민국) / dev.mingood@sk.com
 * @since 2020. 03. 26
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BadgeServiceImpl implements BadgeService {

    private final MetaClient metaClient;
    private final BadgeIssueMapper badgeIssueMapper;

    /**
     * 배지 개별 상세 조회
     *
     * @return
     */
    @Override
    public BadgeDetailResponseDto getBadgeDetailResponseDtoByBadgeIssueId(int badgeIssueId) {
        BadgeIssueDto badgeIssueDto = badgeIssueMapper.findByBadgeIssueId(badgeIssueId);
        BadgeDetailResponseDto badgeDetailResponseDto = new BadgeDetailResponseDto(badgeIssueDto);

        String badgeType = badgeIssueDto.getBadgeTypeDto().getBadgeType();
        switch (badgeType) {
            case "BA01":
                badgeDetailResponseDto.setDescription(
                        String.format(badgeDetailResponseDto.getDescription(), badgeIssueDto.getListenCnt())
                );
                return this.appendToBadgeDetailResponseDtoByTrack(badgeIssueDto, badgeDetailResponseDto);

            case "BA02":
                return this.appendToBadgeDetailResponseDtoByTrack(badgeIssueDto, badgeDetailResponseDto);

            default:
                return badgeDetailResponseDto;
        }
    }

    /**
     * BA01, BA02 일 경우
     */
    private BadgeDetailResponseDto appendToBadgeDetailResponseDtoByTrack(BadgeIssueDto badgeIssueDto,
                                                                         BadgeDetailResponseDto badgeDetailResponseDto) {
        TrackDto trackDto = metaClient.track(Long.valueOf(badgeIssueDto.getIssuTypeId())).getData();
        badgeDetailResponseDto.setSubTitle1(trackDto.getTrackNm());
        badgeDetailResponseDto.setSubTitle2(trackDto.getArtist().getArtistName());
        badgeDetailResponseDto.setSubImgList(trackDto.getAlbum().getImgList());

        // 미권리곡일 경우
        if (YnType.Y != trackDto.getSvcStreamingYn() && YnType.Y != trackDto.getSvcDrmYn()) {
            badgeDetailResponseDto.setUiType("B");
        }
        return badgeDetailResponseDto;
    }

    /**
     * New 배지가 있는지 체크
     */
    @Override
    public NewBadgeExistCheckVo getNewBadgeExistCheckVoByCharacterNo(Long characterNo) {
        List<BadgeIssueDto> badgeIssueList = badgeIssueMapper.findBadgeIssueByCharacterNo(characterNo);

        Date now = new Date();
        for (BadgeIssueDto badgeIssue : badgeIssueList) {
            int afterDays = DateUtil.getAfterDays(now, badgeIssue.getIssuDtime());
            log.debug("### afterDays={}", afterDays);
            if (afterDays <= 30) {
                return new NewBadgeExistCheckVo(true);
            }
        }
        return new NewBadgeExistCheckVo(false);
    }

    /**
     * 배지 확인 처리
     */
    @Override
    public void userBadgeConfirm(int badgeIssueId, Long characterNo, OsType osType) {
        badgeIssueMapper.updateConfirmDtimeAndOsType(badgeIssueId, characterNo, osType);
    }

    /**
     * New 배지 리스트 조회
     */
    @Override
    public List<BadgeDetailResponseDto> getAllNewBadgeList(Long characterNo) {
        List<BadgeDetailResponseDto> resultNewBadgeList = new ArrayList<>();
        List<BadgeIssueDto> allNewBadgeList = badgeIssueMapper.findAllNewBadgeList(characterNo);

        Date now = new Date();
        for (BadgeIssueDto badgeIssue : allNewBadgeList) {
            int afterDays = DateUtil.getAfterDays(now, badgeIssue.getIssuDtime());
            if (afterDays <= 30) {
                resultNewBadgeList.add(new BadgeDetailResponseDto(badgeIssue));
            }
        }
        return resultNewBadgeList;
    }

}
