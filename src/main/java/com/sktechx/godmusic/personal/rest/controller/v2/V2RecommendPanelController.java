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

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
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
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendPanelResponse;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendV2DummyDataRequest;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.ChannelService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendDataService;
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
    private RecommendDataService recommendDataService;

	@Autowired
	private PersonalRecommendPhaseService personalRecommendPhaseService;

	@Autowired
	private ChannelService channelService;

	@ApiOperation(value = "추천 개인화 정보 조회 ( New )", httpMethod = "GET" , hidden = true)
	@GetMapping(value = "/phase/meta")
    public CommonApiResponse<PersonalPhaseMeta> personalPhaseMeta(
    		@ApiIgnore @RequestGMContext GMContext ctx){
		return new CommonApiResponse<>(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(ctx.getCharacterNo(),ctx.getOsType(), ctx.getAppVer()));
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
    		@ApiIgnore @RequestGMContext GMContext ctx,
		    @RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO, required = false) Long characterNo,
		    @RequestHeader(value = CommonConstant.X_GM_OS_TYPE) OsType osType,
		    @RequestHeader(value = CommonConstant.X_GM_APP_VERSION) String appVer
    ){
		RecommendPanelResponse recommendPanelResponse = new RecommendPanelResponse();
	    List<Panel> recommendPanelList = recommendPanelService
			    .createRecommendV2PanelList(ctx.getCharacterNo(), ctx.getOsType(), ctx.getAppVer());

	    if(CollectionUtils.isEmpty(recommendPanelList)){
	    	return null;
	    }

	    Integer mostRecentPanelIndex = 0;
	    Date updateDtime = null;

	    PersonalPhaseMeta personalPhaseMeta = personalRecommendPhaseService.getPersonalRecommendPhaseMeta(ctx.getCharacterNo(),ctx.getOsType(), ctx.getAppVer());
	    if(!ObjectUtils.isEmpty(personalPhaseMeta)) {
	    	if(!ObjectUtils.isEmpty(personalPhaseMeta.getRecommendPersonalPanelTopItem())) {

			   Optional<Panel> panel = recommendPanelList.stream().filter(x-> personalPhaseMeta.getRecommendPersonalPanelTopItem().getRecommendId().equals(x.getContent().getId())).findFirst();

			   if(panel.isPresent()){
			   	    mostRecentPanelIndex = recommendPanelList.indexOf(panel.get());
			   	    updateDtime = panel.get().getContent().getUpdateDtime();
			   }
		    }
	    }

	    recommendPanelResponse.setList(recommendPanelList);
	    recommendPanelResponse.setMostRecentPanelIndex(mostRecentPanelIndex);

		return new CommonApiResponse<>(recommendPanelResponse);
    }

    @ApiOperation(value = "추천 패널 목록 조회 API", httpMethod = "GET", notes = "추천 패널 트랙 목록 조회 API - 추천 홈 패널 API 에서 제공하는 RecommendPanelContentType와 id 값으로 트랙 목록 조회 \r\n"
			+ "RC_ATST_TR (아티스트 FLO)\r\n RC_SML_TR (오늘의 FLO)\r\n RC_CF_TR (나를 위한 FLO)" )
	@RequestMapping(value = "/panel/list", method = RequestMethod.GET)
	public CommonApiResponse recommendPanelTrackList(
			@ApiIgnore @RequestGMContext GMContext ctx,
			@ApiParam(value = "추천 패널 컨텐트 타입", allowableValues = "RC_ATST_TR, RC_SML_TR, RC_CF_TR") @RequestParam(value = "type") RecommendPanelContentType recommendPanelContentType,
            @RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO, required = false) Long characterNo,
		    @RequestHeader(value = CommonConstant.X_GM_OS_TYPE) OsType osType
    ){

		List<Panel> recommendPanelList = recommendPanelService.getRecommendPanelList(ctx.getCharacterNo(), recommendPanelContentType, ctx.getOsType());


	    if(CollectionUtils.isEmpty(recommendPanelList)){
		    return null;
	    }

		return new CommonApiResponse<>(new ListDto<>(recommendPanelList));
	}

	@ApiOperation(value = "선호 장르 테마리스트 리스트 ")
	@GetMapping("/preferGenreChnl/list")
	public CommonApiResponse<ChannelListResponse> getPreferGenreChannelList(
			@ApiIgnore @RequestGMContext GMContext ctx,
			@RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO, required = false) Long characterNo,
			@RequestHeader(value = CommonConstant.X_GM_OS_TYPE) OsType osType,
			@RequestHeader(value = CommonConstant.X_GM_APP_VERSION) String appVer
	){

		PersonalPhaseMeta personalPhaseMeta = personalRecommendPhaseService.getPersonalRecommendPhaseMeta(ctx.getCharacterNo(),ctx.getOsType(), ctx.getAppVer());

		List<Long> preferGenreIdList = personalPhaseMeta.getPreferGenreList().stream().map( x -> x.getPreferGenreId()).collect(
				Collectors.toList());

		List<ChnlDto> preferGenrePopularChannelList = channelService.getPreferGenreThemeList(preferGenreIdList, 50, ctx.getOsType());

		if(CollectionUtils.isEmpty(preferGenrePopularChannelList)) throw new CommonBusinessException(
				CommonErrorDomain.EMPTY_DATA);

		return new CommonApiResponse<>(ChannelListResponse.builder().list(preferGenrePopularChannelList).build());

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
			@Valid @RequestBody RecommendV2DummyDataRequest recommendV2DummyDataRequest,
			@ApiParam(value = "캐릭터 번호", defaultValue = "1") @RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO, required = true) Long characterNo){
		if(characterNo != null){
			recommendDataService.createRecommendV2DummyData(characterNo,recommendV2DummyDataRequest);
		}

		return CommonApiResponse.emptySuccess();
	}

	@DeleteMapping(value = "/home/panels/delete")
	public CommonApiResponse deleteRecommendDummyData(@ApiIgnore @RequestGMContext GMContext ctx,
			@Valid @RequestBody RecommendV2DummyDataRequest recommendV2DummyDataRequest,
			@ApiParam(value = "캐릭터 번호", defaultValue = "1") @RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO, required = true) Long characterNo){
		if(characterNo != null){
			recommendDataService.deleteRecommendV2DummyData(characterNo,recommendV2DummyDataRequest);
		}

		return CommonApiResponse.emptySuccess();
	}

}
