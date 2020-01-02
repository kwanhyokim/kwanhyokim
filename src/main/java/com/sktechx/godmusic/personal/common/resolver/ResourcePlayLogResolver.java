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
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 설명 : SourceType에 따른 ResourcePlayLogService를 찾아주는 Resolver
 *
 * @author groot
 * @since 2019. 12. 20
 */
@Component
public class ResourcePlayLogResolver {

    private Map<SourceType, ResourcePlayLogService> resourcePlayLogServiceMap;

    public ResourcePlayLogResolver(List<ResourcePlayLogService> resourcePlayLogServices) {
        resourcePlayLogServiceMap = resourcePlayLogServices.stream()
                .collect(Collectors.toMap(
                        ResourcePlayLogService::handleSourceType,
                        Function.identity()
                ));
    }

    public Optional<ResourcePlayLogService> findResolver(SourceType sourceType) {
        return Optional.ofNullable(resourcePlayLogServiceMap.get(sourceType));
    }

}
