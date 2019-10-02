package com.sktechx.godmusic.personal.rest.controller.v1;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.RequestGMContext;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenRequest;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenTrackRequest;
import com.sktechx.godmusic.personal.rest.model.vo.video.WatchedVideoLogRequest;
import com.sktechx.godmusic.personal.rest.service.ListenService;
import com.sktechx.godmusic.personal.rest.validate.Validator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

	@ApiOperation(value = "채널 청취 로그 by Kobe ( 기존 /v2/user/log/channel POST )"
			, httpMethod = "POST", notes = "채널 전체 재생시 로그를 DB 로 남김")
	@PostMapping("/channel")
	public CommonApiResponse addListenHistByChannel(
			@RequestBody ListenRequest request
	) {
		GMContext currentContext = GMContext.getContext();

		Validator.loginValidate(currentContext);

		listenService.addListenHistByChannel(request, currentContext.getMemberNo(), currentContext.getCharacterNo());
		return CommonApiResponse.emptySuccess();
	}

	@ApiOperation(value = "곡 청취 로그 by Kobe ( 기존 /v2/user/log/track POST )"
			, httpMethod = "POST", notes = "곡 재생시점에 따른 재생 로그를 MQ 로 남김")
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

	/**
	 * 동영상 시청 로그
	 */
	@ApiOperation(value = "영상 시청 로그", httpMethod = "POST", notes = "동영상 재생 로그를 MQ 로 남김")
	@PostMapping("/video")
	public CommonApiResponse recordWatchedVideoHistory(
			@ApiIgnore @RequestGMContext GMContext context,
			@ApiIgnore @RequestHeader(name = "client_ip", required = false, defaultValue = "") String clientIp,
			@Valid @RequestBody WatchedVideoLogRequest watchLog) {

		Validator.loginValidate(context);

		// TODO. MQ로 시청 로그 전달

		return CommonApiResponse.emptySuccess();
	}

}
