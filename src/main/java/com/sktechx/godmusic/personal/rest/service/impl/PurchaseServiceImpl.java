package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.personal.rest.model.dto.listen.PurchasePassDto;
import com.sktechx.godmusic.personal.rest.repository.PurchaseMapper;
import com.sktechx.godmusic.personal.rest.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Kobe.
 *
 * 실제론 Purchase 에서 처리해야하지만 청취로그 특성상 빈번한 호출이 예상되어 일단 필요한부분 구현
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 9.
 * @time PM 2:43
 */
@Service
public class PurchaseServiceImpl implements PurchaseService {
	@Autowired
	PurchaseMapper purchaseMapper;

	@Override
	public String getPssrlCd(Long memberNo) {
		return purchaseMapper.selectPssrlCd(memberNo);
	}

	@Override
	public PurchasePassDto getInUsePurchaseIdByMemberNo(Long memberNo) {
		return purchaseMapper.selectInUsePurchaseIdByMemberNo(memberNo);
	}
}
