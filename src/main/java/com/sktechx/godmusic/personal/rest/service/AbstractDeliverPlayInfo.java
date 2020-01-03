/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.service;

import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.personal.rest.model.vo.listen.play.ResourcePlayLogRequestParam;

/**
 * 설명 : XXXXXXXXXXX
 *
 * @author groot
 * @since 2020. 01. 03
 */
public abstract class AbstractDeliverPlayInfo implements ResourcePlayLogService {

    final public void deliverPlayInfo(GMContext gmContext, ResourcePlayLogRequestParam param) {
        deliverResourcePlayLog(gmContext, param);
        deliverResourceUserEvent(gmContext, param);
    }

}
