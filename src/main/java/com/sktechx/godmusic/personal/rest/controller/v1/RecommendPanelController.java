/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.controller.v1;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.CommonConstant;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.RequestGMContext;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendPanelTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.chart.ChartVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendPanelResponse;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.header.RecommendPanelHeaderVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.chart.RecommendChartService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendPanelHeaderService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendPanelService;
import com.sktechx.godmusic.personal.rest.service.recommend.phase.PersonalRecommendPhaseService;
import com.sktechx.godmusic.personal.rest.validate.Validator;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Optional;

/**
 * 설명 : 추천 컨트롤러
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
@Slf4j
@RestController
@RequestMapping(Naming.serviceCode+"/v1/recommends")
public class RecommendPanelController {
    private final RecommendPanelService recommendPanelService;
    private final PersonalRecommendPhaseService personalRecommendPhaseService;

    private final RecommendPanelHeaderService recommendPanelHeaderService;

	private final RecommendChartService recommendChartService;

	public RecommendPanelController(
			@Qualifier("recommendPanelService") RecommendPanelService recommendPanelService,
			PersonalRecommendPhaseService personalRecommendPhaseService,
			@Qualifier("v2RecommendPanelHeaderService") RecommendPanelHeaderService recommendPanelHeaderService,
			RecommendChartService recommendChartService
	) {
		this.recommendPanelService = recommendPanelService;
		this.personalRecommendPhaseService = personalRecommendPhaseService;
		this.recommendPanelHeaderService = recommendPanelHeaderService;
		this.recommendChartService = recommendChartService;
	}

	@ApiOperation(value = "추천 개인화 정보 조회 ( New )", httpMethod = "GET" , hidden = true)
	@GetMapping(value = "/phase/meta")
	public CommonApiResponse<PersonalPhaseMeta> personalPhaseMeta(@ApiIgnore @RequestGMContext GMContext ctx){
		return new CommonApiResponse<>(
				personalRecommendPhaseService.getPersonalRecommendPhaseMeta(ctx.getCharacterNo(),ctx.getOsType(), ctx.getAppVer())
		);
	}

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
			@RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO, required = false) Long characterNo,
			@RequestHeader(value = CommonConstant.X_GM_OS_TYPE) OsType osType,
			@RequestHeader(value = CommonConstant.X_GM_APP_VERSION) String appVersion
	){
		return Optional.ofNullable(
				recommendPanelService.createRecommendPanelList(characterNo, osType, appVersion)
			).map(
					panels ->
				new CommonApiResponse<>(new RecommendPanelResponse(panels))
		).orElse(null);
    }

	@ApiOperation(value = "추천 패널 상세 트랙 목록 조회 API", httpMethod = "GET", notes = "추천 패널 트랙 목록 조회 API - 추천 홈 패널 API 에서 제공하는 RecommendPanelContentType와 id 값으로 트랙 목록 조회 \r\n"
			+ "RC_ATST_TR (2-C 선호/유사 아티스트 인기곡)\r\n RC_SML_TR (2-A 유사곡)\r\n RC_GR_TR (2-A' 선호 장르 유사곡)\r\n RC_CF_TR (3-A 추천 CF 곡)" )
	@RequestMapping(value = "/panel/{panelContentId}/track", method = RequestMethod.GET)
	public CommonApiResponse<ListDto<List<RecommendPanelTrackDto>>> recommendPanelTrackList(
			@RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO, required = false) Long characterNo,
			@ApiParam(defaultValue = "52") @PathVariable Long panelContentId,
			@ApiParam(value = "추천 패널 컨텐트 타입", allowableValues = "RC_ATST_TR, RC_SML_TR, RC_GR_TR, RC_CF_TR")
			@RequestParam(value = "type") RecommendPanelContentType recommendPanelContentType){

		return new CommonApiResponse<>(
				recommendPanelService.getRecommendPanelTrackList(
						characterNo, recommendPanelContentType, panelContentId));
	}

	@ApiOperation(value = "추천 패널 상세 헤더 정보 API", httpMethod = "GET", notes = "추천 패널 상세 헤더 정보 API - 추천 홈 패널 API 에서 제공하는 RecommendPanelContentType와 id 값으로 정보 조회 \r\n"
			+ "RC_ATST_TR (2-C 선호/유사 아티스트 인기곡)\r\n RC_SML_TR (2-A 유사곡)\r\n RC_GR_TR (2-A' 선호 장르 유사곡)\r\n RC_CF_TR (3-A 추천 CF 곡)" )
	@RequestMapping(value = "/panel/{panelContentId}", method = RequestMethod.GET)
	public CommonApiResponse<RecommendPanelHeaderVo> recommendPanelInfo(
			@RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO, required = false) Long characterNo,
			@RequestHeader(value = CommonConstant.X_GM_OS_TYPE) OsType osType,
			@ApiParam(defaultValue = "52") @PathVariable Long panelContentId,
			@ApiParam(value = "추천 패널 컨텐트 타입") @RequestParam(value = "type") RecommendPanelContentType recommendPanelContentType){

		return new CommonApiResponse<>(
					recommendPanelHeaderService.getRecommendPanelInfo(
							characterNo, recommendPanelContentType, panelContentId, osType
					)
		);
	}

	@ApiOperation(value = "Discovery Flow 2-C 선호/유사 아티스트 인기곡 by Kobe", httpMethod = "POST",
			notes = "사용자가 선호/유사 아티스트 변경시 아티스트의 유사 아티스트들의 인기곡 30곡중 랜덤하게 2곡을 뽑아 리스팅해서 저장")
	@PostMapping("/prefer/artist/panel")
	public CommonApiResponse addPreferArtistPanel() {
		GMContext currentContext = GMContext.getContext();

		Validator.loginValidate(currentContext);

		recommendPanelService.addPreferArtistPanel(currentContext.getCharacterNo());

		return CommonApiResponse.emptySuccess();
	}

	@ApiOperation(value = "Discovery Flow 2-A` 선호 장르 유사곡 by Kobe", httpMethod = "POST",
			notes = "사용자가 선호하는 장르 변경시 장르의 대표 아티스트들과 그 아티스트의 유사아티스트 들의 인기곡을 리스팅해서 저장")
	@PostMapping("/prefer/genre/panel")
	public CommonApiResponse addPreferGenrePanel() {
		GMContext currentContext = GMContext.getContext();

		Validator.loginValidate(currentContext);

		recommendPanelService.addPreferGenrePanel(currentContext.getCharacterNo());

		return CommonApiResponse.emptySuccess();
	}

	@ApiOperation(value = "추천 개인화 차트 조회 ( New )", httpMethod = "GET",
			notes="사용자 개인화 차트 조회(개인화 차트가 없는 경우, 기존 실시간/키즈 차트 제공)")
	@GetMapping("/chart/{chartId}")
	public CommonApiResponse<ChartVo> getRecommendChart(
			@RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO, required = false) Long characterNo,
			@RequestHeader(value = CommonConstant.X_GM_OS_TYPE) OsType osType,
			@PathVariable("chartId") Long chartId,
			@RequestParam("mixYn") String mixYn
	){
		return new CommonApiResponse<>(
				recommendChartService.getRecommendChart(characterNo, osType, chartId, mixYn)
		);

	}

}
