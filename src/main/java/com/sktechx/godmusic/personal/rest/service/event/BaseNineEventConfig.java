/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.service.event;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 설명 : event의 baseURL 을 config server에 관리하지 않고 java config 로 설정
 *       note. 향후 이벤트 종료 후 불필요시 삭제 하도록 한다.
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 11. 15.
 */
@Component
public class BaseNineEventConfig {

    @Component
    @Profile("dev")
    public static class BaseNineEventDevConfig implements EventConfig {

        @Override
        public String getBaseURL() {
            return "https://dev-img.music-flo.com";
        } // 현재 DEV향 image baseurl 없음.
    }

    @Component
    @Profile({"qa", "qa-live"})
    public static class BaseNineEventQaConfig implements EventConfig {

        @Override
        public String getBaseURL() {
            return "https://qa-img.music-flo.com";
        }
    }

    @Component
    @Profile({"prod", "stage"})
    public static class BaseNineEventProdConfig implements EventConfig {

        @Override
        public String getBaseURL() {
            return "https://img.music-flo.com";
        }
    }
}
