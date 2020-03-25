/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.model.vo.recommend;

import lombok.Builder;
import lombok.Data;

/**
 * 설명 : 추천 패널 몽고 업데이트용 request vo
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2020-03-25
 */

@Builder
@Data
public class RecommendUpdateRequest {

    private String delTargetYn;

}
