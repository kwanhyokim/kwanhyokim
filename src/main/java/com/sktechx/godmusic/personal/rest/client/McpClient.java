/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.personal.rest.client.model.OneTimeUrlResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 10. 08.
 */
@Slf4j
public class McpClient {

    @Value("${mcp.streaming.baseurl}")
    private String MCP_BASE_URL;

    private RestTemplate restTemplate;
    private ObjectMapper objectMapper = new ObjectMapper();

    public McpClient(RestTemplate restTemplate) {
        this.restTemplate = checkNotNull(restTemplate);
    }

    public CommonApiResponse<OneTimeUrlResponse> getOneTimeURL(
            @Nullable String memberNo, String deviceId, String trackId, String
            bitrate, String osType, @Nullable String serviceId, @Nullable String serviceType) {

        checkNotNull(deviceId);
        checkNotNull(trackId);
        checkNotNull(bitrate);
        checkNotNull(osType);

        String resourcePath = MCP_BASE_URL + "/v1/stream/audio";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(resourcePath)
                .pathSegment(trackId, bitrate)
                .queryParam("osType", osType)
                .queryParam("udid", deviceId);

        if (!Strings.isNullOrEmpty(memberNo)) {
            builder.queryParam("uid", memberNo);
        }

        if (!Strings.isNullOrEmpty(serviceType)) {
            builder.queryParam("svcType", serviceType);
        }

        if (!Strings.isNullOrEmpty(serviceId)) {
            builder.queryParam("svcCd", serviceId);
        }

        String targetUri = builder.toUriString();
        log.info("[MCP요청] uri = {}", targetUri);

        try {
            long startTime = System.currentTimeMillis();

            ResponseEntity<String> resultStr = restTemplate.getForEntity(targetUri, String.class);
            long endTime = System.currentTimeMillis();

            log.info("[MCP응답 OK] code = {}, body = {}, elapsed time = [{}ms]", resultStr.getStatusCode(), resultStr.getBody(), endTime - startTime);

            return getResultFromMcpResponse(resultStr.getBody());
        }
        catch (HttpClientErrorException ce) {
            CommonApiResponse<OneTimeUrlResponse> response = getResultFromMcpResponse(ce.getResponseBodyAsString());
            log.warn("[MCP응답 NOK] clientError={}, code={}, message={}, body={}", ce.getMessage(), response.getCode(), response.getMessage(), ce.getResponseBodyAsString());
            return response;
        }
        catch (HttpServerErrorException se) {
            CommonApiResponse<OneTimeUrlResponse> response = getResultFromMcpResponse(se.getResponseBodyAsString());
            log.warn("[MCP응답 NOK] serverError={}, code={}, message={}, body={}", se.getMessage(), response.getCode(), response.getMessage(), se.getResponseBodyAsString());
            return response;
        }
        catch (Exception e) {
            log.error("[MCPClient ERROR] message={}", e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    private CommonApiResponse<OneTimeUrlResponse> getResultFromMcpResponse(String response) {
        try {
            return objectMapper.readValue(response, new TypeReference<CommonApiResponse<OneTimeUrlResponse>>() {});
        }
        catch (Exception e) {
            log.error("[MCP응답 파싱 에러] response = {}", response);
            e.printStackTrace();
            throw new CommonBusinessException(CommonErrorDomain.INTERNAL_SERVER_ERROR);
        }
    }

    @PostConstruct
    private void propertyLogging() {
        log.info("[MCP.STREAMING.BASEURL]...{}", MCP_BASE_URL);
    }
}
