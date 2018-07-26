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
import com.sktechx.godmusic.lib.domain.RequestGMContext;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.vo.preference.Chart;
import com.sktechx.godmusic.personal.rest.model.vo.preference.PreferenceResponse;
import com.sktechx.godmusic.personal.rest.service.PreferenceService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
public class PreferenceController {

    @Autowired
    private PreferenceService preferenceService;

    @ApiOperation(value = "장르 추천", httpMethod = "GET", notes = "선호 장르 추천 API", response = PreferenceResponse.class)
    @GetMapping(value = "/geners")
    public CommonApiResponse<PreferenceResponse> preferencesGeners(@ApiIgnore @RequestGMContext GMContext ctx) {
        log.info("GMContext : {}", GMContext.getContext());
        return new CommonApiResponse<>(preferenceService.getPreferenceGenreList(ctx.getCharacterNo()));
    }

    @ApiOperation(value = "장르 추천 상세", httpMethod = "GET", notes = "선호 장르 추천별 플레이 리스트 API", response = Chart.class)
    @GetMapping(value = "/geners/{chartId}")
    public CommonApiResponse<Chart> preferencesGeners(
            @ApiParam(name = "chartId", required = true, value = "차트아이디", defaultValue = "3325") @PathVariable("chartId") Long chartId) {
        return new CommonApiResponse<>(preferenceService.getPreferenceGenre(chartId));
    }


    @ApiOperation(value = "아티스트 추천", httpMethod = "GET", notes = "선호 아티스트 추천 API", response = ArtistDto.class)
    @GetMapping(value = "/artits")
    public CommonApiResponse<PreferenceResponse> preferencesArtists(@ApiIgnore @RequestGMContext GMContext ctx) {
        return new CommonApiResponse<>(preferenceService.getPreferenceArtistList(ctx.getCharacterNo()));
    }

//    @ApiOperation(value = "아티스트 추천 상세", httpMethod = "GET", notes = "선호 아티스트 추천별 트랙 리스트 API", response = ArtistDto.class)
//    @GetMapping(value = "/artists/{artistId}")
//    public CommonApiResponse<ArtistDto> preferenceArtists(
//            @ApiParam(name = "artistId", required = true, value = "아티스트아이디", defaultValue = "1151") @PathVariable("artistId") Long artistId) {
//
//        Map<String, String> uriVariables = new HashMap<>();
//        uriVariables.put("artistId", "1151");
//
//        ResponseEntity<CommonApiResponse<ArtistDto>> responseEntity = new RestTemplate().getForEntity(
//                "http://localhost:8085/meta/v1/artist/{artistId}",
//                CommonApiResponse.class,
//                uriVariables);
//
//        CommonApiResponse response = responseEntity.getBody();
//        ArtistDto artistDto = (ArtistDto) response.getData();
//
//        log.info(artistDto.toString());
//
//        return new CommonApiResponse<>(artistDto);
//
//    }
}
