package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.exception.CommonErrorMessage;
import com.sktechx.godmusic.personal.common.exception.InternalException;
import com.sktechx.godmusic.personal.common.exception.NotFoundException;
import com.sktechx.godmusic.personal.common.exception.ValidationException;
import com.sktechx.godmusic.personal.common.util.CommonUtils;
import com.sktechx.godmusic.personal.rest.model.dto.AlbumDto;
import com.sktechx.godmusic.personal.rest.model.dto.ImageDto;
import com.sktechx.godmusic.personal.rest.model.dto.like.LikeAlbumDto;
import com.sktechx.godmusic.personal.rest.model.dto.like.LikeArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.like.LikePlaylistDto;
import com.sktechx.godmusic.personal.rest.model.dto.like.LikeTrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.vo.like.*;
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

	private final static String CHANNEL = "CHNL";
	private final static String ALBUM = "ALBUM";
	private final static String CHART = "CHART";
	private final static String ARTIST = "ARTIST";
	private final static String TRACK = "TRACK";
	private final static String PLAYLIST = "PLAYLIST";

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Autowired
	private LikeMapper likeMapper;

	@Override
	public ListDto<List<LikeListResponse>> getLikeListByLikeType(String likeType, Long characterNo) {
		List<LikeListResponse> likeListResponses = new ArrayList<>();
		List<ImageDto> imageDtos = new ArrayList<>();
		switch (likeType) {
			case PLAYLIST :
				imageDtos.add(new ImageDto().builder().size(75).url("http://asp-image.bugsm.co.kr/album/images/75/201178/20117815.jpg?updateDate=1504812601").build());
				imageDtos.add(new ImageDto().builder().size(140).url("http://asp-image.bugsm.co.kr/album/images/140/201178/20117815.jpg?updateDate=1504812601").build());
				imageDtos.add(new ImageDto().builder().size(200).url("http://asp-image.bugsm.co.kr/album/images/200/201178/20117815.jpg?updateDate=1504812601").build());
				imageDtos.add(new ImageDto().builder().size(350).url("http://asp-image.bugsm.co.kr/album/images/350/201178/20117815.jpg?updateDate=1504812601").build());
				imageDtos.add(new ImageDto().builder().size(500).url("http://asp-image.bugsm.co.kr/album/images/500/201178/20117815.jpg?updateDate=1504812601").build());
				imageDtos.add(new ImageDto().builder().size(1000).url("http://asp-image.bugsm.co.kr/album/images/1000/201178/20117815.jpg?updateDate=1504812601").build());
				likeListResponses.add(new LikeListResponse(new LikePlaylistDto().builder()
						.playListId(new Long(18583))
						.playListName("남들 몰래 듣고 싶은 인디")
						.imgList(imageDtos)
						.build(), new LikeVo().builder()
						.characterNo(new Long(12))
						.likeType("CHNL")
						.likeTypeId(new Long(18583))
						.dispSn(0)
						.build()));

				imageDtos = new ArrayList<>();
				imageDtos.add(new ImageDto().builder().size(75).url("http://asp-image.bugsm.co.kr/album/images/75/6691/669152.jpg?updateDate=1509737410").build());
				imageDtos.add(new ImageDto().builder().size(140).url("http://asp-image.bugsm.co.kr/album/images/140/6691/669152.jpg?updateDate=1509737410").build());
				imageDtos.add(new ImageDto().builder().size(200).url("http://asp-image.bugsm.co.kr/album/images/200/6691/669152.jpg?updateDate=1509737410").build());
				imageDtos.add(new ImageDto().builder().size(350).url("http://asp-image.bugsm.co.kr/album/images/350/6691/669152.jpg?updateDate=1509737410").build());
				imageDtos.add(new ImageDto().builder().size(500).url("http://asp-image.bugsm.co.kr/album/images/500/6691/669152.jpg?updateDate=1509737410").build());
				imageDtos.add(new ImageDto().builder().size(1000).url("http://asp-image.bugsm.co.kr/album/images/1000/6691/669152.jpg?updateDate=1509737410").build());
				likeListResponses.add(new LikeListResponse(new LikePlaylistDto().builder()
						.playListId(new Long(18584))
						.playListName("감성이 몽글몽글 귀르가즘 제대로인 감성팝")
						.imgList(imageDtos)
						.build(), new LikeVo().builder()
						.characterNo(new Long(12))
						.likeType("CHART")
						.likeTypeId(new Long(18584))
						.dispSn(1)
						.build()));
				break;
			case ALBUM :
				imageDtos.add(new ImageDto().builder().size(75).url("http://asp-image.bugsm.co.kr/album/images/75/7035/703573.jpg?updateDate=1516303811").build());
				imageDtos.add(new ImageDto().builder().size(140).url("http://asp-image.bugsm.co.kr/album/images/140/7035/703573.jpg?updateDate=1516303811").build());
				imageDtos.add(new ImageDto().builder().size(200).url("http://asp-image.bugsm.co.kr/album/images/200/7035/703573.jpg?updateDate=1516303811").build());
				imageDtos.add(new ImageDto().builder().size(350).url("http://asp-image.bugsm.co.kr/album/images/350/7035/703573.jpg?updateDate=1516303811").build());
				imageDtos.add(new ImageDto().builder().size(500).url("http://asp-image.bugsm.co.kr/album/images/500/7035/703573.jpg?updateDate=1516303811").build());
				imageDtos.add(new ImageDto().builder().size(1000).url("http://asp-image.bugsm.co.kr/album/images/1000/7035/703573.jpg?updateDate=1516303811").build());
				likeListResponses.add(new LikeListResponse(new LikeAlbumDto().builder()
						.albumId(new Long(703573))
						.albumTitle("Sick Boy")
						.albumTypeStr("정규")
						.artistId(new Long(80143014))
						.artistName("The Chainsmokers(체인스모커스)")
						.imgList(imageDtos)
						.build(), new LikeVo().builder()
						.characterNo(new Long(12))
						.likeType("ALBUM")
						.likeTypeId(new Long(703573))
						.dispSn(0)
						.build()));

				imageDtos = new ArrayList<>();
				imageDtos.add(new ImageDto().builder().size(75).url("http://asp-image.bugsm.co.kr/album/images/75/7154/715456.jpg?updateDate=1519414210").build());
				imageDtos.add(new ImageDto().builder().size(140).url("http://asp-image.bugsm.co.kr/album/images/140/7154/715456.jpg?updateDate=1519414210").build());
				imageDtos.add(new ImageDto().builder().size(200).url("http://asp-image.bugsm.co.kr/album/images/200/7154/715456.jpg?updateDate=1519414210").build());
				imageDtos.add(new ImageDto().builder().size(350).url("http://asp-image.bugsm.co.kr/album/images/350/7154/715456.jpg?updateDate=1519414210").build());
				imageDtos.add(new ImageDto().builder().size(500).url("http://asp-image.bugsm.co.kr/album/images/500/7154/715456.jpg?updateDate=1519414210").build());
				imageDtos.add(new ImageDto().builder().size(1000).url("http://asp-image.bugsm.co.kr/album/images/1000/7154/715456.jpg?updateDate=1519414210").build());
				likeListResponses.add(new LikeListResponse(new LikeAlbumDto().builder()
						.albumId(new Long(715456))
						.albumTitle("Good Morning")
						.albumTypeStr("싱글")
						.artistId(new Long(80168480))
						.artistName("Max Frost(맥스 프로스트)")
						.imgList(imageDtos)
						.build(), new LikeVo().builder()
						.characterNo(new Long(12))
						.likeType("ALBUM")
						.likeTypeId(new Long(715456))
						.dispSn(1)
						.build()));
				break;
			case ARTIST :
				imageDtos.add(new ImageDto().builder().size(70).url("http://asp-image.bugsm.co.kr/artist/images/70/6/600.jpg?updateDate=1520323393").build());
				imageDtos.add(new ImageDto().builder().size(75).url("http://asp-image.bugsm.co.kr/artist/images/75/6/600.jpg?updateDate=1520323393").build());
				imageDtos.add(new ImageDto().builder().size(140).url("http://asp-image.bugsm.co.kr/artist/images/140/6/600.jpg?updateDate=1520323393").build());
				imageDtos.add(new ImageDto().builder().size(200).url("http://asp-image.bugsm.co.kr/artist/images/200/6/600.jpg?updateDate=1520323393").build());
				imageDtos.add(new ImageDto().builder().size(350).url("http://asp-image.bugsm.co.kr/artist/images/350/6/600.jpg?updateDate=1520323393").build());
				imageDtos.add(new ImageDto().builder().size(500).url("http://asp-image.bugsm.co.kr/artist/images/500/6/600.jpg?updateDate=1520323393").build());
				likeListResponses.add(new LikeListResponse(new LikeArtistDto().builder()
						.artistId(new Long(600))
						.artistName("98 Degrees(98 디그리스)")
						.artistGroupTypeStr("그룹")
						.genderCdStr("남성")
						.imgList(imageDtos)
						.build(), new LikeVo().builder()
						.characterNo(new Long(12))
						.likeType("ARTIST")
						.likeTypeId(new Long(600))
						.dispSn(0)
						.build()));

				imageDtos = new ArrayList<>();
				imageDtos.add(new ImageDto().builder().size(70).url("http://asp-image.bugsm.co.kr/artist/images/70/26/2600.jpg?updateDate=1520323393").build());
				imageDtos.add(new ImageDto().builder().size(75).url("http://asp-image.bugsm.co.kr/artist/images/75/26/2600.jpg?updateDate=1520323393").build());
				imageDtos.add(new ImageDto().builder().size(140).url("http://asp-image.bugsm.co.kr/artist/images/140/26/2600.jpg?updateDate=1520323393").build());
				imageDtos.add(new ImageDto().builder().size(200).url("http://asp-image.bugsm.co.kr/artist/images/200/26/2600.jpg?updateDate=1520323393").build());
				imageDtos.add(new ImageDto().builder().size(350).url("http://asp-image.bugsm.co.kr/artist/images/350/26/2600.jpg?updateDate=1520323393").build());
				imageDtos.add(new ImageDto().builder().size(500).url("http://asp-image.bugsm.co.kr/artist/images/500/26/2600.jpg?updateDate=1520323393").build());
				likeListResponses.add(new LikeListResponse(new LikeArtistDto().builder()
						.artistId(new Long(2600))
						.artistName("Mya(마야)")
						.artistGroupTypeStr("솔로")
						.genderCdStr("여성")
						.imgList(imageDtos)
						.build(), new LikeVo().builder()
						.characterNo(new Long(12))
						.likeType("ARTIST")
						.likeTypeId(new Long(2600))
						.dispSn(1)
						.build()));
				break;
			case TRACK :
				imageDtos.add(new ImageDto().builder().size(75).url("http://asp-image.bugsm.co.kr/album/images/75/3/311.jpg?updateDate=1498474016").build());
				imageDtos.add(new ImageDto().builder().size(140).url("http://asp-image.bugsm.co.kr/album/images/140/3/311.jpg?updateDate=1498474016").build());
				imageDtos.add(new ImageDto().builder().size(200).url("http://asp-image.bugsm.co.kr/album/images/200/3/311.jpg?updateDate=1498474016").build());
				imageDtos.add(new ImageDto().builder().size(350).url("http://asp-image.bugsm.co.kr/album/images/350/3/311.jpg?updateDate=1498474016").build());
				imageDtos.add(new ImageDto().builder().size(500).url("http://asp-image.bugsm.co.kr/album/images/500/3/311.jpg?updateDate=1498474016").build());
				imageDtos.add(new ImageDto().builder().size(1000).url("http://asp-image.bugsm.co.kr/album/images/1000/3/311.jpg?updateDate=1498474016").build());
				likeListResponses.add(new LikeListResponse(new LikeTrackDto().builder()
						.trackId(Long.parseLong("511"))
						.trackName("Give Me Just One Night (Una Noche)")
						.artistId(Long.parseLong("600"))
						.artistName("98 Degrees(98 디그리스)")
						.imgList(imageDtos)
						.build(), new LikeVo().builder()
						.characterNo(new Long(12))
						.likeType("TRACK")
						.likeTypeId(new Long(511))
						.dispSn(0)
						.build()));

				imageDtos = new ArrayList<>();

				imageDtos.add(new ImageDto().builder().size(75).url("http://asp-image.bugsm.co.kr/album/images/75/18/1886.jpg?updateDate=1498474016").build());
				imageDtos.add(new ImageDto().builder().size(140).url("http://asp-image.bugsm.co.kr/album/images/140/18/1886.jpg?updateDate=1498474016").build());
				imageDtos.add(new ImageDto().builder().size(200).url("http://asp-image.bugsm.co.kr/album/images/200/18/1886.jpg?updateDate=1498474016").build());
				imageDtos.add(new ImageDto().builder().size(350).url("http://asp-image.bugsm.co.kr/album/images/350/18/1886.jpg?updateDate=1498474016").build());
				imageDtos.add(new ImageDto().builder().size(500).url("http://asp-image.bugsm.co.kr/album/images/500/18/1886.jpg?updateDate=1498474016").build());
				imageDtos.add(new ImageDto().builder().size(1000).url("http://asp-image.bugsm.co.kr/album/images/1000/18/1886.jpg?updateDate=1498474016").build());
				likeListResponses.add(new LikeListResponse(new LikeTrackDto().builder()
						.trackId(Long.parseLong("2088"))
						.trackName("Turn It Up (Intro)")
						.artistId(Long.parseLong("2600"))
						.artistName("Mya(마야)")
						.imgList(imageDtos)
						.build(), new LikeVo().builder()
						.characterNo(new Long(12))
						.likeType("TRACK")
						.likeTypeId(new Long(2088))
						.dispSn(1)
						.build()));
				break;
			default :
				throw new CommonBusinessException(CommonErrorMessage.BAD_REQUEST);
		}

		return new ListDto<>(likeListResponses);
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

	@Override
	public LikeYnResponse getLikeYn(String likeType, Long likeTypeId, Long characterNo) {
		if (likeMapper.getLikeCountByLikeTypeAndLikeTypeId(likeType, likeTypeId, characterNo) > 0) return new LikeYnResponse("Y");
		return new LikeYnResponse("N");
	}

	private void validCheckAddLike(LikeRequest request, Long characterNo){
		validMeta(getLikeTypePath(request.getLikeType()), request.getLikeTypeId(), getLikeTypeNotFoundMessage(request.getLikeType()));

		int likeCnt = likeMapper.getLikeCountByLikeTypeAndLikeTypeId(request.getLikeType(), request.getLikeTypeId(), characterNo);

		if (CHANNEL.equals(request.getLikeType())) {
			likeCnt += likeMapper.getLikeCountByLikeTypeAndLikeTypeId(CHART, request.getLikeTypeId(), characterNo);
		} else if (CHART.equals(request.getLikeType())) {
			likeCnt += likeMapper.getLikeCountByLikeTypeAndLikeTypeId(CHANNEL, request.getLikeTypeId(), characterNo);
		}

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
			case CHANNEL :
				return "channel/";
			case ALBUM :
				return "album/";
			case CHART :
				return "chart/track/";
			case ARTIST :
				return "artist/";
			case TRACK :
				return "track/";
			default :
				throw new CommonBusinessException(CommonErrorMessage.BAD_REQUEST);
		}
	}

	private CommonErrorMessage getLikeTypeNotFoundMessage(String likeType) {
		switch (likeType) {
			case CHANNEL :
				return CommonErrorMessage.CHANNEL_NOT_FOUND;
			case ALBUM :
				return CommonErrorMessage.ALBUM_NOT_FOUND;
			case CHART :
				return CommonErrorMessage.CHART_NOT_FOUND;
			case ARTIST :
				return CommonErrorMessage.ARTIST_NOT_FOUND;
			case TRACK :
				return CommonErrorMessage.TRACK_NOT_FOUND;
			default :
				throw new CommonBusinessException(CommonErrorMessage.BAD_REQUEST);
		}
	}

	private CommonErrorMessage getLikeTypeDuplicated(String likeType) {
		switch (likeType) {
			case CHANNEL :
				return CommonErrorMessage.CHANNEL_DUPLICATED_LIKE;
			case ALBUM :
				return CommonErrorMessage.ALBUM_DUPLICATED_LIKE;
			case CHART :
				return CommonErrorMessage.CHART_DUPLICATED_LIKE;
			case ARTIST :
				return CommonErrorMessage.ARTIST_DUPLICATED_LIKE;
			case TRACK :
				return CommonErrorMessage.TRACK_DUPLICATED_LIKE;
			default :
				throw new CommonBusinessException(CommonErrorMessage.BAD_REQUEST);
		}
	}

	private CommonErrorMessage getLikeTypeOverAdd(String likeType) {
		switch (likeType) {
			case CHANNEL :
				return CommonErrorMessage.CHANNEL_OVER_ADD_LIKE;
			case ALBUM :
				return CommonErrorMessage.ALBUM_OVER_ADD_LIKE;
			case CHART :
				return CommonErrorMessage.CHART_OVER_ADD_LIKE;
			case ARTIST :
				return CommonErrorMessage.ARTIST_OVER_ADD_LIKE;
			case TRACK :
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

			if(StringUtils.isEmpty(result) || StringUtils.isEmpty(result.getCode()) || !"2000000".equals(result.getCode()) || CommonUtils.empty(result.getData())) throw new NotFoundException(message);
		} catch (Exception e){
			log.error(e.getMessage() , e);
			throw new NotFoundException(message);
		}
	}

}
