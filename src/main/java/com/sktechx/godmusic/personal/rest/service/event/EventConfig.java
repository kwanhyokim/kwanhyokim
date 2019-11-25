/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.service.event;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 11. 15.
 */
public interface EventConfig {

    /**
     * 이벤트에 이용되는 resource 들의 baseURL 정보를 반환한다.
     * @return baseURL string
     */
    String getBaseURL();
}
