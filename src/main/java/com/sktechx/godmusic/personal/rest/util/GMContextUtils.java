/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.util;

import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.personal.common.domain.type.AppNameType;
import com.sktechx.godmusic.personal.rest.model.vo.listen.play.SourcePlayLogGMContextVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 설명 : XXXXXXXXXXX
 *
 * @author groot
 * @since 2019. 12. 26
 */
@Slf4j
@Component
public class GMContextUtils {

    public SourcePlayLogGMContextVo buildGMContextVo(HttpServletRequest httpServletRequest,
                                                     GMContext gmContext) {
        return SourcePlayLogGMContextVo.builder()
                .playChnl(Optional.ofNullable(AppNameType.fromCode(gmContext.getAppName())).map(AppNameType::getCode).orElse(""))
                .memberNo(gmContext.getMemberNo())
                .characterNo(gmContext.getCharacterNo())
                .deviceId(gmContext.getDeviceId())
                .userClientIp(Optional.ofNullable(httpServletRequest.getHeader("client_ip")).orElse(""))
                .build();
    }

}
