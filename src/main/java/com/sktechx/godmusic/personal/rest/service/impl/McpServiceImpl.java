/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.rest.client.StreamClient;
import com.sktechx.godmusic.personal.rest.client.model.OneTimeUrlDto;
import com.sktechx.godmusic.personal.rest.service.McpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 설명 : XXXXXXXXXXX
 *
 * @author groot
 * @since 2019. 12. 19
 */
@Slf4j
@Service
public class McpServiceImpl implements McpService {

    private final StreamClient streamClient;

    public McpServiceImpl(StreamClient streamClient) {
        this.streamClient = streamClient;
    }

    @Override
    public String getServiceCodeFromMCP(Long trackId, String bitrate, String osType) {
        CommonApiResponse<OneTimeUrlDto> oneTimeUrlResponse = streamClient.getTrackStreamingUrl(
                trackId, bitrate, osType, null, null
        );
        log.info("[TRACK 청취로그][MCP 조회 응답] {}", oneTimeUrlResponse);

        if (oneTimeUrlResponse != null && oneTimeUrlResponse.getData() != null) {
            return oneTimeUrlResponse.getData().getSvcCd();
        }
        return null;
    }

}
