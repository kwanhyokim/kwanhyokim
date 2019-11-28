/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.client.model;

import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.domain.type.BitrateType;
import lombok.Data;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 10. 08.
 */
@Data
public class OneTimeUrlResponse {

    /**
     * 서비스 코드
     * 요청시 전달된 Service Code, 단, 무료곡의 경우 해당 무료 Service Code로 전달
     */
    private String svcCd;

    /**
     * 성인 여부
     */
    private YnType adultYn;

    /**
     * 앨범 음 조정 값
     */
    private String albumLoudness;

    /**
     * 음질 값
     */
    private BitrateType bitrate;

    /**
     * 콘텐트 수정 일시
     */
    private Long contentUpdDt;

    /**
     * 무료 여부
     */
    private YnType freeYn;

    /**
     * 전체 재생 여부
     */
    private YnType fullYn;

    /**
     * 암호화시 이용하는 일회용 숫자값
     */
    private Long nonce;

    /**
     * 암호화
     */
    private String signature;

    /**
     * 수정 일시
     */
    private Long updDt;

    /**
     * 재생 URL
     */
    private String url;

}
