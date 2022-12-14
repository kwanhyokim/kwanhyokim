package com.sktechx.godmusic.personal.rest.service.impl;

import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.lib.utils.ComparableVersion;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEvent;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEventTarget;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEventType;
import com.sktechx.godmusic.personal.common.amqp.service.AmqpService;
import com.sktechx.godmusic.personal.common.domain.constant.LikeConstant;
import com.sktechx.godmusic.personal.common.domain.type.AppNameType;
import com.sktechx.godmusic.personal.common.domain.type.SourceType;
import com.sktechx.godmusic.personal.common.exception.PersonalErrorDomain;
import com.sktechx.godmusic.personal.common.util.CommonUtils;
import com.sktechx.godmusic.personal.rest.client.MemberClient;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.client.model.MetaVideoRequestVo;
import com.sktechx.godmusic.personal.rest.model.dto.AlbumDto;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.PlayListDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.member.CharacterDto;
import com.sktechx.godmusic.personal.rest.model.dto.member.CharacterType;
import com.sktechx.godmusic.personal.rest.model.vo.like.*;
import com.sktechx.godmusic.personal.rest.model.vo.video.RangeResponse;
import com.sktechx.godmusic.personal.rest.model.vo.video.VideoVo;
import com.sktechx.godmusic.personal.rest.repository.LikeMapper;
import com.sktechx.godmusic.personal.rest.service.LikeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toMap;

/**
 * Created by Kobe.
 *
 * @author Kobe/?????????/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 7. 31.
 * @time PM 3:41
 */
@Slf4j
@Service("likeService")
public class LikeServiceImpl implements LikeService {

	@Autowired
	private MetaClient metaClient;

	@Autowired
	private MemberClient memberClient;

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Autowired
	private LikeMapper likeMapper;

	@Autowired
	private AmqpService amqpService;

	@Override
	public LikePlaylistListResponse getPlayListLikeListByLikeType(Long characterNo, String appVersion, Pageable pageable) {
		int totalCount = 0;

		List<LikeTypeVo> likeTypeVos = likeMapper.getLikePlaylistIdsByLikeType(characterNo, pageable);

		if (CollectionUtils.isEmpty(likeTypeVos)) return null;

		List<Long> chnlIds = new ArrayList<>();
		List<Long> chartIds = new ArrayList<>();

		for (LikeTypeVo l : likeTypeVos) {
			if (LikeConstant.LIKE_CHANNEL.equals(l.getLikeType())
			) {
				chnlIds.add(l.getLikeTypeId());

			} else if (LikeConstant.LIKE_CHART.equals(l.getLikeType())) {
				chartIds.add(l.getLikeTypeId());
			}
		}

		Boolean exceptFlacChnl = false;
		Boolean exceptAfloChnl = false;

		if(!ObjectUtils.isEmpty(appVersion) && new ComparableVersion(appVersion).compareTo( new ComparableVersion("4.6.0")) < 0 ){
			exceptFlacChnl = true;
		}

		CharacterDto characterDto = getCharacter(characterNo);

		if(characterDto != null && !CharacterType.AFLO.equals(characterDto.getCharacterType())){
			exceptAfloChnl = true;
		}

		List<PlayListDto> playListDtos = likeMapper.getLikePlaylistByLikeType(characterNo,
				(CollectionUtils.isEmpty(chnlIds) ? null : chnlIds),
				(CollectionUtils.isEmpty(chartIds) ? null : chartIds),
				exceptFlacChnl,
				exceptAfloChnl
		);

		if (CollectionUtils.isEmpty(playListDtos)) return null;

		for (PlayListDto p : playListDtos) {
			p.setRenewYn(YnType.N);
			p.replacePlayListImageIfRepImageExists();
			if (p.getPlayListType().getCode().equals(LikeConstant.LIKE_CHANNEL) && p.getRenewDateTime() != null) {
				Calendar c = Calendar.getInstance();
				c.setTime(new Date());
				c.add(Calendar.DATE , -1);
				if(p.getRenewDateTime().after(c.getTime())){
					p.setRenewYn(YnType.Y);
				}
			}
		}

		totalCount = getLikeTotalCount(LikeConstant.LIKE_CHANNEL, characterNo);
		totalCount += getLikeTotalCount(LikeConstant.LIKE_CHART, characterNo);

		return new LikePlaylistListResponse(new PageImpl(playListDtos, pageable, totalCount));
	}

	@Override
	public LikeAlbumListResponse getAlbumLikeListByLikeType(Long characterNo, Pageable pageable) {
		int totalCount = 0;

		List<AlbumDto> albumDtos = likeMapper.getLikeAlbumByLikeType(characterNo, pageable);

		if (CollectionUtils.isEmpty(albumDtos)) return null;

		totalCount = getLikeTotalCount(LikeConstant.LIKE_ALBUM, characterNo);

		return new LikeAlbumListResponse(new PageImpl(albumDtos, pageable, totalCount));
	}

	@Override
	public LikeArtistListResponse getArtistLikeListByLikeType(Long characterNo, Pageable pageable) {
		int totalCount = 0;

		List<ArtistDto> artistDtos = likeMapper.getLikeArtistByLikeType(characterNo, pageable);

		if (CollectionUtils.isEmpty(artistDtos)) return null;

		totalCount = getLikeTotalCount(LikeConstant.LIKE_ARTIST, characterNo);

		return new LikeArtistListResponse(new PageImpl(artistDtos, pageable, totalCount));
	}

	@Override
	public LikeTrackListResponse getTrackLikeListByLikeType(Long characterNo, Pageable pageable) {
		int totalCount = 0;
		long startTime = System.currentTimeMillis();

		List<TrackDto> trackDtos = likeMapper.getLikeTrackByLikeType(characterNo, pageable);

		long elapsed = System.currentTimeMillis() - startTime;

		log.debug("getTrackLikeListByLikeType:: " + elapsed);

		if (CollectionUtils.isEmpty(trackDtos)) return null;

		totalCount = getLikeTotalCount(LikeConstant.LIKE_TRACK, characterNo);

		return new LikeTrackListResponse(new PageImpl(trackDtos, pageable, totalCount));
	}

	@Override
	public RangeResponse<VideoVo> getLikeVideos(Long characterNo, Pageable pageable) {
		requireNonNull(characterNo);
		requireNonNull(pageable);

		long startTime = System.currentTimeMillis();

		List<Long> videoIds = likeMapper.getLikeVideoByLikeType(characterNo, pageable);
		if (CollectionUtils.isEmpty(videoIds)) {
			return RangeResponse.empty();
		}

		List<VideoVo> response = Optional.ofNullable(
				metaClient.getVideos(MetaVideoRequestVo.builder()
						.videoIds(videoIds)
						.build()).getData().getList())
				.orElse(Collections.emptyList());

		long elapsed = System.currentTimeMillis() - startTime;
		log.info("[MetaClient][{}ms] responseCount={}", elapsed, response.size());

		Map<Long, VideoVo> videoIndex = response.stream()
				.filter(VideoVo::exhibitable)
				.collect(toMap(VideoVo::getVideoId, Functions.identity()));

		// videoIds ????????? ???????????? ?????? VideoVo ??????
		List<VideoVo> result = Lists.newArrayList();
		for (Long videoId : videoIds) {
			if (videoIndex.containsKey(videoId)) {
				result.add(videoIndex.get(videoId));
			}
		}

		int totalCount = likeMapper.getLikeVideoCountByLikeType(characterNo);

		log.info("[?????? ????????? ??????] likeVideoCount={}, filteredVideoCount={}, totalCount={}", videoIds.size(), result.size(), totalCount);

		return RangeResponse.of(new PageImpl(result, pageable, totalCount));
	}

	/**
	 * ????????? ????????? VIDEO ??? ?????? UserEvent ??? sourceType ????????? meta ????????? ?????????
	 * Note. ??????????????? ????????? ?????? ????????? ???????????? ?????? ?????? ????????? ?????? ????????? ??????
	 */
	@Override
	@Transactional
	public void addLike(LikeRequest request, Long characterNo) {
		Long memberNo = GMContext.getContext().getMemberNo();

		String likeType = request.getLikeType();
		String sourceType = inspectMetaValidationAndReturnSourceType(likeType, request.getLikeTypeId(), getLikeTypeNotFoundMessage(likeType));

		validCheckAddLike(request, characterNo);

		likeMapper.updateLikeDispSn(request.getLikeType(), characterNo);
		likeMapper.insertLike(request.getLikeType(), request.getLikeTypeId(), characterNo);

		try {
			sendUserEvent(
					UserEventType.LIKE,
					AppNameType.FLO_APP.getCode(),
					memberNo,
					characterNo,
					request.getLikeTypeId(),
					UserEventTarget.valueOf(request.getLikeType()),
					sourceType, null);
		}
		catch (Exception e){
			log.warn("[UserEvent] Like :: like add UserEvent :: failed Message :: {}", e.getMessage());
		}
	}

	@Override
	public void deleteLike(LikeTypeIdListRequest request, Long characterNo) {

		if(!ObjectUtils.isEmpty(request) && !CollectionUtils.isEmpty(request.getLikeTypeList())){
			request.getLikeTypeList().stream().forEach(likeTypeVo -> {

				if(needToChangeLikeTypeToChannel(likeTypeVo.getLikeType())){
					likeTypeVo.setLikeType(LikeConstant.LIKE_CHANNEL);
				}

			});
		}

		Map<String, Object> batchParam = new HashMap<>();

		try(SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false)){
			IntStream.range(0, request.getLikeTypeList().size())
					.forEach(index ->
							{
								batchParam.clear();
								batchParam.put("likeType", request.getLikeTypeList().get(index).getLikeType());
								batchParam.put("likeTypeId", request.getLikeTypeList().get(index).getLikeTypeId());
								batchParam.put("characterNo", characterNo);
								sqlSession.update("deleteLike", batchParam);
							}
					);
			sqlSession.flushStatements();
			sqlSession.commit();
		}catch(Exception e){
			log.error("Like :: like delete :: Error Message :: {} ", e.getMessage());
			throw new CommonBusinessException(CommonErrorDomain.INTERNAL_SERVER_ERROR);
		}

		try {
			/*
			 * UserEvent ??? bulk field ?????? ???
			 * bulk ????????? ?????? ??????????????? ???????????? ??????????????? ?????? ???????????? ????????? ????????? ????????? ????????? ????????????
			 * ???????????? bulk = false or null ??????.
			 * bulk = true ??? ?????? Batch ?????? skip ????????? ????????????
			 */
			boolean notBulked = true;
			for (LikeTypeVo each : request.getLikeTypeList()) {

				UserEvent.UserEventBuilder builder = UserEvent.newBuilder()
						.playChnl(AppNameType.FLO_APP.getCode())
						.event(UserEventType.UNLIKE)
						.memberNo(GMContext.getContext().getMemberNo())
						.charactorNo(characterNo)
						.targetId(String.valueOf(each.getLikeTypeId()))
						.targetType(UserEventTarget.valueOf(each.getLikeType()))
						.sourceType(null)
						.timeMillis(System.currentTimeMillis());

				if (notBulked) {
					builder.bulk(Boolean.FALSE);
					notBulked = false;
				}
				else {
					builder.bulk(Boolean.TRUE);
				}

				UserEvent userEvent = builder.build();
				amqpService.deliverUserEvent(userEvent);
				log.info("[UserEvent] {}", userEvent);
			}
		}
		catch (Exception e) {
			log.warn("Like :: like delete UserEvent :: failed Message :: {}", e.getMessage());
		}

	}

	@Override
	public void updateLike(LikeTypeIdListRequest request, Long characterNo) {
		Map<String, Object> batchParam = new HashMap<>();

		try(SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false)){
			IntStream.range(0, request.getLikeTypeList().size())
					.forEach(index ->
							{
								batchParam.clear();
								batchParam.put("dispSn", index+1);
								batchParam.put("likeType", request.getLikeTypeList().get(index).getLikeType());
								batchParam.put("likeTypeId", request.getLikeTypeList().get(index).getLikeTypeId());
								batchParam.put("characterNo", characterNo);
								sqlSession.update("updateLikeListByLikeTypeId", batchParam);
							}
					);
			sqlSession.flushStatements();
			sqlSession.commit();
		}catch(Exception e){
			log.error("Like :: like update :: Error Message :: {}", e.getMessage());
			throw new CommonBusinessException(CommonErrorDomain.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public LikeYnResponse getLikeYn(String likeType, Long likeTypeId, Long characterNo) {
		if (likeMapper.getLikeCountByLikeTypeAndLikeTypeId(likeType, likeTypeId, characterNo) > 0) return new LikeYnResponse("Y");
		return new LikeYnResponse("N");
	}

	private Boolean needToChangeLikeTypeToChannel(String likeType){

		return LikeConstant.LIKE_FLAC.equals(likeType) ||
				LikeConstant.LIKE_AFLO.equals(likeType);
	}

	private void validCheckAddLike(LikeRequest request, Long characterNo){

		String likeType = request.getLikeType();

		if( needToChangeLikeTypeToChannel(likeType)){
			likeType = LikeConstant.LIKE_CHANNEL;
			request.setLikeType(likeType);
		}

		int likeCnt = likeMapper.getLikeCountByLikeTypeAndLikeTypeId(likeType, request.getLikeTypeId(), characterNo);

		if(likeCnt > 0){
			throw new CommonBusinessException(getLikeTypeDuplicated(likeType));
		}

		int likeTotalCnt = getLikeTotalCount(likeType, characterNo);

		if(LikeConstant.LIKE_CHANNEL.equals(likeType)) {
			likeTotalCnt += getLikeTotalCount(LikeConstant.LIKE_CHART, characterNo);
		} else if(LikeConstant.LIKE_CHART.equals(likeType)) {
			likeTotalCnt += getLikeTotalCount(LikeConstant.LIKE_CHANNEL, characterNo);
		}

		if(likeTotalCnt >= 1000){
			throw new CommonBusinessException(getLikeTypeOverAdd(likeType));
		}
	}

	private int getLikeTotalCount(String likeType, Long characterNo) {
		switch (likeType) {
			case LikeConstant.LIKE_CHANNEL :
				return likeMapper.getLikeChannelCountByLikeType(likeType, characterNo);
			case LikeConstant.LIKE_ALBUM :
				return likeMapper.getLikeAlbumCountByLikeType(characterNo);
			case LikeConstant.LIKE_CHART :
				return likeMapper.getLikeChartCountByLikeType(characterNo);
			case LikeConstant.LIKE_ARTIST :
				return likeMapper.getLikeArtistCountByLikeType(characterNo);
			case LikeConstant.LIKE_TRACK :
				return likeMapper.getLikeTrackCountByLikeType(characterNo);
			case LikeConstant.LIKE_VIDEO:
				return likeMapper.getLikeVideoCountByLikeType(characterNo);
			default :
				throw new CommonBusinessException(CommonErrorDomain.BAD_REQUEST);
		}
	}

	private PersonalErrorDomain getLikeTypeNotFoundMessage(String likeType) {
		switch (likeType) {
			case LikeConstant.LIKE_FLAC:
			case LikeConstant.LIKE_CHANNEL :
				return PersonalErrorDomain.CHANNEL_NOT_FOUND;
			case LikeConstant.LIKE_ALBUM :
				return PersonalErrorDomain.ALBUM_NOT_FOUND;
			case LikeConstant.LIKE_CHART :
				return PersonalErrorDomain.CHART_NOT_FOUND;
			case LikeConstant.LIKE_ARTIST :
				return PersonalErrorDomain.ARTIST_NOT_FOUND;
			case LikeConstant.LIKE_TRACK :
				return PersonalErrorDomain.TRACK_NOT_FOUND;
			case LikeConstant.LIKE_AFLO:
				return PersonalErrorDomain.CHANNEL_NOT_FOUND;
			case LikeConstant.LIKE_VIDEO:
				return PersonalErrorDomain.VIDEO_NOT_FOUND;
			default :
				throw new CommonBusinessException(CommonErrorDomain.BAD_REQUEST);
		}
	}

	private PersonalErrorDomain getLikeTypeDuplicated(String likeType) {
		switch (likeType) {
			case LikeConstant.LIKE_CHANNEL :
				return PersonalErrorDomain.CHANNEL_DUPLICATED_LIKE;
			case LikeConstant.LIKE_ALBUM :
				return PersonalErrorDomain.ALBUM_DUPLICATED_LIKE;
			case LikeConstant.LIKE_CHART :
				return PersonalErrorDomain.CHART_DUPLICATED_LIKE;
			case LikeConstant.LIKE_ARTIST :
				return PersonalErrorDomain.ARTIST_DUPLICATED_LIKE;
			case LikeConstant.LIKE_TRACK :
				return PersonalErrorDomain.TRACK_DUPLICATED_LIKE;
			case LikeConstant.LIKE_VIDEO :
				return PersonalErrorDomain.VIDEO_DUPLICATED_LIKE;
			default :
				throw new CommonBusinessException(CommonErrorDomain.BAD_REQUEST);
		}
	}

	private PersonalErrorDomain getLikeTypeOverAdd(String likeType) {
		switch (likeType) {
			case LikeConstant.LIKE_CHANNEL :
				return PersonalErrorDomain.CHANNEL_OVER_ADD_LIKE;
			case LikeConstant.LIKE_ALBUM :
				return PersonalErrorDomain.ALBUM_OVER_ADD_LIKE;
			case LikeConstant.LIKE_CHART :
				return PersonalErrorDomain.CHART_OVER_ADD_LIKE;
			case LikeConstant.LIKE_ARTIST :
				return PersonalErrorDomain.ARTIST_OVER_ADD_LIKE;
			case LikeConstant.LIKE_TRACK :
				return PersonalErrorDomain.TRACK_OVER_ADD_LIKE;
			case LikeConstant.LIKE_VIDEO:
				return PersonalErrorDomain.VIDEO_OVER_ADD_LIKE;
			default :
				throw new CommonBusinessException(CommonErrorDomain.BAD_REQUEST);
		}
	}

	/**
	 * 2019.08.26 updated by Daniel
	 * GODMUSIC-10649 - (WEB) Artist&FLO ??????????????? ????????? ?????? ????????? ????????? ?????? ?????? ?????? ????????? ???????????? ??????????????? ????????????????????????
	 * META API ?????? AFLO type ??? ?????? ?????? ?????? ?????? ????????? ?????? valid api ??? ???????????? ????????? ?????????.
	 *
	 * 2019.11.19 updated by Daniel
	 * GODMUSIC-13314 - UserEvent ??? sourceType ??? ???????????? ????????? VideoType ????????? ???????????? ??????????????? ??????????????? method ??????
	 */
	private String inspectMetaValidationAndReturnSourceType(String likeType, Long likeTypeId, PersonalErrorDomain message) {
		String sourceType = null;
		CommonApiResponse response;
		switch (likeType) {
			case LikeConstant.LIKE_FLAC :
			case LikeConstant.LIKE_CHANNEL :
				response = metaClient.channel(likeTypeId);
				break;
			case LikeConstant.LIKE_ALBUM :
				response = metaClient.album(likeTypeId);
				break;
			case LikeConstant.LIKE_CHART :
				response = metaClient.chart(likeTypeId);
				break;
			case LikeConstant.LIKE_ARTIST :
				response = metaClient.artists(likeTypeId);
				break;
			case LikeConstant.LIKE_TRACK :
				response = metaClient.track(likeTypeId);
				break;
			case LikeConstant.LIKE_AFLO :
				response = metaClient.validChannelOrEmpty(likeTypeId, likeType);
				break;
			case LikeConstant.LIKE_VIDEO :
				response = metaClient.getVideo(likeTypeId);
				if (response != null || CommonUtils.notEmpty(response.getData())) {
					sourceType = "VIDEO_" + ((VideoVo) response.getData()).getVideoType();
				}
				break;
			default :
				throw new CommonBusinessException(CommonErrorDomain.BAD_REQUEST);
		}

		log.info("validMeta :: " + response.toString());

		if(StringUtils.isEmpty(response) || StringUtils.isEmpty(response.getCode())
				|| !"2000000".equals(response.getCode()) || CommonUtils.empty(response.getData())) {
			throw new CommonBusinessException(message);
		}

		return sourceType;
	}

	private void sendUserEvent(UserEventType userEventType, String appName, Long memberNo,
							   Long characterNo, Long targetId, UserEventTarget targetType,
							   @Nullable String sourceType, @Nullable Boolean bulked) {

		log.debug("[SendUserEvent] sourceType = {}", sourceType);

		UserEvent userEvent = UserEvent.newBuilder()
				.playChnl(appName)
				.event(userEventType)
				.memberNo(memberNo)
				.charactorNo(characterNo)
				.targetId(String.valueOf(targetId))
				.targetType(targetType)
				.sourceType(SourceType.fromCode(sourceType))
				.timeMillis(System.currentTimeMillis())
				.bulk(bulked == null ? Boolean.FALSE : bulked)
				.build();

		amqpService.deliverUserEvent(userEvent);
		log.info("[Add Like - User event] " + userEvent.toString());

	}

	private CharacterDto getCharacter(Long characterNo){
		CommonApiResponse<CharacterDto> response = memberClient.getCharacter(characterNo);
		if(response != null && "2000000".equals(response.getCode())){
			return response.getData();
		}
		return null;
	}
}
