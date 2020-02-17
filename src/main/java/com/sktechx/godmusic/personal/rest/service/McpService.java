/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.service;

/**
 * 설명 : XXXXXXXXXXX
 *
 * @author groot
 * @since 2019. 12. 19
 */
public interface McpService {

    String getServiceCodeFromMCP(Long trackId, String bitrate, String osType);

}
