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

    @Override
    public BadgeDetailResponseDto getBadgeDetailResponseDtoByBadgeIssueId(int badgeIssueId) {
        BadgeIssueDto badgeIssueDto = badgeIssueMapper.findByBadgeIssueId(badgeIssueId);
        BadgeDetailResponseDto badgeDetailResponseDto = new BadgeDetailResponseDto(badgeIssueDto);

        String badgeType = badgeIssueDto.getBadgeTypeDto().getBadgeType();
        switch (badgeType) {
            case "BA01":
                TrackDto vipTrackDto = metaClient.track(Long.valueOf(badgeIssueDto.getIssuTypeId())).getData();
                badgeDetailResponseDto.setDescription(
                        String.format(badgeDetailResponseDto.getDescription(), badgeIssueDto.getListenCnt())
                );
                badgeDetailResponseDto.setSubTitle1(vipTrackDto.getTrackNm());
                badgeDetailResponseDto.setSubTitle2(vipTrackDto.getArtist().getArtistName());
                badgeDetailResponseDto.setSubImgList(vipTrackDto.getAlbum().getImgList());

                // 미권리곡일 경우
                if (YnType.Y != vipTrackDto.getSvcStreamingYn() && YnType.Y != vipTrackDto.getSvcDrmYn()) {
                    badgeDetailResponseDto.setUiType("B");
                }
                return badgeDetailResponseDto;
            case "BA02":
                TrackDto supporterTrackDto = metaClient.track(Long.valueOf(badgeIssueDto.getIssuTypeId())).getData();
                badgeDetailResponseDto.setSubTitle1(supporterTrackDto.getTrackNm());
                badgeDetailResponseDto.setSubTitle2(supporterTrackDto.getArtist().getArtistName());
                badgeDetailResponseDto.setSubImgList(supporterTrackDto.getAlbum().getImgList());

                // 미권리곡일 경우
                if (YnType.Y != supporterTrackDto.getSvcStreamingYn() && YnType.Y != supporterTrackDto.getSvcDrmYn()) {
                    badgeDetailResponseDto.setUiType("B");
                }
                return badgeDetailResponseDto;
            default:
                return badgeDetailResponseDto;
        }
    }

    @Override
    public NewBadgeExistCheckVo getNewBadgeExistCheckVoByCharacterNo(Long characterNo) {
        List<BadgeIssueDto> badgeIssueList = badgeIssueMapper.findBadgeIssueByCharacterNo(characterNo);
        Date now = new Date();
        for (BadgeIssueDto badgeIssue : badgeIssueList) {
            log.debug("### badgeIssue={}", badgeIssue);
            int afterDays = DateUtil.getAfterDays(now, badgeIssue.getIssuDtime());
            log.debug("### afterDays={}", afterDays);
            if (afterDays <= 30) {
                return new NewBadgeExistCheckVo(true);
            }
        }
        return new NewBadgeExistCheckVo(false);
    }

}
