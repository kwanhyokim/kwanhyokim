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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.CommonConstant;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.RequestGMContext;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.vo.preference.ChartResponse;
import com.sktechx.godmusic.personal.rest.model.vo.video.VideoVo;
import com.sktechx.godmusic.personal.rest.service.PreferenceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

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

	@ApiOperation(value = "좋아하는 아티스트 최신영상 조회")
	@GetMapping("/video/artist/new/list")
	public CommonApiResponse<ListDto<List<VideoVo>>> getPreferenceVideoArtistNewList(
			@ApiIgnore @RequestGMContext GMContext ctx,
			@RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO, required = false) Long characterNo,
			@RequestHeader(value = CommonConstant.X_GM_OS_TYPE) OsType osType
	){

		if(ObjectUtils.isEmpty(characterNo)){
			return null;
		}

		List<VideoVo> videoVoList = preferenceService.getPreferenceVideoArtistNewList(characterNo, osType);

		if(CollectionUtils.isEmpty(videoVoList)){
			return null;
		}

		return new CommonApiResponse<>(new ListDto<>(videoVoList));

	}

	@ApiOperation(value = "좋아하는 장르 최신영상 조회")
	@GetMapping("/video/genre/new/list")
	public CommonApiResponse<ListDto<List<VideoVo>>>  getPreferenceVideoGenreNewList(
			@ApiIgnore @RequestGMContext GMContext ctx,
			@RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO, required = false) Long characterNo,
			@RequestHeader(value = CommonConstant.X_GM_OS_TYPE) OsType osType
	){


		List<VideoVo> videoVoList = preferenceService.getPreferenceVideoGenreNewList(characterNo, osType);

    	if(CollectionUtils.isEmpty(videoVoList)){
    		return null;
	    }

		return new CommonApiResponse<>(new ListDto<>(videoVoList));

	}
}
