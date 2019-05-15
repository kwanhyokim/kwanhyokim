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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import com.google.common.primitives.Ints;
import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.RequestGMContext;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.personal.common.domain.ListResponse;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.PreferGenrePopularChnlDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendPanelResponse;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.ChannelService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendDataService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendPanelService;
import com.sktechx.godmusic.personal.rest.service.recommend.phase.PersonalRecommendPhaseService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
    public CommonApiResponse<PersonalPhaseMeta> personalPhaseMeta(@ApiIgnore @RequestGMContext GMContext ctx){
		return new CommonApiResponse<>(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(ctx.getCharacterNo(),ctx.getOsType()));
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
    public CommonApiResponse<RecommendPanelResponse> recommendHomePanels(@ApiIgnore @RequestGMContext GMContext ctx){
		RecommendPanelResponse recommendPanelResponse = new RecommendPanelResponse();
	    List<Panel> recommendPanelList = recommendPanelService
			    .createRecommendV2PanelList(ctx.getCharacterNo(), ctx.getOsType());

	    if(CollectionUtils.isEmpty(recommendPanelList)){
	    	return null;
	    }

	    recommendPanelResponse.setList(recommendPanelList);

		return new CommonApiResponse<>(recommendPanelResponse);
    }

    @ApiOperation(value = "추천 패널 목록 조회 API", httpMethod = "GET", notes = "추천 패널 트랙 목록 조회 API - 추천 홈 패널 API 에서 제공하는 RecommendPanelContentType와 id 값으로 트랙 목록 조회 \r\n"
			+ "RC_ATST_TR (아티스트 FLO)\r\n RC_SML_TR (오늘의 FLO)\r\n RC_CF_TR (나를 위한 FLO)" )
	@RequestMapping(value = "/panel/list", method = RequestMethod.GET)
	public CommonApiResponse recommendPanelTrackList(
			@ApiIgnore @RequestGMContext GMContext ctx,
			@ApiParam(value = "추천 패널 컨텐트 타입", allowableValues = "RC_ATST_TR, RC_SML_TR, RC_CF_TR") @RequestParam(value = "type") RecommendPanelContentType recommendPanelContentType){

		List<Panel> recommendPanelList = recommendPanelService.getRecommendPanelList(ctx.getCharacterNo(), recommendPanelContentType, ctx.getOsType());


	    if(CollectionUtils.isEmpty(recommendPanelList)){
		    return null;
	    }

		return new CommonApiResponse<>(recommendPanelList);
	}

	@ApiOperation(value = "선호 장르 테마리스트 리스트 ")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", required = false, dataType = "int", paramType = "query", value = "페이지", defaultValue = "1"),
			@ApiImplicitParam(name = "size", required = false, dataType = "int", paramType = "query", value = "사이즈", defaultValue = "20")
	})
	@GetMapping("/preferGenreChnl/list")
	public CommonApiResponse<ListResponse> getPreferGenreChannelList(
			@ApiIgnore @RequestGMContext GMContext ctx, @ApiIgnore @PageableDefault(size=50, page=0) Pageable pageable){

		int start = Ints.checkedCast(pageable.getOffset());
		int end = Ints.checkedCast(pageable.getOffset()) + pageable.getPageSize();

		PersonalPhaseMeta personalPhaseMeta = personalRecommendPhaseService.getPersonalRecommendPhaseMeta(ctx.getCharacterNo(),ctx.getOsType());

		List<Long> preferGenreIdList = personalPhaseMeta.getPreferGenreList().stream().map( x -> x.getPreferGenreId()).collect(
				Collectors.toList());

		List<PreferGenrePopularChnlDto> preferGenrePopularChannelList = channelService.getPreferGenrePopularChannelList(preferGenreIdList, 50, ctx.getOsType());

		if(CollectionUtils.isEmpty(preferGenrePopularChannelList)) throw new CommonBusinessException(
				CommonErrorDomain.EMPTY_DATA);

		if(start >= preferGenrePopularChannelList.size() || start >= end) throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);

		if(end > preferGenrePopularChannelList.size()) end = preferGenrePopularChannelList.size();

		return new CommonApiResponse<>(new ListResponse(new PageImpl<>(preferGenrePopularChannelList.subList(start, end), pageable, preferGenrePopularChannelList.size())));

	}


}
