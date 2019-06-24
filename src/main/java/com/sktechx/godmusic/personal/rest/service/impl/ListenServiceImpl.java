package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEvent;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEventTarget;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEventType;
import com.sktechx.godmusic.personal.common.amqp.service.AmqpService;
import com.sktechx.godmusic.personal.common.domain.type.AppNameType;
import com.sktechx.godmusic.personal.common.domain.type.BitrateType;
import com.sktechx.godmusic.personal.common.domain.type.SourceType;
import com.sktechx.godmusic.personal.common.domain.type.TrackLogType;
import com.sktechx.godmusic.personal.common.exception.PersonalErrorDomain;
import com.sktechx.godmusic.personal.rest.model.dto.listen.SettlementInfoDto;
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

import java.util.Optional;

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
	
	private final String FLAC_ALTERTIVE_STREAMING_SERVCIE_ID = "TS2";
	private final String FLAC_ALTERTIVE_DRM_SERVCIE_ID = "TD2";
	private final String FLAC_ALTERTIVE_MUSIC_VIDEOD_SERVCIE_ID = "TM2";
	
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
		String playType = Optional.ofNullable(request.getSourceType()).map(SourceType::getPlayType).orElse(null);
		
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
			SettlementInfoDto settlementInfo = settlementService.getSettlementInfo(memberNo, playType);
			
			if(ObjectUtils.isEmpty(settlementInfo)){
				log.warn("정산 정보 조회 실패 : request={}", request);
				throw new CommonBusinessException(PersonalErrorDomain.USER_PSSRL_NOT_FOUND);
			}
			
			String serviceId = evaluateServiceId(request, settlementInfo);
			
			if(ObjectUtils.isEmpty(serviceId)){
				log.warn("serviceId 조회 실패 : request={}", request);
				throw new CommonBusinessException(PersonalErrorDomain.USER_PSSRL_NOT_FOUND);
			}
			
			trackListenBuilder
					.pssrlCd(serviceId)	// todo evtTrackCharge 에서 제거시 같이 제거 필요
					.serviceId(serviceId)
					.prchsId(settlementInfo.getPrchsId())
					.goodsId(settlementInfo.getGoodsId());
			
			if (SourceType.DN == request.getSourceType()) {
				if (StringUtils.isEmpty(request.getOwnerToken())) {
					log.warn("OwnerToken 없음 (DRM 스트리밍)");
//					throw new CommonBusinessException(PersonalErrorDomain.OWNER_TOKEN_INVALID);
				} else {
					OwnerTokenClaim ownerToken = drmService.getOwnerTokenInfo(request.getOwnerToken());
					
					if (ObjectUtils.isEmpty(ownerToken)) {
						log.warn("OwnerToken Parse 실패 (DRM 스트리밍)");
//						throw new CommonBusinessException(PersonalErrorDomain.OWNER_TOKEN_INVALID);
					} else {
						trackListenBuilder.drmMemberNo(ownerToken.getMemberNo());
						trackListenBuilder.drmGoodsId(ownerToken.getGoodsId());
						trackListenBuilder.drmPrchsId(ownerToken.getPurchaseId());
						trackListenBuilder.drmPssrlCd(ownerToken.getPssrlCode());
						trackListenBuilder.drmServiceId(ownerToken.getServiceId());
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
	
	private String evaluateServiceId(ListenTrackRequest request, SettlementInfoDto settlement) {
		if (ObjectUtils.isEmpty(request.getBitrate())) {
			log.info("1분 청취 요청 Bitrate 없음");
			return null;
		}

		if (ObjectUtils.isEmpty(request.getSourceType())) {
			log.info("1분 청취 요청 SourceType 없음");
			return null;
		}
		
		// todo 무료곡 서비스 시점에 다시 수정 필요
//		if (YnType.Y == request.getFreeYn()) {
//			return "FREE_SVC";
//		}
		
		// flac 요청일 경우 대체
		if (request.getBitrate() == BitrateType.BITRATE_FLAC16 || request.getBitrate() == BitrateType.BITRATE_FLAC24) {
			switch (request.getSourceType()) {
				case STRM:
					return FLAC_ALTERTIVE_STREAMING_SERVCIE_ID;
				case DN:
					return FLAC_ALTERTIVE_DRM_SERVCIE_ID;
				case MV:
					return FLAC_ALTERTIVE_MUSIC_VIDEOD_SERVCIE_ID;
			}
		}
		
		return settlement.getSvcId();
	}
}
