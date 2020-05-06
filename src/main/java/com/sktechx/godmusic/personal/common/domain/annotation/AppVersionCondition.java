/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.common.domain.annotation;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2020-03-19
 */

public class AppVersionCondition implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        if (annotatedTypeMetadata.isAnnotated(AppVersion.class.getCanonicalName())){
            return annotatedTypeMetadata.getAnnotationAttributes(AppVersion.class.getCanonicalName())
                    .entrySet().stream().anyMatch(
                            f -> f.getValue().equals("Dog")
                    );
        }
        return false;
    }

}
