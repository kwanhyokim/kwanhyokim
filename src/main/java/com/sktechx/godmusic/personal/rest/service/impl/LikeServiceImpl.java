package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.domain.constant.LikeConstant;
import com.sktechx.godmusic.personal.common.exception.CommonErrorMessage;
import com.sktechx.godmusic.personal.common.exception.InternalException;
import com.sktechx.godmusic.personal.common.exception.NotFoundException;
import com.sktechx.godmusic.personal.common.exception.ValidationException;
import com.sktechx.godmusic.personal.common.util.CommonUtils;
import com.sktechx.godmusic.personal.rest.model.dto.AlbumDto;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.PlayListDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.like.*;
import com.sktechx.godmusic.personal.rest.repository.LikeMapper;
import com.sktechx.godmusic.personal.rest.service.LikeService;
import com.sktechx.godmusic.personal.rest.service.MetaApiProxy;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 7. 31.
 * @time PM 3:41
 */
@Slf4j
@Service
public class LikeServiceImpl implements LikeService {

	@Autowired
	private MetaApiProxy metaApiProxy;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Autowired
	private LikeMapper likeMapper;

	@Override
	public LikePlaylistListResponse getPlayListLikeListByLikeType(Long characterNo, Pageable pageable) {
		int totalCount = 0;

		List<PlayListDto> playListDtos = likeMapper.getLikePlaylistByLikeType(characterNo, pageable);

		if (CollectionUtils.isEmpty(playListDtos)) return null;

		for (PlayListDto p : playListDtos) {
			p.setRenewYn(YnType.N);
			if (p.getPlayListType().equals(LikeConstant.LIKE_CHANNEL) && p.getRenewDateTime() != null) {
				Calendar c = Calendar.getInstance();
				c.setTime(new Date());
				c.add(Calendar.DATE , -1);
				if(p.getRenewDateTime().after(c.getTime())){
					p.setRenewYn(YnType.Y);
				}
			}
		}

		totalCount = likeMapper.getLikeCountByLikeType(LikeConstant.LIKE_CHANNEL, characterNo);
		totalCount += likeMapper.getLikeCountByLikeType(LikeConstant.LIKE_CHART, characterNo);

		return new LikePlaylistListResponse(new PageImpl(playListDtos, pageable, totalCount));
	}

	@Override
	public LikeAlbumListResponse getAlbumLikeListByLikeType(Long characterNo, Pageable pageable) {
		int totalCount = 0;

		List<AlbumDto> albumDtos = likeMapper.getLikeAlbumByLikeType(characterNo, pageable);

		if (CollectionUtils.isEmpty(albumDtos)) return null;

		totalCount = likeMapper.getLikeCountByLikeType(LikeConstant.LIKE_ALBUM, characterNo);

		return new LikeAlbumListResponse(new PageImpl(albumDtos, pageable, totalCount));
	}

	@Override
	public LikeArtistListResponse getArtistLikeListByLikeType(Long characterNo, Pageable pageable) {
		int totalCount = 0;

		List<ArtistDto> artistDtos = likeMapper.getLikeArtistByLikeType(characterNo, pageable);

		if (CollectionUtils.isEmpty(artistDtos)) return null;

		totalCount = likeMapper.getLikeCountByLikeType(LikeConstant.LIKE_ARTIST, characterNo);

		return new LikeArtistListResponse(new PageImpl(artistDtos, pageable, totalCount));
	}

	@Override
	public LikeTrackListResponse getTrackLikeListByLikeType(Long characterNo, Pageable pageable) {
		int totalCount = 0;

		List<TrackDto> trackDtos = likeMapper.getLikeTrackByLikeType(characterNo, pageable);

		if (CollectionUtils.isEmpty(trackDtos)) return null;

		totalCount = likeMapper.getLikeCountByLikeType(LikeConstant.LIKE_TRACK, characterNo);

		return new LikeTrackListResponse(new PageImpl(trackDtos, pageable, totalCount));
	}

	@Override
	public void addLike(LikeRequest request, Long characterNo) {
		validCheckAddLike(request, characterNo);

		likeMapper.insertLike(request.getLikeType(), request.getLikeTypeId(), characterNo);
	}

	@Override
	public void deleteLike(LikeListRequest request, Long characterNo) {
		Map<String, Object> batchParam = new HashMap<>();

		try(SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false)){
			IntStream.range(0, request.getLikeTypeIds().size())
					.forEach(index ->
							{
								batchParam.clear();
								batchParam.put("likeType", request.getLikeType());
								batchParam.put("likeTypeId", request.getLikeTypeIds().get(index));
								batchParam.put("characterNo", characterNo);
								sqlSession.update("deleteLike", batchParam);
							}
					);
			sqlSession.flushStatements();
			sqlSession.commit();
		}catch(Exception e){
			log.error("Like :: like delete :: Error Message", e.getMessage());
			throw new InternalException(CommonErrorMessage.INTERNAL_SERVER_ERROR);
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
			log.error("Like :: like update :: Error Message", e.getMessage());
			throw new InternalException(CommonErrorMessage.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public LikeYnResponse getLikeYn(String likeType, Long likeTypeId, Long characterNo) {
		if (likeMapper.getLikeCountByLikeTypeAndLikeTypeId(likeType, likeTypeId, characterNo) > 0) return new LikeYnResponse("Y");
		return new LikeYnResponse("N");
	}

	private void validCheckAddLike(LikeRequest request, Long characterNo){
		validMeta(request.getLikeType(), request.getLikeTypeId(), getLikeTypeNotFoundMessage(request.getLikeType()));

		int likeCnt = likeMapper.getLikeCountByLikeTypeAndLikeTypeId(request.getLikeType(), request.getLikeTypeId(), characterNo);

		if (LikeConstant.LIKE_CHANNEL.equals(request.getLikeType())) {
			likeCnt += likeMapper.getLikeCountByLikeTypeAndLikeTypeId(LikeConstant.LIKE_CHART, request.getLikeTypeId(), characterNo);
		} else if (LikeConstant.LIKE_CHART.equals(request.getLikeType())) {
			likeCnt += likeMapper.getLikeCountByLikeTypeAndLikeTypeId(LikeConstant.LIKE_CHANNEL, request.getLikeTypeId(), characterNo);
		}

		if(likeCnt > 0){
			throw new ValidationException(getLikeTypeDuplicated(request.getLikeType()));
		}

		int likeTotalCnt = likeMapper.getLikeCountByLikeType(request.getLikeType(), characterNo);

		if(likeTotalCnt >= 1000){
			throw new ValidationException(getLikeTypeOverAdd(request.getLikeType()));
		}
	}

	private CommonErrorMessage getLikeTypeNotFoundMessage(String likeType) {
		switch (likeType) {
			case LikeConstant.LIKE_CHANNEL :
				return CommonErrorMessage.CHANNEL_NOT_FOUND;
			case LikeConstant.LIKE_ALBUM :
				return CommonErrorMessage.ALBUM_NOT_FOUND;
			case LikeConstant.LIKE_CHART :
				return CommonErrorMessage.CHART_NOT_FOUND;
			case LikeConstant.LIKE_ARTIST :
				return CommonErrorMessage.ARTIST_NOT_FOUND;
			case LikeConstant.LIKE_TRACK :
				return CommonErrorMessage.TRACK_NOT_FOUND;
			default :
				throw new CommonBusinessException(CommonErrorMessage.BAD_REQUEST);
		}
	}

	private CommonErrorMessage getLikeTypeDuplicated(String likeType) {
		switch (likeType) {
			case LikeConstant.LIKE_CHANNEL :
				return CommonErrorMessage.CHANNEL_DUPLICATED_LIKE;
			case LikeConstant.LIKE_ALBUM :
				return CommonErrorMessage.ALBUM_DUPLICATED_LIKE;
			case LikeConstant.LIKE_CHART :
				return CommonErrorMessage.CHART_DUPLICATED_LIKE;
			case LikeConstant.LIKE_ARTIST :
				return CommonErrorMessage.ARTIST_DUPLICATED_LIKE;
			case LikeConstant.LIKE_TRACK :
				return CommonErrorMessage.TRACK_DUPLICATED_LIKE;
			default :
				throw new CommonBusinessException(CommonErrorMessage.BAD_REQUEST);
		}
	}

	private CommonErrorMessage getLikeTypeOverAdd(String likeType) {
		switch (likeType) {
			case LikeConstant.LIKE_CHANNEL :
				return CommonErrorMessage.CHANNEL_OVER_ADD_LIKE;
			case LikeConstant.LIKE_ALBUM :
				return CommonErrorMessage.ALBUM_OVER_ADD_LIKE;
			case LikeConstant.LIKE_CHART :
				return CommonErrorMessage.CHART_OVER_ADD_LIKE;
			case LikeConstant.LIKE_ARTIST :
				return CommonErrorMessage.ARTIST_OVER_ADD_LIKE;
			case LikeConstant.LIKE_TRACK :
				return CommonErrorMessage.TRACK_OVER_ADD_LIKE;
			default :
				throw new CommonBusinessException(CommonErrorMessage.BAD_REQUEST);
		}
	}

	private void validMeta(String likeType, Long likeTypeId, CommonErrorMessage message) {
		CommonApiResponse response;
		log.info("validMeta :: " + likeType);
		switch (likeType) {
			case LikeConstant.LIKE_CHANNEL :
				response = metaApiProxy.channel(likeTypeId);
				break;
			case LikeConstant.LIKE_ALBUM :
				response = metaApiProxy.album(likeTypeId);
				break;
			case LikeConstant.LIKE_CHART :
				response = metaApiProxy.chart(likeTypeId);
				break;
			case LikeConstant.LIKE_ARTIST :
				response = metaApiProxy.artists(likeTypeId);
				break;
			case LikeConstant.LIKE_TRACK :
				response = metaApiProxy.track(likeTypeId);
				break;
			default :
				throw new CommonBusinessException(CommonErrorMessage.BAD_REQUEST);
		}

		log.info("validMeta :: " + response.toString());

		if(StringUtils.isEmpty(response) || StringUtils.isEmpty(response.getCode())
				|| !"2000000".equals(response.getCode()) || CommonUtils.empty(response.getData())) throw new NotFoundException(message);
	}
}
