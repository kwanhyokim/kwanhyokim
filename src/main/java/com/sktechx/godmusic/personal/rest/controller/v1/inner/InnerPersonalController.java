package com.sktechx.godmusic.personal.rest.controller.v1.inner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.CommonConstant;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.RequestGMContext;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.model.vo.aflo.MigrateAFloCharacterRequest;
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

    @ApiOperation(value = "개인 정보 좋아요"
            , httpMethod = "POST", notes = "타입별 좋아요 추가")
    @PostMapping("/migrate-aflo")
    public CommonApiResponse migrateAFloCharacter(@RequestBody MigrateAFloCharacterRequest request) {

        if(!ObjectUtils.isEmpty(request)){
            aFloService.migrateAFloCharacter(request.getMemberNo(), request.getFromCharacterNo(), request.getToCharacterNo());
        }

        return CommonApiResponse.emptySuccess();
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
