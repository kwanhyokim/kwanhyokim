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

import org.apache.commons.lang.StringUtils;

/**
 * 설명 :
 *
 * @author N/A
 * @date 2020. 05. 29.
 */
public final class NumberBasedVersionStringComparator {

    public static boolean isSameOrAboveVersion(String basisVersion, String clientVersion) {

        try {
            if (StringUtils.isNotBlank(basisVersion) && StringUtils.isNotBlank(clientVersion)) {
                return compare(basisVersion, clientVersion) <= 0;
            }
        } catch (NumberFormatException ignore) {
        }

        return false;
    }

    public static int compare(String version1, String version2) throws NumberFormatException {

        String[] v1 = StringUtils.split(version1, '.');
        String[] v2 = StringUtils.split(version2, '.');

        for (int i = 0, limit = (Math.max(v1.length, v2.length)); i < limit; i++) {

            String component1 = i >= v1.length ? "0" : v1[i];
            String component2 = i >= v2.length ? "0" : v2[i];

            if (Integer.parseInt(component1) != Integer.parseInt(component2)) {
                return Integer.compare(Integer.parseInt(component1), Integer.parseInt(component2));
            }
        }

        return 0;
    }
}
