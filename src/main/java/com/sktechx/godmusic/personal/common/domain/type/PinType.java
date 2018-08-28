/*
 *
 *  * Copyright (c) 2018 SK TECHX.
 *  * All right reserved.
 *  *
 *  * This software is the confidential and proprietary information of SK TECHX.
 *  * You shall not disclose such Confidential Information and
 *  * shall use it only in accordance with the terms of the license agreement
 *  * you entered into with SK TECHX.
 *
 */

package com.sktechx.godmusic.personal.common.domain.type;

import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 8. 1.
 */
public enum PinType implements CodeEnum {
    RC_SML_TR("RC_SML_TR", "Like U(2-A)"),
    RC_GR_TR("RC_GR_TR", "Mix Tape(2-A')"),
    RC_ATST_TR("RC_ATST_TR", "Musician focus(2-C)"),
    RC_CF_TR("RC_CF_TR", "Made for U(3-A)"),
    CHNL("CHNL", "채널"),
    MY_CHNL("MY_CHNL", "마이 채널");
//    CHART("CHART" , "차트");

    private final String code;
    private final String title;

    PinType(String code, String title) {
        this.code = code; this.title = title;
    }

    @Override
    public String getCode() {
        return code;
    }

    public String getTitle(){
        return title;
    }

    @Override
    public CodeEnum getDefault() {
        return null;
    }
}
