/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.client.fallback;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.domain.type.AwsBucketType;
import com.sktechx.godmusic.personal.common.exception.PersonalErrorDomain;
import com.sktechx.godmusic.personal.rest.client.ExternalClient;
import com.sktechx.godmusic.personal.rest.model.vo.external.AwsFileVo;
import feign.FeignException;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 설명 : external 클라이언트 fallback
 *
 * @author Groot(조민국) / dev.mingood@sk.com
 * @since 2020. 04. 24
 */
@Slf4j
@Component
public class ExternalClientFallbackFactory implements FallbackFactory<ExternalClient> {

    @Override
    public ExternalClient create(Throwable throwable) {
        return new ExternalClient() {
            @Override
            public CommonApiResponse<Void> ping() {
                log.warn("[WARM-UP] ... external Ping 호출 실패. message={}", throwable.getMessage());
                return null;
            }

            @Override
            public CommonApiResponse ocrRecognize(Long ocrNo,
                                                  Integer imageCount,
                                                  Integer ocrFileNo,
                                                  String bucketKey,
                                                  String bucketName) {
                log.error("[ocrRecognize] 호출 실패, message={}", throwable.getMessage());
                return null;
            }

            @Override
            public CommonApiResponse createOcrFile(MultipartFile file,
                                                              AwsBucketType awsBucketType,
                                                              Long memberNo) {

                // 4XX error warn 처리
                FeignException fe = ((FeignException) throwable);
                int status = fe.status();

                if (HttpStatus.valueOf(status).is4xxClientError()) {
                    log.warn("[createOcrFile] 호출 실패, status={}, message={}", status, fe.getMessage());

                    // return CommonApiResponse.buildError(PersonalErrorDomain.FAIL_UPLOAD_OCR_FILE, HttpStatus.valueOf(status));
                    return CommonApiResponse.builder()
                            .errorDomain(PersonalErrorDomain.FAIL_UPLOAD_OCR_FILE)
                            .build();
                }
                else {
                    log.error("[createOcrFile] 호출 실패, message={}", throwable.getMessage());
                    return null;
                }
            }
        };
    }

}
