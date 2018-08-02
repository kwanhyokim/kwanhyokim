package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.exception.CommonErrorMessage;
import com.sktechx.godmusic.personal.common.exception.InternalException;
import com.sktechx.godmusic.personal.common.exception.NotFoundException;
import com.sktechx.godmusic.personal.common.exception.ValidationException;
import com.sktechx.godmusic.personal.rest.model.vo.like.LikeRequest;
import com.sktechx.godmusic.personal.rest.model.vo.like.LikeTypeIdListRequest;
import com.sktechx.godmusic.personal.rest.repository.LikeMapper;
import com.sktechx.godmusic.personal.rest.service.LikeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.Array;
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
	RestTemplate restTemplate;

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Autowired
	private LikeMapper likeMapper;

	@Override
	public void addLike(LikeRequest request, Long characterNo) {
		validCheckAddLike(request, characterNo);

		likeMapper.insertLike(request.getLikeType(), request.getLikeTypeId(), characterNo);
	}

	@Override
	public void deleteLike(LikeRequest request, Long characterNo) {
		likeMapper.deleteLike(request.getLikeType(), request.getLikeTypeId(), characterNo);
	}

	@Override
	public void updateLike(LikeTypeIdListRequest request, Long characterNo) {
		Map<String, Object> batchParam = new HashMap<>();

		try(SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false)){
			Date now = new Date();
			IntStream.range(0, request.getLikeTypeIdList().size())
					.forEach(index ->
							{
								batchParam.clear();
								batchParam.put("dispSn", index+1);
								batchParam.put("likeType", request.getLikeType());
								batchParam.put("likeTypeId", request.getLikeTypeIdList().get(index));
								batchParam.put("characterNo", characterNo);
								batchParam.put("now", now);
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

	private void validCheckAddLike(LikeRequest request, Long characterNo){
		validMeta(getLikeTypePath(request.getLikeType()), request.getLikeTypeId(), getLikeTypeNotFoundMessage(request.getLikeType()));

		int likeCnt = likeMapper.getLikeCountByLikeTypeAndLikeTypeId(request.getLikeType(), request.getLikeTypeId(), characterNo);

		if(likeCnt > 0){
			throw new ValidationException(getLikeTypeDuplicated(request.getLikeType()));
		}

		int likeTotalCnt = likeMapper.getLikeCountByLikeType(request.getLikeType(), characterNo);

		if(likeTotalCnt >= 1000){
			throw new ValidationException(getLikeTypeOverAdd(request.getLikeType()));
		}
	}

	private String getLikeTypePath(String likeType) {
		switch (likeType) {
			case "CHNL" :
				return "channel/";
			case "ALBUM" :
				return "album/";
			case "CHART" :
				return "chart/track/";
			case "ARTIST" :
				return "artist/";
			case "TRACK" :
				return "track/";
			default :
				throw new CommonBusinessException(CommonErrorMessage.BAD_REQUEST);
		}
	}

	private CommonErrorMessage getLikeTypeNotFoundMessage(String likeType) {
		switch (likeType) {
			case "CHNL" :
				return CommonErrorMessage.CHANNEL_NOT_FOUND;
			case "ALBUM" :
				return CommonErrorMessage.ALBUM_NOT_FOUND;
			case "CHART" :
				return CommonErrorMessage.CHART_NOT_FOUND;
			case "ARTIST" :
				return CommonErrorMessage.ARTIST_NOT_FOUND;
			case "TRACK" :
				return CommonErrorMessage.TRACK_NOT_FOUND;
			default :
				throw new CommonBusinessException(CommonErrorMessage.BAD_REQUEST);
		}
	}

	private CommonErrorMessage getLikeTypeDuplicated(String likeType) {
		switch (likeType) {
			case "CHNL" :
				return CommonErrorMessage.CHANNEL_DUPLICATED_LIKE;
			case "ALBUM" :
				return CommonErrorMessage.ALBUM_DUPLICATED_LIKE;
			case "CHART" :
				return CommonErrorMessage.CHART_DUPLICATED_LIKE;
			case "ARTIST" :
				return CommonErrorMessage.ARTIST_DUPLICATED_LIKE;
			case "TRACK" :
				return CommonErrorMessage.TRACK_DUPLICATED_LIKE;
			default :
				throw new CommonBusinessException(CommonErrorMessage.BAD_REQUEST);
		}
	}

	private CommonErrorMessage getLikeTypeOverAdd(String likeType) {
		switch (likeType) {
			case "CHNL" :
				return CommonErrorMessage.CHANNEL_OVER_ADD_LIKE;
			case "ALBUM" :
				return CommonErrorMessage.ALBUM_OVER_ADD_LIKE;
			case "CHART" :
				return CommonErrorMessage.CHART_OVER_ADD_LIKE;
			case "ARTIST" :
				return CommonErrorMessage.ARTIST_OVER_ADD_LIKE;
			case "TRACK" :
				return CommonErrorMessage.TRACK_OVER_ADD_LIKE;
			default :
				throw new CommonBusinessException(CommonErrorMessage.BAD_REQUEST);
		}
	}

	private void validMeta(String path, Long likeTypeId, CommonErrorMessage message) {
		URI uri = UriComponentsBuilder.newInstance().scheme("http").host("meta-api")
				.path("meta/v1/" + path + likeTypeId).build().encode().toUri();

		log.info("valid meta uri :" + uri);

		HttpHeaders headers = new HttpHeaders();

		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<?> entity = new HttpEntity<>(headers);

		try {
			CommonApiResponse result = restTemplate.exchange(uri, HttpMethod.GET, entity, new ParameterizedTypeReference<CommonApiResponse>() {}).getBody();

			log.info("chytest result : " + result.getCode());
			log.info("chytest result2 : " + result.getData());
			log.info("chytest result3 : " + result.getMessage());
			log.info("chytest result4 : " + StringUtils.isEmpty(result.getData()));
			log.info("chytest result7 : " + empty(result.getData()));
//			log.info("chytest result5 : " + CommonUtil.empty(result.getData()));
//			log.info("chytest result6 : " + CommonUtil.notEmpty(result.getData()));

			if(StringUtils.isEmpty(result.getCode()) || !"2000000".equals(result.getCode()) || StringUtils.isEmpty(result.getData())) throw new NotFoundException(message);
		} catch (Exception e){
			log.error(e.getMessage() , e);
			throw new NotFoundException(message);
		}
	}

	private Boolean empty(Object obj) {
		if (obj instanceof String) return obj == null || "".equals(obj.toString().trim());
		else if (obj instanceof List) return obj == null || ((List) obj).isEmpty();
		else if (obj instanceof Map) return obj == null || ((Map) obj).isEmpty();
		else if (obj instanceof Object[]) return obj == null || Array.getLength(obj) == 0;
		else return obj == null;
	}
}
