/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.model.vo.listen.token;

import lombok.Builder;
import lombok.Data;

/**
 * Drm OwnerToken
 *
 * @author 박상현/SKTECH (sanghyun.park.tx@sk.com)
 * @date 2019. 3. 18.
 */
@Data
@Builder
public class OwnerTokenClaim {
	Long memberNo;
	Long purchaseId;
	Long goodsId;
	String pssrlCode;
	String serviceId;
}
