/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.client;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.common.config.FeignDefaultConfig;
import com.sktechx.godmusic.personal.common.domain.type.AwsBucketType;
import com.sktechx.godmusic.personal.rest.client.fallback.ExternalClientFallbackFactory;
import com.sktechx.godmusic.personal.rest.model.vo.external.AwsFileVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * 설명 : External Client
 *
 * @author Groot(조민국) / dev.mingood@sk.com
 * @since 2020. 04. 24
 */
@FeignClient(
        value = "external-api",
        fallbackFactory = ExternalClientFallbackFactory.class,
        configuration = FeignDefaultConfig.class
)
public interface ExternalClient {

    @GetMapping("/external/v1/ping")
    CommonApiResponse<Void> ping();

    @PostMapping(value = "/external/v1/ocr/ocr-recognize")
    CommonApiResponse ocrRecognize(@RequestParam("ocrNo") Long ocrNo,
                                   @RequestParam("imageCount") Integer imageCount,
                                   @RequestParam("ocrFileNo") Integer ocrFileNo,
                                   @RequestParam("bucketKey") String bucketKey,
                                   @RequestParam("bucketName") String bucketName);

    @PostMapping(value = "/external/v1/aws/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    CommonApiResponse<?> createOcrFile(@RequestPart(value = "file") MultipartFile file,
                                               @RequestParam("awsBucketType") AwsBucketType awsBucketType,
                                               @RequestParam("memberNo") Long memberNo);

}
