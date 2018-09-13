package com.sktechx.godmusic.personal.rest.controller.v1;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.RequestGMContext;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.personal.common.domain.ListResponse;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.common.domain.type.DayType;
import com.sktechx.godmusic.personal.rest.model.dto.LastListenHistoryDto;
import com.sktechx.godmusic.personal.rest.service.ChannelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDate;
import java.util.List;

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

    @ApiOperation(value = "최근 들은 플레이리스트 상세 by Peter ( 기존 /v2/my/channel/recent/list GET )")
    @GetMapping("recentListened")
    public CommonApiResponse<ListResponse> getLastListenHistory(
            @ApiIgnore @RequestGMContext GMContext ctx, @PageableDefault(size=100, page=0) Pageable pageable){

        DayType dayType = DayType.findDayOfWeek(LocalDate.now().getDayOfWeek());

        List<LastListenHistoryDto> lastListenHistory = channelService.getLastListenHistory(ctx.getCharacterNo(), dayType, ctx.getOsType());
        if(CollectionUtils.isEmpty(lastListenHistory)) throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);

        int start = pageable.getPageNumber() * pageable.getPageSize();
        int end = (pageable.getPageNumber() + 1) * pageable.getPageSize();
        if(end > lastListenHistory.size()) end = lastListenHistory.size();

        return new CommonApiResponse<>(new ListResponse(new PageImpl<>(lastListenHistory.subList(start, end), pageable, lastListenHistory.size())));

    }

}
