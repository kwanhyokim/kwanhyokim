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

import com.sktechx.godmusic.personal.common.domain.type.SourceType;
import com.sktechx.godmusic.personal.rest.model.vo.drm.OwnerTokenClaim;
import com.sktechx.godmusic.personal.rest.service.DrmService;
import com.sktechx.godmusic.personal.rest.service.SettlementService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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
	
	@Autowired
	SettlementService settlementService;
	
	@Override
	public OwnerTokenClaim getOwnerTokenInfo(String ownerToken) {
		try {
			Jws<Claims> jws = Jwts.parser().setSigningKey(tokenKey.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(ownerToken);
			Map<String, Object> claims = (Map<String, Object>)jws.getBody().get("ownerInfo");
			
			if(ObjectUtils.isEmpty(claims)) return null;
			
			OwnerTokenClaim ownerTokenClaim = OwnerTokenClaim.builder()
					.memberNo(Long.valueOf((Integer)claims.get("memberNo")))
					.goodsId(Long.valueOf((Integer)claims.get("goodsId")))
					.purchaseId(Long.valueOf((Integer)claims.get("purchaseId")))
					.pssrlCode((String)claims.get("pssrlCode"))
					.serviceId((String)claims.get("serviceId"))
					.build();
			
			// todo 2019.05월 이후 불필요 코드 이므로 아래 method와 함께 삭제 필요
			addServiceIdToOwnerToken(ownerTokenClaim);
			
			return ownerTokenClaim;
		} catch (Exception e) {
			log.error("Owner Token Parse 에러 : {}", e.getMessage());
			return null;
		}
	}
	
	private void addServiceIdToOwnerToken(OwnerTokenClaim ownerTokenClaim) {
		if (ObjectUtils.isEmpty(ownerTokenClaim.getPurchaseId()) == false
				&& StringUtils.isEmpty(ownerTokenClaim.getServiceId())) {
			// todo 테스크 코드 이 로그가 찍히지 않는 걸 확인 하고 해당 메소드 삭제 처리
			log.info("Service Id Extract, token={}", ownerTokenClaim);
			String serviceId = settlementService.getServiceCodeByPrchsId(ownerTokenClaim.getPurchaseId(), SourceType.DN.getPlayType());
			
			ownerTokenClaim.setServiceId(serviceId);
		}
	}
}
