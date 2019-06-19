package com.sktechx.godmusic.personal.rest.controller.v1.inner;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.model.vo.aflo.MigrateAFloCharacterRequest;
import com.sktechx.godmusic.personal.rest.service.AFloService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(Naming.serviceCode + "/v1/inner")
@Api(value = "inner", description = "personal sever to server api")
public class InnerPersonalController {

    @Autowired
    private AFloService aFloService;

    @ApiOperation(value = "개인 정보 좋아요"
            , httpMethod = "POST", notes = "타입별 좋아요 추가")
    @PostMapping("/migrate-aflo")
    public CommonApiResponse migrateAFloCharacter(@RequestBody MigrateAFloCharacterRequest request) {

        if(!ObjectUtils.isEmpty(request)){
            aFloService.migrateAFloCharacter(request.getMemberNo(), request.getFromCharacterNo(), request.getToCharacterNo());
        }

        return CommonApiResponse.emptySuccess();
    }

}
