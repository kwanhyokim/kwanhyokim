package com.sktechx.godmusic.personal.rest.controller.v1.inner;

import com.sktechx.godmusic.personal.rest.model.vo.aflo.NewMigrateAFloCharacterRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.CommonConstant;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.RequestGMContext;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.model.vo.aflo.MigrateAFloCharacterRequest;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.AFloService;
import com.sktechx.godmusic.personal.rest.service.PreferenceService;
import com.sktechx.godmusic.personal.rest.service.recommend.phase.PersonalRecommendPhaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@RestController
@RequestMapping(Naming.serviceCode + "/v1/inner")
@Api(value = "inner", description = "personal sever to server api")
public class InnerPersonalController {

    @Autowired
    private AFloService aFloService;

    @Autowired
    private PreferenceService preferenceService;

    @Autowired
    private PersonalRecommendPhaseService personalRecommendPhaseService;

    @ApiOperation(value = "aflo 캐릭터 복사"
            , httpMethod = "POST", notes = "aflo 캐릭터 복사")
    @PostMapping("/migrate-aflo")
    public CommonApiResponse migrateAFloCharacter(@RequestBody MigrateAFloCharacterRequest request) {
        aFloService.migrateAFloCharacter(request.getMemberNo(), request.getFromCharacterNo(), request.getToCharacterNo());
        return CommonApiResponse.emptySuccess();
    }

    @ApiOperation(value = "aflo 캐릭터 복사"
            , httpMethod = "POST", notes = "aflo 캐릭터 복사")
    @PostMapping("/new-migrate-aflo")
    public CommonApiResponse<NewMigrateAFloCharacterRes> newMigrateAFloCharacter(@RequestBody MigrateAFloCharacterRequest request) {
        NewMigrateAFloCharacterRes response = aFloService.migrateAFloCharacter(request.getMemberNo(), request.getFromCharacterNo(), request.getToCharacterNo());
        return new CommonApiResponse(response);
    }

    @ApiOperation(value = "추천 개인화 정보 조회 ( New )", httpMethod = "GET", hidden = true)
    @GetMapping(value = "/phase/meta")
    public CommonApiResponse<PersonalPhaseMeta> personalPhaseMeta(@ApiIgnore @RequestGMContext GMContext ctx) {
        // APP Version 체크로 personalmeta의 추천 패널 disp end date 사용 여부를 조절..
        return new CommonApiResponse<>(personalRecommendPhaseService.getPersonalRecommendPhaseMeta(ctx.getCharacterNo(), ctx.getOsType(), ctx.getAppVer()));
    }

    @ApiOperation(value = "홈 캐쉬 삭제")
    @GetMapping("/home/clearCache")
    public CommonApiResponse clearHomeCache(
            @ApiIgnore @RequestGMContext GMContext ctx,
            @RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO, required = false) Long characterNo
    ){
        characterNo = ctx.getCharacterNo();

        personalRecommendPhaseService.clearPersonalRecommendPhaseMetaCache(characterNo);
        return CommonApiResponse.emptySuccess();
    }

    @ApiOperation(value = "홈 캐쉬 삭제")
    @GetMapping("/home/clearCache/all")
    public CommonApiResponse clearHomeCacheAll(
            @ApiIgnore @RequestGMContext GMContext ctx,
            @RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO, required = false) Long characterNo
    ){

        characterNo = ctx.getCharacterNo();

        personalRecommendPhaseService.clearPersonalRecommendPhaseMetaCache(characterNo);
        preferenceService.deletePreferSimilarArtistName(characterNo);
        preferenceService.clearCachePreferenceVideoArtistNewList(characterNo);
        preferenceService.clearCachePreferenceVideoGenreNewList(characterNo);

        return CommonApiResponse.emptySuccess();
    }

}
