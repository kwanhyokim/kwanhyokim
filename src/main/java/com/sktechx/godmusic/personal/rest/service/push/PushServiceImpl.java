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
 * @date 19. 12. 19. 오후 2:28
 */

package com.sktechx.godmusic.personal.rest.service.push;

import com.sktechx.godmusic.personal.rest.repository.PushArchiveMapper;
import com.sktechx.godmusic.personal.rest.service.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 설명 : PUSH 서비스 impl
 *
 * @author dave(djin.chung @ sk.com)
 * @date 2019. 12. 19.
 */

@Slf4j
@Service
public class PushServiceImpl implements PushService {

    private PushArchiveMapper pushArchiveMapper;

    public PushServiceImpl(PushArchiveMapper pushArchiveMapper) {
        this.pushArchiveMapper = pushArchiveMapper;
    }

    @Override
    public void clickLog(long clickId) {
        pushArchiveMapper.updatePushClickCount(clickId);
    }
}
