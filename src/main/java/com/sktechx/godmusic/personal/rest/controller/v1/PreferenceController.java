/*
 *
 *  * Copyright (c) 2018 SK TECHX.
 *  * All right reserved.
 *  *
 *  * This software is the confidential and proprietary information of SK TECHX.
 *  * You shall not disclose such Confidential Information and
 *  * shall use it only in accordance with the terms of the license agreement
 *  * you entered into with SK TECHX.
 *
 */

package com.sktechx.godmusic.personal.rest.controller.v1;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.vo.preference.ChartResponse;
import com.sktechx.godmusic.personal.rest.service.PreferenceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 설명 : 선호 장르/아티스트 아티스 컨트롤러 아티스트 컨트롤
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 7. 19.
 */
@Slf4j
@RestController
@RequestMapping(Naming.serviceCode + "/v1/preferences")
@Api(value = "선호 장르/아티스트", description = "선호 장르/아티스트 API - Lake")
public class PreferenceController {

    @Autowired
    private PreferenceService preferenceService;

    @ApiOperation(value = "장르 추천", httpMethod = "GET", notes = "선호 장르 추천 API", response = ChartResponse.class)
    @GetMapping(value = "/genres")
    public CommonApiResponse<ChartResponse> preferencesGenres() {
        Long characterNo = GMContext.getContext().getCharacterNo();
        return new CommonApiResponse<>(preferenceService.getPreferenceGenreList(characterNo));
    }

    @ApiOperation(value = "아티스트 추천", httpMethod = "GET", notes = "선호 아티스트 추천 API", response = ArtistDto.class)
    @GetMapping(value = "/artists")
    public CommonApiResponse<ChartResponse> preferencesArtists( @RequestParam(name="sectionNumber", required = false) Integer sectionNumber) {

    	Long characterNo = GMContext.getContext().getCharacterNo();

    	if(sectionNumber != null){
		    return new CommonApiResponse<>(preferenceService.getPreferSimilarArtistList(characterNo, sectionNumber));
	    }else{
		    return new CommonApiResponse<>(preferenceService.getPreferenceArtistList(characterNo));
	    }
    }

	@ApiOperation(value = "유사 시드 아티스트 이름", httpMethod = "GET", notes = "유사 시드 아티스트 이름 받기 API", response = ArtistDto.class)
	@GetMapping(value = "/artist/name")
	public CommonApiResponse<String> getPreferSimilarArtistTitle( @RequestParam(name="sectionNumber") Integer sectionNumber) {
		Long characterNo = GMContext.getContext().getCharacterNo();
		return new CommonApiResponse<>(preferenceService.getPreferSimilarArtistName(characterNo, sectionNumber));
	}

	@ApiOperation(value = "유사 시드 아티스트 캐쉬 삭제", httpMethod = "GET", notes = "유사 시드 아티스트 캐쉬 삭제 API", response = ArtistDto.class)
	@GetMapping(value = "/artist/clear")
	public CommonApiResponse<ChartResponse> deletePreferSimilarArtistCache() {
		Long characterNo = GMContext.getContext().getCharacterNo();
		return new CommonApiResponse<>(preferenceService.deletePreferSimilarArtistName(characterNo));
	}
}
