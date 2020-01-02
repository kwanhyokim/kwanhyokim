/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.resolver;

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
 * 설명 : Resolver Pattern TEST
 *
 * @author groot
 * @since 2019. 12. 20
 */
@Slf4j
@SpringBootTest
@ActiveProfiles("!prod")
public class ResolverPatternTest {

    interface ProcessService {

        String doProcess(String a);

        String handleableMetaType();

    }

    static class TrackProcessService implements ProcessService {
        @Override
        public String doProcess(String a) {
            return a + " : track";
        }
        @Override
        public String handleableMetaType() {
            return "track";
        }
    }

    static class ArtistProcessService implements ProcessService {
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
    public TrackProcessService track() {
        return new TrackProcessService();
    }

    @Bean
    public ArtistProcessService artist() {
        return new ArtistProcessService();
    }

    static class HandlerResolver {

        private Map<String, ProcessService> mapper;

        public HandlerResolver(List<ProcessService> all) {
            mapper = all.stream().collect(Collectors.toMap(ProcessService::handleableMetaType, Function.identity()));
        }

        public Optional<ProcessService> findResolver(String metaType) {
            return Optional.ofNullable(mapper.get(metaType));
        }

    }

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ResolverPatternTest.class);
        HandlerResolver resolver = context.getBean(HandlerResolver.class);
        String param = "가나다";
        ProcessService a = resolver.findResolver("track").get();
        System.out.println(a.doProcess(param));
        a = resolver.findResolver("artist").get();
        System.out.println(a.doProcess(param));
    }

}
