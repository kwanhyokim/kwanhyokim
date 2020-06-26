package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.exception.ErrorDomain;
import com.sktechx.godmusic.personal.common.exception.PersonalErrorDomain;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * 설명 :
 *
 * @author 이민호(Mark) / minholee93@sk.com
 * 2020/06/26
 * 5:59 오후
 */


public class OcrServiceImplTest {

    @Before
    public void test(){

    }


    /**
     * CommonApiResponse의 errorDomain이 null인 경우 -> is4xxClientError가 존재하지 않는다.
     */
    @Test
    public void commonApiReponse_erroDomain_isNull_then_is4xxClientError_isFalse(){

        // given
       CommonApiResponse response = CommonApiResponse.builder()
                                                     // .errorDomain(PersonalErrorDomain.FAIL_UPLOAD_OCR_FILE)
                                                     .build();

       // when
        boolean is4xxClientErrorPresent = Optional.ofNullable(response)
                .map(CommonApiResponse::getErrorDomain)
                .map(ErrorDomain::getHttpStatus)
                .map(HttpStatus::is4xxClientError)
                .orElse(false);

        // then
        assertThat(is4xxClientErrorPresent).isFalse();
    }


    /**
     * CommonApiResponse의 errorDomain이 null이 아닌 경우 -> is4xxClientError가 존재한다.
     */
    @Test
    public void commonApiReponse_erroDomain_isNotNull_then_is4xxClientError_isTrue(){

        // given
        CommonApiResponse response = CommonApiResponse.builder()
                .errorDomain(PersonalErrorDomain.FAIL_UPLOAD_OCR_FILE)
                .build();

        // when
        boolean is4xxClientErrorPresent = Optional.ofNullable(response)
                .map(CommonApiResponse::getErrorDomain)
                .map(ErrorDomain::getHttpStatus)
                .map(HttpStatus::is4xxClientError)
                .isPresent();

        // then
        assertThat(is4xxClientErrorPresent).isTrue();
    }
}