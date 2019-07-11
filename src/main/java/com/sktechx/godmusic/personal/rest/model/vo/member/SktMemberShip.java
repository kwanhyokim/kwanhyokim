/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.vo.member;

import com.sktechx.godmusic.lib.domain.code.YnType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SktMemberShip {

    private String mbrCardNum;		//	멤버쉽 카드번호 (16자리)

    private YnType availableYn;	//	멤버쉽 상태코드

}
