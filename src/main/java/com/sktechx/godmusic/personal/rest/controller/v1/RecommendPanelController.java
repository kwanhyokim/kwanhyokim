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

import java.util.List;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.RequestGMContext;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.MyMostTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendPanelResponse;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendPanelService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "추천 홈 패널 조회", httpMethod = "GET", notes = "추천 패널 조회 MockUp API" , response = RecommendPanelResponse.class)
    @GetMapping(value = "/home/panels")
    public RecommendPanelResponse recommendHomePanels(@ApiIgnore @RequestGMContext GMContext ctx){
        RecommendPanelResponse mockResponse = new RecommendPanelResponse();
        mockResponse.setList(recommendPanelService.createMockupRecommendPanelList());
        return mockResponse;
    }


    // added by bob 2018.08.01
	// edited by bob 2018.08.02
//	@ApiOperation(value = "선호/유사 아티스트 인기곡 목록", httpMethod = "GET", notes = "2-C 선호/유사 아티스트 인기곡 조회 API" , response = RecommendPanelResponse.class)
//	@RequestMapping(value = "/panel/{rcmmdArtistId}/popular/track", method = RequestMethod.GET)
//	public CommonApiResponse<List<TrackDto>> recommendPanelPopularTrackList(@ApiIgnore @RequestGMContext GMContext ctx, @PathVariable Long rcmmdArtistId){
//		ListDto<List<MyMostTrackDto>> response = recommendPanelService.getRecommendPanelPopularTrackList(ctx.getCharacterNo(), rcmmdArtistId);
//		return new CommonApiResponse(response);
//	}
//
//
//	@ApiOperation(value = "유사곡 트랙 목록", httpMethod = "GET", notes = "2-A Like U 패널 트랙 목록 조회 API" , response = RecommendPanelResponse.class)
//	@RequestMapping(value = "/panel/{rcmmdTrackId}/similar/track", method = RequestMethod.GET)
//	public CommonApiResponse<ListDto<List<MyMostTrackDto>>>  recommendPanelSimilarTrackList(@ApiIgnore @RequestGMContext GMContext ctx, @PathVariable Long rcmmdTrackId){
//		ListDto<List<MyMostTrackDto>> response = recommendPanelService.getRecommendPanelSimilarTrackList(ctx.getCharacterNo(), rcmmdTrackId);
//		return new CommonApiResponse(response);
//	}
//
//
//	@ApiOperation(value = "선호장르 유사록 패널 트랙 목록", httpMethod = "GET", notes = "2-A' 선호 장르 유사곡 API" , response = RecommendPanelResponse.class)
//	@RequestMapping(value = "/panel/{rcmmdGenreId}/genre/track", method = RequestMethod.GET)
//	public CommonApiResponse<ListDto<List<MyMostTrackDto>>>  recommendPanelGenreTrackList(@ApiIgnore @RequestGMContext GMContext ctx, @PathVariable Long rcmmdGenreId){
//		ListDto<List<MyMostTrackDto>> response = recommendPanelService.getRecommendPanelGenreTrackList(ctx.getCharacterNo(), rcmmdGenreId);
//		return new CommonApiResponse(response);
//	}
//
//
//	@ApiOperation(value = "CF 추천 패널 트랙 목록", httpMethod = "GET", notes = "3-A 추천 CF(collaborative filtering)패널 API" , response = RecommendPanelResponse.class)
//	@RequestMapping(value = "/panel/{rcmmdMforuId}/cf/track", method = RequestMethod.GET)
//	public CommonApiResponse<ListDto<List<MyMostTrackDto>>>  recommendPanelCfTrackList(@ApiIgnore @RequestGMContext GMContext ctx, @PathVariable Long rcmmdMforuId){
//		ListDto<List<MyMostTrackDto>> response = recommendPanelService.getRecommendPanelCfTrackList(ctx.getCharacterNo(), rcmmdMforuId);
//		return new CommonApiResponse(response);
//	}

	// added by bob 2018.08.03

	@ApiOperation(value = "패널 트랙 목록", httpMethod = "GET", notes = "추천 패널 트랙 목록 조회 API" )
	@RequestMapping(value = "/panel/{panelContentId}/track", method = RequestMethod.GET)
	public CommonApiResponse<ListDto<List<MyMostTrackDto>>>  recommendPanelTrackList(
			@ApiIgnore @RequestGMContext GMContext ctx,
			@PathVariable Long panelContentId,
			@RequestParam(value = "type") RecommendPanelContentType recommendPanelContentType){

		log.info("GMContext : {}", GMContext.getContext());

		ListDto<List<MyMostTrackDto>> response = recommendPanelService.getRecommendPanelTrackList(ctx.getCharacterNo(), recommendPanelContentType, panelContentId);
		return new CommonApiResponse(response);
	}


}
