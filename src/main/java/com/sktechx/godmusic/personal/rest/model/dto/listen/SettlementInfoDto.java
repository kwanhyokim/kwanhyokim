/*
 *  Copyright (c) 2018 SK TECHX.
 *  All right reserved.
 *
 *  This software is the confidential and proprietary information of SK TECHX.
 *  You shall not disclose such Confidential Information and
 *  shall use it only in accordance with the terms of the license agreement
 *  you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.dto.listen;

import com.sktechx.godmusic.personal.common.domain.type.PlayType;
import lombok.Data;

/**
 * 정상 정보 객체
 *
 * @author M.Ryan (sanghyun.park.tx@sk.com)
 * @date 2019. 6. 24.
 */
@Data
public class SettlementInfoDto {
	private Long prchsId;
	private Long memberNo;
	private Long passId;
	private Long goodsId;
	private PlayType playType;
	private String svcId;
}
