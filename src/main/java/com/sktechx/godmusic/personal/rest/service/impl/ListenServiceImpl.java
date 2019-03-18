package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEvent;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEventTarget;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEventType;
import com.sktechx.godmusic.personal.common.amqp.service.AmqpService;
import com.sktechx.godmusic.personal.common.domain.type.AppNameType;
import com.sktechx.godmusic.personal.common.domain.type.SourceType;
import com.sktechx.godmusic.personal.common.domain.type.TrackLogType;
import com.sktechx.godmusic.personal.common.exception.PersonalErrorDomain;
import com.sktechx.godmusic.personal.rest.model.dto.listen.PurchasePassDto;
import com.sktechx.godmusic.personal.rest.model.dto.listen.TrackListen;
import com.sktechx.godmusic.personal.rest.model.vo.drm.OwnerTokenClaim;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenRequest;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenTrackRequest;
import com.sktechx.godmusic.personal.rest.repository.ListenMapper;
import com.sktechx.godmusic.personal.rest.service.DrmService;
import com.sktechx.godmusic.personal.rest.service.ListenService;
import com.sktechx.godmusic.personal.rest.service.PurchaseService;
import com.sktechx.godmusic.personal.rest.service.SettlementService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import static com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType.*;
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
	RecommendDataService recommendDataService;
	
	@Autowired
	SettlementService settlementService;

	@Autowired
	DrmService drmService;
	
	@Override
	public void addListenHistByChannel(ListenRequest request, Long memberNo, Long characterNo) {
		listenMapper.addListenHistByChannel(request.getListenType(), request.getListenTypeId(),
				memberNo, characterNo);

		//추천 패널의 경우 기존 추천 데이터  삭제 방지를 위한 DB 업데이트 처리
		if(isRecommendListen(request.getListenType())){
			recommendDataService.updateRecommendDataRemovePrevent(request , characterNo);
		}

	}

	@Override
	public void addListenHistByTrack(ListenTrackRequest request, GMContext currentContext, HttpServletRequest httpServletRequest) {
		Long memberNo = currentContext.getMemberNo();
		Long characterNo = currentContext.getCharacterNo();
		String deviceId = currentContext.getDeviceId();
		String playChannel = AppNameType.fromCode(currentContext.getAppName()) != null ? AppNameType.fromCode(currentContext.getAppName()).getCode() : "";
		String logType = request.getTrackLogType() != null ? request.getTrackLogType().getCode() : "";
		String bitrate = request.getBitrate() != null ? request.getBitrate().getCode() : "";
		String osType = request.getOsType() != null ? request.getOsType().getCode() : "";
		String clientIp = httpServletRequest.getHeader("client_ip") != null ? httpServletRequest.getHeader("client_ip") : "";
		String chnlType = StringUtils.isEmpty(request.getChannelType()) ? null : request.getChannelType();
		String listenSessionId = StringUtils.isEmpty(request.getListenSessionId()) ? null : request.getListenSessionId();
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
				.chnlType(chnlType)
				.memberRcmdId(request.getRecommendTrackId())
				.addTm(request.getAddDateTime())
				.sessionToken("")
				.sourceType(request.getSourceType())
				.free(false)
				.timeMillis(System.currentTimeMillis())
				.userClientIp(clientIp)
				.ownerToken(request.getOwnerToken())
				.listenSessionId(listenSessionId)
				.build();

		TrackListen.TrackListenBuilder trackListenBuilder = trackListen.toBuilder();

		log.info("addListenHistByTrack...");
		if(request.getTrackLogType() == TrackLogType.ONEMIN){
			String pssrlCd = purchaseService.getPssrlCd(memberNo);
			String serviceId = getServiceCode(memberNo, request);
			if(ObjectUtils.isEmpty(serviceId)){
				throw new CommonBusinessException(PersonalErrorDomain.USER_PSSRL_NOT_FOUND);
			}
			PurchasePassDto purchasePassDto = purchaseService.getInUsePurchaseIdByMemberNo(memberNo);

			if(purchasePassDto == null){
				throw new CommonBusinessException(PersonalErrorDomain.USER_PSSRL_NOT_FOUND);
			}

			trackListenBuilder
					.pssrlCd(pssrlCd)
					.serviceId(serviceId)
					.prchsId(purchasePassDto.getPrchsId())
					.goodsId(purchasePassDto.getGoodsId());
			
			if (SourceType.DN == request.getSourceType()) {
				if (StringUtils.isEmpty(request.getOwnerToken())) {
					// todo throw error ???
					log.warn("OwnerToken이 존재하지 않습니다. (DRM 스트리밍)");
				} else {
					OwnerTokenClaim ownerToken = drmService.getOwnerTokenInfo(request.getOwnerToken());
					
					if (ObjectUtils.isEmpty(ownerToken)) {
						// todo throw error ???
						log.warn("OwnerToken 값이 유효하지 않습니다. (DRM 스트리밍)");
					} else {
						trackListenBuilder.drmMemberNo(ownerToken.getMemberNo());
						trackListenBuilder.drmGoodsId(ownerToken.getGoodsId());
						trackListenBuilder.drmPrchsId(ownerToken.getPurchaseId());
						trackListenBuilder.drmPssrlCd(ownerToken.getPssrlCode());
						trackListenBuilder.serviceId(ownerToken.getServiceId());
					}
				}
			}
		}
		
		if(YnType.Y.equals(request.getFreeYn())) {
			trackListenBuilder.free(true);
		}

		amqpService.deliverTrackListen(trackListenBuilder.build());
		log.info("[Track Listen Hist] " + trackListenBuilder.toString());

		UserEventType userEventType = UserEventType.fromTrackLogType(request.getTrackLogType());
		if( !userEventType.equals(UserEventType.UNKNOWN) )	{
			UserEvent userEvent = UserEvent.newBuilder()
					.playChnl(currentContext.getAppName())
					.event(userEventType)
					.memberNo(memberNo)
					.charactorNo(characterNo)
					.targetId(String.valueOf(request.getTrackId()))
					.targetType(UserEventTarget.TRACK)
					.sourceType(request.getSourceType())
					.trackTotTm(request.getTrackTotalSec())
					.elapsedTm(request.getElapsedSec())
					.timeMillis(System.currentTimeMillis())
					.build();
			amqpService.deliverUserEvent(userEvent);
			log.info("[Track Listen Hist - User event] " + userEvent.toString());
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
	
	private String getServiceCode(Long memberNo, ListenTrackRequest request) {
		if (YnType.Y == request.getFreeYn()) {
			// todo 스트리밍 api를 이용하여 무료 서비스 코드를 가져오는 코드로 변경 필요
			return "FREE_SVC";
		} else {
			return settlementService.getServiceCode(memberNo, request.getSourceType().getPlayType());
		}
	}
}
