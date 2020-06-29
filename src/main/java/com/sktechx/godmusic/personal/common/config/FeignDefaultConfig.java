/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.common.config;

import java.lang.reflect.Type;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;

/**
 * 설명 : XXXXXXXXXXX
 *
 * @author Groot(조민국) / dev.mingood@sk.com
 * @since 2020. 04. 28
 */
@Configuration
public class FeignDefaultConfig {

    @Bean
    public Encoder feignEncoder(ObjectFactory<HttpMessageConverters> messageConverterObjectFactory) {
        return new Encoder() {
            @Override
            public void encode(Object o, Type bodyType, RequestTemplate requestTemplate) throws EncodeException {
                if (bodyType.equals(MultipartFile.class)) {
                    springFormEncoder.encode(o, bodyType, requestTemplate);
                } else {
                    springEncoder.encode(o, bodyType, requestTemplate);
                }
            }

            private final SpringFormEncoder springFormEncoder = new SpringFormEncoder();
            private final SpringEncoder springEncoder = new SpringEncoder(messageConverterObjectFactory);
        };
    }

//    @Bean
//    Logger.Level feignLoggerLevel() {
//        return Logger.Level.FULL;
//    }

}
