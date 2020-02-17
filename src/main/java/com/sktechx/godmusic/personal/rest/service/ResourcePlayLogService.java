/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.service;

import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.personal.common.domain.type.SourceType;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ResourcePlayLogRequestParam;

/**
 * 설명 : Resource 청취(재생) 로그
 *
 * @author groot
 * @since 2019. 12. 19
 */
public interface ResourcePlayLogService {

    SourceType handleSourceType();

    void deliverResourcePlayLog(GMContext gmContext, ResourcePlayLogRequestParam logRequestParam);

    void deliverResourceUserEvent(GMContext gmContext, ResourcePlayLogRequestParam logRequestParam);

}
