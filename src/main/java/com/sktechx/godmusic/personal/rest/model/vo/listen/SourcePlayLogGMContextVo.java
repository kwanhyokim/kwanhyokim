/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.model.vo.listen;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 설명 : 청취 로그에 필요한 GMContext 값들을 갖고 있을 클래스
 *
 * @author groot
 * @since 2019. 12. 19
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SourcePlayLogGMContextVo {
    private String playChnl;
    private Long memberNo;
    private Long characterNo;
    private String deviceId;
    private String userClientIp;
}
