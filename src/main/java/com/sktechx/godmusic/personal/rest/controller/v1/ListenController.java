package com.sktechx.godmusic.personal.rest.controller.v1;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenRequest;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenTrackRequest;
import com.sktechx.godmusic.personal.rest.service.ListenService;
import com.sktechx.godmusic.personal.rest.validate.Validator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 8.
 * @time PM 7:15
 */
@Api(basePath = "personal/v1/listen", value = "청취", description = "청취 API")
@RestController
@RequestMapping(Naming.serviceCode + "/v1/listen")
public class ListenController {
	@Autowired
	ListenService listenService;

	@ApiOperation(value = "채널 청취 로그 by Kobe ( 기존 /v2/user/log/channel POST )")
	@PostMapping("/channel")
	public CommonApiResponse addListenHistByChannel(
			@RequestBody ListenRequest request
	) {
		GMContext currentContext = GMContext.getContext();

		Validator.loginValidate(currentContext);

		listenService.addListenHistByChannel(request, currentContext.getMemberNo(), currentContext.getCharacterNo());
		return CommonApiResponse.emptySuccess();
	}

	@ApiOperation(value = "곡 청취 로그 by Kobe ( 기존 /v2/user/log/track POST )")
	@PostMapping("/track")
	public CommonApiResponse addListenHistByTrack(
			HttpServletRequest httpServletRequest,
			@RequestBody ListenTrackRequest request
	) {
		GMContext currentContext = GMContext.getContext();

		Validator.loginValidate(currentContext);

		listenService.addListenHistByTrack(request, currentContext, httpServletRequest);
		return CommonApiResponse.emptySuccess();
	}

}
