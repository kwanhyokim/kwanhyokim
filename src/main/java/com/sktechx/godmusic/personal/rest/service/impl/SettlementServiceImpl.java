package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.personal.rest.model.dto.listen.SettlementInfoDto;
import com.sktechx.godmusic.personal.rest.repository.SettlementMapper;
import com.sktechx.godmusic.personal.rest.service.SettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 정산 서비스 구현체
 * 향후 MSA 정산 서버로 변경 필요
 *
 * @author M.Ryan (sanghyun.park.tx@sk.com)
 * @date 2019. 6. 24.
 */
@Service
public class SettlementServiceImpl implements SettlementService {
	
	@Autowired
	SettlementMapper settlementMapper;
	
	@Override
	public String getServiceCode(Long memberNo, String settlementTypeCode) {
		return getServiceCode(this.getSettlementInfo(memberNo, settlementTypeCode));
	}
	
	@Override
	public String getServiceCodeByPrchsId(Long prchsId, String settlementTypeCode) {
		
		return getServiceCode(settlementMapper.selectSettlementInfoByPrchsId(prchsId, settlementTypeCode));
	}
	
	@Override
	public SettlementInfoDto getSettlementInfo(Long memberNo, String settlementTypeCode) {
		return settlementMapper.selectSettlementInfo(memberNo, settlementTypeCode);
	}
	
	private String getServiceCode(SettlementInfoDto settlement) {
		return Optional.ofNullable(settlement)
				.map(SettlementInfoDto::getSvcId)
				.orElse(null)
				;
	}
}
