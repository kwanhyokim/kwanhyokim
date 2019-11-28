package com.sktechx.godmusic.personal.rest.service.impl;

import com.google.common.base.Strings;
import com.sktechx.godmusic.lib.domain.CommonApiResponse;
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
import com.sktechx.godmusic.personal.rest.client.McpClient;
import com.sktechx.godmusic.personal.rest.client.model.OneTimeUrlResponse;
import com.sktechx.godmusic.personal.rest.model.dto.listen.ResourceListen;
import com.sktechx.godmusic.personal.rest.model.dto.listen.SettlementInfoDto;
import com.sktechx.godmusic.personal.rest.model.dto.listen.TrackListen;
import com.sktechx.godmusic.personal.rest.model.vo.drm.OwnerTokenClaim;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenRequest;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenTrackRequest;
import com.sktechx.godmusic.personal.rest.model.vo.video.ResourcePlayLogRequest;
import com.sktechx.godmusic.personal.rest.repository.ListenMapper;
import com.sktechx.godmusic.personal.rest.service.DrmService;
import com.sktechx.godmusic.personal.rest.service.ListenService;
import com.sktechx.godmusic.personal.rest.service.PurchaseService;
import com.sktechx.godmusic.personal.rest.service.SettlementService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendDataService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import java.nio.charset.StandardCharsets;
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

	@Value("${gd.settlement.jwt.secret-key}")
	private String JWT_SECRET_KEY;

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

	@Autowired
	McpClient mcpClient;
	
	@Override
	public void addListenHistByChannel(ListenRequest request, Long memberNo, Long characterNo) {
		listenMapper.addListenHistByChannel(request.getListenType(), request.getListenTypeId(),
				memberNo, characterNo);

		//추천 패널의 경우 기존 추천 데이터  삭제 방지를 위한 DB 업데이트 처리
		if(isRecommendListen(request.getListenType())){
			recommendDataService.updateRecommendDataRemovePrevent(request , characterNo);
		}

	}

	public void addPlayHistoryByResource(ResourcePlayLogRequest request, GMContext currentContext, HttpServletRequest httpServletRequest) {
		Long memberNo = currentContext.getMemberNo();
		Long characterNo = currentContext.getCharacterNo();
		String deviceId = currentContext.getDeviceId();
		String playChannel = AppNameType.fromCode(currentContext.getAppName()) != null ? AppNameType.fromCode(currentContext.getAppName()).getCode() : "";
		Long sourceId = request.getResourceId();
		String logType = request.getLogType();
		String bitrate = request.getQuality();
		String quality = request.getQuality();
		String osType = request.getOsType() != null ? request.getOsType().getCode() : "";
		String clientIp = httpServletRequest.getHeader("client_ip") != null ? httpServletRequest.getHeader("client_ip") : "";
		Long channelId = request.getChannelId();
		String channelType = request.getChannelType();
		String listenSessionId = StringUtils.isEmpty(request.getSessionId()) ? null : request.getSessionId();
		SourceType sourceType = SourceType.fromCode(request.getSourceType());

		String serviceId = null;
		Long purchaseId = null;
		Long goodsId = null;

		if (!Strings.isNullOrEmpty(request.getSttToken())) {
			String sttToken = request.getSttToken();

			Jws<Claims> claims = Jwts.parser()
					.setSigningKey(JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8))
					.parseClaimsJws(sttToken);

			Integer version = claims.getBody().get("version", Integer.class);
			serviceId = claims.getBody().get("serviceId", String.class);
			purchaseId = claims.getBody().get("purchaseId", Long.class);
			goodsId = claims.getBody().get("goodsId", Long.class);

			log.debug("[정산 토큰(sttToken) 정보] version={}, serviceId={}, purchaseId={}, goodsId={}", version, serviceId, purchaseId, goodsId);
		}

		ResourceListen listen = ResourceListen.builder()
				.playChnl(playChannel)
				.memberNo(memberNo)
				.characterNo(characterNo)
				.sourceId(String.valueOf(sourceId))
				.logType(logType)
				.bitrate(bitrate)
				.quality(quality)
				.trackTotTm(request.getRunningTimeSecs())
				.elapsedTm(request.getDuration())
				.osType(osType)
				.dvcId(deviceId)
				.chnlId(channelId)
				.chnlType(channelType)
				.memberRcmdId(null)
				.addTm(request.getAddDateTime())
				.sessionToken(null)
				.sourceType(sourceType)
				.free(request.isFree())
				.timeMillis(System.currentTimeMillis())
				.userClientIp(clientIp)
				.ownerToken(request.getOwnerToken())
				.listenSessionId(listenSessionId)
				.pssrlCd(serviceId)
				.build();

		ResourceListen.ResourceListenBuilder listenBuilder = listen.toBuilder();

		ResourcePlayLogRequest.LogType playLogType = ResourcePlayLogRequest.LogType.fromCode(request.getLogType());
		if(ResourcePlayLogRequest.LogType.ONEMIN == playLogType) {

			if (SourceType.VIDEO_MV == sourceType) {
				if (Strings.isNullOrEmpty(serviceId)) {
					log.warn("[1분 리소스 청취로그] 정산정보 없음");
					throw new CommonBusinessException(PersonalErrorDomain.USER_PSSRL_NOT_FOUND);
				}
			}

			listenBuilder
					.pssrlCd(serviceId)
					.serviceId(serviceId)
					.prchsId(purchaseId)
					.goodsId(goodsId);
		}
		amqpService.deliverTrackListen(listenBuilder.build());
		log.info("[RESOURCE 청취로그 MQ 발송] listen = {}", listenBuilder.toString());

		UserEventType userEventType = UserEventType.fromPlayLogType(playLogType);
		if( !userEventType.equals(UserEventType.UNKNOWN) )	{
			UserEvent userEvent = UserEvent.newBuilder()
					.playChnl(currentContext.getAppName())
					.event(userEventType)
					.memberNo(memberNo)
					.charactorNo(characterNo)
					.targetId(String.valueOf(sourceId))
					.targetType(UserEventTarget.VIDEO)
					.sourceType(sourceType)
					.trackTotTm(request.getRunningTimeSecs())
					.elapsedTm(request.getDuration())
					.timeMillis(System.currentTimeMillis())
					.build();
			amqpService.deliverUserEvent(userEvent);
			log.info("[RESOURCE 청취로그 사용자 EVENT MQ 발송] event = {}", userEvent.toString());
		}
	}

	@Override
	public void addListenHistByTrack(ListenTrackRequest request, GMContext currentContext, HttpServletRequest httpServletRequest) {
		Long memberNo = currentContext.getMemberNo();
		Long characterNo = currentContext.getCharacterNo();
		Long trackId = request.getTrackId();
		String deviceId = currentContext.getDeviceId();
		String playChannel = AppNameType.fromCode(currentContext.getAppName()) != null ? AppNameType.fromCode(currentContext.getAppName()).getCode() : "";
		String logType = request.getTrackLogType() != null ? request.getTrackLogType().getCode() : "";
		String bitrate = request.getBitrate() != null ? request.getBitrate().getCode() : BitrateType.UNKNOWN.getCode();
		String osType = request.getOsType() != null ? request.getOsType().getCode() : "";
		String clientIp = httpServletRequest.getHeader("client_ip") != null ? httpServletRequest.getHeader("client_ip") : "";
		String chnlType = StringUtils.isEmpty(request.getChannelType()) ? null : request.getChannelType();
		String listenSessionId = StringUtils.isEmpty(request.getListenSessionId()) ? null : request.getListenSessionId();
		String playType = Optional.ofNullable(request.getSourceType()).map(SourceType::getPlayType).orElse(null);
		
		TrackListen trackListen = TrackListen.builder()
				.playChnl(playChannel)
				.memberNo(memberNo)
				.characterNo(characterNo)
				.trackId(trackId)
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

		log.info("[TRACK 청취로그] addListenHistByTrack...");
		if (request.getTrackLogType() == TrackLogType.ONEMIN) {

			SettlementToken sttToken = Optional.ofNullable(request.getSttToken())
					.map(token -> parseSettlementToken(token))
					.orElse(null);

			if (sttToken != null) {
				log.debug("[TRACK 청취로그] sttToken 이용하여 serviceId 전달");
				trackListenBuilder
						.pssrlCd(sttToken.getServiceId())
						.serviceId(sttToken.getServiceId())
						.prchsId(sttToken.getPurchaseId())
						.goodsId(sttToken.getGoodsId());
			}
			else {
				log.debug("[TRACK 청취로그][sttToken 없음]");
				SettlementInfoDto settlementInfo = settlementService.getSettlementInfo(memberNo, playType);

				if (ObjectUtils.isEmpty(settlementInfo)) {
					log.warn("[TRACK 청취로그] 정산 정보 조회 실패. request={}", request);
					throw new CommonBusinessException(PersonalErrorDomain.USER_PSSRL_NOT_FOUND);
				}

				/*
				 * Notice. 무료곡인 경우 MCP에 조회하여 MCP쪽 svcCd를 청취 로그의 serviceId 로 남긴다.
				 *         무료곡인 경우는 정산쪽의 serviceId 와 MCP쪽의 serviceId(svcCd)가 다르기 때문.
				 */
				String serviceId = null;
				if (request.getFreeYn() == YnType.Y) {
					log.debug("[TRACK 청취로그] 무료곡 청취 로그. trackId={}, freeYn={}", trackId, request.getFreeYn());
					serviceId = getServiceCodeFromMCP(memberNo, deviceId, trackId, bitrate, osType);
				}
				else {
					serviceId = evaluateServiceId(request, settlementInfo);
				}

				if(ObjectUtils.isEmpty(serviceId)){
					log.warn("[TRACK 청취로그] serviceId 조회 실패. request={}", request);
					throw new CommonBusinessException(PersonalErrorDomain.USER_PSSRL_NOT_FOUND);
				}

				trackListenBuilder
						.pssrlCd(serviceId)
						.serviceId(serviceId)
						.prchsId(settlementInfo.getPrchsId())
						.goodsId(settlementInfo.getGoodsId());
			}
		}

		if (SourceType.DN == request.getSourceType()) {
			if (StringUtils.isEmpty(request.getOwnerToken())) {
				log.warn("OwnerToken 없음 (DRM 스트리밍)");
			}
			else {
				OwnerTokenClaim ownerToken = drmService.getOwnerTokenInfo(request.getOwnerToken());

				if (ObjectUtils.isEmpty(ownerToken)) {
					log.warn("OwnerToken Parse 실패 (DRM 스트리밍)");
				}
				else {
					trackListenBuilder.drmMemberNo(ownerToken.getMemberNo());
					trackListenBuilder.drmPssrlCd(ownerToken.getPssrlCode());
					trackListenBuilder.drmServiceId(ownerToken.getServiceId());
					trackListenBuilder.drmPrchsId(ownerToken.getPurchaseId());
					trackListenBuilder.drmGoodsId(ownerToken.getGoodsId());
				}
			}
		}
		
		if(YnType.Y.equals(request.getFreeYn())) {
			trackListenBuilder.free(true);
		}

		amqpService.deliverTrackListen(trackListenBuilder.build());
		log.info("[TRACK 청취로그][MQ 발송] {}", trackListenBuilder.toString());

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
			log.info("[TRACK 청취로그][UserEvent MQ 발송] {}", userEvent.toString());
		}
	}

	private String getServiceCodeFromMCP(Long memberNo, String deviceId, Long trackId, String bitrate, String osType) {
		CommonApiResponse<OneTimeUrlResponse> oneTimeUrlResponse = mcpClient.getOneTimeURL(
				String.valueOf(memberNo), deviceId, String.valueOf(trackId), bitrate, osType, null, null);
		if (oneTimeUrlResponse != null && oneTimeUrlResponse.getData() != null) {
			return oneTimeUrlResponse.getData().getSvcCd();
		}
		return null;
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
			return settlement.getSvcId();
		}

		if (ObjectUtils.isEmpty(request.getSourceType())) {
			log.info("1분 청취 요청 SourceType 없음");
			return settlement.getSvcId();
		}
		
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

	private SettlementToken parseSettlementToken(String sttToken) {
		Jws<Claims> claims = Jwts.parser()
				.setSigningKey(JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8))
				.parseClaimsJws(sttToken);

		Integer version = claims.getBody().get("version", Integer.class);
		String serviceId = claims.getBody().get("serviceId", String.class);
		Long purchaseId = claims.getBody().get("purchaseId", Long.class);
		Long goodsId = claims.getBody().get("goodsId", Long.class);

		log.debug("[정산 토큰(sttToken) 정보] version={}, serviceId={}, purchaseId={}, goodsId={}", version, serviceId, purchaseId, goodsId);

		return SettlementToken.builder()
				.version(version)
				.serviceId(serviceId)
				.purchaseId(purchaseId)
				.goodsId(goodsId)
				.build();
	}

	@Getter
	@ToString
	@Builder
	static class SettlementToken {
		Integer version;
		String serviceId;
		Long purchaseId;
		Long goodsId;
	}
}
