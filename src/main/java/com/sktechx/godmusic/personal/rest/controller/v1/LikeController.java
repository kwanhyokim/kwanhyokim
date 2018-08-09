package com.sktechx.godmusic.personal.rest.controller.v1;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.vo.like.*;
import com.sktechx.godmusic.personal.rest.service.LikeService;
import com.sktechx.godmusic.personal.rest.validate.Validator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

	@ApiOperation(value = "좋아요 타입별 목록 by Kobe ( 기존 /v2/my/album/like/list , /v2/my/track/like/list , /v2/my/channel/like/list, /v2/my/artist/like/list GET )")
	@GetMapping("/type/{likeType}/list")
	public CommonApiResponse<ListDto<List<LikeListResponse>>> getLikeListByLikeType(
			@ApiParam(defaultValue = "ALBUM",
					value = "좋아하는 타입(PLAYLIST: 플레이 리스트 (채널+차트), ALBUM: 앨범, ARTIST: 아티스트, TRACK: 곡)",
					allowableValues = "PLAYLIST, ALBUM, ARTIST, TRACK") @PathVariable String likeType
	) {
		GMContext currentContext = GMContext.getContext();

//		Validator.loginValidate(currentContext);

//		return new CommonApiResponse(likeService.getLikeListByLikeType(likeType, currentContext.getCharacterNo()));
		ListDto<List<LikeListResponse>> response = likeService.getLikeListByLikeType(likeType, Long.parseLong("123"));
		return new CommonApiResponse(response);
	}

	@ApiOperation(value = "좋아요 여부 확인 by Kobe")
	@GetMapping("/type/{likeType}/ids/{likeTypeId}")
	public CommonApiResponse<LikeYnResponse> getLikeYn(
			@ApiParam(defaultValue = "ALBUM",
					value = "좋아하는 타입(CHNL: 채널, CHART: 차트, ALBUM: 앨범, ARTIST: 아티스트, TRACK: 곡)",
					allowableValues = "CHNL, CHART, ALBUM, ARTIST, TRACK") @PathVariable String likeType,
			@ApiParam(value = "좋아하는 타입의 ID") @PathVariable Long likeTypeId
	) {
		GMContext currentContext = GMContext.getContext();

//		Validator.loginValidate(currentContext);

//		return new CommonApiResponse(likeService.getLikeYn(likeType, likeTypeId, currentContext.getCharacterNo()));
		return new CommonApiResponse(likeService.getLikeYn(likeType, likeTypeId, new Long(12)));
	}

	@ApiOperation(value = "좋아요 추가 by Kobe ( 기존 /v2/my/album/like , /v2/my/track/like , /v2/my/channel/like, /v2/my/artist/like POST )")
	@PostMapping("")
	public CommonApiResponse addLike(
			@RequestBody LikeRequest request
	) {
		GMContext currentContext = GMContext.getContext();

		Validator.loginValidate(currentContext);

		likeService.addLike(request, currentContext.getCharacterNo());
		return CommonApiResponse.emptySuccess();
	}

	@ApiOperation(value = "좋아요 삭제 by Kobe ( 기존 /v2/my/album/like , /v2/my/track/like , /v2/my/channel/like, /v2/my/artist/like DELETE )")
	@DeleteMapping("")
	public CommonApiResponse deleteLike(
			@RequestBody LikeListRequest request
	) {
		GMContext currentContext = GMContext.getContext();

		Validator.loginValidate(currentContext);

		likeService.deleteLike(request, currentContext.getCharacterNo());
		return CommonApiResponse.emptySuccess();
	}

	@ApiOperation(value = "좋아요 정렬 수정 by Kobe ( 기존 /v2/my/album/like , /v2/my/track/like , /v2/my/channel/like, /v2/my/artist/like PUT )")
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
