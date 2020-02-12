/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.repository;

import com.sktechx.godmusic.lib.mybatis.annotation.ReadOnlyMapper;

/**
 * 설명 :
 *
 * @author N/A
 * @date 2020. 02. 11.
 */
@ReadOnlyMapper
public interface WarmUpMapper {

    @SuppressWarnings("UnusedReturnValue")
    int select1();
}
