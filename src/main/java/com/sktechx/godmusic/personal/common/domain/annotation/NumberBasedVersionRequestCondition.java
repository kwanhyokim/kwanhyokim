/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.common.domain.annotation;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import com.sktechx.godmusic.lib.domain.CommonConstant;

/**
 * 설명 :
 *
 * @author N/A
 * @date 2020. 05. 29.
 */
public class NumberBasedVersionRequestCondition implements RequestCondition<NumberBasedVersionRequestCondition> {

    private final String version;

    private NumberBasedVersionRequestCondition(String version) {
        this.version = version;
    }

    public static NumberBasedVersionRequestCondition createInstance(
            NumberBasedVersionRequestMapping mapping) {
        return mapping == null ? null : new NumberBasedVersionRequestCondition(mapping.sameOrAbove());
    }

    @Override
    @NonNull
    public NumberBasedVersionRequestCondition combine(@NonNull NumberBasedVersionRequestCondition other) {
        return this;
    }

    @Override
    public NumberBasedVersionRequestCondition getMatchingCondition(@NonNull HttpServletRequest request) {

        String clientVersion = request.getHeader(CommonConstant.X_GM_APP_VERSION);
        if (StringUtils.isBlank(clientVersion)) return null;

        return NumberBasedVersionStringComparator.isSameOrAboveVersion(this.version, clientVersion) ? this : null;
    }

    @Override
    public int compareTo(@NonNull NumberBasedVersionRequestCondition other, @NonNull HttpServletRequest request) {
        return NumberBasedVersionStringComparator.compare(other.version, this.version);
    }
}
