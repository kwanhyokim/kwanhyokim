package com.sktechx.godmusic.personal.rest.controller.v1;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import com.google.common.primitives.Ints;
import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.CommonConstant;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.RequestGMContext;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.personal.common.domain.ListResponse;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.LastListenHistoryDto;
import com.sktechx.godmusic.personal.rest.model.dto.MemberChannelDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.vo.ChannelListResponse;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenDeleteRequest;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.service.ChannelService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendPanelService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 남재우(Peter)/njw0619@sk.com
 * @date 2018.08.16
 */

@Slf4j
@RestController
@RequestMapping(Naming.serviceCode + "/v1/channels")
@Api(value = "Channel", description = "개인화 영역 중 채널 관련 API - Peter")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private RecommendPanelService recommendPanelService;

    @Value("${spring.profiles.active}")
    private String activeProfile;


    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve (0..N)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page.", defaultValue = "5")
    })
    @ApiOperation(value = "최근 들은 플레이리스트 상세 by Peter ( 기존 /v2/my/channel/recent/list GET )")
    @GetMapping("recentListened")
    public CommonApiResponse<ListResponse> getLastListenHistory(
            @ApiIgnore @RequestGMContext GMContext ctx,
            @ApiIgnore @PageableDefault(size=50, page=0) Pageable pageable){

        int start = Ints.checkedCast(pageable.getOffset());
        int end = Ints.checkedCast(pageable.getOffset()) + pageable.getPageSize();

        List<LastListenHistoryDto> lastListenHistory = channelService.getLastListenHistory(ctx.getMemberNo(), ctx.getCharacterNo(), ctx.getOsType(), ctx.getAppVer());
        if(CollectionUtils.isEmpty(lastListenHistory)) throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);

        if(start >= lastListenHistory.size() || start >= end) throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);

        if(end > lastListenHistory.size()) end = lastListenHistory.size();

        return new CommonApiResponse<>(new ListResponse(new PageImpl<>(lastListenHistory.subList(start, end), pageable, lastListenHistory.size())));

    }

    @DeleteMapping("/recentListened")
    @ApiOperation(value = "최근 들은 플레이리스트 삭제", httpMethod = "DELETE", response = MemberChannelDto.class)
    public CommonApiResponse<MemberChannelDto> deleteMyLastListenHistory(
            @ApiIgnore @RequestGMContext GMContext ctx,
            @Valid @RequestBody ListenDeleteRequest listenDeleteRequest) {

        Long memberNo = ctx.getMemberNo();
        Long characterNo = ctx.getCharacterNo();

        if(listenDeleteRequest == null){
            throw new CommonBusinessException(CommonErrorDomain.BAD_REQUEST);
        }

        channelService.removeLastListenHistory(memberNo, characterNo, listenDeleteRequest.getListenRequests());

        return CommonApiResponse.emptySuccess();
    }


    @ApiOperation(value = "FLO AND DATA 테마 리스트 ")
    @GetMapping("/floAndDataChnl/list")
    public CommonApiResponse<ChannelListResponse> getFloAndDataChannelList(
            @ApiIgnore @RequestGMContext GMContext ctx){

        ChnlDto floAndDataChannel = channelService.getFloAndDataChannel();

        if(ObjectUtils.isEmpty(floAndDataChannel)){
            throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);
        }

        List<ChnlDto> floAndDataChannelList = new ArrayList<>();
        floAndDataChannelList.add(floAndDataChannel);

        return new CommonApiResponse<>(ChannelListResponse.builder().list(floAndDataChannelList).build());

    }

    @ApiOperation(value = "AFLO 테마 리스트 ")
    @GetMapping("/afloChnl/list")
    public CommonApiResponse recommendPanelTrackList(
            @ApiIgnore @RequestGMContext GMContext ctx,
            @RequestHeader(value = CommonConstant.X_GM_CHARACTER_NO, required = false) Long characterNo,
            @RequestHeader(value = CommonConstant.X_GM_OS_TYPE) OsType osType
    ){


        if( "local".equals(activeProfile) ||
                "dev".equals(activeProfile)
        ) {
            characterNo = 190526002003230373L;
        }

        List<Panel> recommendPanelList = recommendPanelService.getRecommendPanelList(characterNo, RecommendPanelContentType.AFLO, ctx.getOsType());

        if(CollectionUtils.isEmpty(recommendPanelList)){
            return null;
        }

        return new CommonApiResponse<>(new ListDto<>(recommendPanelList));
    }
}
