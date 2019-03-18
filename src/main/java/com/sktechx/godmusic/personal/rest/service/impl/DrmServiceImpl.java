/*
 *  Copyright (c) 2018 SK TECHX.
 *  All right reserved.
 *
 *  This software is the confidential and proprietary information of SK TECHX.
 *  You shall not disclose such Confidential Information and
 *  shall use it only in accordance with the terms of the license agreement
 *  you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.personal.rest.model.vo.drm.OwnerTokenClaim;
import com.sktechx.godmusic.personal.rest.service.DrmService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Drm 서비스 구현체
 *
 * @author 박상현/SKTECH (sanghyun.park.tx@sk.com)
 * @date 2019. 3. 18.
 */
@Service
@Slf4j
public class DrmServiceImpl implements DrmService {
	
	@Value("${token.drm.owner.key}")
	private String tokenKey;
	
	@Override
	public OwnerTokenClaim getOwnerTokenInfo(String ownerToken) {
		try {
			Jws<Claims> jws = Jwts.parser().setSigningKey(tokenKey.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(ownerToken);
			Map<String, Object> claims = (Map<String, Object>)jws.getBody().get("ownerInfo");
			
			if(ObjectUtils.isEmpty(claims)) return null;
			
			return OwnerTokenClaim.builder()
					.memberNo(Long.valueOf((Integer)claims.get("memberNo")))
					.goodsId(Long.valueOf((Integer)claims.get("goodsId")))
					.purchaseId(Long.valueOf((Integer)claims.get("purchaseId")))
					.pssrlCode((String)claims.get("pssrlCode"))
					.serviceId((String)claims.get("serviceId"))
					.build();
		} catch (Exception e) {
			log.error("Owner Token Parse 에러 : {}", e.getMessage());
			return null;
		}
	}
}
