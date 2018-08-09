package com.sktechx.godmusic.personal.rest.controller.v1;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.RequestGMContext;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.model.vo.player.StreamingStatus;
import com.sktechx.godmusic.personal.rest.service.PlayerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 남재우(Peter)/njw0619@sk.com
 * @date 2018.08.09
 */

@Slf4j
@RestController
@RequestMapping(Naming.serviceCode + "/v1/player")
@Api(value = "Playlist", description = "Playlist API - Peter")
public class PlayerController {

    @Autowired
    public PlayerService playerService;

    @ApiOperation(value = "해당 세션의 스트리밍 점유권 획득 by Peter ( 기존 /v2/player/streaming/permission PUT )")
    @PutMapping(value = "/streaming/permission")
    public CommonApiResponse putStreamingPermission(@RequestHeader("X-GM-DEVICE-NO") Long deviceNo, @ApiIgnore @RequestGMContext GMContext ctx){

        playerService.putStreamingPermission(ctx.getCharacterNo(), deviceNo);

        return CommonApiResponse.emptySuccess();
    }

    @ApiOperation(value = "해당 세션의 스트리밍 점유권 조회 by Peter ( 기존 /v2/player/streaming/permission GET )")
    @GetMapping(value = "/streaming/permission")
    public CommonApiResponse<StreamingStatus> getStreamingPermission(@RequestHeader("X-GM-DEVICE-NO") Long deviceNo, @ApiIgnore @RequestGMContext GMContext ctx){

        return new CommonApiResponse<>(playerService.getStreamingPermission(ctx.getMemberNo() , deviceNo));

    }
}
