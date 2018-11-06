package com.sktechx.godmusic.personal.rest.service;

import com.sktechx.godmusic.personal.rest.model.dto.listen.PurchasePassDto;

/**
 * Created by Kobe.
 *
 * 실제론 Purchase 에서 처리해야하지만 청취로그 특성상 빈번한 호출이 예상되어 일단 필요한부분 구현
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 9.
 * @time PM 2:38
 */
public interface PurchaseService {
	String getPssrlCd(Long memberNo);
	PurchasePassDto getInUsePurchaseIdByMemberNo(Long memberNo);
}
