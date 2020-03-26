/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.controller.v1;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.model.dto.badge.AllBadgeListResponseDto;
import com.sktechx.godmusic.personal.rest.model.dto.badge.BadgeDetailListResponseDto;
import com.sktechx.godmusic.personal.rest.model.dto.badge.BadgeDetailResponseDto;
import com.sktechx.godmusic.personal.rest.model.dto.badge.ChallengeBadgeResponseDto;
import com.sktechx.godmusic.personal.rest.model.dto.badge.MyBadgeResponseDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.badge.NewBadgeExistCheckVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 설명 : 칭찬하기(배지) Controller
 *
 * @author Groot(조민국) / dev.mingood@sk.com
 * @since 2020. 03. 24
 */
@Slf4j
@Api(tags = "칭찬하기(배지) API")
@RequiredArgsConstructor
@RestController
@RequestMapping(Naming.serviceCode + "/v1/badge")
public class BadgeController {

    // 캐릭터명은 노출하지 않는다.

    @ApiOperation(value = "캐릭터별 배지 전체 리스트 조회(획득 + 미획득)")
    @GetMapping("/list/all")
    public CommonApiResponse<AllBadgeListResponseDto> getBadgeList() {
        Long characterNo = GMContext.getContext().getCharacterNo();

        // TODO 배지 전체 조회 로직 (획득 + 미획독 포함) - 아직 UI 미확정

        // FIXME Mock 데이터
        MyBadgeResponseDto myBadge1 = new MyBadgeResponseDto(1L, "이 곡의 VIP", "https://w7.pngwing.com/pngs/855/469/png-transparent-gold-medal-logo-medal-gold-icon-golden-atmosphere-medal-golden-frame-atmosphere-decorative-thumbnail.png");
        MyBadgeResponseDto myBadge2 = new MyBadgeResponseDto(2L, "이 곡의 서포터", "https://w7.pngwing.com/pngs/855/469/png-transparent-gold-medal-logo-medal-gold-icon-golden-atmosphere-medal-golden-frame-atmosphere-decorative-thumbnail.png");
        MyBadgeResponseDto myBadge3 = new MyBadgeResponseDto(3L, "좋아요 걸음마", "https://w7.pngwing.com/pngs/855/469/png-transparent-gold-medal-logo-medal-gold-icon-golden-atmosphere-medal-golden-frame-atmosphere-decorative-thumbnail.png");
        List<MyBadgeResponseDto> myBadgeList = Arrays.asList(myBadge1, myBadge2, myBadge3);

        ChallengeBadgeResponseDto challengeBadge1 = new ChallengeBadgeResponseDto("장르주의자", "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQll7p-Xy3WaTHuDRvYRjA1ocUm6WVXNuGTD7m7SY_2D1qryP8l");
        ChallengeBadgeResponseDto challengeBadge2 = new ChallengeBadgeResponseDto("박애주의자", "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQll7p-Xy3WaTHuDRvYRjA1ocUm6WVXNuGTD7m7SY_2D1qryP8l");
        ChallengeBadgeResponseDto challengeBadge3 = new ChallengeBadgeResponseDto("DJ 10", "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQll7p-Xy3WaTHuDRvYRjA1ocUm6WVXNuGTD7m7SY_2D1qryP8l");
        ChallengeBadgeResponseDto challengeBadge4 = new ChallengeBadgeResponseDto("DJ 30", "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQll7p-Xy3WaTHuDRvYRjA1ocUm6WVXNuGTD7m7SY_2D1qryP8l");
        ChallengeBadgeResponseDto challengeBadge5 = new ChallengeBadgeResponseDto("DJ 50", "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQll7p-Xy3WaTHuDRvYRjA1ocUm6WVXNuGTD7m7SY_2D1qryP8l");
        List<ChallengeBadgeResponseDto> challengeBadgeList = Arrays.asList(
                challengeBadge1,
                challengeBadge2,
                challengeBadge3,
                challengeBadge4,
                challengeBadge5
        );

        return new CommonApiResponse<>(new AllBadgeListResponseDto(myBadgeList, challengeBadgeList));
    }

    @ApiOperation(value = "New 배지 리스트 조회 (상세 팝업 용도)")
    @GetMapping("/list/new")
    public CommonApiResponse<BadgeDetailListResponseDto> getNewBadgeList() {
        Long characterNo = GMContext.getContext().getCharacterNo();

        // TODO confirm_dtime == null인 배지 리스트 조회 (배지 생성일 30일이 넘지 않은 것만 해당)

        // FIXME Mock 데이터
        List<ImageInfo> subImgList = new ArrayList<>();
        subImgList.add(new ImageInfo(75L, "https://cdn.music-flo.com/image/album/096/371/02/04/402371096_5cb421cc.jpg?1555309005714/dims/resize/75x75/quality/90"));
        subImgList.add(new ImageInfo(140L, "https://cdn.music-flo.com/image/album/096/371/02/04/402371096_5cb421cc.jpg?1555309005714/dims/resize/140x140/quality/90"));
        subImgList.add(new ImageInfo(200L, "https://cdn.music-flo.com/image/album/096/371/02/04/402371096_5cb421cc.jpg?1555309005714/dims/resize/200x200/quality/90"));
        subImgList.add(new ImageInfo(350L, "https://cdn.music-flo.com/image/album/096/371/02/04/402371096_5cb421cc.jpg?1555309005714/dims/resize/350x350/quality/90"));
        subImgList.add(new ImageInfo(500L, "https://cdn.music-flo.com/image/album/096/371/02/04/402371096_5cb421cc.jpg?1555309005714/dims/resize/500x500/quality/90"));
        subImgList.add(new ImageInfo(1000L, "https://cdn.music-flo.com/image/album/096/371/02/04/402371096_5cb421cc.jpg?1555309005714/dims/resize/1000x1000/quality/90"));
        BadgeDetailResponseDto badgeDetail1 = new BadgeDetailResponseDto(
                29L,
                "A",
                "이 곡의 VIP",
                "XXX님은 어제 999회 재생으로 이 곡을 들은 사람 중 제일 많이 들었어요.",
                "https://w7.pngwing.com/pngs/855/469/png-transparent-gold-medal-logo-medal-gold-icon-golden-atmosphere-medal-golden-frame-atmosphere-decorative-thumbnail.png",
                "Spring Song",
                "나얼",
                subImgList,
                "2020-03-23"
        );

        BadgeDetailResponseDto badgeDetail2 = new BadgeDetailResponseDto(
                30L,
                "B",
                "알앤비 장르주의자",
                "알앤비 장르를 좋아하는 FLO의 1%에요.",
                "https://w7.pngwing.com/pngs/855/469/png-transparent-gold-medal-logo-medal-gold-icon-golden-atmosphere-medal-golden-frame-atmosphere-decorative-thumbnail.png",
                null,
                null,
                null,
                "2020-03-26"
        );

        List<BadgeDetailResponseDto> badgeDetailList = Arrays.asList(badgeDetail1, badgeDetail2);
        return new CommonApiResponse<>(new BadgeDetailListResponseDto(badgeDetailList));
    }

    @ApiOperation(value = "배지 상세 조회")
    @GetMapping("/{badgeIssueId}")
    public CommonApiResponse<BadgeDetailResponseDto> getBadgeDetail(@PathVariable("badgeIssueId") String badgeIssueId) {
        Long characterNo = GMContext.getContext().getCharacterNo();

        // TODO 배지 개별 조회 로직

        // FIXME Mock 데이터
        List<ImageInfo> subImgList = new ArrayList<>();
        subImgList.add(new ImageInfo(75L, "https://cdn.music-flo.com/image/album/096/371/02/04/402371096_5cb421cc.jpg?1555309005714/dims/resize/75x75/quality/90"));
        subImgList.add(new ImageInfo(140L, "https://cdn.music-flo.com/image/album/096/371/02/04/402371096_5cb421cc.jpg?1555309005714/dims/resize/140x140/quality/90"));
        subImgList.add(new ImageInfo(200L, "https://cdn.music-flo.com/image/album/096/371/02/04/402371096_5cb421cc.jpg?1555309005714/dims/resize/200x200/quality/90"));
        subImgList.add(new ImageInfo(350L, "https://cdn.music-flo.com/image/album/096/371/02/04/402371096_5cb421cc.jpg?1555309005714/dims/resize/350x350/quality/90"));
        subImgList.add(new ImageInfo(500L, "https://cdn.music-flo.com/image/album/096/371/02/04/402371096_5cb421cc.jpg?1555309005714/dims/resize/500x500/quality/90"));
        subImgList.add(new ImageInfo(1000L, "https://cdn.music-flo.com/image/album/096/371/02/04/402371096_5cb421cc.jpg?1555309005714/dims/resize/1000x1000/quality/90"));
        BadgeDetailResponseDto badgeDetail1 = new BadgeDetailResponseDto(
                29L,
                "A",
                "이 곡의 VIP",
                "XXX님은 어제 999회 재생으로 이 곡을 들은 사람 중 제일 많이 들었어요.",
                "https://w7.pngwing.com/pngs/855/469/png-transparent-gold-medal-logo-medal-gold-icon-golden-atmosphere-medal-golden-frame-atmosphere-decorative-thumbnail.png",
                "Spring Song",
                "나얼",
                subImgList,
                "2020-03-23"
        );

        return new CommonApiResponse<>(badgeDetail1);
    }

    @ApiOperation(value = "배지 확인")
    @PutMapping("/confirm")
    private CommonApiResponse<?> confirmMyNewBadge(@RequestParam("badgeIssueId") String badgeIssueId) {
        GMContext gmContext = GMContext.getContext();
        Long characterNo = gmContext.getCharacterNo();
        OsType osType = gmContext.getOsType();

        // TODO tb_badge_issue update 로직 (os_type, confirm_dtime, update_dtime)

        return CommonApiResponse.emptySuccess();
    }

    @ApiOperation(value = "New 배지 존재 여부 체크 (빈번한 호출용)")
    @GetMapping("/check/new")
    public CommonApiResponse<NewBadgeExistCheckVo> checkNewBadgeExist() {
        Long characterNo = GMContext.getContext().getCharacterNo();

        // TODO confirm_dtime == null인 배지가 하나라도 있으면 true (배지 생성일 30일이 넘지 않은 것만 해당)

        return new CommonApiResponse<>(new NewBadgeExistCheckVo());
    }

}
