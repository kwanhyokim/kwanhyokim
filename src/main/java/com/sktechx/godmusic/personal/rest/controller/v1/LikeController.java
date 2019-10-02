package com.sktechx.godmusic.personal.rest.controller.v1;

import com.google.common.collect.Lists;
import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.model.vo.like.*;
import com.sktechx.godmusic.personal.rest.model.vo.video.RangeResponse;
import com.sktechx.godmusic.personal.rest.model.vo.video.VideoVo;
import com.sktechx.godmusic.personal.rest.service.LikeService;
import com.sktechx.godmusic.personal.rest.validate.Validator;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 7. 31.
 * @time PM 3:19
 */
@Api(basePath = "personal/v1/like", value = "좋아요", description = "좋아요 API")
@RestController
@RequestMapping(Naming.serviceCode + "/v1/like")
public class LikeController {

	@Autowired
	@Qualifier("likeService")
	private LikeService likeService;

	@Autowired
	@Qualifier("likeMongoService")
	private LikeService likeMongoService;


	@ApiOperation(value = "좋아요 플레이리스트 목록 by Kobe ( 기존 /v2/my/channel/like/list GET )", response = LikePlaylistListResponse.class
			, httpMethod = "GET", notes = "My > 좋아요 > 플레이 리스트 목록")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", required = true, dataType = "int", paramType = "query", value = "페이지", defaultValue = "1"),
			@ApiImplicitParam(name = "size", required = true, dataType = "int", paramType = "query", value = "사이즈", defaultValue = "1000")
	})
	@GetMapping("/type/playlist/list")
	public CommonApiResponse<LikePlaylistListResponse> getPlayListLikeListByLikeType(
			@PageableDefault(size=50, page=0) Pageable pageable
	) {
		GMContext currentContext = GMContext.getContext();

		Validator.loginValidate(currentContext);

		return new CommonApiResponse<>(likeService.getPlayListLikeListByLikeType(currentContext.getCharacterNo(), currentContext.getAppVer(), pageable));
	}

	@ApiOperation(value = "좋아요 앨범 목록 by Kobe ( 기존 /v2/my/album/like/list GET )", response = LikeAlbumListResponse.class
			, httpMethod = "GET", notes = "My > 좋아요 > 앨범 리스트 목록")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", required = true, dataType = "int", paramType = "query", value = "페이지", defaultValue = "1"),
			@ApiImplicitParam(name = "size", required = true, dataType = "int", paramType = "query", value = "사이즈", defaultValue = "1000")
	})
	@GetMapping("/type/album/list")
	public CommonApiResponse<LikeAlbumListResponse> getAlbumLikeListByLikeType(
			@PageableDefault(size=50, page=0) Pageable pageable
	) {
		GMContext currentContext = GMContext.getContext();
		Validator.loginValidate(currentContext);

		return new CommonApiResponse<>(likeMongoService.getAlbumLikeListByLikeType(currentContext.getCharacterNo(), pageable));
	}

	@ApiOperation(value = "좋아요 아티스트 목록 by Kobe ( 기존 /v2/my/artist/like/list GET )", response = LikeArtistListResponse.class
			, httpMethod = "GET", notes = "My > 좋아요 > 아티스트 리스트 목록")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", required = true, dataType = "int", paramType = "query", value = "페이지", defaultValue = "1"),
			@ApiImplicitParam(name = "size", required = true, dataType = "int", paramType = "query", value = "사이즈", defaultValue = "1000")
	})
	@GetMapping("/type/artist/list")
	public CommonApiResponse<LikeArtistListResponse> getArtistLikeListByLikeType(
			@PageableDefault(size=50, page=0) Pageable pageable
	) {
		GMContext currentContext = GMContext.getContext();
		Validator.loginValidate(currentContext);

		return new CommonApiResponse<>(likeMongoService.getArtistLikeListByLikeType(currentContext.getCharacterNo(), pageable));
	}

	@ApiOperation(value = "좋아요 트랙별 목록 by Kobe ( 기존 /v2/my/track/like/list GET )", response = LikeTrackListResponse.class
			, httpMethod = "GET", notes = "My > 좋아요 > 곡 리스트 목록")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", required = true, dataType = "int", paramType = "query", value = "페이지", defaultValue = "1"),
			@ApiImplicitParam(name = "size", required = true, dataType = "int", paramType = "query", value = "사이즈", defaultValue = "1000")
	})
	@GetMapping("/type/track/list")
	public CommonApiResponse<LikeTrackListResponse> getTrackLikeListByLikeType(
			@PageableDefault(size=50, page=0) Pageable pageable
	) {
		GMContext currentContext = GMContext.getContext();
		Validator.loginValidate(currentContext);

		return new CommonApiResponse<>(likeMongoService.getTrackLikeListByLikeType(currentContext.getCharacterNo(), pageable));
	}

	/**
	 * 좋아요 영상 목록 조회
	 */
	@ApiOperation(value = "좋아요 영상 목록 조회", response = LikeVideoRangeResponse.class, httpMethod = "GET", notes = "보관함 > 좋아요 영상")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", required = true, dataType = "int", paramType = "query", value = "페이지", defaultValue = "1"),
			@ApiImplicitParam(name = "size", required = true, dataType = "int", paramType = "query", value = "사이즈", defaultValue = "50")
	})
	@GetMapping("/type/video/list")
	public CommonApiResponse<RangeResponse<VideoVo>> getVideoes(
			@PageableDefault(page = 1, size = 50) Pageable pageable) {

		GMContext currentContext = GMContext.getContext();
		Validator.loginValidate(currentContext);

		RangeResponse<VideoVo> mockResponse = RangeResponse.of(new PageImpl(Lists.newArrayList(VideoVo.mock()), pageable, 1L) );

		return new CommonApiResponse(mockResponse);
	}

	/**
	 * 좋아요 대상의 좋아요 여부 확인
	 */
	@ApiOperation(value = "좋아요 여부 확인", httpMethod = "GET",
			notes = "좋아요 대상별 좋아요 여부 확인; 좋아요 대상 타입 - CHNL(채널) | ALBUM(앨범) | CHART(차트) | ARTIST(아티스트) | TRACK(곡) | VIDEO(영상)")
	@GetMapping("/type/{likeType}/ids/{likeTypeId}")
	public CommonApiResponse<LikeYnResponse> getLikeYn(
			@ApiParam(value = "좋아요 대상 타입 - CHNL(채널) | ALBUM(앨범) | CHART(차트) | ARTIST(아티스트) | TRACK(곡) | VIDEO(영상)", defaultValue = "ALBUM",
					allowableValues = "CHNL, CHART, ALBUM, ARTIST, TRACK, VIDEO")
			@PathVariable String likeType,
			@ApiParam(value = "좋아요 대상 ID") @PathVariable Long likeTypeId) {

		GMContext currentContext = GMContext.getContext();
		Validator.loginValidate(currentContext);

		return new CommonApiResponse<>(likeMongoService.getLikeYn(likeType, likeTypeId, currentContext.getCharacterNo()));
	}

	/**
	 * 좋아요 대상 추가 하기
	 */
	@ApiOperation(value = "좋아요 추가 하기", httpMethod = "POST",
			notes = "좋아요 대상 타입 - CHNL(채널) | ALBUM(앨범) | CHART(차트) | ARTIST(아티스트) | TRACK(곡) | VIDEO(영상)")
	@PostMapping("")
	public CommonApiResponse addLike(
			@Valid @RequestBody LikeRequest request
	) {
		GMContext context = GMContext.getContext();
		Validator.loginValidate(context);
		Long characterNo = context.getCharacterNo();

		likeMongoService.addLike(request, characterNo);
		likeService.addLike(request, characterNo);

		return CommonApiResponse.emptySuccess();
	}

	/**
	 * 좋아요 대상 삭제 하기
	 */
	@ApiOperation(value = "좋아요 대상 삭제", httpMethod = "DELETE",
			notes = "좋아요 삭제 대상 타입 - CHNL(채널) | ALBUM(앨범) | CHART(차트) | ARTIST(아티스트) | TRACK(곡) | VIDEO(영상)")
	@DeleteMapping("")
	public CommonApiResponse deleteLike(
			@Valid @RequestBody LikeTypeIdListRequest request
	) {
		GMContext context = GMContext.getContext();
		Validator.loginValidate(context);
		Long characterNo = context.getCharacterNo();

		likeMongoService.deleteLike(request, characterNo);
		likeService.deleteLike(request, characterNo);

		return CommonApiResponse.emptySuccess();
	}

	/**
	 * 좋아요 대상 노출 순서 수정하기
	 */
	@ApiOperation(value = "좋아요 노출 순서 수정", httpMethod = "PUT", notes = "타입별 좋아요 노출 순서 수정")
	@PutMapping("")
	public CommonApiResponse updateLike(
			@RequestBody LikeTypeIdListRequest request
	) {
		GMContext context = GMContext.getContext();
		Validator.loginValidate(context);
		Long characterNo = context.getCharacterNo();

		likeMongoService.updateLike(request, characterNo);
		likeService.updateLike(request, characterNo);

		return CommonApiResponse.emptySuccess();
	}

}
