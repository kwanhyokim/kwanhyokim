/*
 * Copyright (c) 2019 Dreamus.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus.
 *
 * @project personal-api
 * @author dave(djin.chung@sk.com)
 * @date 19. 12. 19. 오후 2:27
 */

package com.sktechx.godmusic.personal.rest.service;

/**
 * 설명 : PUSH 서비스
 *
 * @author dave(djin.chung@sk.com)
 * @date 2019. 12. 19.
 */
public interface PushService {
    /**
     * push 클릭 카운트 추가
     * @param clickId
     */
    void    clickLog(long clickId);
}
