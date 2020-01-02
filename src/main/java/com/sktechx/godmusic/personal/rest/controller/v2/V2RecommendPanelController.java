/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.controller.v2;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.CommonConstant;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.RequestGMContext;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferGenreDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.vo.ChannelListResponse;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendPanelListResponse;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendPanelResponse;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.ChannelService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendPanelService;
import com.sktechx.godmusic.personal.rest.service.recommend.phase.PersonalRecommendPhaseService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequestMapping(Naming.serviceCode+"/v2/recommends")
public class V2RecommendPanelController {

    private final RecommendPanelService recommendPanelService;
	private final PersonalRecommendPhaseService personalRecommendPhaseService;
	private final ChannelService channelService;

	public V2RecommendPanelController(RecommendPanelService recommendPanelService,
			PersonalRecommendPhaseService personalRecommendPhaseService,
			ChannelService channelService) {
		this.recommendPanelService = recommendPanelService;
		this.personalRecommendPhaseService = personalRecommendPhaseService;
		this.channelService = channelService;
	}

	@ApiOperation(value = "추천 홈 패널 조회", httpMethod = "GET",response = RecommendPanelResponse.class,
			notes = "사용자 별 홈 데이터를 조회 하는 API \r\n" +
					"\r\n" +
					"우선 순위 - Artist&FLO 전용 패널 > 나를 위한 FLO (3A) > 오늘의 FLO (2A) > 아티스트 FLO (2C) > 선호 장르 테마리스트 (1A’) > 운영 TPO \r\n"
	)
    @GetMapping(value = "/home/panels")
    public CommonApiResponse<RecommendPanelResponse> recommendHomePanels(
    		@ApiIgnore @RequestGMContext GMContext ctx,
		    @RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO, required = false) Long characterNo,
		    @RequestHeader(value = CommonConstant.X_GM_OS_TYPE) OsType osType,
		    @RequestHeader(value = CommonConstant.X_GM_APP_VERSION) String appVer
    ){
		return new CommonApiResponse<>(
				Optional.ofNullable(
					recommendPanelService.createRecommendV2PanelList(ctx.getCharacterNo(), ctx.getOsType(), ctx.getAppVer())
				).orElseThrow( () -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA))

		);
    }

    @ApiOperation(value = "추천 패널 목록 조회 API", httpMethod = "GET",
		    notes = "추천 패널 트랙 목록 조회 API - 추천 홈 패널 API 에서 제공하는 RecommendPanelContentType와 id 값으로 트랙 목록 조회 \r\n"
				    + "RC_ATST_TR (아티스트 FLO)\r\n "
				    + "RC_SML_TR (오늘의 FLO)\r\n "
				    + "RC_CF_TR (나를 위한 FLO)" )
	@RequestMapping(value = "/panel/list", method = RequestMethod.GET)
	public CommonApiResponse recommendPanelTrackList(
			@ApiIgnore @RequestGMContext GMContext ctx,
			@ApiParam(value = "추천 패널 컨텐트 타입", allowableValues = "RC_ATST_TR, RC_SML_TR, RC_CF_TR") @RequestParam(value = "type") RecommendPanelContentType recommendPanelContentType,
            @RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO, required = false) Long characterNo,
		    @RequestHeader(value = CommonConstant.X_GM_OS_TYPE) OsType osType
    ){

		return new CommonApiResponse<>(new ListDto<>(
				Optional.ofNullable(
						recommendPanelService.getRecommendPanelList(ctx.getCharacterNo(), recommendPanelContentType, ctx.getOsType())
				).orElseThrow( () -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA))

		));
	}

	@SuppressWarnings("ParameterCanBeLocal")
	@ApiOperation(value = "추천 패널 목록 3종 전부 조회 API", httpMethod = "GET", notes = "추천 패널 트랙 목록 조회 API" )
	@RequestMapping(value = "/panels/list", method = RequestMethod.GET)
	public CommonApiResponse recommendPanelsTrackList(
			@ApiIgnore @RequestGMContext GMContext ctx,
			@RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO, required = false) Long characterNo,
			@RequestHeader(value = CommonConstant.X_GM_OS_TYPE) OsType osType
	){
		characterNo = ctx.getCharacterNo();

		return new CommonApiResponse<>(
				RecommendPanelListResponse.builder()
					.forMePanelList(recommendPanelService.getRecommendPanelList(characterNo, RecommendPanelContentType.RC_CF_TR, ctx.getOsType()))
					.todayFloPanelList(recommendPanelService.getRecommendPanelList(characterNo, RecommendPanelContentType.RC_SML_TR, ctx.getOsType()))
					.artistFloPanelList(recommendPanelService.getRecommendPanelList(characterNo, RecommendPanelContentType.RC_ATST_TR, ctx.getOsType()))
				.build());
	}

	@ApiOperation(value = "선호 장르 테마리스트 리스트 ")
	@GetMapping("/preferGenreChnl/list")
	public CommonApiResponse<ChannelListResponse> getPreferGenreChannelList(
			@ApiIgnore @RequestGMContext GMContext ctx,
			@RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO, required = false) Long characterNo,
			@RequestHeader(value = CommonConstant.X_GM_OS_TYPE) OsType osType,
			@RequestHeader(value = CommonConstant.X_GM_APP_VERSION) String appVer
	){

		PersonalPhaseMeta personalPhaseMeta = personalRecommendPhaseService.getPersonalRecommendPhaseMeta(ctx.getCharacterNo(), ctx.getOsType(), ctx.getAppVer());

		return new CommonApiResponse<>(ChannelListResponse.builder().list(
				Optional.ofNullable(
						channelService.getPreferGenreThemeList(
								personalPhaseMeta.getPreferGenreList().stream()
										.map(CharacterPreferGenreDto::getPreferGenreId)
										.collect(Collectors.toList()),
								50,
								ctx.getOsType())
				).orElseThrow(()-> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA))
		).build());

	}

}
