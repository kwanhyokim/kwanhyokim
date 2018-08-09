package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.domain.type.TrackLogType;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenRequest;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenTrackRequest;
import com.sktechx.godmusic.personal.rest.repository.ListenMapper;
import com.sktechx.godmusic.personal.rest.service.ListenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 8.
 * @time PM 7:29
 */
@Slf4j
@Service
public class ListenServiceImpl implements ListenService {
	@Autowired
	ListenMapper listenMapper;

	@Override
	public void addListenHistByChannel(ListenRequest request, Long memberNo, Long characterNo) {
		listenMapper.addListenHistByChannel(request.getListenType(), request.getListenTypeId(),
				memberNo, characterNo);
	}

	@Override
	public void addListenHistByTrack(ListenTrackRequest request, GMContext currentContext) {
		Long memberNo = currentContext.getMemberNo();
		Long characterNo = currentContext.getCharacterNo();
		String deviceId = currentContext.getDeviceId();
		String appName = currentContext.getAppName();
		Date logCreateDate = new Date();

		log.info("addListenHistByTrack...");
		if(request.getTrackLogType() == TrackLogType.ONEMIN){
			if(!YnType.Y.equals(request.getFreeYn())){
//				String pssrl = purchaseService.getPssrlCd(memberNo);
//				if(ObjectUtils.isEmpty(pssrl)){
//					throw new NotFoundException(CommonErrorMessage.USER_PSSRL_NOT_FOUND);
//				}
//				trackListenLog.setPssrlCd(pssrl);
//
//				PurchasePassDto purchasePassDto = purchaseService.getInUsePurchaseIdByMemberNo(memberNo);
//				trackListenLog.setPurchaseId(purchasePassDto.getPrchsId());
//				trackListenLog.setGoodsId(purchasePassDto.getGoodsId());
			}
		}
	}
}
