package com.sktechx.godmusic.personal.rest.controller.v1;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.model.vo.like.LikeRequest;
import com.sktechx.godmusic.personal.rest.model.vo.like.LikeTypeIdListRequest;
import com.sktechx.godmusic.personal.rest.service.LikeService;
import com.sktechx.godmusic.personal.rest.validate.Validator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

	@ApiOperation(value = "좋아요 추가 ( 기존 /v2/my/album/like , /v2/my/track/like , /v2/my/channel/like, /v2/my/artist/like POST )")
	@PostMapping("")
	public CommonApiResponse addLike(
			@RequestBody LikeRequest request
	) {
		GMContext currentContext = GMContext.getContext();

		Validator.loginValidate(currentContext);

		likeService.addLike(request, currentContext.getCharacterNo());
		return CommonApiResponse.emptySuccess();
	}

	@ApiOperation(value = "좋아요 삭제 ( 기존 /v2/my/album/like , /v2/my/track/like , /v2/my/channel/like, /v2/my/artist/like DELETE )")
	@DeleteMapping("")
	public CommonApiResponse deleteLike(
			@RequestBody LikeRequest request
	) {
		GMContext currentContext = GMContext.getContext();

		Validator.loginValidate(currentContext);

		likeService.deleteLike(request, currentContext.getCharacterNo());
		return CommonApiResponse.emptySuccess();
	}

	@ApiOperation(value = "좋아요 정렬 수정 ( 기존 /v2/my/album/like , /v2/my/track/like , /v2/my/channel/like, /v2/my/artist/like PUT )")
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
