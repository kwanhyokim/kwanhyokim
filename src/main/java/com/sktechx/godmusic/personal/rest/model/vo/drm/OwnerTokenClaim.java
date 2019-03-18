/*
 *  Copyright (c) 2018 SK TECHX.
 *  All right reserved.
 *
 *  This software is the confidential and proprietary information of SK TECHX.
 *  You shall not disclose such Confidential Information and
 *  shall use it only in accordance with the terms of the license agreement
 *  you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.vo.drm;

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
