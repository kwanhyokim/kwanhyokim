/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 안영현(Younghyun Ahn)
 * @date 2018.06.30
 */

@SpringBootApplication
@EnableDiscoveryClient
//@EnableFeignClients
public class WebInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        // set register error pagefilter false
        setRegisterErrorPageFilter(false);
        return application.sources(WebInitializer.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(WebInitializer.class, args);
    }
}

