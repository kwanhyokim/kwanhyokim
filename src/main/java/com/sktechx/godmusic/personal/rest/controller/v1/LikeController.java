package com.sktechx.godmusic.personal.rest.controller.v1;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.personal.common.domain.constant.LikeConstant;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.model.dto.AlbumDto;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.PlayListDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.vo.like.*;
import com.sktechx.godmusic.personal.rest.service.LikeService;
import com.sktechx.godmusic.personal.rest.validate.Validator;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

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
	private LikeService likeService;

	@ApiOperation(value = "좋아요 플레이리스트 목록 by Kobe ( 기존 /v2/my/channel/like/list GET )", response = LikePlaylistListResponse.class
			, httpMethod = "GET", notes = "My > 좋아요 > 플레이 리스트 목록")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", required = false, dataType = "int", paramType = "query", value = "페이지", defaultValue = "1"),
			@ApiImplicitParam(name = "size", required = false, dataType = "int", paramType = "query", value = "사이즈", defaultValue = "1000")
	})
	@GetMapping("/type/playlist/list")
	public CommonApiResponse<LikePlaylistListResponse> getPlayListLikeListByLikeType(
			@ApiIgnore @PageableDefault(size=1000, page=0) Pageable pageable
	) {
		GMContext currentContext = GMContext.getContext();

		Validator.loginValidate(currentContext);

		return new CommonApiResponse(likeService.getPlayListLikeListByLikeType(currentContext.getCharacterNo(), pageable));
	}

	@ApiOperation(value = "좋아요 앨범 목록 by Kobe ( 기존 /v2/my/album/like/list GET )", response = LikeAlbumListResponse.class
			, httpMethod = "GET", notes = "My > 좋아요 > 앨범 리스트 목록")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", required = false, dataType = "int", paramType = "query", value = "페이지", defaultValue = "1"),
			@ApiImplicitParam(name = "size", required = false, dataType = "int", paramType = "query", value = "사이즈", defaultValue = "1000")
	})
	@GetMapping("/type/album/list")
	public CommonApiResponse<LikeAlbumListResponse> getAlbumLikeListByLikeType(
			@ApiIgnore @PageableDefault(size=1000, page=0) Pageable pageable
	) {
		GMContext currentContext = GMContext.getContext();

		Validator.loginValidate(currentContext);

		return new CommonApiResponse(likeService.getAlbumLikeListByLikeType(currentContext.getCharacterNo(), pageable));
	}

	@ApiOperation(value = "좋아요 아티스트 목록 by Kobe ( 기존 /v2/my/artist/like/list GET )", response = LikeArtistListResponse.class
			, httpMethod = "GET", notes = "My > 좋아요 > 아티스트 리스트 목록")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", required = false, dataType = "int", paramType = "query", value = "페이지", defaultValue = "1"),
			@ApiImplicitParam(name = "size", required = false, dataType = "int", paramType = "query", value = "사이즈", defaultValue = "1000")
	})
	@GetMapping("/type/artist/list")
	public CommonApiResponse<LikeArtistListResponse> getArtistLikeListByLikeType(
			@ApiIgnore @PageableDefault(size=1000, page=0) Pageable pageable
	) {
		GMContext currentContext = GMContext.getContext();

		Validator.loginValidate(currentContext);

		return new CommonApiResponse(likeService.getArtistLikeListByLikeType(currentContext.getCharacterNo(), pageable));
	}

	@ApiOperation(value = "좋아요 트랙별 목록 by Kobe ( 기존 /v2/my/track/like/list GET )", response = LikeTrackListResponse.class
			, httpMethod = "GET", notes = "My > 좋아요 > 곡 리스트 목록")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", required = false, dataType = "int", paramType = "query", value = "페이지", defaultValue = "1"),
			@ApiImplicitParam(name = "size", required = false, dataType = "int", paramType = "query", value = "사이즈", defaultValue = "1000")
	})
	@GetMapping("/type/track/list")
	public CommonApiResponse<LikeTrackListResponse> getTrackLikeListByLikeType(
			@ApiIgnore @PageableDefault(size=1000, page=0) Pageable pageable
	) {
		GMContext currentContext = GMContext.getContext();

		Validator.loginValidate(currentContext);

		return new CommonApiResponse(likeService.getTrackLikeListByLikeType(currentContext.getCharacterNo(), pageable));
	}

	@ApiOperation(value = "좋아요 여부 확인 by Kobe"
			, httpMethod = "GET", notes = "사용자가 좋아요 한것(타입별)이 맞는지 확인")
	@GetMapping("/type/{likeType}/ids/{likeTypeId}")
	public CommonApiResponse<LikeYnResponse> getLikeYn(
			@ApiParam(defaultValue = "ALBUM",
					value = "좋아하는 타입(CHNL: 채널, CHART: 차트, ALBUM: 앨범, ARTIST: 아티스트, TRACK: 곡)",
					allowableValues = "CHNL, CHART, ALBUM, ARTIST, TRACK") @PathVariable String likeType,
			@ApiParam(value = "좋아하는 타입의 ID") @PathVariable Long likeTypeId
	) {
		GMContext currentContext = GMContext.getContext();

		Validator.loginValidate(currentContext);

		return new CommonApiResponse(likeService.getLikeYn(likeType, likeTypeId, currentContext.getCharacterNo()));
	}

	@ApiOperation(value = "좋아요 추가 by Kobe ( 기존 /v2/my/album/like , /v2/my/track/like , /v2/my/channel/like, /v2/my/artist/like POST )"
			, httpMethod = "POST", notes = "타입별 좋아요 추가")
	@PostMapping("")
	public CommonApiResponse addLike(
			@RequestBody LikeRequest request
	) {
		GMContext currentContext = GMContext.getContext();

		Validator.loginValidate(currentContext);

		likeService.addLike(request, currentContext.getCharacterNo());
		return CommonApiResponse.emptySuccess();
	}

	@ApiOperation(value = "좋아요 삭제 by Kobe ( 기존 /v2/my/album/like , /v2/my/track/like , /v2/my/channel/like, /v2/my/artist/like DELETE )"
			, httpMethod = "DELETE", notes = "타입별 좋아요 삭제")
	@DeleteMapping("")
	public CommonApiResponse deleteLike(
			@RequestBody LikeTypeIdListRequest request
	) {
		GMContext currentContext = GMContext.getContext();

		Validator.loginValidate(currentContext);

		likeService.deleteLike(request, currentContext.getCharacterNo());
		return CommonApiResponse.emptySuccess();
	}

	@ApiOperation(value = "좋아요 정렬 수정 by Kobe ( 기존 /v2/my/album/like , /v2/my/track/like , /v2/my/channel/like, /v2/my/artist/like PUT )"
			, httpMethod = "PUT", notes = "타입별 좋아요 정렬 순서 수정")
	@PutMapping("")
	public CommonApiResponse updateLike(
			@RequestBody LikeTypeIdListRequest request
	) {
		GMContext currentContext = GMContext.getContext();

		Validator.loginValidate(currentContext);

		likeService.updateLike(request, currentContext.getCharacterNo());
		return CommonApiResponse.emptySuccess();
	}

}
