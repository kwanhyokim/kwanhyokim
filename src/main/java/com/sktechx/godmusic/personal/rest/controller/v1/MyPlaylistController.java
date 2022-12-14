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
import com.sktechx.godmusic.personal.common.domain.type.AppNameType;
import com.sktechx.godmusic.personal.common.domain.type.PinType;
import com.sktechx.godmusic.personal.rest.model.dto.MemberChannelDto;
import com.sktechx.godmusic.personal.rest.model.vo.myplaylist.MyPlaylistCreateRequest;
import com.sktechx.godmusic.personal.rest.model.vo.myplaylist.MyPlaylistDeleteRequest;
import com.sktechx.godmusic.personal.rest.model.vo.myplaylist.MyPlaylistPinRequest;
import com.sktechx.godmusic.personal.rest.model.vo.myplaylist.MyPlaylistRetriveAllResponse;
import com.sktechx.godmusic.personal.rest.model.vo.myplaylist.MyPlaylistTrackCreateRequest;
import com.sktechx.godmusic.personal.rest.model.vo.myplaylist.MyPlaylistTrackCreateResponse;
import com.sktechx.godmusic.personal.rest.model.vo.myplaylist.MyPlaylistTrackDeleteRequest;
import com.sktechx.godmusic.personal.rest.model.vo.myplaylist.MyPlaylistTrackRetrieveAllResponse;
import com.sktechx.godmusic.personal.rest.model.vo.myplaylist.MyPlaylistTrackUpdateOrderRequest;
import com.sktechx.godmusic.personal.rest.model.vo.myplaylist.MyPlaylistUpdateOrderRequest;
import com.sktechx.godmusic.personal.rest.model.vo.myplaylist.MyPlaylistUpdateRequest;
import com.sktechx.godmusic.personal.rest.service.MemberChannelService;
import com.sktechx.godmusic.personal.rest.validate.Validator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

/**
 * ?????? :
 *
 * @author ?????????/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 8. 3.
 */
@Slf4j
@RestController
@RequestMapping(Naming.serviceCode + "/v1/myplaylist")
@Api(value = "My Playlist", description = "My Playlist API - Lake")
public class MyPlaylistController {

    @Autowired
    MemberChannelService memberChannelService;

    private static final String PIN_MY_PLAYLIST_DEFAULT_REQUEST = "{\n" +
            " \"type\": \"????????? ????????? ?????????????????? ??????\", \n" +
            " \"id\": \"????????? ????????? ?????????????????? ?????? ID\" \n" +
            "}";

    private static final String CREATE_MY_PLAYLIST_DEFAULT_REQUEST = "{\n" +
            " \"memberChannelName\": \"????????? ????????? ?????????????????? ???\" \n" +
            "}";

    private static final String UPDATE_MY_PLAYLIST_DEFAULT_REQUEST = "{\n" +
            " \"memberChannelName\": \"????????? ????????? ?????????????????? ???\" \n" +
            "}";

    @PostMapping("/pin")
    @ApiOperation(value = "My Playlist Pin", httpMethod = "POST", notes = "My Playlist Pin", response = MyPlaylistTrackCreateResponse.class)
    public CommonApiResponse<MyPlaylistTrackCreateResponse> pinMyPlaylist(
            @ApiParam(value = "type - ????????? ????????? ?????????????????? ??????(RC_SML_TR: Like U(2-A), RC_GR_TR: Mix Tape(2-A'), RC_ATST_TR: Musician focus(2-C), RC_CF_TR: Made for U(3-A), CHNL: ??????, MY_CHNL: ????????????)\n id - ????????? ????????? ?????????????????? ?????? ID", defaultValue = PIN_MY_PLAYLIST_DEFAULT_REQUEST)
            @Valid @RequestBody MyPlaylistPinRequest myPlaylistPinRequest) {
        Long memberNo = getMemberNo();
        Long characterNo = getCharacterNo();
        MyPlaylistTrackCreateResponse myPlaylistTrackCreateResponse = memberChannelService.pinMemberChannel(memberNo, characterNo, myPlaylistPinRequest.getPinType(), myPlaylistPinRequest.getPinTypeId());

        return new CommonApiResponse<>(myPlaylistTrackCreateResponse);
    }

    @PostMapping
    @ApiOperation(value = "My Playlist ??????", httpMethod = "POST", notes = "My Playlist ?????? API(/v2/my/channel)", response = MemberChannelDto.class)
    public CommonApiResponse<MemberChannelDto> createMyPlaylist(
            @ApiParam(value = "memberChannelName - ?????? ?????????????????? ???", defaultValue = CREATE_MY_PLAYLIST_DEFAULT_REQUEST) @Valid @RequestBody MyPlaylistCreateRequest myPlaylistCreateRequest) {
        Long memberNo = getMemberNo();
        Long characterNo = getCharacterNo();
        MemberChannelDto memberChannel;

        if(!ObjectUtils.isEmpty(myPlaylistCreateRequest.getPinType()) && myPlaylistCreateRequest.getPinType() == PinType.OCR){
            memberChannel = memberChannelService.createMemberChannel(memberNo, characterNo, myPlaylistCreateRequest.getMemberChannelName(), PinType.OCR, null);
        }else{
            memberChannel = memberChannelService.createMemberChannel(memberNo, characterNo, myPlaylistCreateRequest.getMemberChannelName());
        }

        MemberChannelDto memberChannelDto = memberChannelService.getMemberChannel(memberNo, characterNo, memberChannel.getMemberChannelId());

        return new CommonApiResponse<>(memberChannelDto);
    }

    @GetMapping
    @ApiOperation(value = "My Playlist ?????? ??????", httpMethod = "GET", notes = "My Playlist ?????? ?????? API(/v2/my/channel/list)", response = MyPlaylistRetriveAllResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", required = false, dataType = "int", paramType = "query", value = "?????????", defaultValue = "1"),
            @ApiImplicitParam(name = "size", required = false, dataType = "int", paramType = "query", value = "?????????", defaultValue = "20")
    })
    public CommonApiResponse<MyPlaylistRetriveAllResponse> retrieveAllMyPlaylist(@ApiIgnore @PageableDefault(size=20, page= 0) Pageable pageable) {
        Long memberNo = getMemberNo();
        Long characterNo = getCharacterNo();
        return new CommonApiResponse<>(memberChannelService.getMemberChannelPageImpl(memberNo, characterNo, null, pageable));
    }

    @GetMapping("/{memberChannelId}")
    @ApiOperation(value = "My Playlist ?????? ??????", httpMethod = "GET", notes = "My Playlist ?????? ?????? API(/v2/my/channel/{channelId})", response = MemberChannelDto.class)
    public CommonApiResponse<MemberChannelDto> retrieveMyPlaylist(@ApiParam(name = "memberChannelId", required = true, value = "?????? ?????????????????? ?????????", defaultValue = "170") @PathVariable("memberChannelId") Long memberChannelId) {
        Long memberNo = getMemberNo();
        Long characterNo = getCharacterNo();
        return new CommonApiResponse<>(memberChannelService.getMemberChannel(memberNo, characterNo, memberChannelId));
    }

    @PutMapping("/{memberChannelId}")
    @ApiOperation(value = "My Playlist ??????", httpMethod = "PUT", notes = "My Playlist ?????? API(/v2/my/channel/{channelId})", response = CommonApiResponse.class)
    public CommonApiResponse updateMyPlaylist(
            @ApiParam(name = "memberChannelId", required = true, value = "?????? ?????????????????? ?????????", defaultValue = "170") @PathVariable("memberChannelId") Long memberChannelId,
            @ApiParam(value = "memberChannelName - ?????? ?????????????????? ???", defaultValue = UPDATE_MY_PLAYLIST_DEFAULT_REQUEST) @Valid @RequestBody MyPlaylistUpdateRequest myPlaylistUpdateRequest) {
        Long memberNo = getMemberNo();
        Long characterNo = getCharacterNo();
        memberChannelService.modifyMemberChannel(memberNo, characterNo, memberChannelId, myPlaylistUpdateRequest.getMemberChannelName());

        return CommonApiResponse.emptySuccess();
    }

    @PutMapping
    @ApiOperation(value = "My Playlist ?????? ??????", httpMethod = "PUT", notes = "My Playlist ?????? ?????? API(/v2/my/channel/list)", response = CommonApiResponse.class)
    public CommonApiResponse updateMyPlaylist(@Valid @RequestBody MyPlaylistUpdateOrderRequest myPlaylistUpdateOrderRequest) {
        Long memberNo = getMemberNo();
        Long characterNo = getCharacterNo();
        memberChannelService.modifyMemberChannelList(memberNo, characterNo, myPlaylistUpdateOrderRequest.getMemberChannelIdList());

        return CommonApiResponse.emptySuccess();
    }

    @DeleteMapping
    @ApiOperation(value = "My Playlist ??????", httpMethod = "DELETE", notes = "My Playlist ?????? API(/v2/my/channel/)", response = CommonApiResponse.class)
    public CommonApiResponse deleteMyPlaylist(@Valid @RequestBody MyPlaylistDeleteRequest myPlaylistDeleteRequest) {
        Long memberNo = getMemberNo();
        Long characterNo = getCharacterNo();
        List<Long> memberChannelIdList = myPlaylistDeleteRequest.getMemberChannelIdList();
        memberChannelService.removeMemberChannel(memberNo, characterNo, memberChannelIdList);

        return CommonApiResponse.emptySuccess();
    }

    @PostMapping("/{memberChannelId}/tracks")
    @ApiOperation(value = "My Playlist ??????(???) ??????", httpMethod = "POST", notes = "My Playlist ??????(???) ?????? API(/v2/my/channel/{channelId}/track)", response = MyPlaylistTrackCreateResponse.class)
    public CommonApiResponse<MyPlaylistTrackCreateResponse> createMyPlaylistTrack(
            @ApiParam(name = "memberChannelId", required = true, value = "?????? ?????????????????? ?????????", defaultValue = "170") @PathVariable("memberChannelId") Long memberChannelId,
            @Valid @RequestBody MyPlaylistTrackCreateRequest myPlaylistTrackCreateRequest) {
        Long memberNo = getMemberNo();
        Long characterNo = getCharacterNo();
        String appName = GMContext.getContext().getAppName();

        MyPlaylistTrackCreateResponse res =  memberChannelService.addTrackList(AppNameType.fromCode(appName), memberNo, characterNo, memberChannelId, myPlaylistTrackCreateRequest.getTrackIdList());

        return new CommonApiResponse<>(res);
    }

    @GetMapping("/{memberChannelId}/tracks")
    @ApiOperation(value = "My Playlist ??????(???) ?????? ??????", httpMethod = "GET", notes = "My Playlist ??????(???) ?????? ??????(/v2/my/channel/{channelId}/track/list)", response = MyPlaylistTrackRetrieveAllResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", required = false, dataType = "int", paramType = "query", value = "?????????", defaultValue = "1"),
            @ApiImplicitParam(name = "size", required = false, dataType = "int", paramType = "query", value = "?????????", defaultValue = "20")
    })
    public CommonApiResponse<MyPlaylistTrackRetrieveAllResponse> retrieveAllMyPlaylistTrack(
            @ApiParam(name = "memberChannelId", required = true, value = "?????? ?????????????????? ?????????", defaultValue = "170") @PathVariable("memberChannelId") Long memberChannelId,
            @ApiIgnore @PageableDefault(size=300, page= 0) Pageable pageable) {
        Long memberNo = getMemberNo();
        Long characterNo = getCharacterNo();

        return new CommonApiResponse(memberChannelService.getMemberChannelTrackList(memberNo, characterNo, memberChannelId, pageable));
    }

    @PutMapping("/{memberChannelId}/tracks")
    @ApiOperation(value = "My Playlist ??????(???) ?????? ??????", httpMethod = "PUT", notes = "My Playlist ??????(???) ?????? ?????? API(/v2/my/channel/{channelId}/track)", response = MemberChannelDto.class)
    public CommonApiResponse<MemberChannelDto> updateMyPlaylistTrack(
            @ApiParam(name = "memberChannelId", required = true, value = "?????? ?????????????????? ?????????", defaultValue = "170") @PathVariable("memberChannelId") Long memberChannelId,
            @Valid @RequestBody MyPlaylistTrackUpdateOrderRequest myPlaylistTrackUpdateOrderRequest) {
        Long memberNo = getMemberNo();
        Long characterNo = getCharacterNo();

        MemberChannelDto memberChannel =  memberChannelService.modifyTrackList(memberNo, characterNo, memberChannelId, myPlaylistTrackUpdateOrderRequest.getTrackIdList());
        return new CommonApiResponse<>(memberChannel);
    }

    @DeleteMapping("/{memberChannelId}/tracks")
    @ApiOperation(value = "My Playlist ??????(???) ??????", httpMethod = "DELETE", notes = "My Playlist ??????(???) ?????? API(/v2/my/channel/{channelId}/track)", response = MemberChannelDto.class)
    public CommonApiResponse<MemberChannelDto> deleteMyPlaylistTrack(
            @ApiParam(name = "memberChannelId", required = true, value = "?????? ?????????????????? ?????????", defaultValue = "170") @PathVariable("memberChannelId") Long memberChannelId,
            @Valid @RequestBody MyPlaylistTrackDeleteRequest myPlaylistTrackDeleteRequest) {
        Long memberNo = getMemberNo();
        Long characterNo = getCharacterNo();
        String appName = GMContext.getContext().getAppName();

        MemberChannelDto memberChannel = memberChannelService.removeTrackList(AppNameType.fromCode(appName), memberNo, characterNo, memberChannelId, myPlaylistTrackDeleteRequest.getTrackIdList());
        return new CommonApiResponse<>(memberChannel);
    }

    private Long getMemberNo() {
        GMContext currentContext = GMContext.getContext();
        Validator.loginValidate(currentContext);

        return currentContext.getMemberNo();
//        return 1L; // Mockup
    }
    private Long getCharacterNo() {
        GMContext currentContext = GMContext.getContext();
        Validator.loginValidate(currentContext);

        return currentContext.getCharacterNo();
//        return 1L; // Mockup
    }

}
