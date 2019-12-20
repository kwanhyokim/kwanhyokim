/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 설명 : XXXXXXXXXXX
 *
 * @author groot
 * @since 2019. 12. 20
 */
@Slf4j
@SpringBootTest
@ActiveProfiles("local")
public class ResolverPatternTest {

    interface 조민국 {
        String doProcess(String a);
        String handleableMetaType();
    }

    static class Track implements 조민국 {
        @Override
        public String doProcess(String a) {
            return a + " : track";
        }
        @Override
        public String handleableMetaType() {
            return "track";
        }
    }

    static class Artist implements 조민국 {
        @Override
        public String doProcess(String a) {
            return a + " : artist";
        }
        @Override
        public String handleableMetaType() {
            return "artist";
        }
    }

    @Bean
    public Track track() {
        return new Track();
    }

    @Bean
    public Artist artist() {
        return new Artist();
    }

    static class HandlerResolver {
        public HandlerResolver(List<조민국> all) {
            mapper = all.stream().collect(Collectors.toMap(조민국::handleableMetaType, Function.identity()));
        }
        public Optional<조민국> findResolver(String metaType) {
            return Optional.ofNullable(mapper.get(metaType));
        }
        private Map<String, 조민국> mapper;
    }

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ResolverPatternTest.class);
        HandlerResolver resolver = context.getBean(HandlerResolver.class);
        String param = "가나다";
        조민국 a = resolver.findResolver("track").get();
        System.out.println(a.doProcess(param));
        a = resolver.findResolver("artist").get();
        System.out.println(a.doProcess(param));
    }

}
