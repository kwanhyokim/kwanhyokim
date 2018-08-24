package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEvent;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEventTarget;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEventType;
import com.sktechx.godmusic.personal.common.amqp.service.AmqpService;
import com.sktechx.godmusic.personal.common.domain.type.AppNameType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.TrackLogType;
import com.sktechx.godmusic.personal.common.exception.CommonErrorMessage;
import com.sktechx.godmusic.personal.common.exception.NotFoundException;
import com.sktechx.godmusic.personal.common.util.DateUtil;
import com.sktechx.godmusic.personal.rest.model.dto.listen.PurchasePassDto;
import com.sktechx.godmusic.personal.rest.model.dto.listen.TrackListen;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenRequest;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenTrackRequest;
import com.sktechx.godmusic.personal.rest.repository.ListenMapper;
import com.sktechx.godmusic.personal.rest.service.ListenService;
import com.sktechx.godmusic.personal.rest.service.PurchaseService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendPanelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import static  com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType.*;
/**
 * Created by Kobe.
 *
 * 실제론 Purchase 에서 처리해야하지만 청취로그 특성상 빈번한 호출이 예상되어 일단 필요한부분 구현
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

	@Autowired
	AmqpService amqpService;

	@Autowired
	PurchaseService purchaseService;    // 실제론 Purchase 에서 처리해야하지만 청취로그 특성상 빈번한 호출이 예상되어 일단 필요한부분 구현

	@Autowired
	RecommendPanelService recommendPanelService;

	@Override
	public void addListenHistByChannel(ListenRequest request, Long memberNo, Long characterNo) {
		listenMapper.addListenHistByChannel(request.getListenType(), request.getListenTypeId(),
				memberNo, characterNo);

		//추천 패널의 경우 기존 추천 데이터  삭제 방지를 위한 DB 업데이트 처리
		if(isRecommendListen(request.getListenType())){
			recommendPanelService.updateRecommendDataPrevent(request , characterNo);
		}

	}


	@Override
	public void addListenHistByTrack(ListenTrackRequest request, GMContext currentContext) {
		Long memberNo = currentContext.getMemberNo();
		Long characterNo = currentContext.getCharacterNo();
		String deviceId = currentContext.getDeviceId();
		String playChannel = AppNameType.fromCode(currentContext.getAppName()) != null ? AppNameType.fromCode(currentContext.getAppName()).getCode() : "";
		String logType = request.getTrackLogType() != null ? request.getTrackLogType().getCode() : "";
		String bitrate = request.getBitrate() != null ? request.getBitrate().getCode() : "";
		String osType = request.getOsType() != null ? request.getOsType().getCode() : "";

		TrackListen trackListen = TrackListen.builder()
				.playChnl(playChannel)
				.memberNo(memberNo)
				.characterNo(characterNo)
				.trackId(request.getTrackId())
				.logType(logType)
				.bitrate(bitrate)
				.trackTotTm(request.getTrackTotalSec())
				.elapsedTm(request.getElapsedSec())
				.osType(osType)
				.dvcId(deviceId)
				.albumId(request.getAlbumId())
				.chnlId(request.getChannelId())
				.memberRcmdId(request.getRecommendTrackId())
				.addTm(request.getAddDateTime())
				.sessionToken("")
				.build();

		TrackListen.TrackListenBuilder trackListenBuilder = trackListen.toBuilder();

		log.info("addListenHistByTrack...");
		if(request.getTrackLogType() == TrackLogType.ONEMIN){
			if(!YnType.Y.equals(request.getFreeYn())){
				String pssrl = purchaseService.getPssrlCd(memberNo);
				if(ObjectUtils.isEmpty(pssrl)){
					throw new NotFoundException(CommonErrorMessage.USER_PSSRL_NOT_FOUND);
				}
				PurchasePassDto purchasePassDto = purchaseService.getInUsePurchaseIdByMemberNo(memberNo);

				trackListenBuilder
						.pssrlCd(pssrl)
						.prchsId(purchasePassDto.getPrchsId())
						.goodsId(purchasePassDto.getGoodsId());
			}
		}

		if(YnType.Y.equals(request.getFreeYn()))	{
			trackListenBuilder.free(true);
		}

		amqpService.deliverTrackListen(trackListenBuilder.build());

		UserEventType userEventType = UserEventType.fromTrackLogType(request.getTrackLogType());
		if( !userEventType.equals(UserEventType.UNKNOWN) )	{
			UserEvent userEvent = UserEvent.newBuilder()
					.setPlayChnl(AppNameType.fromCode(currentContext.getAppName()))
					.setEvent(userEventType)
					.setMemberNo(memberNo)
					.setCharactorNo(characterNo)
					.setTargetId(request.getTrackId())
					.setTargetType(UserEventTarget.TRACK)
					.build();
			amqpService.deliverUserEvent(userEvent);
		}
	}

	private boolean isRecommendListen(String listenType){
		if(RC_ATST_TR.getCode().equals(listenType)
				|| RC_SML_TR.getCode().equals(listenType)
					|| RC_GR_TR.getCode().equals(listenType)
						|| RC_CF_TR.getCode().equals(listenType)){
			return true;
		}
		return false;
	}
}
