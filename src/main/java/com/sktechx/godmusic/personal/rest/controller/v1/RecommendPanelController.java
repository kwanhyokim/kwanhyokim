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
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendPanelInfoDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendPanelTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.myplaylist.MyPlaylistUpdateOrderRequest;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendDummyDataRequest;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendPanelResponse;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendDataService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendPanelService;
import com.sktechx.godmusic.personal.rest.service.recommend.phase.PersonalRecommendPhaseService;
import com.sktechx.godmusic.personal.rest.validate.Validator;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

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
    @Autowired
    private RecommendPanelService recommendPanelService;

    @Autowired
    private RecommendDataService recommendDataService;

	@Autowired
	private PersonalRecommendPhaseService personalRecommendPhaseService;

	@ApiOperation(value = "추천 개인화 정보 조회 ( New )", httpMethod = "GET" , hidden = true)
	@GetMapping(value = "/phase/meta")
    public CommonApiResponse<PersonalPhaseMeta> personalPhaseMeta(@ApiIgnore @RequestGMContext GMContext ctx){
		return new CommonApiResponse<>(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(ctx.getCharacterNo(),ctx.getOsType()));
	}

    @ApiOperation(value = "추천 홈 패널 조회 ( New )", httpMethod = "GET", notes = "추천 패널 조회 API" , response = RecommendPanelResponse.class)
    @GetMapping(value = "/home/panels")
    public CommonApiResponse<RecommendPanelResponse> recommendHomePanels(@ApiIgnore @RequestGMContext GMContext ctx){
		RecommendPanelResponse recommendPanelResponse = new RecommendPanelResponse();
		recommendPanelResponse.setList(recommendPanelService.createRecommendPanelList(ctx.getCharacterNo(),ctx.getOsType()));
		return new CommonApiResponse<RecommendPanelResponse>(recommendPanelResponse);
    }


	@ApiOperation(value = "추천 단계별 데이터 생성 ( New )", httpMethod = "POST", notes =
			"추천 단계별 데이터 생성\r\n" +
			"1단계 : 기존 2,3단계 데이터를 모두 삭제 \r\n" +
			"2단계 : 기존 3단계 데이터를 모두 삭제 후 2단계 패널 생성 ( 2단계 패널 데이터가 있는 경우 유지 ) \r\n" +
			"3단계 : 3단계 데이터를 생성 ( 3단계 패널 데이터가 있는 경우 유지 ) \r\n" +
			"TPO : TPO 데이터 추가 (기존 1,2,3 단계와는 무관) "
			, response = CommonApiResponse.class)
	@PostMapping(value = "/home/panels/create")
	public CommonApiResponse recommendDummyData(@ApiIgnore @RequestGMContext GMContext ctx,
												 @Valid @RequestBody RecommendDummyDataRequest recommendDummyDataRequest,
												@ApiParam(value = "캐릭터 번호", defaultValue = "1") @RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO, required = true) Long characterNo){
		if(characterNo != null){
			recommendDataService.createRecommendDummyData(characterNo,recommendDummyDataRequest);
		}

		String tpoYn = recommendDummyDataRequest.getTpoYn();
		if("Y".equals(tpoYn)){
			recommendDataService.addTpoRecommendDummyData(characterNo);
		}
		else if("N".equals(tpoYn)){
			recommendDataService.deleteTpoRecommendDummyData(characterNo);
		}

		return CommonApiResponse.emptySuccess();
	}


	// added by bob 2018.08.03
	@ApiOperation(value = "추천 패널 상세 트랙 목록 조회 API", httpMethod = "GET", notes = "추천 패널 트랙 목록 조회 API - 추천 홈 패널 API 에서 제공하는 RecommendPanelContentType와 id 값으로 트랙 목록 조회 \r\n"
			+ "RC_ATST_TR (2-C 선호/유사 아티스트 인기곡)\r\n RC_SML_TR (2-A 유사곡)\r\n RC_GR_TR (2-A' 선호 장르 유사곡)\r\n RC_CF_TR (3-A 추천 CF 곡)" )
	@RequestMapping(value = "/panel/{panelContentId}/track", method = RequestMethod.GET)
	public CommonApiResponse<ListDto<List<RecommendPanelTrackDto>>>  recommendPanelTrackList(
			@ApiIgnore @RequestGMContext GMContext ctx,
			@ApiParam(defaultValue = "52") @PathVariable Long panelContentId,
			@ApiParam(value = "추천 패널 컨텐트 타입", allowableValues = "RC_ATST_TR, RC_SML_TR, RC_GR_TR, RC_CF_TR") @RequestParam(value = "type") RecommendPanelContentType recommendPanelContentType){

		ListDto<List<RecommendPanelTrackDto>> response = recommendPanelService.getRecommendPanelTrackList(ctx.getCharacterNo(), recommendPanelContentType, panelContentId);
		return new CommonApiResponse(response);
	}


	// added by bob 2018.08.16
	@ApiOperation(value = "추천 패널 상세 헤더 정보 API", httpMethod = "GET", notes = "추천 패널 상세 헤더 정보 API - 추천 홈 패널 API 에서 제공하는 RecommendPanelContentType와 id 값으로 정보 조회 \r\n"
			+ "RC_ATST_TR (2-C 선호/유사 아티스트 인기곡)\r\n RC_SML_TR (2-A 유사곡)\r\n RC_GR_TR (2-A' 선호 장르 유사곡)\r\n RC_CF_TR (3-A 추천 CF 곡)" )
	@RequestMapping(value = "/panel/{panelContentId}", method = RequestMethod.GET)
	public CommonApiResponse<RecommendPanelInfoDto>  recommendPanelInfo(
			@ApiIgnore @RequestGMContext GMContext ctx,
			@ApiParam(defaultValue = "52") @PathVariable Long panelContentId,
			@ApiParam(value = "추천 패널 컨텐트 타입", allowableValues = "RC_ATST_TR, RC_SML_TR, RC_GR_TR, RC_CF_TR") @RequestParam(value = "type") RecommendPanelContentType recommendPanelContentType){

		RecommendPanelInfoDto response = recommendPanelService.getRecommendPanelInfo(recommendPanelContentType, panelContentId);
		return new CommonApiResponse(response);
	}

	@ApiOperation(value = "Discovery Flow 2-C 선호/유사 아티스트 인기곡 by Kobe")
	@PostMapping("/prefer/artist/panel")
	public CommonApiResponse addPreferArtistPanel() {
		GMContext currentContext = GMContext.getContext();

		Validator.loginValidate(currentContext);

		recommendPanelService.addPreferArtistPanel(currentContext.getCharacterNo());

		return CommonApiResponse.emptySuccess();
	}

	@ApiOperation(value = "Discovery Flow 2-A` 선호 장르 유사곡 by Kobe")
	@PostMapping("/prefer/genre/panel")
	public CommonApiResponse addPreferGenrePanel() {
		GMContext currentContext = GMContext.getContext();

		Validator.loginValidate(currentContext);

		recommendPanelService.addPreferGenrePanel(currentContext.getCharacterNo());

		return CommonApiResponse.emptySuccess();
	}

}
