package com.sktechx.godmusic.personal.rest.controller.test;

import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.domain.badge.BadgeDto;
import com.sktechx.godmusic.personal.rest.domain.badge.BadgeIssueDto;
import com.sktechx.godmusic.personal.rest.repository.BadgeIssueMapper;
import com.sktechx.godmusic.personal.rest.repository.BadgeMapper;
import com.sktechx.godmusic.personal.rest.repository.BadgeTypeMapper;
import com.sktechx.godmusic.personal.rest.service.badge.BadgeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@ApiIgnore
@Slf4j
@Profile("!prod")
@RequiredArgsConstructor
@Controller
@RequestMapping(Naming.serviceCode + "/test/badge")
public class BadgeTestController {

    private final BadgeTypeMapper badgeTypeMapper;
    private final BadgeMapper badgeMapper;
    private final BadgeIssueMapper badgeIssueMapper;
    private final BadgeService badgeService;

    /**
     * 배지 테스트 데이터 index 페이지
     */
    @GetMapping("/index")
    public String testBadge(Model model,
                            @RequestParam(value = "successMsg", required = false) String successMsg,
                            @RequestParam(value = "errorMsg", required = false) String errorMsg) {
        List<BadgeDto> badgeList = badgeMapper.findAll();
        model.addAttribute("badgeList", badgeList);
        model.addAttribute("successMsg" ,successMsg);
        model.addAttribute("errorMsg", errorMsg);
        return "testBadgeIndex";
    }

    @GetMapping("/all")
    public String testMyBadgeList(Model model,
                                  @RequestParam("characterNo") String characterNo) {
        List<BadgeIssueDto> receivedBadgeList =
                badgeIssueMapper.findAllReceivedBadgeListByCharacterNo(Long.valueOf(characterNo));

        model.addAttribute("receivedBadgeList", receivedBadgeList);
        model.addAttribute("characterNo", characterNo);
        return "testMyBadgeList";
    }

    /**
     * 배지 테스트 데이터 생성
     */
    @PostMapping("/save")
    public String testReceivedBadge(@RequestParam("characterNo") String characterNo,
                                    @RequestParam("badgeId") int badgeId) {
        int badgeTypeId = badgeMapper.findByBadgeId(badgeId);
        String badgeType = badgeTypeMapper.findByBadgeTypeId(badgeTypeId);
        Long characterNoParseLong = Long.valueOf(characterNo);

        if (null != badgeIssueMapper.findByCharacterNoAndBadgeId(characterNoParseLong, badgeId)) {
            String errorMsg = "FAIL - The badge already exists.";
            return "redirect:/personal/test/badge/index?errorMsg=" + errorMsg;
        }

        switch (badgeType) {
            case "BA01":
            case "BA02":
                badgeIssueMapper.testSaveBadgeIssue(
                        characterNoParseLong,
                        badgeId,
                        "TRACK",
                        "31006619",
                        (int) (Math.random() * 10000 + 1)
                );
                break;

            default:
                badgeIssueMapper.testSaveBadgeIssue(
                        characterNoParseLong,
                        badgeId,
                        null,
                        null,
                        0
                );
                break;
        }

        String successMsg = "SUCCESS - Got a badge.";
        return "redirect:/personal/test/badge/index?successMsg=" + successMsg;
    }

    @GetMapping("/delete")
    public String testDeleteReceivedBadge(@RequestParam("badgeIssueId") int badgeIssueId,
                                          @RequestParam("characterNo") String characterNo) {
        badgeIssueMapper.testDeleteByBadgeIssueId(badgeIssueId);
        return "redirect:/personal/test/badge/all?characterNo=" + characterNo;
    }

}
