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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.domain.type.OsType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * 통합 ResourcePlayLog 클래스 (트랙, 비디오. 오디오 등 모두 포함)
 */
@Data
@Builder
@ToString
public class ResourcePlayLogRequestParam {
    @NotNull
    @ApiModelProperty(
            name = "resourceId",
            value = "리소스 ID ex) videoId || trackId",
            example = "31228110",
            position = 1
    )
    private Long resourceId;

    @NotBlank
    @ApiModelProperty(
            name = "sourceType",
            value = "소스 타입 - STRM | DN | VIDEO_MV | VIDEO_TEASER | VIDEO_SPECIAL | VIDEO_LIVE | VIDEO_INTERVIEW | VIDEO_ETC",
            example = "STRM",
            position = 2
    )
    private String sourceType;

    @NotBlank
    @ApiModelProperty(
            name = "logType",
            value = "로그 타입 - STRT | 1MIN | MEND",
            allowableValues = "STRT | 1MIN | MEND",
            example = "MEND",
            position = 3
    )
    private String logType;

    @NotNull
    @ApiModelProperty(
            name = "osType",
            value = "OS Type(ALL, AOS, IOS, WEB)",
            allowableValues = "ALL, AOS, IOS, WEB",
            example = "AOS",
            position = 4
    )
    private OsType osType;

    @NotBlank
    @ApiModelProperty(
            name = "quality",
            value = "영상 해상도 또는 재생할 비트레이트 - aac | 192k | 320k | flac16bit | flac24bit | 480p | 720p | 1080p",
            allowableValues = "aac | 192k | 320k | flac16bit | flac24bit | 480P | 720P | 1080P",
            example = "aac",
            position = 5
    )
    private String quality;

    @Min(1)
    @Max(value = Long.MAX_VALUE)
    @ApiModelProperty(
            name = "duration",
            value = "리소스 Play Time - Duration(초단위)",
            example = "101",
            position = 6
    )
    private Long duration;

    @Min(0)
    @Max(value = Long.MAX_VALUE)
    @ApiModelProperty(
            name = "runningTimeSecs",
            value = "리소스 전체 Play 길이(초단위), 못 구할 시 0 입력",
            example = "101",
            position = 7
    )
    private Long runningTimeSecs;

    @NotNull
    @ApiModelProperty(
            name = "freeYn",
            value = "무료 여부(Y, N)",
            allowableValues = "Y | N",
            example = "N",
            position = 8
    )
    private YnType freeYn;

    @JsonProperty(defaultValue = "N")
    @ApiModelProperty(
            name = "playOfflineYn",
            value = "오프라인 여부(Y, N)",
            example = "N",
            position = 9
    )
    private YnType playOfflineYn;

    @JsonProperty(defaultValue = "N")
    @ApiModelProperty(
            name = "playCacheYn",
            value = "Cache 여부(Y, N)",
            example = "N",
            position = 10
    )
    private YnType playCacheYn;

    @ApiModelProperty(
            name = "sessionId",
            value = "세션 아이디",
            example = "2d78cd21-7a22-468c-96b6-19c310111da6",
            position = 11
    )
    private String sessionId;

    @ApiModelProperty(
            name = "sttToken",
            value = "정산 토큰",
            position = 12
    )
    private String sttToken;

    @ApiModelProperty(
            name = "cachedToken",
            value = "캐시드 스트리밍 토큰",
            position = 13
    )
    private String cachedToken;

    @ApiModelProperty(
            name = "ownerToken",
            value = "파일 원천 정보",
            position = 14
    )
    private String ownerToken;

    @ApiModelProperty(
            name = "albumId",
            value = "앨범 ID",
            example = "20193170",
            position = 15
    )
    private Long albumId;

    @ApiModelProperty(
            name = "channelId",
            value = "채널 ID",
            example = "20193170",
            position = 16
    )
    private Long channelId;

    @ApiModelProperty(
            name = "channelType",
            value = "채널 Type",
            example = "ALBUM",
            position = 17
    )
    private String channelType;

    @ApiModelProperty(
            name = "recommendTrackId",
            value = "추천곡 ID",
            position = 18
    )
    private Long recommendTrackId;

    @ApiModelProperty(
            name = "addDateTime",
            value = "추가 시간",
            example = "20191211161526",
            position = 19
    )
    private String addDateTime;

    @ApiModelProperty(
            name = "offlineStartDtime",
            value = "오프라인 시작 일시",
            example = "2019-11-12 12:10:10",
            position = 20
    )
    private String offlineStartDtime;

    @ApiModelProperty(
            name = "metaCacheUpdateDtime",
            value = "meta cache 업데이트 일시",
            example = "2019-11-12 12:10:10",
            position = 21
    )
    private String metaCacheUpdateDtime;

    @ApiModelProperty(
            name = "freeCachedStreamingToken",
            value = "캐시드 스트리밍 곡이 무료곡일 경우 serviceId 토큰",
            position = 22
    )
    private String freeCachedStreamingToken;

    @JsonIgnore
    public boolean isFree() {
        return YnType.Y == freeYn;
    }

    @JsonIgnore
    public String getOsTypeToStr() {
        return Optional.ofNullable(osType).map(OsType::getCode).orElse("");
    }

    @JsonIgnore
    public String getClientIp() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String clientIp = request.getHeader("client_ip");
        if (StringUtils.isEmpty(clientIp)) {
            clientIp = request.getHeader("x-gm-client-ip");
        }
        return StringUtils.defaultIfBlank(clientIp, StringUtils.EMPTY);
    }

}
