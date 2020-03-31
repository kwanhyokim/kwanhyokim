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
import com.sktechx.godmusic.personal.rest.domain.badge.BadgeIssueDto;
import com.sktechx.godmusic.personal.rest.domain.badge.BadgeTypeDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.badge.BadgeDetailResponseDto;
import com.sktechx.godmusic.personal.rest.model.dto.badge.ChallengeBadgeResponseDto;
import com.sktechx.godmusic.personal.rest.model.vo.badge.NewBadgeExistCheckVo;
import com.sktechx.godmusic.personal.rest.repository.BadgeIssueMapper;
import com.sktechx.godmusic.personal.rest.repository.BadgeTypeMapper;
import com.sktechx.godmusic.personal.rest.service.badge.BadgeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final BadgeTypeMapper badgeTypeMapper;
    private final BadgeIssueMapper badgeIssueMapper;

    /**
     * 배지 개별 상세 조회
     */
    @Override
    public BadgeDetailResponseDto getBadgeDetailResponseDtoByBadgeIssueId(int badgeIssueId) {
        BadgeIssueDto badgeIssueDto = badgeIssueMapper.findByBadgeIssueId(badgeIssueId);
        BadgeDetailResponseDto badgeDetailResponseDto = new BadgeDetailResponseDto(badgeIssueDto);
        return this.appendIssueTargetInfoByBadgeType(badgeIssueDto, badgeDetailResponseDto);
    }

    /**
     * BadgeType별 IssueTargetType에 따른 values 덧붙이기 (현재는 BA01, BA02만 해당)
     */
    private BadgeDetailResponseDto appendIssueTargetInfoByBadgeType(BadgeIssueDto badgeIssueDto,
                                                                    BadgeDetailResponseDto badgeDetailResponseDto) {
        String badgeType = badgeIssueDto.getBadgeTypeDto().getBadgeType();
        switch (badgeType) {
            case "BA01":
                this.appendTrackIssueTargetTypeInfo(badgeIssueDto.getIssuTypeId(), badgeDetailResponseDto);
                badgeDetailResponseDto.setDescription(
                        String.format(badgeDetailResponseDto.getDescription(), badgeIssueDto.getListenCnt())
                );
                return badgeDetailResponseDto;

            case "BA02":
                this.appendTrackIssueTargetTypeInfo(badgeIssueDto.getIssuTypeId(), badgeDetailResponseDto);
                return badgeDetailResponseDto;

            default:
                return badgeDetailResponseDto;
        }
    }

    /**
     * IssueTargetType이 Track인 경우 value 덧붙이기
     */
    private void appendTrackIssueTargetTypeInfo(String issueTypeId, BadgeDetailResponseDto badgeDetailResponseDto) {
        TrackDto floTrack = metaClient.track(Long.valueOf(issueTypeId)).getData();
        badgeDetailResponseDto.setSubTitle1(floTrack.getTrackNm());
        badgeDetailResponseDto.setSubTitle2(floTrack.getArtist().getArtistName());
        badgeDetailResponseDto.setSubImgList(floTrack.getAlbum().getImgList());

        // 미권리곡일 경우
        if (YnType.Y != floTrack.getSvcStreamingYn() && YnType.Y != floTrack.getSvcDrmYn()) {
            badgeDetailResponseDto.setUiType("B");
        }
    }

    /**
     * New 배지가 있는지 체크
     */
    @Override
    public NewBadgeExistCheckVo getNewBadgeExistCheckVoByCharacterNo(Long characterNo) {
        List<BadgeIssueDto> badgeIssueList = badgeIssueMapper.findAllBadgeIssueDtimeByCharacterNo(characterNo);

        Date now = new Date();
        for (BadgeIssueDto badgeIssue : badgeIssueList) {
            int afterDays = DateUtil.getAfterDays(now, badgeIssue.getIssuDtime());
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
        Date now = new Date();

        return badgeIssueMapper.findAllNewBadgeListByCharacterNo(characterNo)
                .stream()
                .filter(badgeIssue -> DateUtil.getAfterDays(now, badgeIssue.getIssuDtime()) <= 30)
                .map(badgeIssueDto -> {
                    BadgeDetailResponseDto badgeDetailResponseDto = new BadgeDetailResponseDto(badgeIssueDto);
                    return this.appendIssueTargetInfoByBadgeType(badgeIssueDto, badgeDetailResponseDto);
                })
                .collect(Collectors.toList());
    }

    /**
     * 획득한 배지 목록 조회
     */
    @Override
    public List<BadgeDetailResponseDto> getAllReceivedBadgeList(Long characterNo) {
        return badgeIssueMapper.findAllReceivedBadgeListByCharacterNo(characterNo)
                .stream()
                .map(badgeIssueDto -> {
                    BadgeDetailResponseDto badgeDetailResponseDto = new BadgeDetailResponseDto(badgeIssueDto);
                    return this.appendIssueTargetInfoByBadgeType(badgeIssueDto, badgeDetailResponseDto);
                })
                .collect(Collectors.toList());
    }

    /**
     * 도전 중인 배지 목록 조회
     */
    @Override
    public List<ChallengeBadgeResponseDto> getAllChallengeBadgeList(Long characterNo,
                                                                    List<BadgeDetailResponseDto> receivedBadgeList) {
        List<BadgeTypeDto> badgeTypeDtoList = badgeTypeMapper.findAllBadgeType();
        Set<String> receivedBadgeTypeSet = receivedBadgeList.stream()
                .map(BadgeDetailResponseDto::getBadgeType)
                .collect(Collectors.toSet());

        return badgeTypeDtoList.stream()
                .filter(badgeTypeDto -> !receivedBadgeTypeSet.contains(badgeTypeDto.getBadgeType()))
                .map(ChallengeBadgeResponseDto::new)
                .collect(Collectors.toList());
    }

}
