/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.client.model;

import lombok.Builder;
import lombok.Data;

/**
 * 설명 : 메타 비디오 API POST 요청 위한 인자
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2019-10-11
 */

@Data
@Builder
public class MetaVideoRequestVo {
    private Long[] videoIds;
}
