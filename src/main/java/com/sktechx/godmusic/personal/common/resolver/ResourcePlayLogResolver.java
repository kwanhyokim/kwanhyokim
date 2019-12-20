/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.common.resolver;

import com.sktechx.godmusic.personal.common.domain.type.SourceType;
import com.sktechx.godmusic.personal.rest.service.ResourcePlayLogService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 설명 : XXXXXXXXXXX
 *
 * @author groot
 * @since 2019. 12. 20
 */
public class ResourcePlayLogResolver {

    private Map<SourceType, ResourcePlayLogService> resourcePlayLogServiceMap;

    public ResourcePlayLogResolver(List<ResourcePlayLogService> resourcePlayLogServices) {
        resourcePlayLogServiceMap = resourcePlayLogServices.stream()
                .collect(Collectors.toMap())
    }
}
