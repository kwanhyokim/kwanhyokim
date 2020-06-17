/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.support.error;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadBase.SizeLimitExceededException;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2020. 06. 11.
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ApiErrorResponseControllerAdvice {

    @ExceptionHandler(value = {
            MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class,
            MaxUploadSizeExceededException.class,
            ServletRequestBindingException.class
    })
    public ResponseEntity<CommonApiResponse<?>> handleBadRequestException(Exception ex, HttpServletRequest request, HttpServletResponse response) {

        log.warn("[ApiErrorResponseControllerAdvice] 파라미터전달값오류 - {}", ex.getMessage());

        CommonErrorDomain errorDomain = CommonErrorDomain.BAD_REQUEST;

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonApiResponse.builder()
                        .code(String.valueOf(errorDomain.getCode()))
                        .message(errorDomain.getDisplayMessage())
                        .traceId(MDC.get("transactionId"))
                        .errorDomain(errorDomain)
                        .build());
    }

    @SuppressWarnings("unchecked")
    @ExceptionHandler(value = {
            DuplicateKeyException.class
    })
    public ResponseEntity<CommonApiResponse<?>> handleDuplicateKeyException(DuplicateKeyException ex, HttpServletRequest request, HttpServletResponse response) {

        log.warn("[ApiErrorResponseControllerAdvice] DB중복에러발생 - {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonApiResponse.emptySuccess());
    }
}
