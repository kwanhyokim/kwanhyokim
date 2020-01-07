/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.personal.common.domain.type.BitrateType;
import com.sktechx.godmusic.personal.common.domain.type.SourceType;
import com.sktechx.godmusic.personal.rest.model.dto.listen.SettlementInfoDto;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenTrackRequest;
import com.sktechx.godmusic.personal.rest.model.vo.listen.play.ResourcePlayLogRequestParam;
import com.sktechx.godmusic.personal.rest.repository.SettlementMapper;
import com.sktechx.godmusic.personal.rest.service.SettlementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

/**
 * 정산 서비스 구현체
 * 향후 MSA 정산 서버로 변경 필요
 *
 * @author M.Ryan (sanghyun.park.tx@sk.com)
 * @date 2019. 6. 24.
 */
@Slf4j
@Service
public class SettlementServiceImpl implements SettlementService {

    private final String FLAC_ALTERTIVE_STREAMING_SERVCIE_ID = "TS2";
    private final String FLAC_ALTERTIVE_DRM_SERVCIE_ID = "TD2";
    private final String FLAC_ALTERTIVE_MUSIC_VIDEOD_SERVCIE_ID = "TM2";

    private final SettlementMapper settlementMapper;

    public SettlementServiceImpl(SettlementMapper settlementMapper) {
        this.settlementMapper = settlementMapper;
    }

    @Override
    public String getServiceCode(Long memberNo, String settlementTypeCode) {
        SettlementInfoDto settlementInfo = this.getSettlementInfo(memberNo, settlementTypeCode);
        return this.getServiceCode(settlementInfo);
    }

    @Override
    public String getServiceCodeByPrchsId(Long prchsId, String settlementTypeCode) {
        SettlementInfoDto settlementInfo = settlementMapper.selectSettlementInfoByPrchsId(prchsId, settlementTypeCode);
        return this.getServiceCode(settlementInfo);
    }

    @Override
    public SettlementInfoDto getSettlementInfo(Long memberNo, String settlementTypeCode) {
        return settlementMapper.selectSettlementInfo(memberNo, settlementTypeCode);
    }

    @Override
    public String evaluateServiceId(ListenTrackRequest request, SettlementInfoDto settlement) {
        if (ObjectUtils.isEmpty(request.getBitrate())) {
            log.info("1분 청취 요청 Bitrate 없음");
            return settlement.getSvcId();
        }

        if (ObjectUtils.isEmpty(request.getSourceType())) {
            log.info("1분 청취 요청 SourceType 없음");
            return settlement.getSvcId();
        }

        // flac 요청일 경우 대체
        if (request.getBitrate() == BitrateType.BITRATE_FLAC16 || request.getBitrate() == BitrateType.BITRATE_FLAC24) {
            switch (request.getSourceType()) {
                case STRM:
                    return FLAC_ALTERTIVE_STREAMING_SERVCIE_ID;
                case DN:
                    return FLAC_ALTERTIVE_DRM_SERVCIE_ID;
                case MV:    // TODO 이 MV는 무엇?
                    return FLAC_ALTERTIVE_MUSIC_VIDEOD_SERVCIE_ID;
            }
        }
        return settlement.getSvcId();
    }

    @Override
    public String evaluateServiceIdByResourcePlayLogRequest(ResourcePlayLogRequestParam param,
                                                            SettlementInfoDto settlementInfo) {
        if (ObjectUtils.isEmpty(param.getQuality())) {
            log.info("1분 청취 요청 Bitrate 없음");
            return settlementInfo.getSvcId();
        }

        if (ObjectUtils.isEmpty(param.getSourceType())) {
            log.info("1분 청취 요청 SourceType 없음");
            return settlementInfo.getSvcId();
        }

        // flac 요청일 경우 대체
        String quality = param.getQuality();
        if (BitrateType.BITRATE_FLAC16.getCode().equalsIgnoreCase(quality)
                || BitrateType.BITRATE_FLAC24.getCode().equalsIgnoreCase(quality)) {

            switch (SourceType.fromCode(param.getSourceType())) {
                case STRM:
                    return FLAC_ALTERTIVE_STREAMING_SERVCIE_ID;
                case DN:
                    return FLAC_ALTERTIVE_DRM_SERVCIE_ID;
                case MV:    // TODO 이 MV는 무엇?
                    return FLAC_ALTERTIVE_MUSIC_VIDEOD_SERVCIE_ID;
            }

        }
        return settlementInfo.getSvcId();
    }

    private String getServiceCode(SettlementInfoDto settlement) {
        return Optional.ofNullable(settlement)
                .map(SettlementInfoDto::getSvcId)
                .orElse(null);
    }

}
