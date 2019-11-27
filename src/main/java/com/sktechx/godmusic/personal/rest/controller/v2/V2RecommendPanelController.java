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

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
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
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.vo.ChannelListResponse;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendPanelListResponse;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendPanelResponse;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.ChannelService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendDummyDataService;
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
    @Autowired
    private RecommendPanelService recommendPanelService;

    @Autowired
    private RecommendDummyDataService recommendDummyDataService;

	@Autowired
	private PersonalRecommendPhaseService personalRecommendPhaseService;

	@Autowired
	private ChannelService channelService;

    @ApiOperation(value = "추천 홈 패널 조회 ( New )", httpMethod = "GET",response = RecommendPanelResponse.class,
			notes = "사용자 별 추천 단계에 따른 추천 데이터를 조회 하는 API \r\n" +
					"\r\n" +
					"0단계 : 비로그인 단계 ( 1-A : 인기채널 , 실시간 차트 ) \r\n" +
					"1단계 : 방문 단계 ( 1-A : 인기채널 , 1-A' : 선호장르 인기 채널, 2-C : 선호/유사아티스트 인기곡 , 차트 ) \r\n" +
					"2단계 : 청취 단계 ( 2-A : 유사곡 , 2-A' : 선호장르 유사곡 , 2-C : 선호/유사아티스트 인기곡 , 2-B : 청취무드 인기채널 , 차트) \r\n" +
					"3단계 : 추천 단계 ( 3-A : 청취CF, 2-A : 유사곡 , 2-A' : 선호장르 유사곡 , 2-C : 선호/유사아티스트 인기곡, 차트 )"
	)
    @GetMapping(value = "/home/panels")
    public CommonApiResponse<RecommendPanelResponse> recommendHomePanels(
    		@ApiIgnore @RequestGMContext GMContext ctx,
		    @RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO, required = false) Long characterNo,
		    @RequestHeader(value = CommonConstant.X_GM_OS_TYPE) OsType osType,
		    @RequestHeader(value = CommonConstant.X_GM_APP_VERSION) String appVer
    ){
		characterNo = ctx.getCharacterNo();

		RecommendPanelResponse recommendPanelResponse =  recommendPanelService
			    .createRecommendV2PanelList(characterNo, ctx.getOsType(), ctx.getAppVer());

		return new CommonApiResponse<>(recommendPanelResponse);
    }

    @ApiOperation(value = "추천 패널 목록 조회 API", httpMethod = "GET", notes = "추천 패널 트랙 목록 조회 API - 추천 홈 패널 API 에서 제공하는 RecommendPanelContentType와 id 값으로 트랙 목록 조회 \r\n"
			+ "RC_ATST_TR (좋아할만한 아티스트 MIX)\r\n RC_SML_TR (오늘의 추천)\r\n RC_CF_TR (나를 위한 새로운 발견)" )
	@RequestMapping(value = "/panel/list", method = RequestMethod.GET)
	public CommonApiResponse recommendPanelTrackList(
			@ApiIgnore @RequestGMContext GMContext ctx,
			@ApiParam(value = "추천 패널 컨텐트 타입", allowableValues = "RC_ATST_TR, RC_SML_TR, RC_CF_TR") @RequestParam(value = "type") RecommendPanelContentType recommendPanelContentType,
            @RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO, required = false) Long characterNo,
		    @RequestHeader(value = CommonConstant.X_GM_OS_TYPE) OsType osType
    ){

		characterNo = ctx.getCharacterNo();

		List<Panel> recommendPanelList = recommendPanelService.getRecommendPanelList(characterNo, recommendPanelContentType, ctx.getOsType());

	    if(CollectionUtils.isEmpty(recommendPanelList)){
		    return null;
	    }

		return new CommonApiResponse<>(new ListDto<>(recommendPanelList));
	}

	@ApiOperation(value = "추천 패널 목록 3종 전부 조회 API", httpMethod = "GET", notes = "추천 패널 트랙 목록 조회 API" )
	@RequestMapping(value = "/panels/list", method = RequestMethod.GET)
	public CommonApiResponse recommendPanelsTrackList(
			@ApiIgnore @RequestGMContext GMContext ctx,
			@RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO, required = false) Long characterNo,
			@RequestHeader(value = CommonConstant.X_GM_OS_TYPE) OsType osType
	){
		characterNo = ctx.getCharacterNo();

		return new CommonApiResponse<>(RecommendPanelListResponse.builder()
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

		characterNo = ctx.getCharacterNo();

		PersonalPhaseMeta personalPhaseMeta = personalRecommendPhaseService.getPersonalRecommendPhaseMeta(characterNo, ctx.getOsType(), ctx.getAppVer());

		List<Long> preferGenreIdList = personalPhaseMeta.getPreferGenreList().stream().map( x -> x.getPreferGenreId()).collect(
				Collectors.toList());

		List<ChnlDto> preferGenrePopularChannelList = channelService.getPreferGenreThemeList(preferGenreIdList, 50, ctx.getOsType());

		if(CollectionUtils.isEmpty(preferGenrePopularChannelList)) throw new CommonBusinessException(
				CommonErrorDomain.EMPTY_DATA);

		return new CommonApiResponse<>(ChannelListResponse.builder().list(preferGenrePopularChannelList).build());

	}

}
