package com.sktechx.godmusic.personal.rest.service;

import com.sktechx.godmusic.personal.rest.model.dto.listen.SettlementInfoDto;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenTrackRequest;
import com.sktechx.godmusic.personal.rest.model.vo.listen.SettlementToken;
import com.sktechx.godmusic.personal.rest.model.vo.video.ResourcePlayLogRequest;

/**
 * 정산 처리를 위한 서비스
 * 향후 별도 서버로 분리 필요
 *
 * @author 박상현/SKTECH (sanghyun.park.tx@sk.com)
 * @date 2019. 3. 11.
 */
public interface SettlementService {

    // 정산 정보 조회
    String getServiceCode(Long memberNo, String settlementTypeCode);

    String getServiceCodeByPrchsId(Long prchsId, String settlementTypeCode);

    SettlementInfoDto getSettlementInfo(Long memberNo, String settlementTypeCode);

	SettlementToken parseSettlementToken(String sttToken);

    String evaluateServiceId(ListenTrackRequest request, SettlementInfoDto settlement);

    String evaluateServiceIdByResourcePlayLogRequest(ResourcePlayLogRequest request,
                                                     SettlementInfoDto settlementInfo);

}
