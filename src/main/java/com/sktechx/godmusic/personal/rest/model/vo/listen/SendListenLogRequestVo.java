/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.model.vo.listen;

import lombok.Builder;
import lombok.Data;

/**
 * 설명 : Kafka에 Send하기 위한 Vo
 *
 * @author Groot(조민국) / dev.mingood@sk.com
 * @since 2020. 04. 24
 */
@Data
@Builder
public class SendListenLogRequestVo {
    private Long resourceId;
    private String sourceType;
    private String logType;
    private String osType;
    private String quality;
    private Long duration;
    private Long runningTimeSecs;
    private String freeYn;
    private String playOfflineYn;
    private String playCachedYn;
    private String sessionId;
    private String sttToken;
    private String cachedStreamingToken;
    private String ownerToken;
    private Long albumId;
    private Long channelId;
    private String channelType;
    private Long recommendTrackId;
    private String addDateTime;
    private String offlineStartDtime;
    private String metaCachedUpdateDtime;
    private String freeCachedStreamingToken;
    private String traceType;
    private String buildNumber;
}
