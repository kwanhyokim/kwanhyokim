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
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.personal.common.util.DateUtil;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.domain.badge.BadgeIssueDto;
import com.sktechx.godmusic.personal.rest.domain.badge.BadgeTypeDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.badge.AllBadgeListResponseVo;
import com.sktechx.godmusic.personal.rest.model.dto.badge.BadgeDetailResponseDto;
import com.sktechx.godmusic.personal.rest.model.vo.badge.ChallengeBadgeResponseVo;
import com.sktechx.godmusic.personal.rest.model.vo.badge.NewBadgeExistCheckVo;
import com.sktechx.godmusic.personal.rest.repository.BadgeIssueMapper;
import com.sktechx.godmusic.personal.rest.repository.BadgeTypeMapper;
import com.sktechx.godmusic.personal.rest.service.badge.BadgeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
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
    public BadgeDetailResponseDto getBadgeDetailResponseDtoByBadgeIssueId(Long characterNo, int badgeIssueId) {
        BadgeIssueDto badgeIssueDto = badgeIssueMapper.findByCharacterNoAndBadgeIssueId(characterNo, badgeIssueId);
        if (badgeIssueDto == null) {
            throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);
        }
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
                badgeDetailResponseDto.setDescription(
                        String.format(badgeDetailResponseDto.getDescription(), badgeIssueDto.getListenCnt())
                );
                this.appendTrackIssueTargetTypeInfo(badgeIssueDto, badgeDetailResponseDto);
                log.debug("### BA01 {}", badgeDetailResponseDto);
                return badgeDetailResponseDto;

            case "BA02":
                this.appendTrackIssueTargetTypeInfo(badgeIssueDto, badgeDetailResponseDto);
                return badgeDetailResponseDto;

            default:
                return badgeDetailResponseDto;
        }
    }

    /**
     * IssueTargetType이 Track인 경우 value 덧붙이기
     */
    private void appendTrackIssueTargetTypeInfo(BadgeIssueDto badgeIssueDto,
                                                BadgeDetailResponseDto badgeDetailResponseDto) {

        TrackDto floTrack = metaClient.track(badgeIssueDto.getIssuTypeIdToLong()).getData();

        // 미권리곡일 경우
        if (null == floTrack ||
                YnType.Y != floTrack.getSvcStreamingYn() && YnType.Y != floTrack.getSvcDrmYn()) {

            log.debug("### BadgeDto {}", badgeIssueDto.getBadgeDto());
            badgeDetailResponseDto.setNonRightTrackInfo(badgeIssueDto);

        } else {
            badgeDetailResponseDto.setRightTrackInfo(floTrack);
        }
    }

    /**
     * New 배지가 있는지 체크
     */
    @Override
    public NewBadgeExistCheckVo getNewBadgeExistCheckVoByCharacterNo(Long characterNo) {
        List<BadgeIssueDto> badgeIssueList = badgeIssueMapper.findAllBadgeIssueDtimeByCharacterNo(characterNo);

        if (!CollectionUtils.isEmpty(badgeIssueList)) {
            Date now = new Date();
            for (BadgeIssueDto badgeIssue : badgeIssueList) {
                int afterDays = DateUtil.getAfterDays(now, badgeIssue.getIssuDtime());
                if (afterDays <= 30) {
                    return new NewBadgeExistCheckVo(true);
                }
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
        List<BadgeIssueDto> allNewBadgeList = badgeIssueMapper.findAllNewBadgeListByCharacterNo(characterNo);
        if (CollectionUtils.isEmpty(allNewBadgeList)) {
            throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);
        }

        Date now = new Date();
        return allNewBadgeList.stream()
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
    @SuppressWarnings("unchecked")
    @Override
    public List<BadgeDetailResponseDto> getAllReceivedBadgeList(Long characterNo) {
        List<BadgeIssueDto> allReceivedBadgeList = badgeIssueMapper.findAllReceivedBadgeListByCharacterNo(characterNo);
        if (CollectionUtils.isEmpty(allReceivedBadgeList)) {
            return Collections.EMPTY_LIST;
        }

        return allReceivedBadgeList.stream()
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
    public List<ChallengeBadgeResponseVo> getAllChallengeBadgeList(Long characterNo,
                                                                   List<BadgeDetailResponseDto> receivedBadgeList) {

        List<BadgeTypeDto> badgeTypeDtoList = badgeTypeMapper.findAllBadgeType();
        Set<String> receivedBadgeTypeSet = receivedBadgeList.stream()
                .map(BadgeDetailResponseDto::getBadgeType)
                .collect(Collectors.toSet());

        return badgeTypeDtoList.stream()
                .filter(badgeTypeDto -> !receivedBadgeTypeSet.contains(badgeTypeDto.getBadgeType()))
                .map(ChallengeBadgeResponseVo::new)
                .collect(Collectors.toList());
    }

    /**
     * 도전중인 배지, 획득한 배지 전체 조회
     */
    @Override
    public AllBadgeListResponseVo getAllBadgeListByCharacterNo(Long characterNo) {
        List<BadgeDetailResponseDto> allReceivedBadgeList = this.getAllReceivedBadgeList(characterNo);
        List<ChallengeBadgeResponseVo> allChallengeBadgeList = this.getAllChallengeBadgeList(characterNo, allReceivedBadgeList);
        return new AllBadgeListResponseVo(allReceivedBadgeList, allChallengeBadgeList);
    }

}
