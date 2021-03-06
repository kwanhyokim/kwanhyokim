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

import org.springframework.beans.factory.annotation.Qualifier;
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
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.vo.ChannelListResponse;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendPanelListResponse;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendPanelResponse;
import com.sktechx.godmusic.personal.rest.service.ChannelService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendPanelService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequestMapping(Naming.serviceCode+"/v2/recommends")
public class V2RecommendPanelController {

    private final RecommendPanelService recommendPanelService;
	private final ChannelService channelService;

	private final int PREFER_GENRE_CHNL_TRACK_LIMIT_SIZE = 50;

	public V2RecommendPanelController(
			@Qualifier("recommendPanelService") RecommendPanelService recommendPanelService,
			ChannelService channelService) {
		this.recommendPanelService = recommendPanelService;
		this.channelService = channelService;
	}

	@ApiOperation(value = "추천 홈 패널 조회", httpMethod = "GET",response = RecommendPanelResponse.class,
			notes = "사용자 별 홈 데이터를 조회 하는 API \r\n" +
					"\r\n" +
					"우선 순위 - Artist&FLO 전용 패널 > 나를 위한 새로운 발견 (3A) > 오늘의 추천 (2A) > 좋아할만한 아티스트 MIX (2C) > 선호 장르 테마리스트 (1A’) > 운영 TPO \r\n"
	)
    @GetMapping(value = "/home/panels")
    public CommonApiResponse<RecommendPanelResponse> recommendHomePanels(
		    @RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO, required = false) Long characterNo,
		    @RequestHeader(value = CommonConstant.X_GM_OS_TYPE) OsType osType,
		    @RequestHeader(value = CommonConstant.X_GM_APP_VERSION) String appVer
    ){
		return new CommonApiResponse<>(
				recommendPanelService.createRecommendV2PanelList(characterNo, osType, appVer)
		);
    }

    @ApiOperation(value = "추천 패널 목록 조회 API", httpMethod = "GET",
		    notes = "추천 패널 트랙 목록 조회 API - 추천 홈 패널 API 에서 제공하는 RecommendPanelContentType와 id 값으로 트랙 목록 조회 \r\n"
				    + "RC_ATST_TR (좋아할만한 아티스트 MIX)\r\n "
				    + "RC_SML_TR (오늘의 추천)\r\n "
				    + "RC_CF_TR (나를 위한 새로운 발견)" )
	@RequestMapping(value = "/panel/list", method = RequestMethod.GET)
	public CommonApiResponse recommendPanelTrackList(
			@ApiIgnore @RequestGMContext GMContext ctx,
			@ApiParam(value = "추천 패널 컨텐트 타입", allowableValues = "RC_ATST_TR, RC_SML_TR, RC_CF_TR, RC_LIKE_SML_TR")
			@RequestParam(value = "type") RecommendPanelContentType recommendPanelContentType,
            @RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO) Long characterNo,
		    @RequestHeader(value = CommonConstant.X_GM_OS_TYPE) OsType osType,
		    @RequestHeader(value = CommonConstant.X_GM_APP_VERSION) String appVer
    ){

		return new CommonApiResponse<>(new ListDto<>(
				Optional.ofNullable(
						recommendPanelService.getRecommendPanelList(
								characterNo,
								recommendPanelContentType,
								osType,
								appVer
						)
				).orElseThrow( () -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA))

		));
	}

	@SuppressWarnings("ParameterCanBeLocal")
	@ApiOperation(value = "추천 패널 목록 3종 전부 조회 API", httpMethod = "GET", notes = "추천 패널 트랙 목록 조회 API" )
	@RequestMapping(value = "/panels/list", method = RequestMethod.GET)
	public CommonApiResponse recommendPanelsTrackList(
			@ApiIgnore @RequestGMContext GMContext ctx,
			@RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO) Long characterNo,
			@RequestHeader(value = CommonConstant.X_GM_OS_TYPE) OsType osType,
			@RequestHeader(value = CommonConstant.X_GM_APP_VERSION) String appVer
	){
		characterNo = ctx.getCharacterNo();

		return new CommonApiResponse<>(
				RecommendPanelListResponse.builder()
					.forMePanelList(
							recommendPanelService.getRecommendPanelList(
									characterNo, RecommendPanelContentType.RC_CF_TR,
									osType, appVer))
					.todayFloPanelList(
							recommendPanelService.getRecommendPanelList(
									characterNo, RecommendPanelContentType.RC_SML_TR,
									osType, appVer))
					.artistFloPanelList(
							recommendPanelService.getRecommendPanelList(
									characterNo, RecommendPanelContentType.RC_ATST_TR,
									osType, appVer))
				.build());
	}

	@ApiOperation(value = "선호 장르 테마리스트 리스트 ")
	@GetMapping("/preferGenreChnl/list")
	public CommonApiResponse<ChannelListResponse> getPreferGenreChannelList(
			@RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO) Long characterNo,
			@RequestHeader(value = CommonConstant.X_GM_OS_TYPE) OsType osType,
			@RequestHeader(value = CommonConstant.X_GM_APP_VERSION) String appVer
	){
		return new CommonApiResponse<>(
						channelService.getPreferGenreThemeList(
								characterNo,
								osType,
								appVer,
								PREFER_GENRE_CHNL_TRACK_LIMIT_SIZE)
		);

	}

}
