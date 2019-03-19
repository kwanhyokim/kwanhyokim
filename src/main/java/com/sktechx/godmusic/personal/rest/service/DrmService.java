/*
 *  Copyright (c) 2018 SK TECHX.
 *  All right reserved.
 *
 *  This software is the confidential and proprietary information of SK TECHX.
 *  You shall not disclose such Confidential Information and
 *  shall use it only in accordance with the terms of the license agreement
 *  you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service;

import com.sktechx.godmusic.personal.rest.model.vo.drm.OwnerTokenClaim;

/**
 * Drm 서비스
 *
 * @author 박상현/SKTECH (sanghyun.park.tx@sk.com)
 * @date 2019. 3. 18.
 */
public interface DrmService {
	OwnerTokenClaim getOwnerTokenInfo(String ownerToken);
}
