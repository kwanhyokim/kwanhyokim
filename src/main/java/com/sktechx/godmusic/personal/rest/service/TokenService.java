/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.service;

import com.sktechx.godmusic.personal.rest.model.vo.listen.token.CachedStreamingToken;
import com.sktechx.godmusic.personal.rest.model.vo.listen.token.FreeCachedStreamingToken;
import com.sktechx.godmusic.personal.rest.model.vo.listen.token.OwnerTokenClaim;
import com.sktechx.godmusic.personal.rest.model.vo.listen.token.SettlementToken;

/**
 * 설명 : XXXXXXXXXXX
 *
 * @author groot
 * @since 2019. 12. 23
 */
public interface TokenService {

    SettlementToken parseSettlementToken(String sttToken);

    OwnerTokenClaim parseOwnerToken(String ownerToken);

    CachedStreamingToken parseCachedStreamingToken(String cachedStreamingToken);

    FreeCachedStreamingToken parseFreeCachedStreamingToken(String freeCachedStreamingToken);

}
