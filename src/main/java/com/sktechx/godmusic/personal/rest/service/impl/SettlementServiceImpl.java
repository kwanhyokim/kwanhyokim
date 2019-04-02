package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.personal.rest.repository.SettlementMapper;
import com.sktechx.godmusic.personal.rest.service.SettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 정산 서비스 구현체
 * 향후 MSA 정산 서버로 변경 필요
 *
 * @author 박상현/SKTECH (sanghyun.park.tx@sk.com)
 * @date 2019. 3. 11.
 */
@Service
public class SettlementServiceImpl implements SettlementService {
	
	@Autowired
	SettlementMapper settlementMapper;
	
	@Override
	public String getServiceCode(Long memberNo, String settlementTypeCode) {
		return settlementMapper.selectServiceCode(memberNo, settlementTypeCode);
	}
	
	@Override
	public String getServiceCodeByPrchsId(Long prchsId, String settlementTypeCode) {
		return settlementMapper.selectServiceCodeByPrchsId(prchsId, settlementTypeCode);
	}
}
